package com.roy.www.smartwheelchair.mvp.interfaces;

/**
 * Created by 李杨
 * On 2021/6/21
 * Email: 631934797@qq.com
 * Description:
 */
public interface Callback<K, V> {


    void onSuccess(K data);


    void onFail(V data);


}
