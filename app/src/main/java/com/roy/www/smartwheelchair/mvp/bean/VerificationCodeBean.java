package com.roy.www.smartwheelchair.mvp.bean;

/**
 * Created by 李杨
 * On 2021/7/27
 * Email: 631934797@qq.com
 * Description:
 */
public class VerificationCodeBean {

    /**
     * statusCode : 200
     * message : 该用户已存在，请直接登录。
     * timestamp : 1627363421
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
