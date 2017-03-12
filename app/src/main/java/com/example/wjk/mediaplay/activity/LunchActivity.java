package com.example.wjk.mediaplay.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.wjk.mediaplay.R;

public class LunchActivity extends AppCompatActivity {

    private static final String TAG = LunchActivity.class.getSimpleName();
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {

                //两秒之后启动MainActivity
                //执行在主线程
                startMainActivity();
            }
        },2000);
    }
    private boolean isStartActivity = false;//MainActivit启动标示，保证MainActivity只启动一次

    //通过显示意图进行Activity之间的跳转
    private void startMainActivity() {
        if(!isStartActivity){
            isStartActivity = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //关闭当前页面
            finish();
        }

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.e(TAG, "onTouchEvent-Action:"+event.getAction());
//        startMainActivity();
//        return super.onTouchEvent(event);
//    }

    @Override
    protected void onDestroy() {
        //移除所有的回调和消息
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
