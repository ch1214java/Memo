package com.example.administrator.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


/**
 * Created by Administrator on 2018/4/18.
 */

class Lookover extends AppCompatActivity {
    String noteId;
    SQLiteDatabase sdb;
    ActivityManager activityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookover);
        final TextView etnoteMain = (TextView) findViewById(R.id.etnoteMain);
        final TextView etnoteName = (TextView) findViewById(R.id.etnoteName);
        final TextView etnoteTime = (TextView) findViewById(R.id.etnoteTime);
        //实例化activityManager
        activityManager = ActivityManager.getInstance();
        activityManager.addActivity(this);
        Intent intent = getIntent();
        noteId = (String) intent.getSerializableExtra("noteId");
        SqliteDbHelp sdh = new SqliteDbHelp(Lookover.this);
        sdb = sdh.getReadableDatabase();
        Cursor c = sdb.query("note",new String[]{"noteId","noteName","noteTime","noteContext"},"noteId=?",new String[]{noteId},null,null,null);
        while (c.moveToNext()){
            etnoteName.setText(c.getString(c.getColumnIndex("noteName")));
            etnoteMain.setText(c.getString(c.getColumnIndex("noteContext")));
            etnoteTime.setText(c.getString(c.getColumnIndex("noteTime")));
        }
    }
    //新建菜单选项，
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,1,"关于");
        menu.add(0,2,2,"设置响铃");
        menu.add(0,3,3,"退出");


        return super.onCreateOptionsMenu(menu);
    }
    //为菜单选项绑定监听器

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //关于
            case 1:
                AlertDialog.Builder adb = new AlertDialog.Builder(Lookover.this);
                adb.setTitle("关于");
                adb.setMessage("备忘录V1.0");
                adb.setPositiveButton("确定",null);
                adb.show();
                break;
            //设置闹铃
            case 2:
                Intent intent = new Intent(Lookover.this,Clock.class);
                startActivity(intent);
               /* Intent alarms = new Intent(AlarmClock.ACTION_SET_ALARM);
                startActivity(alarms); //调用系统闹钟*/
                break;
                /*Intent i = new Intent();
                ComponentName cn = null;
                if(Integer.parseInt (Build.VERSION.SDK ) >=8){
                    cn = new ComponentName("com.android.calendar","com.android.calendar.LaunchActivity");
                }
                else{
                    cn = new ComponentName("com.google.android.calendar","com.android.calendar.LaunchActivity");
                }
                i.setComponent(cn);
                startActivity(i);
                //调用系统日历这个不用添加权限。*/

            //退出
            case 3:
                AlertDialog.Builder adb2 = new AlertDialog.Builder(Lookover.this);
                adb2.setTitle("消息");
                adb2.setMessage("真的要退出吗？");
                adb2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //关闭列表中所有Activity   关于闹钟的详细解释https://blog.csdn.net/shouwangyaoyuan/article/details/50557374
                        activityManager.exitAllprogress();
                    }
                });
                adb2.setNegativeButton("取消",null);
                //显示对话框
                adb2.show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
