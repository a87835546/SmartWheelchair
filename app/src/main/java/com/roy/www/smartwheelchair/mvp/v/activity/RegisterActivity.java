//package com.roy.www.smartwheelchair.mvp.v.activity;
//
//import android.os.Bundle;
//import android.text.Selection;
//import android.text.Spannable;
//import android.text.TextUtils;
//import android.text.method.HideReturnsTransformationMethod;
//import android.text.method.PasswordTransformationMethod;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.blankj.utilcode.util.ToastUtils;
//import com.roy.www.smartwheelchair.R;
//import com.roy.www.smartwheelchair.api.HttpResponse;
//import com.roy.www.smartwheelchair.api.HttpUtil;
//import com.roy.www.smartwheelchair.api.URL_Constant;
//import com.roy.www.smartwheelchair.mvp.bean.RegisterBean;
//import com.roy.www.smartwheelchair.mvp.bean.VerificationCodeBean;
//import com.roy.www.smartwheelchair.utils.IntentUtil;
//import com.roy.www.smartwheelchair.widget.CountDownText;
//import com.roy.www.smartwheelchair.widget.ImgEditText;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by 李杨
// * On 2021/7/23
// * Email: 631934797@qq.com
// * Description:
// */
//public class RegisterActivity extends AppCompatActivity {
//    @BindView(R.id.phone_num)
//    ImgEditText phoneNum;
//    @BindView(R.id.v_code)
//    ImgEditText vCode;
//    @BindView(R.id.cdt_get_verification_code)
//    CountDownText cdtGetVerificationCode;
//    @BindView(R.id.pwd_new_1)
//    ImgEditText pwdNew1;
//    @BindView(R.id.pwd_new_2)
//    ImgEditText pwdNew2;
//    @BindView(R.id.but_register)
//    Button butRegister;
//
//    private boolean isHidden_1= true;
//    private boolean isHidden_2 = true;
//
//    private String mPhone= "";
//    private String mPwd= "";
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        ButterKnife.bind(this);
//        initViews();
//    }
//
//
//
//    @OnClick({R.id.cdt_get_verification_code, R.id.but_register})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.cdt_get_verification_code:
//
//                getVerificationCode();
//                break;
//            case R.id.but_register:
//                register();
//                break;
//        }
//    }
//
//    private void register() {
//         String pwdStr1 = pwdNew1.getText().toString();
//         String pwdStr2 = pwdNew2.getText().toString();
//         String code = vCode.getText().toString();
//         if (TextUtils.isEmpty(code) ||TextUtils.isEmpty(pwdStr1) || TextUtils.isEmpty(pwdStr2) ){
//             ToastUtils.showLong("密码不能为空");
//             return;
//         }
//
//         if (pwdStr1.equals(pwdStr2)){
//
//             mPwd = pwdStr1;
//
//             Map<String, Object> map = new HashMap<>();
//             map.put("mobile", mPhone);
//             map.put("vCode", code);
//             map.put("password", mPwd);
//             HttpUtil.getInstance().doPost(URL_Constant.AUTH_REGISTER_URL, null,map, new HttpResponse(String.class) {
//                 @Override
//                 public void onSuccess(Object o) {
//
//                     RegisterBean bean = (RegisterBean) o;
//                     if (bean.getStatusCode() ==200){
//                         IntentUtil.startActivity(RegisterActivity.this,HomeActivity.class,true);
//                     }
//
//                 }
//
//                 @Override
//                 public void onError(String msg) {
//                     ToastUtils.showLong(msg);
//                 }
//             });
//
//         }else {
//             ToastUtils.showLong("密码有误，请重新输入");
//         }
//    }
//
//    private void getVerificationCode() {
//        mPhone = phoneNum.getText().toString().trim();
//        if (!validateMobilePhone(mPhone)){
//            ToastUtils.showLong("输入手机号格式有误，请重新输入");
//            return;
//        }
//        cdtGetVerificationCode.start();
//        Map<String, Object> map = new HashMap<>();
//        map.put("mobile", mPhone);
//
//        HttpUtil.getInstance().doPost(URL_Constant.AUTH_SEND_MOBILE_CODE_URL, null,map, new HttpResponse(String.class) {
//            @Override
//            public void onSuccess(Object o) {
//
//                VerificationCodeBean bean = (VerificationCodeBean) o;
//                if (bean.getStatusCode() ==200){
//                    ToastUtils.showLong(bean.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onError(String msg) {
//                ToastUtils.showLong(msg);
//
//            }
//        });
//    }
//
//    public static boolean validateMobilePhone(String in) {
//        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
//        return pattern.matcher(in).matches();
//    }
//
//    /**
//     * 初始化View
//     */
//    private void initViews() {
//        phoneNum.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
//            @Override
//            public void rightDrawableClick() {
//                phoneNum.setText("");
//            }
//        });
//        pwdNew1.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
//            @Override
//            public void rightDrawableClick() {
//                if (isHidden_1) {
//                    // 设置EditText文本为可见的
//                    pwdNew1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    pwdNew1.setRightDrawable(getResources().getDrawable(R.mipmap.eye_selected));
//                } else {
//                    // 设置EditText文本为隐藏的
//                    pwdNew1.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    pwdNew1.setRightDrawable(getResources().getDrawable(R.mipmap.eye_normal));
//                }
//                isHidden_1 = !isHidden_1;
//                pwdNew1.postInvalidate();
//
//                // 切换后将EditText光标置于末尾
//                CharSequence charSequence = pwdNew1.getText();
//                if (charSequence instanceof Spannable) {
//                    Spannable spanText = (Spannable) charSequence;
//                    Selection.setSelection(spanText, charSequence.length());
//                }
//            }
//        });
//
//        pwdNew2.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
//            @Override
//            public void rightDrawableClick() {
//                if (isHidden_2) {
//                    // 设置EditText文本为可见的
//                    pwdNew2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    pwdNew2.setRightDrawable(getResources().getDrawable(R.mipmap.eye_selected));
//                } else {
//                    // 设置EditText文本为隐藏的
//                    pwdNew2.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    pwdNew2.setRightDrawable(getResources().getDrawable(R.mipmap.eye_normal));
//                }
//                isHidden_2 = !isHidden_2;
//                pwdNew1.postInvalidate();
//
//                // 切换后将EditText光标置于末尾
//                CharSequence charSequence = pwdNew2.getText();
//                if (charSequence instanceof Spannable) {
//                    Spannable spanText = (Spannable) charSequence;
//                    Selection.setSelection(spanText, charSequence.length());
//                }
//            }
//        });
//
//
//    }
//
//}
