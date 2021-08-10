package com.roy.www.smartwheelchair.mvp.v.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.library.SuperButton;
import com.allen.library.shape.ShapeButton;
import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.utils.IntentUtil;
import com.roy.www.smartwheelchair.utils.Validate;
import com.roy.www.smartwheelchair.widget.ImgEditText;
import com.vondear.rxtool.RxPhotoTool;
import com.vondear.rxtool.RxRegTool;
import com.vondear.rxtool.RxTool;
import com.vondear.rxtool.RxVibrateTool;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class RegiseterAcitivity1 extends AppCompatActivity implements TextWatcher {

    @BindView(R.id.iet_register_phonenum)
    ImgEditText mImgEdtPhoneNum;

    @BindView(R.id.tv_register_tologin)
    TextView mTvRegisterToLogin;

    @BindView(R.id.cb_register_aggrement)
    CheckBox mCkbRegisterAgg;

    @BindView(R.id.tv_register_to_user_aggrement)
    TextView mTvRegisterToUserAgg;

    @BindView(R.id.tv_register_to_user_privacy)
    TextView mTvRisterToUserPri;

    @BindView(R.id.sbtn_register_next)
    ShapeButton mSbtnRegisterToNext;

    private boolean isCheckBoxSelect= true;

    private boolean isPhoneNum = false;

    private String phoneNum;



    @OnClick({R.id.tv_register_tologin, R.id.tv_register_to_user_aggrement, R.id.tv_register_to_user_privacy,R.id.sbtn_register_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_register_tologin:

                IntentUtil.startActivity(this,LoginActivity1.class,true);

                break;
            case R.id.tv_register_to_user_aggrement:
                IntentUtil.startActivity(this,WebViewActivity.class,false);
                break;
            case R.id.tv_register_to_user_privacy:

                IntentUtil.startActivity(this,WebViewActivity.class,false);
                break;
            case R.id.sbtn_register_next:

                if (!isCheckBoxSelect){
                    RxToast.showToast("请阅读并勾选用户协议");
                    return;
                }else{
                    IntentUtil.startActivity(this,RegisterGetVerCodeActivity.class,false);

                }

                break;

        }
    }

    @OnCheckedChanged(R.id.cb_register_aggrement)
    public void onCheckChanged(CheckBox view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.cb_register_aggrement:
                isCheckBoxSelect = isChecked;
                break;
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initViews();
        mImgEdtPhoneNum.addTextChangedListener(this);
        mTvRegisterToLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }


    private void initViews() {
        mImgEdtPhoneNum.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
            @Override
            public void rightDrawableClick() {
                mImgEdtPhoneNum.setText("");
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        phoneNum = mImgEdtPhoneNum.getText().toString().trim();
        isPhoneNum = RxRegTool.isMobile(phoneNum);
        mSbtnRegisterToNext.setEnabled(isPhoneNum);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
