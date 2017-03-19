package com.example.wjk.mediaplay.VideoPagerAdapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wjk.mediaplay.R;
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
        ViewHolder viewholder;
        //设置适配器的Item布局
        if(convertView == null) {
            convertView = View.inflate(context, R.layout.item_video_pager, null);
            //实例化viewholder
            viewholder = new ViewHolder();
            viewholder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewholder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewholder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewholder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            convertView.setTag(viewholder);
        }else{
            viewholder = (ViewHolder) convertView.getTag();
        }

        //根据position得到列表中对应位置的数据
        MediaItem mediaItem = mediaItems.get(position);
        viewholder.tv_name.setText(mediaItem.getName());
        viewholder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));
        viewholder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));

        return convertView;
    }

    /**
     * 使用静态内部类可以在一个外部类对象中实例化多个静态内部类对象
     */
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_size;
    }
}
