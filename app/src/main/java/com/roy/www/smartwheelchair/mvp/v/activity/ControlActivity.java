package com.roy.www.smartwheelchair.mvp.v.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.ble.BleController;
import com.roy.www.smartwheelchair.ble.callback.BleConnectCallback;
import com.roy.www.smartwheelchair.ble.callback.BleScanCallback;
import com.roy.www.smartwheelchair.ble.callback.OnReceiverCallback;
import com.roy.www.smartwheelchair.ble.protocol.Command;
import com.roy.www.smartwheelchair.ble.protocol.PackData;
import com.roy.www.smartwheelchair.ble.protocol.ProtocolAPIImpl;
import com.roy.www.smartwheelchair.mvp.base.WheelchairApplication;
import com.roy.www.smartwheelchair.utils.IntentUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by 李杨
 * On 2021/7/17
 * Email: 631934797@qq.com
 * Description:
 */
public class ControlActivity extends AppCompatActivity implements OnReceiverCallback {

    private static final String TAG = ControlActivity.class.getSimpleName();
    @BindView(R.id.tv_top_titel)
    TextView tvTopTitel;
    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.iv_top_set)
    ImageView ivTopSet;
    @BindView(R.id.iv_but_stop)
    ImageView ivButStop;
    @BindView(R.id.iv_but_top)
    ImageView ivButTop;
    @BindView(R.id.iv_but_down)
    ImageView ivButDown;
    @BindView(R.id.iv_but_left)
    ImageView ivButLeft;
    @BindView(R.id.iv_but_right)
    ImageView ivButRight;
    @BindView(R.id.tv_ble_status)
    TextView tvBleStatus;
    @BindView(R.id.iv_ble_status)
    ImageView ivBleStatus;
    @BindView(R.id.ll_but_speed_info)
    LinearLayout llButSpeedInfo;
    @BindView(R.id.ll_but_power_info)
    LinearLayout llButPowerInfo;
    @BindView(R.id.rl_but_stop)
    RelativeLayout rlButStop;
    @BindView(R.id.tv_c_power)
    TextView tvCPower;
    @BindView(R.id.tv_c_remain_mileage)
    TextView tvCRemainMileage;
    @BindView(R.id.tv_c_total_mileage)
    TextView tvCTotalMileage;


    private boolean isConnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        ButterKnife.bind(this);
        BleController.getInstance().registReciveListener(this);
        tvTopTitel.setText(R.string.home_control);
//        connectBleDevice("34:75:63:B0:66:B7");
//        connectBleDevice("22:22:C5:D8:E1:C1");

        //扫描
        scanBleDevice();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initConfigValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleController.getInstance().disconnection();
        BleController.getInstance().unregistReciveListener();
    }

    private void initConfigValue() {
        tvCPower.setText((WheelchairApplication.mmkv.decodeInt("power") * 10) + "%");
        tvCRemainMileage.setText(WheelchairApplication.mmkv.decodeString("mileage"));
        tvCTotalMileage.setText(WheelchairApplication.mmkv.decodeString("mileage"));

    }

    @OnClick({R.id.iv_top_back, R.id.iv_top_set, R.id.iv_but_stop, R.id.iv_but_top, R.id.iv_but_down, R.id.iv_but_left, R.id.iv_but_right, R.id.ll_but_speed_info, R.id.ll_but_power_info, R.id.rl_but_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_back:
                this.finish();
                break;
            case R.id.iv_top_set:
//                IntentUtil.startActivity(this,HomeActivity.class,true);
                break;
            case R.id.iv_but_stop:
                if (isConnect) {
                    ProtocolAPIImpl.getInstance().stop();
                } else {
                    ToastUtils.showLong(R.string.ble_connect_hint);
                }
                break;
            case R.id.iv_but_top:
                if (isConnect) {
                    ProtocolAPIImpl.getInstance().advance();
                } else {
                    ToastUtils.showLong(R.string.ble_connect_hint);
                }

                break;
            case R.id.iv_but_down:
                if (isConnect) {
                    ProtocolAPIImpl.getInstance().retreat();
                } else {
                    ToastUtils.showLong(R.string.ble_connect_hint);
                }

                break;
            case R.id.iv_but_left:
                if (isConnect) {
                    ProtocolAPIImpl.getInstance().left();
                } else {
                    ToastUtils.showLong(R.string.ble_connect_hint);
                }

                break;
            case R.id.iv_but_right:
                if (isConnect) {
                    ProtocolAPIImpl.getInstance().right();
                } else {
                    ToastUtils.showLong(R.string.ble_connect_hint);
                }

                break;

            case R.id.ll_but_speed_info:
                IntentUtil.startActivity(ControlActivity.this, SpeedInfoActivity.class, false);

                break;

            case R.id.ll_but_power_info:
//                IntentUtil.startActivity(ControlActivity.this, PowerInfoActivity.class, false);

                break;

            case R.id.rl_but_stop:

                break;

        }
    }



    @Override
    public void onReceive(byte[] bytes) {
        // 数据分发
        parseCommPacket(bytes);
    }

    /**
     * @param bytes
     */
    private void parseCommPacket(byte[] bytes) {
        PackData commPacket = PackData.createCommPacket(bytes);
        if (commPacket != null) {

            Log.i(TAG, commPacket.toString());

            switch (commPacket.getCmd()) {

                case Command.CMD_ACTION_ADVANCE:
//                    ToastUtils.showLong("前进");
                    break;

                case Command.CMD_ACTION_RETREAT:
//                    ToastUtils.showLong("后退");
                    break;

                case Command.CMD_ACTION_LEFT:
//                    ToastUtils.showLong("向左");
                    break;

                case Command.CMD_ACTION_RIGHT:
//                    ToastUtils.showLong("向右");
                    break;

                case Command.CMD_ACTION_STOP:
//                    ToastUtils.showLong("停止");
                    break;

                case Command.CMD_ACTION_SET_WIFI:
                    ToastUtils.showLong("SET WIFI");
                    break;


            }

        }

    }


    /**
     * 连接蓝牙设备
     *
     * @param mac
     */
    private void connectBleDevice(String mac) {
        BleController.getInstance().stopScanBle();

        BleController.getInstance().connect(0, mac, new BleConnectCallback() {
            @Override
            public void onConnSuccess() {
                isConnect = true;
                tvBleStatus.setText("已连接智能盒");
                ivBleStatus.setImageResource(R.mipmap.ic_ble_ok);
                ToastUtils.showLong(R.string.ble_connect_successful);
            }

            @Override
            public void onConnFailed() {
                scanBleDevice();//重新扫描
                isConnect = false;
                tvBleStatus.setText("连接失败");
                ivBleStatus.setImageResource(R.mipmap.ic_ble_err);
                ToastUtils.showLong(R.string.ble_connect_fail);
            }
        });
    }

    /**
     * 扫描BLE设备
     */
    private void scanBleDevice() {
        BleController.getInstance().scanBleDevice(0, new BleScanCallback() {
            @Override
            public void onScanning(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if (device.getName() != null && device.getName().equals("SmartBox")){
                    connectBleDevice(device.getAddress());
                }
            }

            @Override
            public void onSuccess() {
                SystemClock.sleep(300);
                scanBleDevice();
            }
        });

    }


}
