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
import com.roy.www.smartwheelchair.widget.ImgEditText;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterSetPwdActivity extends AppCompatActivity implements TextWatcher {


    @BindView(R.id.iedt_register_pwd1)
    ImgEditText mIedtPwd1;

    @BindView(R.id.iedt_register_pwd2)
    ImgEditText mIedtPwd2;

    @BindView(R.id.tv_register_pwd_note)
    TextView mTvNote;

    @BindView(R.id.sbtn_register_set_pwd_confirm)
    ShapeButton mSbtnConfirm;

    private String mPassword;

    private String mPasswordConfirm;

    @OnClick(R.id.sbtn_register_set_pwd_confirm)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sbtn_register_set_pwd_confirm:

                mPassword = mIedtPwd1.getText().toString().trim();
                mPasswordConfirm = mIedtPwd2.getText().toString().trim();

                if (TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mPasswordConfirm)) {
                    mTvNote.setText("请设置你的密码");
                    mTvNote.setVisibility(View.VISIBLE);
                    return;
                }

                if (!mPassword.equals(mPasswordConfirm)) {
                    mTvNote.setText("两次输入的密码不一致");
                    mTvNote.setVisibility(View.VISIBLE);
                    return;
                }

                //如果输入的密码一致,调用设置密码接口，成功后，直接用登录接口登录


                break;
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_set_pwd);
        ButterKnife.bind(this);
        initViews();
        mIedtPwd2.addTextChangedListener(this);
        mIedtPwd2.addTextChangedListener(this);
    }


    private void initViews() {
        mIedtPwd1.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
            @Override
            public void rightDrawableClick() {
                mIedtPwd1.setText("");
            }
        });
        mIedtPwd2.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
            @Override
            public void rightDrawableClick() {
                mIedtPwd2.setText("");
            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean isPwdNotNull = mIedtPwd1.getText().toString().trim().length() > 0;
        boolean isPwdConfirmNotNull = mIedtPwd2.getText().toString().trim().length() > 0;
        boolean isSbtnConfirmEnable = isPwdNotNull && isPwdConfirmNotNull;
        mSbtnConfirm.setEnabled(isSbtnConfirmEnable);
        mTvNote.setVisibility(View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
