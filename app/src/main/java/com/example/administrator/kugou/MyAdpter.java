package com.example.administrator.kugou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/3/12 0012.
 */

public class MyAdpter extends BaseAdapter {
    private Context context;
    private List<Song> songList;
    public  MyAdpter(Context context,List<Song> songList){
        this.context = context;
        this.songList = songList;
    }
    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview,parent,false);
        }
        TextView song_name = (TextView) convertView.findViewById(R.id.name);
        TextView song_size = (TextView) convertView.findViewById(R.id.artist);
        song_name.setText(songList.get(position).song);
        song_size.setText(Music_Utils.formatTime(songList.get(position).duration));


        return convertView;
    }
}
