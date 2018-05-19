package com.example.administrator.memo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/4/18.
 */

class AlarmNote extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        //获得标题
        String messageTitle = intent.getStringExtra("messageTitle");
        //获得内存
        String messageContent = intent.getStringExtra("messageContent");
        Intent intent1 = new Intent(context,Alarm.class);
        intent1.putExtra("mesageTitle",messageTitle);
        intent1.putExtra("messageContent",messageContent);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //调用alarm
        context.startActivity(intent1);
    }
}
