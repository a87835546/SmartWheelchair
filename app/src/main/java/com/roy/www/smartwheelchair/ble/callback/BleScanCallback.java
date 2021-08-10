package com.roy.www.smartwheelchair.ble.callback;

import android.bluetooth.BluetoothDevice;


/**
 * Created by 李杨
 * On 2021/6/20
 * Email: 631934797@qq.com
 * Description:  扫描设备接口
 */

public interface BleScanCallback {

    void onScanning(BluetoothDevice device, int rssi, byte[] scanRecord);


    void onSuccess();
}
