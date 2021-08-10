package com.roy.www.smartwheelchair.mvp.v.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.amap.api.mapcore.util.v;
import com.github.tcking.viewquery.ViewQuery;
import com.roy.www.alirtc.bean.RTCAuthInfo;
import com.roy.www.alirtc.network.RequestRTCAuthInfo;
import com.roy.www.alirtc.utils.MockAliRtcAuthInfo;
import com.roy.www.alirtc.videocall.VideoCallActivity;

import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.api.URL_Constant;

import com.roy.www.smartwheelchair.mvp.base.WheelchairApplication;
import com.roy.www.smartwheelchair.service.MqttService;
import com.roy.www.smartwheelchair.utils.GsonUtil;
import com.roy.www.smartwheelchair.utils.IntentUtil;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tcking.github.com.giraffeplayer2.Option;
import tcking.github.com.giraffeplayer2.PlayerManager;
import tcking.github.com.giraffeplayer2.VideoInfo;
import tcking.github.com.giraffeplayer2.VideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.tv_top_titel)
    TextView tvTopTitel;
    @BindView(R.id.iv_top_back)
    ImageView ivTopBack;
    @BindView(R.id.iv_top_set)
    ImageView ivTopSet;
    @BindView(R.id.but_screen_capture)
    ImageView butScreenCapture;
    @BindView(R.id.but_video_surveillance)
    ImageView butVideoSurveillance;
    @BindView(R.id.but_communicate)
    ImageView butCommunicate;
    @BindView(R.id.but_sound_on_off)
    TextView butSoundOnOff;
    @BindView(R.id.rl_but_file)
    RelativeLayout rlButFile;
    private ViewQuery $;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        tvTopTitel.setText(R.string.home_video);
        initVideo();
    }

    /**
     *
     */
    private void initVideo() {
        PlayerManager.getInstance().getDefaultVideoInfo().addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "multiple_requests", 1L));
        String testUrl = URL_Constant.BASE_VIDEO_URL + WheelchairApplication.DEVICE_ID;

        $ = new ViewQuery(this);
        final VideoView videoView = $.id(R.id.video_view).view();
        videoView.setVideoPath(testUrl);

        videoView.getVideoInfo().setPortraitWhenFullScreen(true);
        videoView.getPlayer().aspectRatio(VideoInfo.AR_ASPECT_FILL_PARENT);
        videoView.getPlayer().start();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @OnClick({R.id.iv_top_back, R.id.iv_top_set, R.id.but_screen_capture, R.id.but_video_surveillance, R.id.but_communicate, R.id.but_sound_on_off, R.id.rl_but_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_top_back:
                this.finish();
                break;
            case R.id.iv_top_set:
                break;
            case R.id.but_screen_capture:
//                ScreenUtils.screenShot(this);
                break;
            case R.id.but_video_surveillance:
                break;
            case R.id.but_communicate:
                createChannel(MockAliRtcAuthInfo.randomRoom(),WheelchairApplication.DEVICE_ID);
                break;
            case R.id.but_sound_on_off:
//                IntentUtil.startActivity(VideoActivity.this, VideoCallActivity.class,false);
                break;
            case R.id.rl_but_file:
                if (butSoundOnOff.isClickable()){
                    butSoundOnOff.setPressed(true);
                }else {
                    butSoundOnOff.setPressed(false);
                }

                break;

        }
    }








    /**
     * 网络获取token创建会议
     *
     * @param channelId
     * @param userName
     */
    private void createChannel(String channelId, String userName) {

//        RequestRTCAuthInfo.getAuthInfo(userName, channelId, new RequestRTCAuthInfo.OnRequestAuthInfoListener() {
//            @Override
//            public void onObtainAuthInfo(RTCAuthInfo rtcAuthInfo) {
//                rtcAuthInfo.data.ConferenceId = channelId;
//                showAuthInfo(channelId, rtcAuthInfo, userName);
//            }
//
//            @Override
//            public void onFailure(String failure) {
//                Toast.makeText(VideoActivity.this, failure, Toast.LENGTH_SHORT).show();
//            }
//        });

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", userName);
        hashMap.put("room", channelId);

        RequestRTCAuthInfo.getAuthInfo(WheelchairApplication.mmkv.decodeString("token") ,
                hashMap, new RequestRTCAuthInfo.OnRequestAuthInfoListener() {
            @Override
            public void onObtainAuthInfo(RTCAuthInfo rtcAuthInfo) {
                rtcAuthInfo.data.ConferenceId = channelId;
                showAuthInfo(channelId, rtcAuthInfo, userName);

                // TODO 发送视频请求消息

                SendVideoRequestMessage(WheelchairApplication.mmkv.decodeString("token"),channelId);


            }

            @Override
            public void onFailure(String failure) {
                Toast.makeText(VideoActivity.this, failure, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SendVideoRequestMessage(String token, String roomId) {
        Map<String,Object> callMap =  new HashMap<>();
        callMap.put("protocol","setting");
        callMap.put("command","video_call");
        callMap.put("device_id",WheelchairApplication.DEVICE_ID);
        callMap.put("roomId",roomId);
        callMap.put("token",token);

        MqttService.publishMessage(GsonUtil.ObjectToString(callMap));
    }


    /**
     * 网络获取加入频道信息
     *
     * @param rtcAuthInfo
     */
    public void showAuthInfo(String channelId, RTCAuthInfo rtcAuthInfo, String userName) {
        Intent intent = new Intent(this, VideoCallActivity.class);
        Bundle b = new Bundle();
        //用户名
        b.putString("username", userName);
        //频道号
        b.putString("channel", channelId);
        //音频播放
        b.putSerializable("rtcAuthInfo", rtcAuthInfo);
        intent.putExtras(b);
        startActivity(intent);
    }


}


