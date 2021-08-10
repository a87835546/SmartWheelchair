package com.roy.www.smartwheelchair.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.roy.www.smartwheelchair.R;

/**
 * Created by 李杨
 * On 2021/7/25
 * Email: 631934797@qq.com
 * Description:
 */
public class CountDownText extends AppCompatTextView {

    private CountDownTimer mCountDownTimer;
    /***
     * 持续时间毫秒
     */
    private int mTimeDuration ;
    /***
     * 间隔时间毫秒
     */
    private int mTimerGap;

    public CountDownText(Context context) {
        this(context, null);
    }

    public CountDownText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownText);
        mTimeDuration = typedArray.getInt(R.styleable.CountDownText_millisDuration, 60000);
        mTimerGap = typedArray.getInt(R.styleable.CountDownText_millisGap, 1000);
        typedArray.recycle();
        initTimer();
        setText("获取验证码");
    }

    private void initTimer() {
        mCountDownTimer = new CountDownTimer(mTimeDuration + 500, mTimerGap) {
            @Override
            public void onTick(long millisUntilFinished) {
                setText(String.format("%ss后重新获取", millisUntilFinished / 1000));
                setEnabled(false);

                setBackgroundColor(Color.parseColor("#BFBEBE"));
            }

            @Override
            public void onFinish() {
                setEnabled(true);
                setText("重新获取");
                setBackgroundColor(Color.parseColor("#5bc2d1"));
            }
        };
    }




    public void start() {
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }
    }

    private void end() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        end();
        mCountDownTimer = null;
    }
}
