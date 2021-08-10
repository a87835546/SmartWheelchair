package com.roy.www.smartwheelchair.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Roy on 2017/6/16.
 */

public class IntentUtil {
    /**
     * 延迟跳转到指定的界面,并关闭当前界面
     *
     * @param activity 当前界面
     * @param clz      指定界面的权限定名
     * @param time     延迟时间
     */

    public static void startActivityForDelayAndFinish(final Activity activity, final Class<?> clz, final long time, final boolean flag) {
        //延迟操作
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //创建一个Intent对象
                Intent intent = new Intent(activity, clz);
                //界面跳转
                activity.startActivity(intent);
                //是否关闭当前界面
                if(flag){
                    activity.finish();
                }
            }
        }.start();
    }



    /**
     * 从当前界面跳转到指定的界面,并判断是否关闭当前界面
     *
     * @param activity 当前界面
     * @param clz      指定界面的权限定名
     * @param flag     标记
     */
    public static void startActivity(Activity activity, Class<?> clz, boolean flag) {
        //创建一个Intent对象
        Intent intent = new Intent(activity, clz);
        //界面跳转
        activity.startActivity(intent);
        //是否关闭当前界面
        if(flag){
            activity.finish();
        }
    }

}
