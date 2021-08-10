package com.roy.www.smartwheelchair.mvp.bean;

/**
 * Created by 李杨
 * On 2021/6/21
 * Email: 631934797@qq.com
 * Description:
 */
public class LoginBean {


    /**
     * data : f4fa7637-fcaf-a84e-9f2c-39fd6413e03b
     * statusCode : 200
     * message : 请求(或处理)成功
     * timestamp : 1624871733
     */

    private String data;
    private int statusCode;
    private String message;
    private String timestamp;

    public String getData() {
        return data;
    }

    public void setData(String data) {
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

    @Override
    public String toString() {
        return "LoginBean{" +
                "data='" + data + '\'' +
                ", statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
