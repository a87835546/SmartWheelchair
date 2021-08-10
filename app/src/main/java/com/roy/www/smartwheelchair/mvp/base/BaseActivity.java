package com.roy.www.smartwheelchair.mvp.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.roy.www.smartwheelchair.ble.BleController;
import com.roy.www.smartwheelchair.mvp.p.IPresenter;
import com.roy.www.smartwheelchair.mvp.v.IView;
import com.vondear.rxtool.RxActivityTool;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 李杨
 * On 2021/6/21
 * Email: 631934797@qq.com
 * Description:
 */
public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView {


    //申请权限
    final static String[] REQUEST_PERMISSION = {
            Manifest.permission.INTERNET,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE,


            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.FOREGROUND_SERVICE

    };
    //权限码
    final int REQUEST_CODE = 1;
    protected boolean mIsPermission = false;


    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxActivityTool.addActivity(this);
        isCheckPermission();
        initPresenter();

        initView(savedInstanceState);
        BleController.getInstance().initBle(this);
    }

    protected void initPresenter() {
        mPresenter = createPresenter();
        //绑定生命周期
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        super.onDestroy();
        RxActivityTool.finishActivity(this);
    }

    /**
     * 创建一个Presenter
     * @return
     */
    protected abstract P createPresenter();

    protected abstract void initView(@Nullable Bundle savedInstanceState);


    /**
     * 申请权限
     */
    protected void isCheckPermission() {
        //判断当前系统是否android.6l.0以及以上
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            //遍历权限组，检查是否拥有权限，只要有一个权限不满足就申请权限
            for (String permiss : REQUEST_PERMISSION) {
                if (ActivityCompat.checkSelfPermission(this, permiss) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(this, REQUEST_PERMISSION, REQUEST_CODE);
                }
            }
        }
    }


    /**
     *  申请权限回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断是否取得权限
        if (requestCode == REQUEST_CODE) {
            //执行读写操作
            Log.e("TAG", "onRequestPermissionsResult " + "获得权限");
            mIsPermission = true;


        } else {
            mIsPermission = false;
        }
    }

}
