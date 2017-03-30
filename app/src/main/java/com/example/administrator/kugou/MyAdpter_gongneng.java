package com.example.administrator.kugou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/3/12 0012.
 */
public class MyAdpter_gongneng extends BaseAdapter{
    private List<String> tool;
    private Context context;
    private List<Integer> headImg;
    public MyAdpter_gongneng(List<String> tool,Context context,List<Integer> headImg){
        this.tool = tool;
        this.context=context;
        this.headImg = headImg;
    }
    @Override
    public int getCount() {
        return tool.size();
    }

    @Override
    public Object getItem(int position) {
        return tool.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tool,parent,false);
        }
        TextView txt = (TextView) convertView.findViewById(R.id.txt_gongneng);
        ImageView head_img = (ImageView) convertView.findViewById(R.id.head_img);
        head_img.setImageResource(headImg.get(position));

        txt.setText(tool.get(position));

        return convertView;
    }
}
