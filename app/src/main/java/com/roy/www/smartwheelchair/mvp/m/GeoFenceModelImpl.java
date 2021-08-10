package com.roy.www.smartwheelchair.mvp.m;

import com.roy.www.smartwheelchair.api.HttpResponse;
import com.roy.www.smartwheelchair.api.HttpUtil;
import com.roy.www.smartwheelchair.mvp.bean.FenceInfoBean;
import com.roy.www.smartwheelchair.mvp.bean.GeoFenceBean;
import com.roy.www.smartwheelchair.mvp.interfaces.Callback;

import java.util.Map;

/**
 * Created by 李杨
 * On 2021/8/1
 * Email: 631934797@qq.com
 * Description:
 */
public class GeoFenceModelImpl implements IBaseModel {



    @Override
    public void doPost(String url, String token, Map<String, Object> map, Class c,Callback callback) {
        HttpUtil.getInstance().doPost(url, token,map, new HttpResponse(c) {
            @Override
            public void onSuccess(Object o) {
                callback.onSuccess(o);

            }

            @Override
            public void onError(String msg) {
                callback.onFail(msg);
            }
        });
    }

    @Override
    public void doGet(String url, String token, Map<String, Object> map, Class c,Callback callback) {
        HttpUtil.getInstance().doGet(url, token, map, new HttpResponse(c) {
            @Override
            public void onSuccess(Object o) {
                callback.onSuccess(o);
            }

            @Override
            public void onError(String msg) {
                callback.onFail(msg);
            }
        });
    }
}
