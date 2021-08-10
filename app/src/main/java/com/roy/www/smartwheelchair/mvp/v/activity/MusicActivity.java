package com.roy.www.smartwheelchair.mvp.v.activity;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.ble.BleController;
import com.roy.www.smartwheelchair.ble.callback.BleScanCallback;
import com.roy.www.smartwheelchair.service.MusicService;
import com.roy.www.smartwheelchair.utils.DisplayUtil;
import com.roy.www.smartwheelchair.utils.FastBlurUtil;
import com.roy.www.smartwheelchair.utils.IntentUtil;
import com.roy.www.smartwheelchair.widget.BackgourndAnimationRelativeLayout;
import com.roy.www.smartwheelchair.widget.DiscView;
import com.roy.www.smartwheelchair.widget.MusicData;
import com.roy.www.smartwheelchair.widget.ScrollTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 李杨
 * On 2021/7/18
 * Email: 631934797@qq.com
 * Description:
 */
public class MusicActivity extends AppCompatActivity implements DiscView.IPlayInfo {



        @BindView(R.id.tv_music_name)
        ScrollTextView tvMusicName;
        @BindView(R.id.iv_back)
        ImageView ivBack;
        @BindView(R.id.discview)
        DiscView mDisc;
        @BindView(R.id.musicSeekBar)
        SeekBar mSeekBar;
        @BindView(R.id.tvTotalTime)
        TextView mTvTotalMusicDuration;
        @BindView(R.id.tvCurrentTime)
        TextView mTvMusicDuration;
        @BindView(R.id.ivLast)
        ImageView mIvLast;
        @BindView(R.id.ivPlayOrPause)
        ImageView mIvPlayOrPause;
        @BindView(R.id.ivNext)
        ImageView mIvNext;
        @BindView(R.id.rootLayout)
        BackgourndAnimationRelativeLayout mRootLayout;

        public static final int MUSIC_MESSAGE = 0;

        public static final String PARAM_MUSIC_LIST = "PARAM_MUSIC_LIST";


        private Handler mMusicHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mSeekBar.setProgress(mSeekBar.getProgress() + 1000);
                        mTvMusicDuration.setText(duration2Time(mSeekBar.getProgress()));
                        startUpdateSeekBarProgress();
                }
        };

        private MusicReceiver mMusicReceiver = new MusicReceiver();
        private List<MusicData> mMusicDatas = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_music);
                ButterKnife.bind(this);
                initMusicDatas();
                initView();
                initMusicReceiver();

                scanA2DPBle();
        }


        private void initMusicReceiver() {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_PLAY);
                intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_PAUSE);
                intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_DURATION);
                intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_COMPLETE);
                /*注册本地广播*/
                LocalBroadcastManager.getInstance(this).registerReceiver(mMusicReceiver, intentFilter);
        }


        private void initView() {

                mDisc.setPlayInfoListener(this);


                mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                mTvMusicDuration.setText(duration2Time(progress));
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                                stopUpdateSeekBarProgree();
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                                seekTo(seekBar.getProgress());
                                startUpdateSeekBarProgress();
                        }
                });

                mTvMusicDuration.setText(duration2Time(0));
                mTvTotalMusicDuration.setText(duration2Time(0));
                mDisc.setMusicDataList(mMusicDatas);
        }



        @OnClick({R.id.iv_back, R.id.ivLast, R.id.ivPlayOrPause, R.id.ivNext})
        public void onViewClicked(View view) {
                switch (view.getId()) {
                        case R.id.iv_back:
                            IntentUtil.startActivity(MusicActivity.this,HomeActivity.class,false);
                            break;
                        case R.id.ivLast:
                            mDisc.last();
                            break;
                        case R.id.ivPlayOrPause:
                            mDisc.playOrPause();
                            break;
                        case R.id.ivNext:
                            mDisc.next();
                            break;
                }
        }




        private void stopUpdateSeekBarProgree() {
                mMusicHandler.removeMessages(MUSIC_MESSAGE);
        }



        private void initMusicDatas() {
                MusicData musicData1 = new MusicData(R.raw.music1, R.raw.ic_music1, "寻", "三亩地");
                MusicData musicData2 = new MusicData(R.raw.music2, R.raw.ic_music2, "Nightingale", "YANI");
                MusicData musicData3 = new MusicData(R.raw.music3, R.raw.ic_music3, "Cornfield Chase", "Hans Zimmer");

                mMusicDatas.add(musicData1);
                mMusicDatas.add(musicData2);
                mMusicDatas.add(musicData3);

                Intent intent = new Intent(this, MusicService.class);
                intent.putExtra(PARAM_MUSIC_LIST, (Serializable) mMusicDatas);
                startService(intent);
        }

        private void try2UpdateMusicPicBackground(final int musicPicRes) {
                if (mRootLayout.isNeed2UpdateBackground(musicPicRes)) {
                        new Thread(new Runnable() {
                                @Override
                                public void run() {
                                        final Drawable foregroundDrawable = getForegroundDrawable(musicPicRes);
                                        runOnUiThread(new Runnable() {
                                                @RequiresApi(api = Build.VERSION_CODES.M)
                                                @Override
                                                public void run() {
                                                        mRootLayout.setForeground(foregroundDrawable);
                                                        mRootLayout.beginAnimation();
                                                }
                                        });
                                }
                        }).start();
                }
        }

        private Drawable getForegroundDrawable(int musicPicRes) {
                /*得到屏幕的宽高比，以便按比例切割图片一部分*/
                final float widthHeightSize = (float) (DisplayUtil.getScreenWidth(this)
                        * 1.0 / DisplayUtil.getScreenHeight(this) * 1.0);

                Bitmap bitmap = getForegroundBitmap(musicPicRes);
                int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
                int cropBitmapWidthX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

                /*切割部分图片*/
                Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
                        bitmap.getHeight());
                /*缩小图片*/
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 50, bitmap
                        .getHeight() / 50, false);
                /*模糊化*/
                final Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true);

                final Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
                /*加入灰色遮罩层，避免图片过亮影响其他控件*/
                foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                return foregroundDrawable;
        }

        private Bitmap getForegroundBitmap(int musicPicRes) {
                int screenWidth = DisplayUtil.getScreenWidth(this);
                int screenHeight = DisplayUtil.getScreenHeight(this);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeResource(getResources(), musicPicRes, options);
                int imageWidth = options.outWidth;
                int imageHeight = options.outHeight;

                if (imageWidth < screenWidth && imageHeight < screenHeight) {
                        return BitmapFactory.decodeResource(getResources(), musicPicRes);
                }

                int sample = 2;
                int sampleX = imageWidth / DisplayUtil.getScreenWidth(this);
                int sampleY = imageHeight / DisplayUtil.getScreenHeight(this);

                if (sampleX > sampleY && sampleY > 1) {
                        sample = sampleX;
                } else if (sampleY > sampleX && sampleX > 1) {
                        sample = sampleY;
                }

                options.inJustDecodeBounds = false;
                options.inSampleSize = sample;
                options.inPreferredConfig = Bitmap.Config.RGB_565;

                return BitmapFactory.decodeResource(getResources(), musicPicRes, options);
        }

        @Override
        public void onMusicInfoChanged(String musicName, String musicAuthor) {
                tvMusicName.setText(musicName + "  " + musicAuthor);

        }

        @Override
        public void onMusicPicChanged(int musicPicRes) {
                try2UpdateMusicPicBackground(musicPicRes);
        }


        @Override
        public void onMusicChanged(DiscView.MusicChangedStatus musicChangedStatus) {
                switch (musicChangedStatus) {
                        case PLAY: {
                                play();
                                break;
                        }
                        case PAUSE: {
                                pause();
                                break;
                        }
                        case NEXT: {
                                next();
                                break;
                        }
                        case LAST: {
                                last();
                                break;
                        }
                        case STOP: {
                                stop();
                                break;
                        }
                }
        }



        private void play() {
                optMusic(MusicService.ACTION_OPT_MUSIC_PLAY);
                startUpdateSeekBarProgress();
        }

        private void pause() {
                optMusic(MusicService.ACTION_OPT_MUSIC_PAUSE);
                stopUpdateSeekBarProgree();
        }

        private void stop() {
                stopUpdateSeekBarProgree();
                mIvPlayOrPause.setImageResource(R.mipmap.ic_play);
                mTvMusicDuration.setText(duration2Time(0));
                mTvTotalMusicDuration.setText(duration2Time(0));
                mSeekBar.setProgress(0);
        }

        private void next() {
                mRootLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                optMusic(MusicService.ACTION_OPT_MUSIC_NEXT);
                        }
                }, DiscView.DURATION_NEEDLE_ANIAMTOR);
                stopUpdateSeekBarProgree();
                mTvMusicDuration.setText(duration2Time(0));
                mTvTotalMusicDuration.setText(duration2Time(0));
        }

        private void last() {
                mRootLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                optMusic(MusicService.ACTION_OPT_MUSIC_LAST);
                        }
                }, DiscView.DURATION_NEEDLE_ANIAMTOR);
                stopUpdateSeekBarProgree();
                mTvMusicDuration.setText(duration2Time(0));
                mTvTotalMusicDuration.setText(duration2Time(0));
        }

        private void complete(boolean isOver) {
                if (isOver) {
                        mDisc.stop();
                } else {
                        mDisc.next();
                }
        }

        private void optMusic(final String action) {
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(action));
        }

        private void seekTo(int position) {
                Intent intent = new Intent(MusicService.ACTION_OPT_MUSIC_SEEK_TO);
                intent.putExtra(MusicService.PARAM_MUSIC_SEEK_TO, position);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        private void startUpdateSeekBarProgress() {
                /*避免重复发送Message*/
                stopUpdateSeekBarProgree();
                mMusicHandler.sendEmptyMessageDelayed(0, 1000);
        }

        /*根据时长格式化称时间文本*/
        private String duration2Time(int duration) {
                int min = duration / 1000 / 60;
                int sec = duration / 1000 % 60;

                return (min < 10 ? "0" + min : min + "") + ":" + (sec < 10 ? "0" + sec : sec + "");
        }

        private void updateMusicDurationInfo(int totalDuration) {
                mSeekBar.setProgress(0);
                mSeekBar.setMax(totalDuration);
                mTvTotalMusicDuration.setText(duration2Time(totalDuration));
                mTvMusicDuration.setText(duration2Time(0));
                startUpdateSeekBarProgress();
        }



        class MusicReceiver extends BroadcastReceiver {

                @Override
                public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        if (action.equals(MusicService.ACTION_STATUS_MUSIC_PLAY)) {
                                mIvPlayOrPause.setImageResource(R.mipmap.ic_pause);
                                int currentPosition = intent.getIntExtra(MusicService.PARAM_MUSIC_CURRENT_POSITION, 0);
                                mSeekBar.setProgress(currentPosition);
                                if (!mDisc.isPlaying()) {
                                        mDisc.playOrPause();
                                }
                        } else if (action.equals(MusicService.ACTION_STATUS_MUSIC_PAUSE)) {
                                mIvPlayOrPause.setImageResource(R.mipmap.ic_play);
                                if (mDisc.isPlaying()) {
                                        mDisc.playOrPause();
                                }
                        } else if (action.equals(MusicService.ACTION_STATUS_MUSIC_DURATION)) {
                                int duration = intent.getIntExtra(MusicService.PARAM_MUSIC_DURATION, 0);
                                updateMusicDurationInfo(duration);
                        } else if (action.equals(MusicService.ACTION_STATUS_MUSIC_COMPLETE)) {
                                boolean isOver = intent.getBooleanExtra(MusicService.PARAM_MUSIC_IS_OVER, true);
                                complete(isOver);
                        }
                }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMusicReceiver);

        }

        private void scanA2DPBle() {
                BleController.getInstance().scanBleDevice(0, new BleScanCallback() {
                        @Override
                        public void onScanning(BluetoothDevice device, int rssi, byte[] scanRecord) {
                             if (device.getName() != null && device.getName().equals("SmartBox")){
                                     Log.w("TAG","===BLE ---> SmartBox ====");
                                     BleController.getInstance().stopScanBle();
                                     BleController.getInstance().connectA2dp(device);
                             }
                        }

                        @Override
                        public void onSuccess() {

                        }
                });
        }


}
