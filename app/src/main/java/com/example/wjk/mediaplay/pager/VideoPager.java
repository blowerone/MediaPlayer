package com.example.wjk.mediaplay.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wjk.mediaplay.R;
import com.example.wjk.mediaplay.VideoPagerAdapter.VideoPagerAdapter;
import com.example.wjk.mediaplay.activity.SystemVideoPlayer;
import com.example.wjk.mediaplay.basepager.BasePager;
import com.example.wjk.mediaplay.domain.MediaItem;
import com.example.wjk.mediaplay.utils.LogUtil;


import java.util.ArrayList;

/**
 * Created by wjk on 2017/3/15.
 * 1、设置本地视频页面的布局，包括ListView/TextView/ProgressBar且进行初始化
 * 2、获取本地视频的数据，在子线程中加载视频，加载的视频放入集合中；
 *    设置适配器要在主线程；用Handler,设置适配器
 * 3、设置item的点击事件，把视频播放出来
 */

public class VideoPager extends BasePager {

    private ListView listview;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;

    private VideoPagerAdapter videoPagerAdapter;

    //保存视频数据信息，后用于加载适配器
    private ArrayList<MediaItem> mediaItems;
    public VideoPager(Context content) {
        super(content);
    }

    //在主线程创建自己的handler，用于接受来自子线程的数据，并进行相应的处理
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems != null && mediaItems.size()>0){
                //有数据
                //设置适配器
                videoPagerAdapter = new VideoPagerAdapter(content, mediaItems);
                listview.setAdapter(videoPagerAdapter);
                //把文本隐藏
                tv_nomedia.setVisibility(View.GONE);
            }else{
                //没有数据，显示文本
                tv_nomedia.setVisibility(View.VISIBLE);
            }
            //隐藏进度条
            pb_loading.setVisibility(View.GONE);
        }
    };
    /**
     * 设置页面的布局，初始化VideoPager页面的控件
     * @return
     */
    @Override
    public View initView() {
        View view = View.inflate(content, R.layout.video_pager, null);
        //获取布局中的每个控件
        listview = (ListView) view.findViewById(R.id.listview);
        tv_nomedia = (TextView) view.findViewById(R.id.tv_nomedia);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);

        listview.setOnItemClickListener(new MyOnItemClickListener());
        return view;

    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地音频的数据被初始化了。。。");
        getDataFromLocal();
    }

    private void getDataFromLocal() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                //初始化mediaItems
                mediaItems = new ArrayList<>();
                //ContentProvider提供数据，ContentResolver获取ContentProvider提供的数据信息
                ContentResolver resolver = content.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Video.Media.DURATION,//视频总时长
                        MediaStore.Video.Media.SIZE,//视频的文件大小
                        MediaStore.Video.Media.DATA,//视频的绝对地址
                        MediaStore.Video.Media.ARTIST,//歌曲的演唱者
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if(cursor != null){
                    while(cursor.moveToNext()){

                        MediaItem mediaItem = new MediaItem();
                        mediaItems.add(mediaItem);

                        String name = cursor.getString(0);//视频的名称
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);//视频的时长
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);//视频的文件大小
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);//视频的播放地址
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);//艺术家
                        mediaItem.setArtist(artist);
                    }
                    cursor.close();
                }
                //发送消息
                handler.sendEmptyMessage(10);
            }
        }.start();
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取对应Item的数据
            MediaItem mediaItem = mediaItems.get(position);

            //调起系统所有的播放器-隐士意图
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            content.startActivity(intent);
            //调用自己写的播放器-显示意图
            Intent intent = new Intent(content, SystemVideoPlayer.class);
            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
            content.startActivity(intent);
        }
    }
}
