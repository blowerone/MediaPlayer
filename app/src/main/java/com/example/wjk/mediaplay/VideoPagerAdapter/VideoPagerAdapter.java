package com.example.wjk.mediaplay.VideoPagerAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.wjk.mediaplay.domain.MediaItem;
import com.example.wjk.mediaplay.utils.Utils;

import java.util.ArrayList;

/**
 * Created by wjk on 2017/3/19.
 */

public class VideoPagerAdapter extends BaseAdapter{

    private  Context context;
    private ArrayList<MediaItem> mediaItems;
    private Utils utils;

    public VideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems){
        this.context = context;
        this.mediaItems = mediaItems;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //设置适配器的Item布局
        if(convertView == null){

        }
        return convertView;
    }
}
