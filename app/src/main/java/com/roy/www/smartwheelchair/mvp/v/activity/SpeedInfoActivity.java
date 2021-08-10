package com.roy.www.smartwheelchair.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.roy.www.smartwheelchair.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 李杨
 * On 2021/7/21
 * Email: 631934797@qq.com
 * Description:
 */
public class SpeedInfoActivity extends AppCompatActivity {

    @BindView(R.id.tv_top_titel)
    TextView tvTopTitel;
    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.iv_top_set)
    ImageView ivTopSet;
    @BindView(R.id.ll_but_speed_info)
    LinearLayout llButSpeedInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_info);
        ButterKnife.bind(this);
        ivTopSet.setVisibility(View.GONE);
    }



    @OnClick({ R.id.iv_top_back, R.id.iv_top_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_back:
                finish();
                break;
            case R.id.iv_top_set:
                break;
        }
    }
}
