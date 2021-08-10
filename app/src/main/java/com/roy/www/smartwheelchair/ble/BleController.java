package com.roy.www.smartwheelchair.ble;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blankj.utilcode.util.ToastUtils;
import com.roy.www.smartwheelchair.ble.callback.BleConnectCallback;
import com.roy.www.smartwheelchair.ble.callback.BleScanCallback;
import com.roy.www.smartwheelchair.ble.callback.OnReceiverCallback;
import com.roy.www.smartwheelchair.ble.callback.OnWriteDataCallback;
import com.roy.www.smartwheelchair.ble.callback.ScanBleDeviceCallback;
import com.roy.www.smartwheelchair.ble.protocol.Command;
import com.roy.www.smartwheelchair.ble.protocol.PackData;
import com.roy.www.smartwheelchair.utils.CRC16;
import com.roy.www.smartwheelchair.utils.HexDump;
import com.roy.www.smartwheelchair.utils.ThreadPool;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 李杨
 * On 2021/6/20
 * Email: 631934797@qq.com
 * Description:
 */

public class BleController {

    public int MTU = 512;

    private static String TAG = "==" + BleController.class.getCanonicalName() + "==" ;

    //监听BLE的状态值
    public static final int BLE_STATE_OFF = 10;// BLE已经完全关闭的状态;
    public static final int BLE_STATE_TURNING_ON = 11;// BLE正要开启的状态, 是 Disabled 和 Enabled 的临界状态
    public static final int BLE_STATE_ON = 12;// BLE已经完全开启的状态
    public static final int BLE_STATE_TURNING_OFF = 13;// BLE正要关闭的状态, 是 Enabled 和 Disabled 的临界状态;

    public static final String BLE_SEND_COMMEND = "ACTION_BLUETOOTH_SEND_COMMEND";
    public static final String ACTION_CONNECTED_SUCCEED = "ACTION_BLUETOOTH_CONNECTED_SUCCEED"; //连接成功
    public static final String ACTION_CONNECTED_FAILURE = "ACTION_CONNECTED_FAILURE"; //连接失败
    public static final String ACTION_BLE_DISCONNECTED = "ACTION_BLUETOOTH_DISCONNECTED";//断开连接
    public static final String ACTION_CONNECTED_TIMEOUT = "ACTION_CONNECTED_TIMEOUT"; //连接超时
    //BleCOntroller实例
    private static BleController sBleManager;

    private Context mContext;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;

    private BleGattCallback mGattCallback;
    private OnWriteDataCallback mWriteDataCallback;
    private OnReceiverCallback mReceiverCallback;


    //获取到所有服务的集合m
    private HashMap<String, Map<String, BluetoothGattCharacteristic>> mServicesMap = new HashMap<>();

    private Handler mHandler = new Handler(Looper.getMainLooper());
    //发起连接是否有响应
    private boolean isConnectResponse = false;
    //是否是用户手动断开
    private boolean isBreakByMyself = false;
    private static boolean mIsReader = false;

    //默认扫描时间：5s
    private static final int SCAN_TIME = 5000;
    //默认连接超时时间:6s
    private static final int CONNECTION_TIME_OUT = 6000;
    //连接结果的回调
    private BleConnectCallback mBleConnectCallback;
    //MAC地址
    private String MAC = "";
    //通信服务体征值
    private BluetoothGattCharacteristic mGattCharacteristic;

    private ScanBleDeviceCallback mScanBleDeviceCallback;
    //此属性一般不用修改
    private static final String BLUETOOTH_NOTIFY_D = "00002902-0000-1000-8000-00805f9b34fb";

    //TODO 712
    private static final String BLUETOOTH_SERVICE_UUID = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String BLUETOOTH_NOTIFY_UUID = "0000fff1-0000-1000-8000-00805f9b34fb";
    private static final String BLUETOOTH_WRITE_UUID = "0000fff2-0000-1000-8000-00805f9b34fb";

    private BluetoothA2dp mBluetoothA2dp;
    //-----------------------------  对外公开的方法 ----------------------------------------------
    /**
     * 获取BleController实例对象
     * @return
     */
    public synchronized static BleController getInstance() {
        if (null == sBleManager) {
            sBleManager = new BleController();
        }
        return sBleManager;
    }

    /**
     * 初始化BLE相关参数
     *
     * @param context
     */
    public void initBle(Context context) {
//        mContext = context.getApplicationContext();
        mContext = context;
        // 初始化mBluetoothManager
        mBluetoothManager = getBleManager(mContext);
        // 初始化mBluetoothAdapter
        mBluetoothAdapter = getBluetoothAdapter();
        // 蓝牙GATT连接及操作事件回调接口
        mGattCallback = new BleGattCallback();


        // 注册BLE的状态变化广播
        context.registerReceiver(mBleStateBroadcastReceiver, makeGattUpdateIntentFilter());
        mBluetoothAdapter.getProfileProxy(mContext, mA2dpListener, BluetoothProfile.A2DP);

    }




    /**
     * 建立a2dp设备的连接
     *
     * @param device device
     */
    public void connectA2dp(BluetoothDevice device) {
        if (mBluetoothA2dp == null || device == null) {
            return;
        }

        try {
            Method method = BluetoothA2dp.class.getMethod("connect", new Class[]{BluetoothDevice.class});
            method.invoke(mBluetoothA2dp, device);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 断开当前a2dp设备
     *
     * @param device device
     */
    public void disconnectA2dp(BluetoothDevice device) {
        if (mBluetoothA2dp == null || device == null) {
            return;
        }
        try {
            Method method = BluetoothA2dp.class.getMethod("disconnect", new Class[]{BluetoothDevice.class});
            method.invoke(mBluetoothA2dp, device);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    //===============================================BluetoothProfile============================================
    private final BluetoothProfile.ServiceListener mA2dpListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (i == BluetoothProfile.A2DP) {
                mBluetoothA2dp = (BluetoothA2dp) bluetoothProfile;
            }
        }

        @Override
        public void onServiceDisconnected(int i) {
            if (i == BluetoothProfile.A2DP) {
                mBluetoothA2dp = null;
            }
        }
    };

    /**
     *  扫描设备
     *
     * @param time    当传入的time值为0以下时默认扫描时间为6秒b
     * @param bleScanCallback
     */
    public void scanBleDevice(int time, final BleScanCallback bleScanCallback) {
        if (!isEnabled()) {
            setBleEnabled(true);
        }
        if (null != mBluetoothGatt && !isConnectResponse) {
            mBluetoothGatt.close();
            reset();
        }

        mScanBleDeviceCallback = new ScanBleDeviceCallback(bleScanCallback);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //time后停止扫描
                stopScanBle();
                bleScanCallback.onSuccess();
            }
        }, time <= 0 ? SCAN_TIME : time);

        mBluetoothAdapter.startLeScan(mScanBleDeviceCallback);
    }

    public void  stopScanBle(){

        mBluetoothAdapter.stopLeScan(mScanBleDeviceCallback);
    }


    /**
     * 连接设备
     *
     * @param connectionTimeOut 连接超时时间,默认是6秒.当赋值为0或更小值时用默认值
     * @param devicesAddress    想要连接的设备地址
     */
    public void connect(final int connectionTimeOut, final String devicesAddress, BleConnectCallback bleConnectCallback) {

        stopScanBle();
        MAC = devicesAddress;
        BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(devicesAddress);
        if (null == remoteDevice) {
            Log.e(TAG, "No device found at this address：" + devicesAddress);
            return;
        }


        this.mBleConnectCallback = bleConnectCallback;

        if (null != mBluetoothGatt) {
            mBluetoothGatt.close();
        }
        reset();
        mBluetoothGatt = remoteDevice.connectGatt(mContext, false, mGattCallback);
        Log.e(TAG, "connecting mac-address:" + devicesAddress);

        delayConnectResponse(connectionTimeOut);
    }

    /**
     * 重连
     */
    public void reconnect(){
        BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(MAC);
        if (null == remoteDevice) {
            Log.e(TAG, "reconnect()  No device found at this address：" + MAC);
            return;
        }
        if (null != mBluetoothGatt) {
            mBluetoothGatt.close();
        }
        reset();
        mBluetoothGatt = remoteDevice.connectGatt(mContext, false, mGattCallback);
        Log.e(TAG, "reconnect()   connecting mac-address:" + MAC);

        delayConnectResponse(0);
    }




    /**
     * 断开连接
     */
    public void disconnection() {
        if (null == mBluetoothAdapter || null == mBluetoothGatt) {
            Log.e(TAG, "disconnection error maybe no init");
            return;
        }
        refreshGattCache(mBluetoothGatt);
        mBluetoothGatt.disconnect();


    }

    /**
     * 手动断开Ble连接
     */
    public void closeBleConn() {
        disconnection();
        isBreakByMyself = true;
        mGattCharacteristic = null;

    }

    public boolean refreshGattCache(BluetoothGatt gatt) {
        boolean result = false;
        try {
            if (gatt != null) {
                Method refresh = BluetoothGatt.class.getMethod("refresh");
                if (refresh != null) {
                    refresh.setAccessible(true);
                    result = (boolean) refresh.invoke(gatt, new Object[0]);
                }
            }
        } catch (Exception e) {
        }
        return result;
    }


    public void transfer(String bytes, OnWriteDataCallback writeCallback) {
        sendbuf(HexDump.hexStringToByteArray(bytes),writeCallback);
    }

    public void transfer(byte[] bytes, OnWriteDataCallback writeCallback) {
        sendbuf(bytes,writeCallback);
    }


    /**
     * 设置读取数据的监听
     *
     * @param onReceiverCallback
     */
    public void registReciveListener(OnReceiverCallback onReceiverCallback) {
        mReceiverCallback = onReceiverCallback;
    }

    /**
     * 设置读取数据的监听
     */
    public void unregistReciveListener() {
        if (mReceiverCallback != null){
            mReceiverCallback = null;
        }
    }


    /**
     * 获取BluetoothManager
     *
     * @param context
     * @return
     */
    public  BluetoothManager getBleManager(Context context) {
        return context == null ? null : (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    /**
     * 获取BluetoothAdapter
     *
     * @return
     */
    public  BluetoothAdapter getBluetoothAdapter(){
        return mBluetoothManager == null ? null : mBluetoothManager.getAdapter();
    }

    /**
     *
     * 开启/关闭BLE
     *
     * @param enabled
     * @return
     */
    public boolean setBleEnabled(boolean enabled){

        if (enabled){
            return  mBluetoothAdapter == null ? false : mBluetoothAdapter.enable();
        }else {
            return  mBluetoothAdapter == null ? false : mBluetoothAdapter.disable();
        }

    }

    /**
     * 获取BLE状态
     * @return
     */
    public boolean isEnabled(){
        return  mBluetoothAdapter == null ? false : mBluetoothAdapter.isEnabled();
    }



    //----------------------------------  私有方法 ----------------------------------------------


    /**
     * 蓝牙GATT连接及操作事件回调
     */
    private class BleGattCallback extends BluetoothGattCallback {

        /**
         * 连接状态的回调
         * @param gatt
         * @param status
         * @param newState
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            reset();
            Log.w(TAG,"onConnectionStateChange  [ status : " + status + " |  newState : " + newState + "]");
            switch (status){
                case 0:
                    switch (newState){
                        case BluetoothProfile.STATE_CONNECTED: // 连接成功
                            if(status == BluetoothGatt.GATT_SUCCESS){
                                isBreakByMyself = false;
                                // 扫描服务
                                mBluetoothGatt.discoverServices();
                            }
                            break;
                        case BluetoothProfile.STATE_DISCONNECTED: // 连接断开
                            if ( mBluetoothGatt != null){
                                mBluetoothGatt.close();
                            }
                            gatt.close();
                            mBluetoothGatt = null;

                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(new Intent(ACTION_BLE_DISCONNECTED)));
                            reConnect();
                            break;
                    }
                    break;
                case 8:  // 连接超时
                    if ( mBluetoothGatt != null){
                        mBluetoothGatt.close();
                    }
                    gatt.close();
                    mBluetoothGatt = null;
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(new Intent(ACTION_CONNECTED_TIMEOUT)));
                    reConnect();
                    break;
                case 133: // 连接失败
                    if ( mBluetoothGatt != null){
                        mBluetoothGatt.close();
                    }
                    gatt.close();
                    mBluetoothGatt = null;
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(new Intent(ACTION_CONNECTED_FAILURE)));
                    reConnect();
                    break;
            }

        }

        /**
         * 服务被发现的回调
         * @param gatt
         * @param status
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            switch (status) {
                case BluetoothGatt.GATT_SUCCESS:
                    if (null != mBluetoothGatt) {
                        List<BluetoothGattService> services = mBluetoothGatt.getServices();
                        int serviceSize = services.size();
                        for (int i = 0; i < serviceSize; i++) {
                            HashMap<String, BluetoothGattCharacteristic> charMap = new HashMap<>();
                            BluetoothGattService bluetoothGattService = services.get(i);
                            String serviceUuid = bluetoothGattService.getUuid().toString();
                            List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
                            int characteristicSize = characteristics.size();
                            for (int j = 0; j < characteristicSize; j++) {
                                charMap.put(characteristics.get(j).getUuid().toString(), characteristics.get(j));
                                if (characteristics.get(j).getUuid().toString().equals(BLUETOOTH_NOTIFY_UUID)) {
                                    enableNotification(true, characteristics.get(j));

                                    isConnectResponse = true;
                                    connSuccess();

                                }

                            }
                            mServicesMap.put(serviceUuid, charMap);
                        }
                    }
                    break;
            }
        }

        /**
         * 收到数据的回调（此处接收BLE设备返回数据）
         *
         * @param gatt
         * @param characteristic
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            byte[] recValue = characteristic.getValue();
            Log.w(TAG,"RT BLE <==="+ HexDump.byteTo16String(recValue));
            if (recValue != null && recValue.length > 0){
                if (mReceiverCallback != null){
                    receivedBleData(recValue,mReceiverCallback);
//                    mReceiverCallback.onReceive(recValue);
                }
            }


        }

        /**
         * 发送数据结果回调
         * @param gatt
         * @param characteristic
         * @param status
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (null != mWriteDataCallback) {
                switch (status){
                    case BluetoothGatt.GATT_SUCCESS:
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            Log.e(TAG,"写入成功  value = " + characteristic.getValue());
                        }
                        runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mWriteDataCallback.onSuccess();
                            }
                        });
                        Log.e(TAG, "Send data success ");
                        break;
                    case BluetoothGatt.GATT_FAILURE:
                        runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mWriteDataCallback.onFailed(OnWriteDataCallback.FAILED_OPERATION);
                            }
                        });
                        Log.e(TAG, "Send data failed ");
                        break;
                    case BluetoothGatt.GATT_WRITE_NOT_PERMITTED:
                        runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                mWriteDataCallback.onFailed(OnWriteDataCallback.FAILED_OPERATION);
                            }
                        });
                        Log.e(TAG, "No write permission ");
                        break;
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG,"读取成功  value = " + characteristic.getValue());
            }
        }

        //描述符被写了
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.w(TAG,"onDescriptorWrite()  [ status : " + status +  "]");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mIsReader = true;
               Log.e(TAG,"描述符号 写入成功  value = " + descriptor.getValue());
            }


        }

        //描述符被读了
        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG,"描述符号 读取成功  value = " + descriptor.getValue());
            }

        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            if (BluetoothGatt.GATT_SUCCESS == status && MTU == mtu) {
                MTU = mtu;
                Log.d(TAG,"MTU change success = " + MTU);

            } else {
                MTU = 16;
                Log.d(TAG,"MTU change fail!");
            }
        }
    }

    /**
     * 发送数据
     *
     * @param bytes
     * @param writeCallback
     */
    private void sendbuf(byte[] bytes, OnWriteDataCallback writeCallback) {
        this.mWriteDataCallback = writeCallback;
        if (!isEnabled()) {
            writeCallback.onFailed(OnWriteDataCallback.FAILED_BLUETOOTH_DISABLE);
            Log.e(TAG, "FAILED_BLUETOOTH_DISABLE");
            return;
        }

        // 获取服务中的特征
        if (mGattCharacteristic == null) {
            mGattCharacteristic = getBluetoothGattCharacteristic(BLUETOOTH_SERVICE_UUID, BLUETOOTH_WRITE_UUID);

        }
        if (null == mGattCharacteristic) {
            writeCallback.onFailed(OnWriteDataCallback.FAILED_INVALID_CHARACTER);
            Log.e(TAG, "FAILED_INVALID_CHARACTER");
            return;
        }

//        //设置该特征具有Notification功能
//        mBluetoothGatt.setCharacteristicNotification(mGattCharacteristic,true);

        //将指令放置进特征中
        mGattCharacteristic.setValue(bytes);

        //设置回复形式
        mGattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);


//        Log.e(TAG, "mGattCharacteristic:" + mGattCharacteristic );
        //发送 //开始写数据
        boolean b = mBluetoothGatt.writeCharacteristic(mGattCharacteristic);
//        if (!b){
//            ToastUtils.showLong("...请先建立BLE连接...");
//        }

        Log.e(TAG, "send:" + b + "   len= " +bytes.length+"   data：" + HexDump.byteTo16String(bytes));
    }

    /**
     * 开启通知
     *
     * @param enable
     * @param characteristic
     * @return
     */
    private boolean enableNotification(boolean enable, BluetoothGattCharacteristic characteristic) {
        if (mBluetoothGatt == null || characteristic == null)
            return false;

        if (!mBluetoothGatt.setCharacteristicNotification(characteristic, enable))
            return false;

        BluetoothGattDescriptor clientConfig = characteristic.getDescriptor(UUID.fromString(BLUETOOTH_NOTIFY_D));
        if (clientConfig == null)
            return false;

        if (enable) {
            clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            clientConfig.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        SystemClock.sleep(200);
        return mBluetoothGatt.writeDescriptor(clientConfig);
    }

    /**
     * 根据服务UUID和特征UUID,获取一个特征{@link BluetoothGattCharacteristic}
     *
     * @param serviceUUID   服务UUID
     * @param characterUUID 特征UUID
     */
    private BluetoothGattCharacteristic getBluetoothGattCharacteristic(String serviceUUID, String characterUUID) {
        if (!isEnabled()) {
            throw new IllegalArgumentException(" Bluetooth is no enable please call BluetoothAdapter.enable()");
        }
        if (null == mBluetoothGatt) {
            Log.e(TAG, "mBluetoothGatt is null");
            return null;
        }

        //找服务
        Map<String, BluetoothGattCharacteristic> bluetoothGattCharacteristicMap = mServicesMap.get(serviceUUID);
        if (null == bluetoothGattCharacteristicMap) {
            Log.e(TAG, "Not found the serviceUUID!");
            return null;
        }

        //找特征
        Set<Map.Entry<String, BluetoothGattCharacteristic>> entries = bluetoothGattCharacteristicMap.entrySet();
        BluetoothGattCharacteristic gattCharacteristic = null;
        for (Map.Entry<String, BluetoothGattCharacteristic> entry : entries) {
            if (characterUUID.equals(entry.getKey())) {
                gattCharacteristic = entry.getValue();
                break;
            }
        }
        return gattCharacteristic;
    }

    private void runOnMainThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            if (mHandler != null) {
                mHandler.post(runnable);
            }
        }
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    // TODO 此方法断开连接或连接失败时会被调用。可在此处理自动重连,内部代码可自行修改，如发送广播
    private void reConnect() {
        if(mBleConnectCallback != null) {
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mBleConnectCallback.onConnFailed();
                }
            });
        }
        Log.e(TAG, "Ble disconnect or connect failed!");
    }

    // TODO 此方法Notify成功时会被调用。可在通知界面连接成功,内部代码可自行修改，如发送广播
    private void connSuccess() {
       if(mBleConnectCallback != null) {
            Log.e(TAG, "connSuccess : " + isConnectResponse );
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mBleConnectCallback.onConnSuccess();
                }
            });
        }
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(new Intent(ACTION_CONNECTED_SUCCEED)));

        Log.e(TAG, "Ble connect success!");
    }

    /**
     * 复位
     */
    private void reset() {
//        BLUETOOTH_SERVICE_UUID_BASE = null;
        isConnectResponse = false;
        mServicesMap.clear();
    }

    /**
     * 如果连接connectionTimeOut时间后还没有响应,手动关掉连接.
     *
     * @param connectionTimeOut
     */
    private void delayConnectResponse(final int connectionTimeOut) {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isConnectResponse && !isBreakByMyself) {
                    Log.e(TAG, "connect timeout");
                } else {
                    isBreakByMyself = false;
                }
            }
        }, connectionTimeOut <= 0 ? CONNECTION_TIME_OUT : connectionTimeOut);

    }

    // BLE消息处理
    private static List<Byte> currentData = new ArrayList<>();
    private static final int READING_HEAD_FLAG = 1;
    private static final int READING_FOOTER_FLAG = 2;
    private static int readFlag = READING_HEAD_FLAG;
    /**
     * ================================接收串口数据================================
     */
    private static synchronized void receivedBleData(final byte[] byteData, OnReceiverCallback receiverCallback){
        if (byteData==null || byteData.length ==0)return;
        final boolean[] isRead = {true};
        ThreadPool.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (isRead[0])
                        for (int i = 0; i < byteData.length; i++) {
                            if (readFlag == READING_HEAD_FLAG) {
                                if (byteData[i] == Command.HEAD) {
                                    currentData.add(byteData[i]);
                                    readFlag = READING_FOOTER_FLAG;
                                }
                            } else if (readFlag == READING_FOOTER_FLAG) {
                                currentData.add(byteData[i]);
                                if (byteData[i] == Command.TAIL && ((currentData.get(2)&0xFF)+6 == currentData.size())){



                                    byte[] cmdData = new byte[currentData.size()];
                                    for (int j = 0; j < currentData.size(); j++) {
                                        cmdData[j] = currentData.get(j);
                                    }



                                    byte[] crc16_modbus = CRC16.getCRC16_Modbus(HexDump.getSubArray(cmdData, 1, cmdData.length - 4));
//                                    Log.e(TAG, "bcc: " +HexDump.byteTo16String(bcc) +   "\r\n");

                                    if (cmdData.length >= 8 && Arrays.equals(crc16_modbus,new byte[]{cmdData[cmdData.length-3],cmdData[cmdData.length-2]})){
                                        receiverCallback.onReceive(cmdData);
                                    }



                                    currentData.clear();
                                    readFlag = READING_HEAD_FLAG;

                                    isRead[0] =false;
                                }else {
                                    isRead[0] =false;
                                }
                            }
                        }
                }
            }
        });
    }


//========================================================华丽的分割线======================================================

    // 监听BLE状态变化
    private  BroadcastReceiver mBleStateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    Log.w(TAG,"===蓝牙状态发生改变===");
                    int bleState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    switch (bleState) {
                        case BLE_STATE_TURNING_OFF:
                            Log.i(TAG, "...正在关闭蓝牙...");
                            if(mHandler != null) mHandler.sendEmptyMessage(BLE_STATE_TURNING_OFF);
                            break;
                        case BLE_STATE_OFF:
                            Log.i(TAG, "...蓝牙已关闭！");
                            if(mHandler != null) mHandler.sendEmptyMessage(BLE_STATE_OFF);
                            break;
                        case BLE_STATE_TURNING_ON:
                            Log.i(TAG, "...正在开启蓝牙...");
                            if(mHandler != null) mHandler.sendEmptyMessage(BLE_STATE_TURNING_ON);
                            break;
                        case BLE_STATE_ON:
                            Log.i(TAG, "...蓝牙已开启...");
                            if(mHandler != null) mHandler.sendEmptyMessage(BLE_STATE_ON);
                            break;
                    }
                    break;

                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:/**扫描开启时的广播*/
                    Log.w(TAG,"===扫描开启时的广播===");
                    break;

                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:/**扫描结束时广播*/
                    Log.w(TAG,"===扫描结束时广播===");
                    if (mBluetoothAdapter!= null)
                        mBluetoothAdapter.startDiscovery();
                    break;

                case BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED:/**A2DP设备连接状态发生改变*/

                    Log.w(TAG,"===A2DP设备连接状态发生改变===");

                    break;
                case BluetoothDevice.ACTION_FOUND:/**扫描到设备时的action*/

                    Log.w(TAG,"===扫描到设备时的ACTION_FOUND===");
                    BluetoothClass btClass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
                    if (btClass.getMajorDeviceClass() != BluetoothClass.Device.Major.AUDIO_VIDEO) {


                        Log.w(TAG,"...有a2dp设备...");
                        /**本demo只处理a2dp设备，所以只显示a2dp，过滤掉其他设备*/
                        break;
                    }

                    break;

                case ACTION_CONNECTED_SUCCEED:
                    Log.i(TAG, "...Bluetooth connection successful...");
                    break;
                case ACTION_BLE_DISCONNECTED:
                    Log.i(TAG, "...Bluetooth disconnection...");
                    break;
                case ACTION_CONNECTED_TIMEOUT:
                    Log.i(TAG, "...Bluetooth connection timeout...");
                    break;
                case ACTION_CONNECTED_FAILURE:
                    Log.i(TAG, "...Bluetooth connection failed...");
                    break;
            }
        }
    };


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);


        intentFilter.addAction(ACTION_CONNECTED_SUCCEED);
        intentFilter.addAction(ACTION_BLE_DISCONNECTED);
        intentFilter.addAction(ACTION_CONNECTED_TIMEOUT);
        intentFilter.addAction(ACTION_CONNECTED_FAILURE);

        return intentFilter;
    }




}