package com.roy.www.smartwheelchair.mvp.base;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.roy.www.smartwheelchair.service.MqttService;
import com.tencent.mmkv.MMKV;
import com.uuzuche.lib_zxing.ZApplication;

/**
 * Created by 李杨
 * On 2021/6/21
 * Email: 631934797@qq.com
 * Description:
 */
public class WheelchairApplication extends ZApplication {


    private static final String TAG = "@@@ ===> " + WheelchairApplication.class.getSimpleName();

    public final static boolean DEBUG = true;

    public static Context mWheelchairContext;

    public static  MMKV  mmkv;

    public static final String DEVICE_ID = "L2108003";
//    public static final String DEVICE_ID = "850f871b3ce9ec92";
//    public static final String DEVICE_ID = "7G4W2ID9TD";





    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "KidsApplication ==> onCreate()");
        /// 初始化工具集
        Utils.init(this);
        MMKV.initialize(this);
        mWheelchairContext = this;

        mmkv = MMKV.defaultMMKV();

        mmkv.encode("DEVICE_ID",DEVICE_ID);

        this.startService(MqttService.newMqttServiceIntent(this, "connect"));

    }



    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public static Context getContext() {
        return mWheelchairContext;
    }




}
