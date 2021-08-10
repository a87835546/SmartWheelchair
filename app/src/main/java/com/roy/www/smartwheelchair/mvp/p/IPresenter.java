package com.roy.www.smartwheelchair.mvp.p;

import com.roy.www.smartwheelchair.mvp.v.IView;


/**
 * Created by 李杨
 * On 2021/6/21
 * Email: 631934797@qq.com
 * Description:
 */
public interface IPresenter<V extends IView> {


    /**
     * 依附生命view
     *
     * @param view
     */
    void attachView(V view);

    /**
     * 分离View
     */
    void detachView();

    /**
     * 判断View是否已经销毁
     *
     * @return
     */
    boolean isViewAttached();
}
