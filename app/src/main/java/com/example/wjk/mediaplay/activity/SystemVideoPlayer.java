package com.example.wjk.mediaplay.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.wjk.mediaplay.R;

/**
 * Created by wjk on 2017/3/19.
 */

public class SystemVideoPlayer extends Activity {

    private VideoView videoview;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        videoview = (VideoView) findViewById(R.id.videoview);

        uri = getIntent().getData();
        if(uri != null){
            videoview.setVideoURI(uri);
        }
        //设置控制面板
        videoview.setMediaController(new MediaController(this));

    }
}
