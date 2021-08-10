package com.roy.www.smartwheelchair.mvp.m;

import com.roy.www.smartwheelchair.mvp.interfaces.Callback;

import java.util.Map;

/**
 * Created by 李杨
 * On 2021/8/1
 * Email: 631934797@qq.com
 * Description:
 */
public interface IBaseModel {


    /**
     *
     * @param url
     * @param teken
     * @param map
     * @param c
     * @param callback
     */
    void doPost(String url, String teken, Map<String,Object> map,Class c, Callback callback);


    /**
     *
     * @param url
     * @param teken
     * @param map
     * @param c
     * @param callback
     */
    void doGet(String url, String teken, Map<String, Object> map, Class c, Callback callback);
}
