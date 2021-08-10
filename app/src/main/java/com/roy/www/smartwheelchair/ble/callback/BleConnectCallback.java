package com.roy.www.smartwheelchair.ble.callback;


/**
 * Created by 李杨
 * On 2021/6/20
 * Email: 631934797@qq.com
 * Description:  连接设备成功回调接口
 */

public interface BleConnectCallback {

    void onConnSuccess();

    void onConnFailed();

}
