package com.example.wjk.mediaplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wjk.mediaplay.R;

/**
 * Created by wjk on 2017/3/17.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {
//    搜索栏
    private View tv_search;
    //游戏图标
    private View rl_game;
    //记录
    private View iv_record;
    //上下文
    private Context context;

    /**
     * 在代码中实例化该类的时候使用这个方法
     * @param context
     */
    public TitleBar(Context context) {
        this(context,null);
    }
    /**
     * 当在布局文件使用该类的时候，Android系统通过这个构造方法实例化该类
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    /**
     * 当需要设置样式的时候，可以使用该方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    /**
     * 当布局文件加载完成的时候回调这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //得到孩子的实例
        tv_search = getChildAt(1);
        rl_game = getChildAt(2);
        iv_record = getChildAt(3);

        //设置点击事件
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Toast.makeText(context,"搜索",Toast.LENGTH_LONG).show();
                break;
            case R.id.rl_game:
                Toast.makeText(context, "游戏",Toast.LENGTH_LONG).show();
                break;
            case R.id.iv_record:
                Toast.makeText(context,"记录",Toast.LENGTH_LONG).show();
                break;
        }

    }
}
