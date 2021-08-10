package com.roy.www.smartwheelchair.mvp.p;

import com.roy.www.smartwheelchair.mvp.base.BasePresenter;
import com.roy.www.smartwheelchair.mvp.base.BaseContract;
import com.roy.www.smartwheelchair.mvp.interfaces.Callback;
import com.roy.www.smartwheelchair.mvp.m.GeoFenceModelImpl;

import java.util.Map;

/**
 * Created by 李杨
 * On 2021/8/1
 * Email: 631934797@qq.com
 * Description:
 */
public class GeoFencePresenter extends BasePresenter<BaseContract.View> implements BaseContract.Presenter  {


    private final GeoFenceModelImpl mGeoFenceModelImpl;


    public GeoFencePresenter() {
        this.mGeoFenceModelImpl = new GeoFenceModelImpl();
    }

    @Override
    public void doPost(String url, String token, Map<String, Object> map,Class c) {
        mGeoFenceModelImpl.doPost(url, token, map,c ,new Callback() {
            @Override
            public void onSuccess(Object data) {
                if (isViewAttached()) {
                    mView.showArticleSuccess(data);
                }
            }

            @Override
            public void onFail(Object data) {
                if (isViewAttached()) {
                    mView.showArticleSuccess(data);
                }
            }
        });
    }

    @Override
    public void doGet(String url, String token, Map<String, Object> mapValue,Class c) {
        mGeoFenceModelImpl.doGet(url, token, mapValue, c,new Callback() {
            @Override
            public void onSuccess(Object data) {
                if (isViewAttached()) {
                    mView.showArticleSuccess(data);
                }
            }

            @Override
            public void onFail(Object data) {
                if (isViewAttached()) {
                    mView.showArticleSuccess(data);
                }
            }
        });
    }
}
