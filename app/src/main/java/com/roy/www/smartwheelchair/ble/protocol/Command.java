package com.roy.www.smartwheelchair.ble.protocol;

/**
 * Created by 李杨
 * On 2021/6/20
 * Email: 631934797@qq.com
 * Description:
 */

public class Command {


    public static final byte HEAD = (byte) 0xA8; //包头
    public static final byte TAIL = (byte) 0xA9; //包尾
    // 状态码
    public static final byte STATUS_SUCCEED = (byte) 0xC8; //成功
    public static final byte STATUS_FAIL    = (byte) 0xC9; //失败


    public final static byte CMD_ACTION_ADVANCE	  = (byte) 0xA1;	// 前进
    public final static byte CMD_ACTION_RETREAT	  = (byte) 0xA2;	// 后退
    public static final byte CMD_ACTION_LEFT      = (byte) 0xA3;	// 向左
    public static final byte CMD_ACTION_RIGHT     = (byte) 0xA4;	// 向右
    public static final byte CMD_ACTION_STOP      = (byte) 0xA5;	// 停止
    public static final byte CMD_ACTION_SET_WIFI  = (byte) 0xA6;	// 设置WIFI


}
