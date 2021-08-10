package com.roy.www.smartwheelchair.ble.callback;


import com.roy.www.smartwheelchair.ble.protocol.PackData;

/**
 * Created by 李杨
 * On 2021/6/20
 * Email: 631934797@qq.com
 * Description:  接收设备向手机发送的广播数据接口
 */
public interface OnReceiverCallback {

    void onReceive(byte[] bytes);
}
