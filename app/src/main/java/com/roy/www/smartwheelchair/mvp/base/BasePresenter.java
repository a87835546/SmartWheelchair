package com.roy.www.smartwheelchair.mvp.base;

import com.roy.www.smartwheelchair.mvp.p.IPresenter;
import com.roy.www.smartwheelchair.mvp.v.IView;

/**
 * Created by 李杨
 * On 2021/6/21
 * Email: 631934797@qq.com
 * Description:
 */
public abstract class BasePresenter<V extends IView> implements IPresenter<V> {

    protected V mView;

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null;
    }
}
