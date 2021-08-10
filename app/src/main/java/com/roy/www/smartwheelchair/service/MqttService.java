package com.roy.www.smartwheelchair.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.roy.www.smartwheelchair.mvp.base.WheelchairApplication;
import com.roy.www.smartwheelchair.utils.GsonUtil;
import com.roy.www.smartwheelchair.utils.HexDump;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Date;

/**
 * Created by 李杨
 * On 2021/7/2
 * Email: 631934797@qq.com
 * Description:
 */
public class MqttService extends Service implements MqttCallbackExtended {
    private static final String TAG = "@@@ ===> " + MqttService.class.getSimpleName();

    private final IBinder mMqttIBinder = new MqttIBinder();
    private static final String broker = "tcp://47.97.212.252:1883";
    private static final String acessKey = "admin";
    private static final String secretKey = "1q2w3e4r5T";
    private static final String Group_ID = "GID_phone";
    private static String Device_ID = WheelchairApplication.DEVICE_ID;

    private static final String Client_ID = Group_ID + "@@@" + Device_ID + "_W"+ HexDump.getRand(1);
    private static final String topic = "topic-smart";

    /**
     * pub发布的消息频道
     */
    private static final String pubTopicSetting = topic + "/" +Device_ID + "/setting/";


    /**
     * sub订阅的消息频道
     */
    public  static  final String subTopicInfo = topic + "/" +Device_ID + "/info/";

    public  static  MqttClient mSampleClient;
    private static  MqttConnectOptions mConnOpts;
    private static  String[] mTopicFilters;
    private static  int[] mQos;


    public static Intent newMqttServiceIntent(Context context, String action) {
        Intent intent = new Intent(context, MqttService.class);
        intent.setAction(action);
        return intent;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"== onBind NetworkService ==");

        // TODO 1：判断网络是否可用-->开启MQTT订阅服务消息频道
        //检测WIFI是否开启
//        checkWifiEnabled();

        initMqttClient();


        return mMqttIBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            Log.i(TAG, "action:" + action);
            if (TextUtils.equals(action, "connect")) {
                initMqttClient();

            } else if (TextUtils.equals(action, "disconnect")) {

            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMqttClient();
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }

    /**
     * 释放MqttClient
     */
    private void releaseMqttClient(){
        try {
            if (mSampleClient == null)
                return;
            mSampleClient.disconnect();
            mSampleClient.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    private void checkWifiEnabled() {
        if (!NetworkUtils.getWifiEnabled()){
            Log.i(TAG,"== Wifi Enabled no ==");
            NetworkUtils.setWifiEnabled(true);
        }else {
            Log.i(TAG,"== Wifi Enabled ==");
            if (NetworkUtils.isWifiConnected() && NetworkUtils.isAvailable()){
                initMqttClient();
            }

        }

    }

    public class MqttIBinder extends Binder {

    }


    /**
     * 初始化MqttClient
     */
    public void initMqttClient() {
        try {

            StringBuilder sb = new StringBuilder();
            sb.append("subMqttMessage ==> Device_ID = " + Device_ID + "\n");
            sb.append("                   Client_ID = " + Client_ID + "\n");
            Log.d(TAG,sb.toString());
            MemoryPersistence persistence = new MemoryPersistence();
            //创建客户端
            mSampleClient = new MqttClient(broker, Client_ID, persistence);

            /**
             * 设置订阅方订阅的Topic集合，此处遵循MQTT的订阅规则，可以是一级Topic，二级Topic,P2P消息不需要显式订阅，
             */
            mTopicFilters = new String[]{subTopicInfo};

            mQos = new int[]{0};

            //配置回调函数
            mSampleClient.setCallback(this);
            //创建连接选择
            mConnOpts = createConnectOptions(acessKey, secretKey);
            Log.w(TAG,"Connecting to broker: " + broker);
            //创建服务连接
            mSampleClient.connect(mConnOpts);
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }


    /**
     *  创建MqttConnectOptions
     * @param userName
     * @param passWord
     * @return
     */
    private static MqttConnectOptions createConnectOptions(String userName, String passWord) {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(userName);
        connOpts.setPassword(passWord.toCharArray());
        connOpts.setAutomaticReconnect(true);
        // 设置连接超时时间, 单位为秒,默认30
        connOpts.setConnectionTimeout(30);
        // 设置会话心跳时间,单位为秒,默认20
        connOpts.setKeepAliveInterval(90);
        return connOpts;
    }


    /**
     * ========================= 发布Mqtt消息 =========================
     *
     * @param sContent  要发送的信息
     * @param topic_ack 要发布的频道
     */
    public static void publishMessage(String topic_ack, String sContent){
        try {
            MqttMessage message = new MqttMessage(sContent.getBytes());

            message.setQos(1);//设置消息等级

            if (mSampleClient != null && mSampleClient.isConnected()) {
                /**
                 *消息发送到某个主题Topic，所有订阅这个Topic的设备都能收到这个消息。
                 * 遵循MQTT的发布订阅规范，Topic也可以是多级Topic。此处设置了发送到二级topic
                 */
                mSampleClient.publish(topic_ack, message);//发布消息

                Log.i(TAG + "==>发布消息", topic_ack+" pushed at " + new Date() + " " + sContent);
                Log.i(TAG + "==>响应服务器", " pushed at " + new Date() + " " + sContent);
//                LogUtils.file(TAG + "==>ResponseMqttServer", " pushed at " + new Date() + " " + sContent + "\r\n");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void publishMessage(String sContent){
        publishMessage(pubTopicSetting, sContent);
    }


    /**
     * 连接成功的回调
     * @param reconnect
     * @param serverUri
     */
    @Override
    public void connectComplete(boolean reconnect, String serverUri) {
        Log.i(TAG,"== connectComplete ==");
        try {
            //连接成功，需要上传客户端所有的订阅关系
            mSampleClient.subscribe(mTopicFilters, mQos);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    /**
     * 连接失败的回调
     * @param cause
     */
    @Override
    public void connectionLost(Throwable cause) {
        Log.i(TAG, "mqtt connection lost");
        // TODO 重连机制
    }

    /**
     * 收到订阅消息的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        // 处理接收到的指令
        if (topic.equals(subTopicInfo)){
            String jsonResult = new String(mqttMessage.getPayload());
            Log.i(TAG + "==>Recv", "messageArrived:   " + topic + "------" + jsonResult );
//            LogUtils.file(TAG + "==>Recv", "messageArrived:   " + topic + "------" + jsonResult + "\r\n");
            splitJsonProtocol(jsonResult);
        }

    }

    /**
     *  接收消息完成后的回调
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Log.i(TAG + "==>Recv", "deliveryComplete:   " + iMqttDeliveryToken.getMessageId());
    }

    /**
     *  ================================载取json字符串中Protocol字段值================================
     *
     * @param jsonBack
     * @return
     */
    private static void splitJsonProtocol(String jsonBack){
        String protocolResult = GsonUtil.acceptAndStopField(jsonBack, "protocol");
        switch (protocolResult) {
            case "info":
                acceptCommandAndNotify(jsonBack);
                break;
        }

    }

    /**
     * ================================通过Command区分收到的指令数据并分类处理================================
     *
     * @param jsonBack 收到的JSON指令
     */
    private static void acceptCommandAndNotify(String jsonBack) {
        String commandResult = GsonUtil.acceptAndStopField(jsonBack, "command");
        switch (commandResult) {

            case "power_info":
                Log.w(TAG,"======= 上报电量信息 =======");
                WheelchairApplication.mmkv.encode("power",GsonUtil.acceptAndStopField(jsonBack, "power"));
                WheelchairApplication.mmkv.encode("speed",GsonUtil.acceptAndStopField(jsonBack, "speed"));
                WheelchairApplication.mmkv.encode("mileage",GsonUtil.acceptAndStopField(jsonBack, "mileage"));
                break;

            case "location":
                Log.w(TAG,"======= 上报定位信息 =======");
                WheelchairApplication.mmkv.encode("loca_info",GsonUtil.acceptAndStopField(jsonBack, "loca_info"));
                break;

            case "SOS":
                Log.w(TAG,"======= SOS =======");
                WheelchairApplication.mmkv.encode("SOS",GsonUtil.acceptAndStopField(jsonBack, "SOS"));
                break;

        }

    }


    /**
     * ================================设置待机音量================================
     *
     * @param volume 传入的音量
     */

    private void setVolume(int volume) {
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }


}
