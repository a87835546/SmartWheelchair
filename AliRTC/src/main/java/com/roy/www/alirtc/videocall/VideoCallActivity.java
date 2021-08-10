package com.roy.www.alirtc.videocall;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.roy.www.alirtc.R;
import com.roy.www.alirtc.bean.RTCAuthInfo;
import com.roy.www.alirtc.utils.StringUtil;


public class VideoCallActivity extends AppCompatActivity {

    private static final String TAG = VideoCallActivity.class.getSimpleName();

    AlivcVideoCallView alivcVideoCallView;
    String displayName;
    String channel;
    private RTCAuthInfo mRtcAuthInfo;
    private TextView mTitleTv;
    private TextView mCopyTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aliyun_video_call_main);
        getIntentData();
        mTitleTv =findViewById(R.id.tv_title);
        mCopyTv =findViewById(R.id.tv_copy);
        alivcVideoCallView = findViewById(R.id.alivc_videocall_view);
//        mCopyTv.setOnClickListener(this);
        alivcVideoCallView.setAlivcVideoCallNotifyListner(new AlivcVideoCallView.AlivcVideoCallNotifyListner() {
            @Override
            public void onLeaveChannel() {
                finish();
            }
        });

        mTitleTv.setText(String.format(getResources().getString(R.string.str_channel_code),channel));

        Log.w(TAG,"displayName:" +displayName);
        Log.w(TAG,"channel:" +channel);
        Log.w(TAG,"mRtcAuthInfo:" +mRtcAuthInfo.toString());
        Log.w(TAG,"RTCAuthInfo_Data:" +mRtcAuthInfo.getData().toString());

        alivcVideoCallView.auth(displayName, channel, mRtcAuthInfo);
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            displayName = getIntent().getExtras().getString("username");
            channel = getIntent().getExtras().getString("channel");
            mRtcAuthInfo = (RTCAuthInfo) getIntent().getExtras().getSerializable("rtcAuthInfo");
        }
    }

    @Override
    protected void onDestroy() {
        if (alivcVideoCallView != null) {
            alivcVideoCallView.leave();
        }
        super.onDestroy();
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.tv_copy:
//                StringUtil.clipChannelId(channel,VideoCallActivity.this);
//
//                break;
//        }
//    }
}
