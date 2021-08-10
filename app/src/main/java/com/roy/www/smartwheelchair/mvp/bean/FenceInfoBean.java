package com.roy.www.smartwheelchair.mvp.bean;

/**
 * Created by 李杨
 * On 2021/8/1
 * Email: 631934797@qq.com
 * Description:
 */
public class FenceInfoBean {


    /**
     * data : {"id":"387e2e77-f0bf-19ff-c662-39fe121f7130","deviceID":"7G4W2ID9TD","gpsTime":"1990-01-01 00:00:00","lng":0,"lat":0,"fenceLng":114.03935102,"fenceLat":22.62302122,"radius":30}
     * statusCode : 200
     * message : 请求(或处理)成功
     * timestamp : 1627826880
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
         * id : 387e2e77-f0bf-19ff-c662-39fe121f7130
         * deviceID : 7G4W2ID9TD
         * gpsTime : 1990-01-01 00:00:00
         * lng : 0.0
         * lat : 0.0
         * fenceLng : 114.03935102
         * fenceLat : 22.62302122
         * radius : 30.0
         */

        private String id;
        private String deviceID;
        private String gpsTime;
        private double lng;
        private double lat;
        private double fenceLng;
        private double fenceLat;
        private double radius;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDeviceID() {
            return deviceID;
        }

        public void setDeviceID(String deviceID) {
            this.deviceID = deviceID;
        }

        public String getGpsTime() {
            return gpsTime;
        }

        public void setGpsTime(String gpsTime) {
            this.gpsTime = gpsTime;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getFenceLng() {
            return fenceLng;
        }

        public void setFenceLng(double fenceLng) {
            this.fenceLng = fenceLng;
        }

        public double getFenceLat() {
            return fenceLat;
        }

        public void setFenceLat(double fenceLat) {
            this.fenceLat = fenceLat;
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }
    }
}
