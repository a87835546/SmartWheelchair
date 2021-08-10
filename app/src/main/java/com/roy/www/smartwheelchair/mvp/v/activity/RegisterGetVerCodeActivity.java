package com.roy.www.smartwheelchair.mvp.v.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.library.shape.ShapeButton;
import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.utils.IntentUtil;
import com.roy.www.smartwheelchair.widget.CountDownText;
import com.roy.www.smartwheelchair.widget.ImgEditText;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterGetVerCodeActivity extends AppCompatActivity implements TextWatcher {


    @BindView(R.id.iv_register_back)
    ImageView mIvBack;
    @BindView(R.id.iedt_register_getvode)
    ImgEditText mIedtVercode;
    @BindView(R.id.cdt_get_verification_code)
    CountDownText sCdtGetVerCode;

    @BindView(R.id.sbtn_register_getvercode_next)
    ShapeButton mSbtnNext;

    private String verCode;

    @OnClick({R.id.iv_register_back, R.id.iedt_register_getvode, R.id.tv_register_tologin, R.id.cdt_get_verification_code,
            R.id.sbtn_register_getvercode_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_register_back:
                finish();
                break;
            case R.id.iedt_register_getvode:
                //调用

                break;
            case R.id.tv_register_tologin:
                //去到登录页面
                IntentUtil.startActivity(this, LoginActivity1.class, true);
                break;

            case R.id.cdt_get_verification_code:
                //重新获取
                sCdtGetVerCode.start();
                break;


            case R.id.sbtn_register_getvercode_next:

                //
                verCode = mIedtVercode.getText().toString().trim();

                if (TextUtils.isEmpty(verCode) || verCode.length() != 6) {
                    RxToast.showToast("请输入正确的验证码");
                    return;
                }

                //调用注册接口，如果手机号码第一次注册，去到设置密码页面，否则提示该用户已经注册

                IntentUtil.startActivity(this, RegisterSetPwdActivity.class, false);
                break;


        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_get_vercode);
        ButterKnife.bind(this);
        initViews();

        //调用获取验证码接口
        sCdtGetVerCode.start();
        sCdtGetVerCode.addTextChangedListener(this);

    }


    private void initViews() {
        mIedtVercode.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
            @Override
            public void rightDrawableClick() {
                mIedtVercode.setText("");
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    private boolean isVercodeNotNull = false;

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        //验证码数量大于0 ，按钮才可用
        isVercodeNotNull = mIedtVercode.getText().toString().trim().length() > 0;
        mSbtnNext.setEnabled(isVercodeNotNull);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
