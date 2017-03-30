package com.example.administrator.kugou;

import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Kugou_test extends AppCompatActivity {


    private MyAdpter adapter;
    private ListView list_view;
    //装载歌曲的列表
    List<Song> test;
    private MediaPlayer mediapalyer;


    //正在播放歌曲的ID
    private int ID = 0 ;
    private TextView flow_name;
    private ImageView play;
    private ImageView last;
    private ImageView next;

    //设置进度条
    private SeekBar seekbar;
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int position = mediapalyer.getCurrentPosition();

            int mMax = mediapalyer.getDuration();
            int sMax = seekbar.getMax();
            seekbar.setProgress(position*sMax/mMax);
        }
    };

    //实现左边功能列表
    private ListView listView_gongneng;
    private MyAdpter_gongneng adpter;


    @Override
    protected void onResume() {
        super.onResume();
        query();
        //进行进度条位置的刷新
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    mHandle.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        init();
        //测试侧滑菜单
        ceshi();
    }

    private void ceshi() {

    }

    private void init() {
        play = (ImageView) findViewById(R.id.play);
        flow_name = (TextView) findViewById(R.id.flow_name);
        list_view = (ListView) findViewById(R.id.list_view);
        last = (ImageView) findViewById(R.id.last);
        next = (ImageView) findViewById(R.id.next);
        //进度条
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int dest = seekBar.getProgress();

                int mMax = mediapalyer.getDuration();
                int sMax = seekBar.getMax();
                mediapalyer.seekTo(mMax*dest/sMax);
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //根据当前播放的ID去播放下一首
                if(mediapalyer.isPlaying()){
                    mediapalyer.stop();
                    mediapalyer.release();
                    try {
                        if(ID == 0){
                            ID = test.size()-1;
                        }else{
                            ID = ID - 1 ;
                        }
                        Log.d("AAA", String.valueOf(ID));
                        mediapalyer = new MediaPlayer();
                        mediapalyer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediapalyer.setDataSource(test.get(ID).path);
                        flow_name.setText(test.get(ID).song);
                        mediapalyer.prepare();
                        mediapalyer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        if(ID == 0){
                            ID = test.size()-1;
                        }else{
                            ID = ID - 1 ;
                        }
                        mediapalyer = new MediaPlayer();
                        mediapalyer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediapalyer.setDataSource(test.get(ID).path);
                        flow_name.setText(test.get(ID).song);
                        mediapalyer.prepare();
                        mediapalyer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediapalyer.isPlaying()){
                    mediapalyer.stop();
                    mediapalyer.release();
                    try {
                        if(ID == (test.size()-1)){
                            ID = 0;
                        }else{
                            ID = ID + 1 ;
                        }
                        Log.d("AAA", String.valueOf(ID));
                        mediapalyer = new MediaPlayer();
                        mediapalyer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediapalyer.setDataSource(test.get(ID).path);
                        flow_name.setText(test.get(ID).song);
                        mediapalyer.prepare();
                        mediapalyer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        if(ID == (test.size()-1)){
                            ID = 0;
                        }else{
                            ID = ID + 1 ;
                        }
                        mediapalyer = new MediaPlayer();
                        mediapalyer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediapalyer.setDataSource(test.get(ID).path);
                        flow_name.setText(test.get(ID).song);
                        mediapalyer.prepare();
                        mediapalyer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediapalyer.isPlaying()){
                    mediapalyer.start();
                    boolean a = mediapalyer.isPlaying();
                    Toast.makeText(Kugou_test.this,"播放状态"+a,Toast.LENGTH_LONG).show();
                    play.setImageResource(R.drawable.pause);

                }else{
                    mediapalyer.pause();
                    boolean a = mediapalyer.isPlaying();
                    Toast.makeText(Kugou_test.this,"播放状态"+a,Toast.LENGTH_LONG).show();
                    play.setImageResource(R.drawable.play);
                }
            }
        });

        mediapalyer = new MediaPlayer();
        mediapalyer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String song_id = String.valueOf(id);
                ID = Integer.parseInt(song_id);
                flow_name.setText(test.get(Integer.parseInt(song_id)).song);
                try {

                    if(!mediapalyer.isPlaying()){
                        mediapalyer.setDataSource(test.get(Integer.parseInt(song_id)).path);
                        mediapalyer.prepare();
                        mediapalyer.start();
                        boolean a = mediapalyer.isPlaying();
                        Toast.makeText(Kugou_test.this,"播放状态"+a,Toast.LENGTH_LONG).show();
                    }else {
                        mediapalyer.stop();
                        mediapalyer.release();
                        mediapalyer = new MediaPlayer();
                        mediapalyer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediapalyer.setDataSource(test.get(Integer.parseInt(song_id)).path);
                        flow_name.setText(test.get(ID).song);
                        mediapalyer.prepare();
                        mediapalyer.start();
                    }





                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        List<String> tool = new ArrayList<>();
        tool.add("本地音乐");
        tool.add("用户中心");
        tool.add("定时关闭");
        tool.add("皮肤中心");
        tool.add("当前定位");

        List<Integer> head = new ArrayList<>();
        head.add(R.drawable.zhuye);
        head.add(R.drawable.yonghu);
        head.add(R.drawable.dingshi);
        head.add(R.drawable.pifu);
        head.add(R.drawable.dingwei);
        //左边功能列表的实现
        listView_gongneng = (ListView) findViewById(R.id.left_listview);
        adpter = new MyAdpter_gongneng(tool,Kugou_test.this,head);
        listView_gongneng.setAdapter(adpter);

        //实现侧边栏的点击事件
        listView_gongneng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //实现位置功能
                if(id == 4){
                    Intent intent = new Intent();
                    intent.setClass(Kugou_test.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private  void query(){
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        test = Music_Utils.getMusicData(Kugou_test.this);
        adapter = new MyAdpter(Kugou_test.this,test);
        list_view.setAdapter(adapter);
    }
}
