package com.example.wjk.mediaplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by wjk on 2017/4/3.
 */

public class VideoView extends android.widget.VideoView {
    public VideoView(Context context) {
        this(context, null);
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setVideoSize(int videoWith, int videoHeight){
        //设置视频的宽和高
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = videoWith;
        params.height = videoHeight;
        setLayoutParams(params);

    }
}
