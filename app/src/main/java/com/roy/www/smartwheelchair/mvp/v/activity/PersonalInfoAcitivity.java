package com.roy.www.smartwheelchair.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.allen.library.SuperTextView;
import com.roy.www.smartwheelchair.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInfoAcitivity extends AppCompatActivity {


    @BindView(R.id.tv_top_titel)
    TextView ivTitle;

    @BindView(R.id.stv_personinfo_account)
    SuperTextView mStvAccount;

    @BindView(R.id.stv_personinfo_nickname)
    SuperTextView mStvNickname;

    @BindView(R.id.stv_personinfo_phonenum)
    SuperTextView mStvPhoneNum;

    @OnClick({R.id.iv_top_back,R.id.stv_personinfo_account,R.id.stv_personinfo_nickname,R.id.stv_personinfo_phonenum,R.id.stv_personinfo_changepwd,})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.iv_top_back:
                finish();
                break;

            case R.id.stv_personinfo_account:


                break;

            case R.id.stv_personinfo_nickname:


                break;

            case R.id.stv_personinfo_phonenum:


                break;
            case R.id.stv_personinfo_changepwd:


                break;

        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personalinfo);
        ButterKnife.bind(this);
        initViews();

    }

    private void initViews() {
        ivTitle.setText("个人中心");
        mStvAccount.setRightString("123");
        mStvNickname.setRightString("123");
        mStvPhoneNum.setRightString("123");
    }
}
