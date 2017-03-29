package com.example.wjk.mediaplay.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.wjk.mediaplay.R;
import com.example.wjk.mediaplay.utils.Utils;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wjk on 2017/3/19.
 */

public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    private VideoView videoview;
    private Uri uri;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSwitchScreen;
    private Utils utils;
    /**
     * 视频进度的更新
     */
    private final int PROGRESS = 1;
    private MyReceiver receiver;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-03-26 21:12:06 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_video_player);
        videoview = (VideoView) findViewById(R.id.videoview);
        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvName = (TextView)findViewById( R.id.tv_name );
        ivBattery = (ImageView)findViewById( R.id.iv_battery );
        tvSystemTime = (TextView)findViewById( R.id.tv_system_time );
        btnVoice = (Button)findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnSwitchPlayer = (Button)findViewById( R.id.btn_switch_player );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvCurrentTime = (TextView)findViewById( R.id.tv_current_time );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvDuration = (TextView)findViewById( R.id.tv_duration );
        btnExit = (Button)findViewById( R.id.btn_exit );
        btnVideoPre = (Button)findViewById( R.id.btn_video_pre );
        btnVideoStartPause = (Button)findViewById( R.id.btn_video_start_pause );
        btnVideoNext = (Button)findViewById( R.id.btn_video_next );
        btnVideoSwitchScreen = (Button)findViewById( R.id.btn_video_switch_screen );

        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSwitchScreen.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-03-26 21:12:06 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            // Handle clicks for btnVoice
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
        } else if ( v == btnExit ) {
            // Handle clicks for btnExit
        } else if ( v == btnVideoPre ) {
            // Handle clicks for btnVideoPre
        } else if ( v == btnVideoStartPause ) {
            // Handle clicks for btnVideoStartPause
            // Handle clicks for btnVideoStartPause
            if(videoview.isPlaying()){
                //视频在播放-设置暂停
                videoview.pause();
                //按钮状态设置播放
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
            }else{
                //视频播放
                videoview.start();
                //按钮状态设置暂停
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
            }
        } else if ( v == btnVideoNext ) {
            // Handle clicks for btnVideoNext
        } else if ( v == btnVideoSwitchScreen ) {
            // Handle clicks for btnVideoSwitchScreen
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:

                    //1.得到当前的视频播放进程
                    int currentPosition = videoview.getCurrentPosition();

                    //2.SeekBar.setProgress(当前进度);
                    seekbarVideo.setProgress(currentPosition);

                    //更新文本播放进度
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    //更新系统时间
                    tvSystemTime.setText(getSystemTime());

                    //3.每秒更新一次
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        initData();
        uri = getIntent().getData();
        if(uri != null){
            videoview.setVideoURI(uri);
        }
        //设置控制面板
//        videoview.setMediaController(new MediaController(this));

        setListener();

    }

    private void initData() {
        utils = new Utils();
        //注册广播
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,filter);
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level",0);
            setBattery(level);
        }
    }

    private void setBattery(int level) {
        if(level<0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if(level<10){
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        }else if(level<20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else if(level<40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else if(level<60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else if(level<80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else if(level<100){
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }else{
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    private void setListener() {
        videoview.setOnPreparedListener(new MyOnPreparedListener());

        videoview.setOnErrorListener(new MyOnErrorListener());

        videoview.setOnCompletionListener(new MyOnCompletionListener());

        seekbarVideo.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            //当底层解码准备好的时候
            videoview.start();//开始播放
            //1.视频的总时长，关联总长度
            int duration = videoview.getDuration();
            seekbarVideo.setMax(duration);
            tvDuration.setText(utils.stringForTime(duration));

            handler.sendEmptyMessage(PROGRESS);
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        //视频播放完成，设置按钮状态
        @Override
        public void onCompletion(MediaPlayer mp) {
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        }
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        /**
         * 当手指滑动的时候，会引起SeekBar进度变化，会回调这个方法
         * @param seekBar
         * @param progress
         * @param fromUser 如果是用户引起的true,不是用户引起的false
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                videoview.seekTo(progress);
            }
        }

        /**
         * 当手指触碰的时候回调这个方法
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        /**
         * 当手指离开的时候回调这个方法
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
