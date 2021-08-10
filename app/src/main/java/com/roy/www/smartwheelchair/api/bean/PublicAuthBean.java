package com.roy.www.smartwheelchair.api.bean;

/**
 * Created by 李杨
 * On 2021/6/25
 * Email: 631934797@qq.com
 * Description:
 */
public class PublicAuthBean {

    /**
     * statusCode : 500
     * message : 用户名或密码错误
     * timestamp : 1624631727
     */

    private int statusCode;
    private String message;
    private String timestamp;

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
