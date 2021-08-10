package com.roy.www.alirtc.bean;

import java.util.List;

/**
 * Created by 李杨
 * On 2021/8/6
 * Email: 631934797@qq.com
 * Description:
 */
public class AliAuthInfoBean {


    /**
     * data : {"appid":"f54jbmek","userid":"2bb9411de67f0517","channelId":"859608","nonce":"AK-4997b5b4-a294-433f-a829-cc46ae22a435","timestamp":"1628413051","gslb":["https://rgslb.rtc.aliyuncs.com"],"token":"7524d71a8885ed562f1f13146015f36ea8b16be29dc88c0b4f85940b0c403c49","expireTime":"2021-08-08 16:57:31","turn":{"username":"12453","password":"7524d71a8885ed562f1f13146015f36ea8b16be29dc88c0b4f85940b0c403c49"},"conferenceId":"859608"}
     * statusCode : 200
     * message : 请求(或处理)成功
     * timestamp : 1628240251
     */

    private RTCAuthInfo.RTCAuthInfo_Data data;
    private int statusCode;
    private String message;
    private String timestamp;

    public RTCAuthInfo.RTCAuthInfo_Data getData() {
        return data;
    }

    public void setData(RTCAuthInfo.RTCAuthInfo_Data data) {
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

    public static class DataBean {
        /**
         * appid : f54jbmek
         * userid : 2bb9411de67f0517
         * channelId : 859608
         * nonce : AK-4997b5b4-a294-433f-a829-cc46ae22a435
         * timestamp : 1628413051
         * gslb : ["https://rgslb.rtc.aliyuncs.com"]
         * token : 7524d71a8885ed562f1f13146015f36ea8b16be29dc88c0b4f85940b0c403c49
         * expireTime : 2021-08-08 16:57:31
         * turn : {"username":"12453","password":"7524d71a8885ed562f1f13146015f36ea8b16be29dc88c0b4f85940b0c403c49"}
         * conferenceId : 859608
         */

        private String appid;
        private String userid;
        private String channelId;
        private String nonce;
        private String timestamp;
        private String token;
        private String expireTime;
        private TurnBean turn;
        private String conferenceId;
        private List<String> gslb;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public TurnBean getTurn() {
            return turn;
        }

        public void setTurn(TurnBean turn) {
            this.turn = turn;
        }

        public String getConferenceId() {
            return conferenceId;
        }

        public void setConferenceId(String conferenceId) {
            this.conferenceId = conferenceId;
        }

        public List<String> getGslb() {
            return gslb;
        }

        public void setGslb(List<String> gslb) {
            this.gslb = gslb;
        }

        public static class TurnBean {
            /**
             * username : 12453
             * password : 7524d71a8885ed562f1f13146015f36ea8b16be29dc88c0b4f85940b0c403c49
             */

            private String username;
            private String password;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }
        }
    }
}
