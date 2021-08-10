package com.roy.www.smartwheelchair.mvp.v.activity;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.utils.IntentUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 李杨
 * On 2021/7/6
 * Email: 631934797@qq.com
 * Description:
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.iv_splash_icon)
    ImageView ivSplashIcon;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            IntentUtil.startActivity(SplashActivity.this,LoginActivity1.class, true);
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

//        makeStatusBarTransparent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //延迟控制界面跳转
        initAnimation();
        mHandler.sendEmptyMessageDelayed(1, 3500);

    }

    /**
     * 渐变动画
     */
    private void initAnimation() {
        //创建一个渐变动画
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(3500l);//设置动画时间
        aa.setFillAfter(true);//设置停留最后一帧
        ivSplashIcon.startAnimation(aa);//开启动画
    }

    /*设置透明状态栏*/
    private void makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
