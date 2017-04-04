package com.example.wjk.mediaplay.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wjk.mediaplay.R;
import com.example.wjk.mediaplay.domain.MediaItem;
import com.example.wjk.mediaplay.utils.Utils;
import com.example.wjk.mediaplay.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wjk on 2017/3/19.
 */

public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    private static final int HIDE_MEDIACONTROLLER = 2;
    private static final int DEFAULT_SCREEN = 1 ;
    private static final int FULL_SCREEN = 2;
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
    private RelativeLayout media_controller;
    /**
     * 视频进度的更新
     */
    private final int PROGRESS = 1;
    private MyReceiver receiver;
    /**
     * 传入的视频播放列表
     */
    private ArrayList<MediaItem> mediaItems;
    /**
     * 传入的点击播放位置
     */
    private int position;
    /**
     * 定义手势识别器
     */
    private GestureDetector detector;

    /**
     * 默认隐藏控制面板
     */
    private boolean ismedia_controller = false;
    private boolean isFullScreen = false;
    /**
     * 屏幕的宽和高
     */
    private int screenWidth;
    private int screenHeight;
    /**
     * 视频的宽和高
     */
    private int videoWidth;
    private int videoHeight;

    /**
     * 调节声音
     */
    private AudioManager am;
    /**
     * 当前音量
     */
    private int currentVoice;
    /**
     * 最大音量
     */
    private int maxVoice;

    /**
     *是否是静音
     */
    private boolean isMute = false;
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
        media_controller = (RelativeLayout)findViewById( R.id.media_controller );

        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSwitchScreen.setOnClickListener( this );

        seekbarVoice.setMax(maxVoice);
        seekbarVoice.setProgress(currentVoice);
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
            isMute = !isMute;
            // Handle clicks for btnVoice
            updateVoice(currentVoice,isMute);
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
        } else if ( v == btnExit ) {
            // Handle clicks for btnExit
            finish();
        } else if ( v == btnVideoPre ) {
            // Handle clicks for btnVideoPre
            playPreVideo();
        } else if ( v == btnVideoStartPause ) {
            // Handle clicks for btnVideoStartPause
            // Handle clicks for btnVideoStartPause
            startAndPause();
        } else if ( v == btnVideoNext ) {
            // Handle clicks for btnVideoNext
            playNextVideo();
        } else if ( v == btnVideoSwitchScreen ) {
            // Handle clicks for btnVideoSwitchScreen
            setFullScreenAndDefault();
        }
        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
    }

    private void startAndPause() {
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
    }

    private void playPreVideo() {
        if(mediaItems != null && mediaItems.size() > 0){
            position-- ;
            if(position >= 0){
                MediaItem mediaitem = mediaItems.get(position);
                tvName.setText(mediaitem.getName());
                videoview.setVideoPath(mediaitem.getData());
                setButtonState();
            }
        }else if (uri != null){
            setButtonState();
        }
    }

    private void playNextVideo() {
        if(mediaItems != null && mediaItems.size() > 0){
            position++ ;
            if(position < mediaItems.size()){
                MediaItem mediaitem = mediaItems.get(position);
                tvName.setText(mediaitem.getName());
                videoview.setVideoPath(mediaitem.getData());
                setButtonState();
            }
        }else if (uri != null){
            setButtonState();
        }

    }

    private void setButtonState() {
        if(mediaItems != null && mediaItems.size() >0){
            //一个视频的情况
            if(mediaItems.size() ==1 ){
                setEnable(false);
            //两个视频的情况
            }else if(mediaItems.size() == 2){
                if(position==0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);

                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);

                }else if(position==mediaItems.size()-1){
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);

                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);

                }
            //三个视频的情况
            }else {
                if (position == 0) {
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);
                } else if (position == mediaItems.size() - 1) {
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                } else {
                    setEnable(true);
                }
            }
        }else if (uri != null){
            setEnable(false);
        }
    }

    private void setEnable(boolean isEnable) {
        if(isEnable){
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoPre.setEnabled(true);
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
            btnVideoNext.setEnabled(true);
        }else{
            //两个按钮设置灰色
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoPre.setEnabled(false);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);
        }
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HIDE_MEDIACONTROLLER:
                    hideMediaController();
                    break;
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

        initData();
        findViews();
        getData();
        setData();
        //设置控制面板
//        videoview.setMediaController(new MediaController(this));

        setListener();

    }

    private void setData() {
        if(mediaItems != null && mediaItems.size()>0){
            MediaItem mediaitem = mediaItems.get(position);
            tvName.setText(mediaitem.getName());//设置视频的名称
            videoview.setVideoPath(mediaitem.getData());
        }else if(uri != null) {
            tvName.setText(uri.toString());
            videoview.setVideoURI(uri);
        }else{
            Toast.makeText(SystemVideoPlayer.this, "没有传递数据",Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        uri = getIntent().getData();
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videoList");
        position = getIntent().getIntExtra("position",0);
    }

    private void initData() {
        utils = new Utils();
        //注册广播
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,filter);
        //实例化手势识别器
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                startAndPause();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                setFullScreenAndDefault();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(ismedia_controller){
                    //隐藏控制面板
                    hideMediaController();
                    handler.removeMessages(HIDE_MEDIACONTROLLER);
                }else{
                    //显示控制面板
                    showMediaController();
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
                }
                return super.onSingleTapConfirmed(e);
            }
        });
        //得到屏幕的宽和高
        //过时的方式
//    WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//    int screenWidth = wm.getDefaultDisplay().getWidth();
//    int screenHeight = wm.getDefaultDisplay().getHeight();
//        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//        screenHeight = getWindowManager().getDefaultDisplay().getHeight();

//    //得到屏幕的宽和高最新方式
//
        WindowManager vm = this.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        vm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        //得到音量
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        //得到当前音量
        currentVoice = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        //得到最大音量
        maxVoice = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

    }

    private void setFullScreenAndDefault() {
        if(isFullScreen){
            //默认
            setVideoType(DEFAULT_SCREEN);
        }else{
            //全屏
            setVideoType(FULL_SCREEN);
        }
    }

    private void setVideoType(int defaultScreen) {
        switch (defaultScreen){
            case FULL_SCREEN://全屏
                //1.设置视频画面的大小-屏幕有多大就是多大
                videoview.setVideoSize(screenWidth,screenHeight);
                //2.设置按钮的状态-默认
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_screen_default_selector);
                isFullScreen = true;
                break;
            case DEFAULT_SCREEN://默认
                //1.设置视频画面的大小
                //视频真实的宽和高
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;

                //屏幕的宽和高
                int width = screenWidth;
                int height = screenHeight;

                // for compatibility, we adjust size based on aspect ratio
                if ( mVideoWidth * height  < width * mVideoHeight ) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if ( mVideoWidth * height  > width * mVideoHeight ) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }

                videoview.setVideoSize(width,height);
                //2.设置按钮的状态--全屏
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_screen_full_selector);
                isFullScreen = false;
                break;
        }
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

        seekbarVoice.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();
            //当底层解码准备好的时候
            videoview.start();//开始播放
            //1.视频的总时长，关联总长度
            int duration = videoview.getDuration();
            seekbarVideo.setMax(duration);
            tvDuration.setText(utils.stringForTime(duration));
            hideMediaController();//默认是隐藏控制面板
            handler.sendEmptyMessage(PROGRESS);
            //屏幕的默认播放
            setVideoType(DEFAULT_SCREEN);
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
            playNextVideo();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
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
            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        /**
         * 当手指离开的时候回调这个方法
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
        }
    }

    private class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                if(progress>0){
                    isMute = false;
                }else{
                    isMute= true;
                }
                updateVoice(progress,isMute);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 4000);
        }
    }

    /**
     * 调节音量大小
     * @param progress
     * @param isMute
     */
    private void updateVoice(int progress, boolean isMute) {
        if(isMute){
            //第四个参数表示是否调起系统调节器：0~不调起 1~调起
            am.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
            seekbarVoice.setProgress(0);
        }else{
            am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            seekbarVoice.setProgress(progress);
            currentVoice = progress;
        }
    }

    @Override
    protected void onDestroy() {
        //释放资源的时候，先释放子类的资源，再释放父类的资源
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将事件传递给手势识别器
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 显示控制面板
     */
    private void showMediaController(){
        media_controller.setVisibility(View.VISIBLE);
        ismedia_controller = true;
    }

    /**
     * 隐藏控制面板
     */
    private void hideMediaController(){
        media_controller.setVisibility(View.GONE);
        ismedia_controller = false;
    }

}
