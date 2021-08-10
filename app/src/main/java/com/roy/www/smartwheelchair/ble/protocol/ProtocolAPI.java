package com.roy.www.smartwheelchair.ble.protocol;




/**
 * Created by 李杨
 * On 2021/6/20
 * Email: 631934797@qq.com
 * Description:
 */
public interface ProtocolAPI {


    /***
     *  前进
     */
    void advance();

    /***
     *  后退
     */
    void retreat();

    /***
     *  向左
     */
    void left();


    /***
     *  向右
     */
    void right();


    /***
     *  停止
     */
    void stop();

    /***
     *  设置WIFI
     */
    void setWifi(String ssid,String password);

}
