package com.roy.www.smartwheelchair.mvp.base;

import com.roy.www.smartwheelchair.mvp.v.IView;

import java.util.Map;

/**
 * Created by 李杨
 * On 2021/8/1
 * Email: 631934797@qq.com
 * Description:
 */
public class BaseContract {

    public interface View extends IView {
        /**
         *
         * @param o
         */
        void showArticleSuccess(Object o);

        /**
         *
         * @param errorMsg
         */
        void showArticleFail(String errorMsg);
    }

    public interface Presenter {
        /**
         *
         * @param url
         * @param token
         * @param map
         * @param c
         */
        void doPost(String url, String token, Map<String,Object> map,Class c);


        /**
         *
         * @param url
         * @param token
         * @param mapValue
         * @param c
         */
        void doGet(String url, String token,Map<String,Object> mapValue,Class c);

    }

}
