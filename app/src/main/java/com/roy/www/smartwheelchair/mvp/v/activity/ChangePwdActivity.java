package com.roy.www.smartwheelchair.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.widget.ImgEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePwdActivity extends AppCompatActivity {
    @BindView(R.id.tv_top_titel)
    TextView tvTopTitle;
    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.iedt_changepwd_getvode)
    ImgEditText mImgEdtPhoneNum;


    @OnClick({R.id.iv_top_back, R.id.iv_top_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_back:
                this.finish();
                break;
            case R.id.iv_top_set:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_changepwd);
        ButterKnife.bind(this);
        initViews();
        tvTopTitle.setText("修改密碼");
    }

    private void initViews() {
        mImgEdtPhoneNum.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
            @Override
            public void rightDrawableClick() {
                mImgEdtPhoneNum.setText("");
            }
        });
    }


}
