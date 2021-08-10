package com.roy.www.smartwheelchair.mvp.bean;

/**
 * Created by 李杨
 * On 2021/7/27
 * Email: 631934797@qq.com
 * Description:
 */
public class RegisterBean {

    /**
     * data : 1
     * statusCode : 200
     * message : 请求(或处理)成功
     * timestamp : 1627362897
     */

    private int data;
    private int statusCode;
    private String message;
    private String timestamp;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
