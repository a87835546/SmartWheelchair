//package com.roy.www.smartwheelchair.mvp.v.activity;
//
//import android.os.Bundle;
//import android.text.Selection;
//import android.text.Spannable;
//import android.text.method.HideReturnsTransformationMethod;
//import android.text.method.PasswordTransformationMethod;
//import android.widget.Button;
//
//import androidx.annotation.Nullable;
//
//import com.blankj.utilcode.util.ToastUtils;
//import com.roy.www.smartwheelchair.R;
//import com.roy.www.smartwheelchair.api.URL_Constant;
//import com.roy.www.smartwheelchair.mvp.base.BaseActivity;
//import com.roy.www.smartwheelchair.mvp.base.WheelchairApplication;
//import com.roy.www.smartwheelchair.mvp.bean.LoginBean;
//import com.roy.www.smartwheelchair.mvp.base.BaseContract;
//import com.roy.www.smartwheelchair.mvp.p.LoginPresenter;
//import com.roy.www.smartwheelchair.widget.ImgEditText;
//import com.roy.www.smartwheelchair.utils.IntentUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//
///**
// * Created by 李杨
// * On 2021/6/21
// * Email: 631934797@qq.com
// * Description:
// */
//
//public class LoginActivity extends BaseActivity<LoginPresenter> implements BaseContract.View {
//
//
//    @BindView(R.id.accountIet)
//    ImgEditText accountIet;
//    @BindView(R.id.pwdIet)
//    ImgEditText pwdIet;
//    @BindView(R.id.login)
//    Button login;
//
//    // 用来判断显示明文或者密码（同时修改显示的图片）
//    private boolean isHidden = true;
//
//    @Override
//    public void showArticleSuccess(Object o) {
//        if (o instanceof LoginBean){
//            LoginBean loginBean = (LoginBean) o;
//            if (loginBean.getStatusCode()==200){
//
//                WheelchairApplication.mmkv.encode("token",loginBean.getData());
//                ToastUtils.showLong("Login Successfully");
//                // TODO 登录成功 跳转到设备列表界面
//                IntentUtil.startActivity(LoginActivity.this, HomeActivity.class, true);
//
//            }else {
//                ToastUtils.showLong("Login Failure："+ loginBean.getMessage());
//            }
//        }
//
//        if (o instanceof LoginBean){
//
//        }
//    }
//
//    @Override
//    public void showArticleFail(String errorMsg) {
//
//        ToastUtils.showLong("Login Failure："+ errorMsg);
//
//    }
//
//
//
//
//    @Override
//    protected LoginPresenter createPresenter() {
//        return new LoginPresenter();
//    }
//
//    @Override
//    protected void initView(@Nullable Bundle savedInstanceState) {
//        setContentView(R.layout.activity_login);
//        ButterKnife.bind(this);
//
//        initViews();
//    }
//
//
//    @OnClick(R.id.login)
//    public void onViewClicked() {
//        String userName = accountIet.getText().toString().trim();
//        String pwd = pwdIet.getText().toString().trim();
//        if (!userName.isEmpty() && !pwd.isEmpty()){
//            Map<String, Object> map = new HashMap<>();
//            map.put("userName", userName);
//            map.put("passWord", pwd);
//            mPresenter.doPost(URL_Constant.AUTH_LOGIN_URL,null, map,LoginBean.class);
//        }else {
//            ToastUtils.showLong("用户名、密码不用能空");
//        }
//
//    }
//
//
//    /**
//     * 初始化View
//     */
//    private void initViews() {
//        accountIet.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
//            @Override
//            public void rightDrawableClick() {
//                accountIet.setText("");
//            }
//        });
//        pwdIet.setDrawableClick(new ImgEditText.IMyRightDrawableClick() {
//            @Override
//            public void rightDrawableClick() {
//                if (isHidden) {
//                    // 设置EditText文本为可见的
//                    pwdIet.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    pwdIet.setRightDrawable(getResources().getDrawable(R.mipmap.eye_selected));
//                } else {
//                    // 设置EditText文本为隐藏的
//                    pwdIet.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    pwdIet.setRightDrawable(getResources().getDrawable(R.mipmap.eye_normal));
//                }
//                isHidden = !isHidden;
//                pwdIet.postInvalidate();
//
//                // 切换后将EditText光标置于末尾
//                CharSequence charSequence = pwdIet.getText();
//                if (charSequence instanceof Spannable) {
//                    Spannable spanText = (Spannable) charSequence;
//                    Selection.setSelection(spanText, charSequence.length());
//                }
//            }
//        });
//
//    }
//}
