package com.app.ebaebo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * author: liuzwei
 * Date: 2014/8/3
 * Time: 11:32
 * 类的功能、说明写在此处.
 */
public class PictureGridview extends GridView {
    public PictureGridview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
