package com.example.wjk.mediaplay.basepager;

import android.content.Context;
import android.view.View;

/**
 * Created by wjk on 2017/3/15.
 * 基类BasePager
 *
 * 四个页面继承此类
 * VideoPager、AudioPager、NetVideoPager、NetAudioPager
 */

public abstract class BasePager {
    /**
     * 上下文
     */
    public Context content;

    public boolean isInitData;
    public View rootView;
    public BasePager(Context content){
        this.content = content;
        rootView = initView();
    }
    /**
     * 强制子页面实现该方法，实现想要的特定的效果
     *
     * @return
     */
    public abstract View initView();


    /**
     * 当子页面，需要绑定数据，或者联网请求数据并且绑定的时候，重写该方法
     */
    public void initData(){

    }
}
