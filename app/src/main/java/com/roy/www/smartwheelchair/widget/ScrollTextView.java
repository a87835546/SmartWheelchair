package com.roy.www.smartwheelchair.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Sandy on 2017/9/5.
 *
 * 自定义TextView控件
 */

public class ScrollTextView extends TextView {


    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean isFocused() {
        return true;
    }
}
