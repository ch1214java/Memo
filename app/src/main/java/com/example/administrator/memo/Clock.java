package com.example.administrator.memo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Clock extends AppCompatActivity {

    Button bt_steeing, bt_quxiao;
    TextView tv_open;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        init();
        setview();
    }

    public void init() {
        tv_open = (TextView) findViewById(R.id.tv_open);
        bt_steeing = (Button) findViewById(R.id.bt_steeing);
        bt_quxiao = (Button) findViewById(R.id.bt_quxiao);
        //获得闹钟管理者
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    public void setview() {
        //获取当前系统的时间
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        bt_steeing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(Clock.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        //时间一到，发送广播（闹钟响了）
                        Intent intent = new Intent();
                        intent.setAction("com.example.administrator.memo.RING");
                        //将来时态的跳转
                        PendingIntent pendingIntent = PendingIntent.getService(Clock.this, 0x101, intent, 0);
                        //设置闹钟
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                        tv_open.setText(hourOfDay + ":" + minute);
                    }
                }, hour,minute , true);
                tpd.setTitle("设置时间");
                //显示时间控件
                tpd.show();
                Toast.makeText(Clock.this,"设置的时间为"+hour+":"+minute,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
