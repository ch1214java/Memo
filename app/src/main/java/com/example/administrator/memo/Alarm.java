package com.example.administrator.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

/**
 * Created by Administrator on 2018/4/18.
 */

class Alarm extends AppCompatActivity{
    //媒体播放器
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        /*try{
            //播放指定音乐
            mediaPlayer = MediaPlayer.create(Alarm.this,ActivityManager.getUri());
            //设置播放nag的音量
            mediaPlayer.setVolume(300,300);
            //设置循环
            mediaPlayer.setLooping(true);
        }catch (Exception e){
            Toast.makeText(Alarm.this,"音乐文件播放异常",Toast.LENGTH_SHORT).show();

        }*/
        startAlarm();
        //开始播放
        mediaPlayer.start();
        Intent intent = getIntent();

        //获得标题
        String messageTitle = intent.getStringExtra("messageTitle");
        //获得内容
        String messageContent = intent.getStringExtra("messageContent");
        //新建对话框
        AlertDialog.Builder adb = new AlertDialog.Builder(Alarm.this);
        adb.setTitle(messageTitle);
        adb.setMessage(messageContent);
        adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //关闭媒体播放器
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
            }
        });
        //显示对话框
        adb.show();
    }
    private void startAlarm() {
        mediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mediaPlayer.setLooping(true);
        try {
            mediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    //获取系统默认铃声的Uri
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }
}
