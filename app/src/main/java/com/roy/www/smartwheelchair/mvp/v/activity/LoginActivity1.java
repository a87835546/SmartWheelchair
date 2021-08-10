package com.roy.www.smartwheelchair.mvp.v.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.library.shape.ShapeButton;
import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.utils.IntentUtil;
import com.roy.www.smartwheelchair.utils.Validate;
import com.roy.www.smartwheelchair.widget.ImgEditText;
import com.vondear.rxtool.RxRegTool;
import com.vondear.rxtool.view.RxToast;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity1 extends AppCompatActivity implements TextWatcher {
    @BindView(R.id.iet_login_phonennum)
    ImgEditText mImgEdtPhoneNum;

    @BindView(R.id.tv_login_to_register)
    TextView mTvLoginToRegister;

    @BindView(R.id.tv_login_by_account)
    TextView mTvLoginByAcoount;
    @BindView(R.id.sbtn_login_next)
    ShapeButton  mSbtnLoginNext;

    private String phoneNum= "";

    private boolean isPhonNum = false;


    @OnClick({R.id.tv_login_to_register, R.id.tv_login_by_account,R.id.sbtn_login_next,
            R.id.iv_login_by_weixin, R.id.iv_login_by_qq,R.id.iv_login_by_other})
    public void onViewClicked(View view) {

        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_login_to_register:

                IntentUtil.startActivity(this,RegiseterAcitivity1.class,true);

                break;
            case R.id.tv_login_by_account:


                break;
            case R.id.sbtn_login_next:
                phoneNum = mImgEdtPhoneNum.getText().toString().trim();
//                if (!Validate.RegExValidate(phoneNum, Validate.REGEX_MOBILE)) {
//                    RxToast.showToastShort("请输入正确的电话号码");
//                    return;
//                }
                //
                intent = new Intent(this,LoginGetVercodeActivity.class);
                intent.putExtra("phoneNum",phoneNum);
                startActivity(intent);

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

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initViews();
        mTvLoginToRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvLoginByAcoount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mImgEdtPhoneNum.addTextChangedListener(this);

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
        isPhonNum = RxRegTool.isMobile(phoneNum);
        mSbtnLoginNext.setEnabled(isPhonNum);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
