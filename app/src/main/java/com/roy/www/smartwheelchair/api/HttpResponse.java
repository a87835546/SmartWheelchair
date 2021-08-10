package com.roy.www.smartwheelchair.api;

import android.text.TextUtils;

import com.roy.www.smartwheelchair.utils.GsonUtil;

/**
 * Created by 李杨
 * On 2021/6/25
 * Email: 631934797@qq.com
 * Description:
 */
public abstract class HttpResponse<T> {
    Class<T> t = null;

    public HttpResponse(Class<T> t) {
        this.t = t;
    }

    //网络请求成功后的回调方法
    public abstract void onSuccess(T t);

    //网络请求失败后的回调方法
    public abstract void onError(String msg);

    public void parse(String json) {
        //返回的json为空
        if (TextUtils.isEmpty(json)) {
            onError("网络请求失败");
            return;
        }
        //httpReson String 类型的话,
        if(t==String.class){
            onSuccess((T)json);
            return;
        }
        //tmp_t 就是我们根据网络请求后解析的结果,它的类型是有T 来决定
        T tmp_t = GsonUtil.parse(json, t);
        if (tmp_t == null) {
            onError("网络请求失败");
        }else{
            onSuccess(tmp_t);
        }
    }
}
