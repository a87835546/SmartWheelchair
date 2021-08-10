package com.roy.www.smartwheelchair.mvp.v.activity;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.blankj.utilcode.util.ToastUtils;

import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.api.URL_Constant;
import com.roy.www.smartwheelchair.ble.BleController;
import com.roy.www.smartwheelchair.ble.callback.BleScanCallback;
import com.roy.www.smartwheelchair.mvp.base.BaseActivity;
import com.roy.www.smartwheelchair.mvp.base.BaseContract;
import com.roy.www.smartwheelchair.mvp.base.WheelchairApplication;

import com.roy.www.smartwheelchair.mvp.bean.UserInfoBean;
import com.roy.www.smartwheelchair.mvp.p.HomePresenter;

import com.roy.www.smartwheelchair.utils.IntentUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 李杨
 * On 2021/7/17
 * Email: 631934797@qq.com
 * Description:
 */
public class HomeActivity extends BaseActivity<HomePresenter> implements BaseContract.View {

    private static final int REQUEST_CODE = 101;
    @BindView(R.id.iv_admin)
    ImageView ivAdmin;
    @BindView(R.id.iv_scan_qr_codes)
    ImageView ivScanQrCodes;
    @BindView(R.id.ll_but_controller)
    LinearLayout llButController;
    @BindView(R.id.ll_but_local)
    LinearLayout llButLocal;
    @BindView(R.id.ll_but_video)
    LinearLayout llButVideo;
    @BindView(R.id.ll_but_body_info)
    LinearLayout llButBodyInfo;
    @BindView(R.id.ll_but_music)
    LinearLayout llButMusic;
    @BindView(R.id.ll_but_setting)
    LinearLayout llButSetting;
    @BindView(R.id.tv_power)
    TextView tvPower;
    @BindView(R.id.tv_mileage)
    TextView tvMileage;
    @BindView(R.id.tv_total_mileage)
    TextView tvTotalMileage;
    @BindView(R.id.tv_connect_status)
    TextView tvConnectStatus;
    @BindView(R.id.tv_music_status)
    TextView tvMusicStatus;



    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mPresenter.doGet(URL_Constant.GET_USER_INFO_URL,
                WheelchairApplication.mmkv.decodeString("token"),
                null, UserInfoBean.class);


//        scanBle();
    }

    private void scanBle() {
        BleController.getInstance().scanBleDevice(0, new BleScanCallback() {
            @Override
            public void onScanning(BluetoothDevice device, int rssi, byte[] scanRecord) {
                //蓝牙耳机
                Log.w("scan_BLE","BLE_Name:"+ device.getName()+"    BLE_Address:"+device.getAddress());

                if(device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET
                        || device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE){
                    //蓝牙耳机
                    Log.w("scan_BLE","耳机 BLE_Name:"+ device.getName()+"    BLE_Address:"+device.getAddress());
                }
            }

            @Override
            public void onSuccess() {
                scanBle();
            }
        });
    }

    @Override
    public void showArticleSuccess(Object o) {
        if (o instanceof UserInfoBean){
            UserInfoBean userInfo = (UserInfoBean) o;
            if (userInfo.getStatusCode()==200){
                userInfo.toString();
                WheelchairApplication.mmkv.encode("userId",userInfo.getData().getUserId());

            }else {
                ToastUtils.showLong("Login Failure："+ userInfo.getMessage());
            }
        }
    }

    @Override
    public void showArticleFail(String errorMsg) {
        ToastUtils.showLong( errorMsg);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initConfigValue();
//        scanBle();
    }

    private void initConfigValue() {
        tvPower.setText((WheelchairApplication.mmkv.decodeInt("power") * 10) + "%");
        tvMileage.setText(WheelchairApplication.mmkv.decodeInt("mileage",0)+"km");
        tvTotalMileage.setText(WheelchairApplication.mmkv.decodeInt("mileage",0)+"km");

    }

    @OnClick({R.id.iv_admin, R.id.iv_scan_qr_codes, R.id.ll_but_controller, R.id.ll_but_local, R.id.ll_but_video, R.id.ll_but_body_info, R.id.ll_but_music, R.id.ll_but_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_admin:
                // TODO
                break;
            case R.id.iv_scan_qr_codes:
                scanQR_Code();
                break;
            case R.id.ll_but_controller:
                // 轮椅控制界面
                IntentUtil.startActivity(HomeActivity.this, ControlActivity.class, false);
                break;
            case R.id.ll_but_local:
                // 定位界面
                IntentUtil.startActivity(HomeActivity.this, GeoFenceRoundActivity.class, false);
                break;
            case R.id.ll_but_video:
                // 视频界面
                IntentUtil.startActivity(HomeActivity.this, VideoActivity.class, false);
                break;
            case R.id.ll_but_body_info:
                // 身体信息界面
                IntentUtil.startActivity(HomeActivity.this, BodyInfoActivity.class, false);
                break;
            case R.id.ll_but_music:
                // 音乐界面
                IntentUtil.startActivity(HomeActivity.this, MusicActivity.class, false);

                break;
            case R.id.ll_but_setting:
                // 功能设置界面
                IntentUtil.startActivity(HomeActivity.this, FunctionSetActivity.class, false);
//                IntentUtil.startActivity(HomeActivity.this, VideoCallJoinActivity.class, false);

                break;
        }
    }


    /**
     * 扫描二维码
     */
    private void scanQR_Code() {
        Intent intent = new Intent(HomeActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    ToastUtils.showLong("解析结果:" + result);
                    // TODO 绑定设备
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.showLong("解析二维码失败");
                }
            }
        }

    }



}
