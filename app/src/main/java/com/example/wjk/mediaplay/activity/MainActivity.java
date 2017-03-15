package com.example.wjk.mediaplay.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.example.wjk.mediaplay.R;
import com.example.wjk.mediaplay.basepager.BasePager;
import com.example.wjk.mediaplay.pager.AudioPager;
import com.example.wjk.mediaplay.pager.NetAudioPager;
import com.example.wjk.mediaplay.pager.NetVideoPager;
import com.example.wjk.mediaplay.pager.VideoPager;

import java.util.ArrayList;

/**
 * Created by wjk on 2017/3/12.
 */

public class MainActivity extends FragmentActivity {

    /**
     * 选中的位置
     */
    private int position;

    /**
     * 页面的集合
     */
    private ArrayList<BasePager> basePagers;

    private RadioGroup rg_bottom_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);

        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));//添加本地视频页面-0
        basePagers.add(new AudioPager(this));//添加本地音乐页面-1
        basePagers.add(new NetVideoPager(this));//添加网络视频页面-2
        basePagers.add(new NetAudioPager(this));//添加网络音频页面-3

        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_bottom_tag.check(R.id.rb_video);

    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default:
                    position = 0;
                    break;
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_net_audio:
                    position =3;
                    break;
            }
            setFragment();
        }
    }

    /**
     * 将页面添加到Fragment
     */
    private void setFragment() {
        //1、获得帧布局事物管理FragmentManager，不应该用此方法getFragmentManager()
        FragmentManager manager = getSupportFragmentManager();
        //2、开启事物
        FragmentTransaction ft = manager.beginTransaction();
        //3、替换
        ft.replace(R.id.fl_main_content,new Fragment(){
            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                BasePager basePager = getBasePager();
                if(basePager != null){
                    //各个页面的视图
                    return basePager.rootView;
                }
                return null;
            }
        });
        //提交事物
        ft.commit();
    }

    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if (basePager != null && basePager.isInitData){
            basePager.initData();//联网请求或者绑定数据
            basePager.isInitData = true;
        }
        return basePager;

    }
}

