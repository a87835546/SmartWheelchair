package com.roy.www.smartwheelchair.ble.protocol;

import android.os.SystemClock;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.roy.www.smartwheelchair.ble.BleController;
import com.roy.www.smartwheelchair.ble.callback.OnReceiverCallback;
import com.roy.www.smartwheelchair.ble.callback.OnWriteDataCallback;
import com.roy.www.smartwheelchair.utils.HexDump;
import com.roy.www.smartwheelchair.utils.ThreadPool;

/**
 * Created by 李杨
 * On 2021/7/1
 * Email: 631934797@qq.com
 * Description:
 */
public class Protocol {


    private static final String TAG = Protocol.class.getSimpleName();
    private static Protocol mInstance;

    private Protocol(){
//        BleController.getInstance().registReciveListener(new OnReceiverCallback() {
//            @Override
//            public void onReceive(byte[] bytes) {
//                // 数据分发
//                parseCommPacket(bytes);
//            }
//        });
    }

    public static Protocol getInstance() {
        if(mInstance == null) {
            mInstance = new Protocol();
        }
        return mInstance;
    }


    /**
     *
     * @param bytes
     */
    private void parseCommPacket(byte[] bytes) {
        PackData commPacket = PackData.createCommPacket(bytes);
        if (commPacket != null){

            switch (commPacket.getCmd()){

                case Command.CMD_ACTION_ADVANCE:
                    ToastUtils.showLong("前进");
                    break;

                case Command.CMD_ACTION_RETREAT:
                    ToastUtils.showLong("后退");
                    break;

                case Command.CMD_ACTION_LEFT:
                    ToastUtils.showLong("向左");
                    break;

                case Command.CMD_ACTION_RIGHT:
                    ToastUtils.showLong("向右");
                    break;

                case Command.CMD_ACTION_STOP:
                    ToastUtils.showLong("停止");
                    break;

                case Command.CMD_ACTION_SET_WIFI:
                    ToastUtils.showLong("SET WIFI");
                    break;



            }

        }

    }



    /**
     * 创建指令包
     *
     * @param cmd
     * @param bytes
     */
    public void createCmdPacket(byte cmd, byte[] bytes){
        PackData packData = new PackData();

        if (bytes == null){
            packData.setLen((byte) (2&0xFF));
            packData.setCmd(cmd);

            sendData(packData);
        }else {
            packData.setLen((byte) ((bytes.length+2)&0xFF));
            packData.setCmd(cmd);
            packData.setDataBody(bytes);

            sendData(packData);
        }

    }

    int sendLen = BleController.getInstance().MTU;
    int offset = 0, sendLegnth;
    private synchronized void sendData(PackData packet) {

        Log.w(TAG,packet.toString());
        byte[] frameStream =  packet.toStreamPack(packet);
        int totalLegnth = frameStream.length;
        while(totalLegnth > 0) {
            if(totalLegnth >= sendLen) {
                sendLegnth = sendLen;
                bleTransfer(HexDump.getSubArray(frameStream,offset,sendLegnth));
                totalLegnth -= sendLen;
                offset += sendLegnth;
            } else {
                sendLegnth = totalLegnth;
                bleTransfer(HexDump.getSubArray(frameStream,offset,sendLegnth));
                totalLegnth = 0;
                offset = 0;
            }

            //加点延时
            SystemClock.sleep(10);
        }

    }

    /***
     * 蓝牙发送通道
     */
    private static synchronized void bleTransfer(final byte[] commData) {
        if (commData == null)
            return;
        BleController.getInstance().transfer(commData, new OnWriteDataCallback() {
            @Override
            public void onSuccess() {

//                ToastUtils.showShort("Ble发送数据成功");
            }

            @Override
            public void onFailed(int i) {
//                DeviceComm.COMM_CODE = 0x00;
//                ToastUtils.showShort("Ble发送数据失败");

            }
        });


    }



}
