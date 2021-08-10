package com.roy.www.smartwheelchair.mvp.bean;

/**
 * Created by 李杨
 * On 2021/8/2
 * Email: 631934797@qq.com
 * Description:
 */
public class UserInfoBean {


    /**
     * data : {"userId":"bc5c0e36-41e2-11eb-b573-2c337a4c8644","tenantId":"00000000-0000-0000-0000-000000000000","userName":"admin","nickName":"系统管理员","sex":"1","avatarUrl":null,"mobile":"18189279827","mobileIsVerified":true,"createTime":"2021-06-24 12:13:20","enabled":true,"oneSession":false,"source":"Web","keepHours":24,"administrator":false,"userPower":null}
     * statusCode : 200
     * message : 请求(或处理)成功
     * timestamp : 1627909433
     */

    private DataBean data;
    private int statusCode;
    private String message;
    private String timestamp;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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
         * userId : bc5c0e36-41e2-11eb-b573-2c337a4c8644
         * tenantId : 00000000-0000-0000-0000-000000000000
         * userName : admin
         * nickName : 系统管理员
         * sex : 1
         * avatarUrl : null
         * mobile : 18189279827
         * mobileIsVerified : true
         * createTime : 2021-06-24 12:13:20
         * enabled : true
         * oneSession : false
         * source : Web
         * keepHours : 24
         * administrator : false
         * userPower : null
         */

        private String userId;
        private String tenantId;
        private String userName;
        private String nickName;
        private String sex;
        private Object avatarUrl;
        private String mobile;
        private boolean mobileIsVerified;
        private String createTime;
        private boolean enabled;
        private boolean oneSession;
        private String source;
        private int keepHours;
        private boolean administrator;
        private Object userPower;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Object getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(Object avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public boolean isMobileIsVerified() {
            return mobileIsVerified;
        }

        public void setMobileIsVerified(boolean mobileIsVerified) {
            this.mobileIsVerified = mobileIsVerified;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isOneSession() {
            return oneSession;
        }

        public void setOneSession(boolean oneSession) {
            this.oneSession = oneSession;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getKeepHours() {
            return keepHours;
        }

        public void setKeepHours(int keepHours) {
            this.keepHours = keepHours;
        }

        public boolean isAdministrator() {
            return administrator;
        }

        public void setAdministrator(boolean administrator) {
            this.administrator = administrator;
        }

        public Object getUserPower() {
            return userPower;
        }

        public void setUserPower(Object userPower) {
            this.userPower = userPower;
        }
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "data=" + data +
                ", statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
