package com.roy.www.smartwheelchair.mvp.m;

import com.roy.www.smartwheelchair.api.HttpResponse;
import com.roy.www.smartwheelchair.api.HttpUtil;
import com.roy.www.smartwheelchair.mvp.interfaces.Callback;

import java.util.Map;

/**
 * Created by 李杨
 * On 2021/8/2
 * Email: 631934797@qq.com
 * Description:
 */
public class HomeModelImpl implements IBaseModel {
    @Override
    public void doPost(String url, String teken, Map<String, Object> map, Class c, Callback callback) {
        HttpUtil.getInstance().doPost(url, teken,map, new HttpResponse(c) {
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
    public void doGet(String url, String teken, Map<String, Object> mapValue,Class c, Callback callback) {
        HttpUtil.getInstance().doGet(url, teken,mapValue, new HttpResponse(c) {
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
