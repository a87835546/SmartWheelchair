package com.roy.www.smartwheelchair.mvp.p;

import com.roy.www.smartwheelchair.mvp.base.BasePresenter;
import com.roy.www.smartwheelchair.mvp.base.BaseContract;
import com.roy.www.smartwheelchair.mvp.interfaces.Callback;
import com.roy.www.smartwheelchair.mvp.m.LoginModelImpl;

import java.util.Map;

/**
 * Created by 李杨
 * On 2021/8/1
 * Email: 631934797@qq.com
 * Description:
 */
public class LoginPresenter extends BasePresenter<BaseContract.View> implements BaseContract.Presenter  {

    private final LoginModelImpl mLoginModelImpl;


    public LoginPresenter() {
        this.mLoginModelImpl = new LoginModelImpl();
    }

    @Override
    public void doPost(String url, String token, Map<String, Object> map,Class c) {
        mLoginModelImpl.doPost(url, token, map, c,new Callback() {
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
    public void doGet(String url, String token, Map<String,Object> value,Class c) {
        mLoginModelImpl.doGet(url, token, value,c, new Callback() {
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
