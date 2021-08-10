package com.roy.www.smartwheelchair.ble.callback;


/**
 * Created by 李杨
 * On 2021/6/20
 * Email: 631934797@qq.com
 * Description:  写数据操作接口
 */
public interface OnWriteDataCallback {

    /**
     * 蓝牙未开启
     */
    int FAILED_BLUETOOTH_DISABLE = 1;
    /**
     * 服务无效
     */
    int FAILED_INVALID_SERVICE = 2;
    /**
     * 特征无效
     */
    int FAILED_INVALID_CHARACTER = 3;
    /**
     * 操作失败
     */
    int FAILED_OPERATION = 5;

    /**
     * 写入成功
     */
    void onSuccess();

    /**
     * 写入失败
     *
     * @param state
     */
    void onFailed(int state);
}
