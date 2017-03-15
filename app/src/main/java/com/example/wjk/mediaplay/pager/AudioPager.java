package com.example.wjk.mediaplay.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjk.mediaplay.basepager.BasePager;
import com.example.wjk.mediaplay.utils.LogUtil;

/**
 * Created by wjk on 2017/3/15.
 */

public class AudioPager extends BasePager {


    public AudioPager(Context content) {
        super(content);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(content);
        textView.setText("AudioPager");
        Toast.makeText(content, "AudioPager", Toast.LENGTH_LONG).show();
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地音频的数据被初始化了。。。");
    }
}
