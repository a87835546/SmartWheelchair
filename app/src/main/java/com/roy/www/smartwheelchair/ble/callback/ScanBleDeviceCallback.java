package com.roy.www.smartwheelchair.ble.callback;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;


/**
 * Created by 李杨
 * On 2021/6/20
 * Email: 631934797@qq.com
 * Description:  扫描BLE设备回调
 */

public class ScanBleDeviceCallback implements BluetoothAdapter.LeScanCallback {

   private BleScanCallback mBleScanCallback;

    public ScanBleDeviceCallback(BleScanCallback onLeScanCallback) {
        this.mBleScanCallback = onLeScanCallback;
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (mBleScanCallback != null){
            mBleScanCallback.onScanning(device,rssi,scanRecord);
        }

    }
}
