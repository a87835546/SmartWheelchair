package com.roy.www.smartwheelchair.mvp.v.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.library.shape.ShapeButton;
import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.utils.IntentUtil;
import com.roy.www.smartwheelchair.widget.CountDownText;
import com.roy.www.smartwheelchair.widget.ImgEditText;
import com.vondear.rxtool.view.RxToast;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginGetVercodeActivity extends AppCompatActivity implements TextWatcher {
    @BindView(R.id.iedt_login_getvode)
    ImgEditText mImgEdtVercode;

    @BindView(R.id.tv_login_to_register)
    TextView mTvLoginToRegister;

    @BindView(R.id.tv_login_to_account)
    TextView mTvLoginByAcoount;
    @BindView(R.id.cdt_login_get_verification_code)
    CountDownText sCdtGetVerCode;

    @BindView(R.id.sbtn_login_getvercode_login)
    ShapeButton mSbtnLogin;

    private String phoneNum = "";

    private String verCode = "";


    @OnClick({R.id.iv_login_back, R.id.tv_login_to_register, R.id.tv_login_to_account, R.id.sbtn_login_getvercode_login,
            R.id.iv_login_by_weixin, R.id.iv_login_by_qq, R.id.iv_login_by_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_login_back:

                finish();

                break;
            case R.id.tv_login_to_register:

                IntentUtil.startActivity(this, RegiseterAcitivity1.class, true);

                break;
            case R.id.tv_login_to_account:


                break;
            case R.id.sbtn_login_getvercode_login:

                //
                verCode = mImgEdtVercode.getText().toString().trim();

                if (TextUtils.isEmpty(verCode) || verCode.length() != 6) {
                    RxToast.showToast("请输入正确的验证码");
                    return;
                }

                //调用注册接口，如果手机号码第一次注册，去到设置密码页面，否则提示该用户已经注册


                break;
            case R.id.iv_login_by_weixin:

                break;
            case R.id.iv_login_by_qq:

                break;
            case R.id.iv_login_by_other:

                break;


        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_get_vercode);
        ButterKnife.bind(this);
        initViews();
        phoneNum = getIntent().getExtras().getString("phonenum");

        //调用获取验证码接口
        sCdtGetVerCode.start();
        mImgEdtVercode.addTextChangedListener(this);

    }


    private void initViews() {
        mImgEdtVercode.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
            @Override
            public void rightDrawableClick() {
                mImgEdtVercode.setText("");
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
        isVercodeNotNull = mImgEdtVercode.getText().toString().trim().length() > 0;
        mSbtnLogin.setEnabled(isVercodeNotNull);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
