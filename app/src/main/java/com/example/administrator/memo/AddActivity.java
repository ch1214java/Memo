package com.example.administrator.memo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    //标题、内容和时间
    private EditText etName,etMain,etTime;
    //“保存、取消”按钮
    private Button btCommit,btCancel;
    //数据库操作类
    private SQLiteDatabase sdb;
    private ActivityManager am;
    //年月日时分秒，用于保存日历详细信息
    private int year,month,day,hours,minute,second;
    private Calendar c;
    private PendingIntent pi;
    private AlarmManager alm;
    //编辑模式标志
    private boolean EDIT = false;
    private String noteId;
    //初始化函数
    /*Called when the activity is first created.*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();
        setTime();
        setTime();
        setView();
    }
    //初始化哥哥元素
    public void init(){
        //将当前activity添加到activity列表中
        am = ActivityManager.getInstance();
        am.addActivity(this);
        etName  = (EditText) findViewById(R.id.noteName);
        etMain  = (EditText) findViewById(R.id.noteMain);
        btCommit = (Button) findViewById(R.id.btCommit);
        btCancel = (Button) findViewById(R.id.btCancel);
        etTime = (EditText) findViewById(R.id.noteTime);
        Intent intent = getIntent();
        noteId = intent.getStringExtra("noteId");
        //如果noteId值不为空，则进入编辑模式
        if (noteId != null){
            EDIT = true;

        }else {
            EDIT = false;
        }
    }
    //设置默认闹钟为当前时间
    public void setTime(){
        //数据库链接类
        SqliteDbHelp sd = new SqliteDbHelp(AddActivity.this);
        //获得数据库操作类
        sdb = sd.getReadableDatabase();
        if (EDIT){
            //通过no忒多获得对应的信息
            Cursor c = sdb.query("note",new String[]{"noteId","noteName","noteContext","noteTime"},"noteId = ?",new String[]{noteId},null,null,null);
            //将获得的信息写对应的EditText
            while (c.moveToNext()){
                etName.setText(c.getString(c.getColumnIndex("noteName")));
                etMain.setText(c.getString(c.getColumnIndex("noteContext")));
                etTime.setText(c.getString(c.getColumnIndex("noteTime")));

            }
            c.close();
        }else{
            //设置默认闹钟为当前时间
            etTime.setText(am.returnTime());

        }
        //设置文本颜色为红色

    }

    //设置控件监听器
    public void setView(){
        etTime.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //实例化日历
                c =Calendar.getInstance();
                //去的日历信息中的年月日时分秒
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                hours = c.get(Calendar.HOUR);
                minute = c.get(Calendar.MINUTE);
                second = c.get(Calendar.SECOND);
                //新建一个日期选择控件
                DatePickerDialog dpd = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String[] time = {"",
                                hours + ":" + minute + ":" + second };
                        try {
                            //将日期和时间分割
                            String[] time2 =
                                    etTime.getText().toString().trim().split(" ");
                            //去的时间的信息保存到time[]中
                            if (time2.length == 2){
                                time[1] = time2[1];
                            }
                        }catch (Exception e){
                            //TODO Auto-generated catch bicck
                            e.printStackTrace();
                        }
                        String mo = "",
                                da = "";
                        //将月份转换成两位数
                        if (month<9){
                            mo = "0" + (month + 1);

                        }else {
                            mo = month+1 + "";
                        }
                        //将天数转换成两位数
                        if(dayOfMonth <10){
                            da = "0" + dayOfMonth;
                        }else {
                            da = dayOfMonth + "";
                        }
                        //将设置的结果保存到etTime中
                        etTime.setText(year + "-" + mo + "-" +da + " " + time[1]);
                    }
                },year,month,day);
                dpd.setTitle("设置日期");
                //显示日期控件
                dpd.show();
                return true;
            }
        });
        //设置单机监听器，弹出时间选择界面
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化日历
                c = Calendar.getInstance();
                //去的日历信息中的年月日时分秒
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                hours = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                second = c.get(Calendar.SECOND);
                //新建时间选择器
                TimePickerDialog tpd = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String[] time = {"",
                                year + "-" + month + "-" + day + " " };
                        try {
                            //将日期和时间分割
                            time = etTime.getText().toString().trim().split("");

                        }catch (Exception e){
                            //TODO Auto-generated catch bicck
                            e.printStackTrace();
                        }
                        String ho = "",mi ="";
                        //设置小时
                        if (hourOfDay<10){
                            ho = "0" + hourOfDay;
                        }else {
                            ho = hourOfDay + "";
                        }
                        //设置分钟
                        if(minute <10){
                            mi = "0" + minute;
                        }else {
                            mi = minute + "";
                        }
                        //将设置的结果保存到etTime中
                        etTime.setText(time[0] + " " + ho + ":" + mi);
                    }
                },hours,minute,true);
                tpd.setTitle("设置时间");
                //显示时间控件
                tpd.show();
            }

        });
        //设置“保存”按钮监听器
        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(AddActivity.this);
                //设置标题和信息
                adb.setTitle("保存");
                adb.setMessage("确定要保存吗？");
                //色画质按钮功能
                adb.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //保存备忘录信息
                        saveNote();
                    }
                });
                adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AddActivity.this,"不保存", Toast.LENGTH_SHORT).show();
                    }
                });
                //显示对话框
                adb.show();
            }
        });
        //设置“取消”按钮监听器
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(AddActivity.this);
                //设置标题和信息
                adb.setTitle("提示");
                adb.setMessage("确定不保存吗？");
                //色画质按钮功能
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //进入主界面
                        Intent ineent = new Intent(AddActivity.this,MainActivity.class);
                        startActivity(ineent);
                    }
                });
                adb.setNegativeButton("取消",null);
                //显示对话框
                adb.show();
            }
        });
    }

    //设置保存备忘录
    private void saveNote() {
        //取得输入的内容
        String name = etName.getText().toString().trim();
        String content = etMain.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        //内容和标题不能为空
        if ("".equals(name) || "".equals(content)){
            Toast.makeText(this,"名称和内容都不能为空",Toast.LENGTH_SHORT).show();

        }else{
            if (EDIT){
                am.saveNote(sdb,name,content,noteId,time);
                Toast.makeText(this,"更新成功",Toast.LENGTH_SHORT).show();
            }else {
               am.addNote(sdb,name,content,time);
                Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
            }
        }
        //分割日期和时间
        String[] t = etTime.getText().toString().trim().split(" ");
        //分割日期
        String[] t1 = t[0].split("-");
        //分割时间
        String[] t2 = t[1].split(":");
        //实例化日历
        Calendar c2 = Calendar.getInstance();
        //设置日历为闹钟的时间、
       c2.set(Integer.parseInt(t1[0]),Integer.parseInt(t1[1])-1,Integer.parseInt(t1[2]),Integer.parseInt(t2[0]),Integer.parseInt(t2[1]));
        c = Calendar.getInstance();
        //闹钟的时间应至少比现在多10s
        if (c.getTimeInMillis() + 1000 * 10 <= c2.getTimeInMillis()){
            String messageContent;
            //当内容大雨20个字时，切到一部分以“......”代替，并储存在messageContent中
            if (content.length() > 20){
                messageContent = content.substring(0,18) + "......";

            }else{
                messageContent = content;

            }
            Intent intent = new Intent(this,AlarmNote.class);
            //锄地标题和内容
            intent.putExtra("messageTitle",name);
            intent.putExtra("messageContent",messageContent);
            pi = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            //获得闹钟服务
            alm = (AlarmManager) getSystemService(ALARM_SERVICE);
            //设置闹钟
            alm.set(AlarmManager.RTC_WAKEUP,c2.getTimeInMillis(),pi);
        }
            Intent intent2 = new Intent(this,MainActivity.class);
            //回到主目录
            startActivity(intent2);
            AddActivity.this.finish();


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
                    AlertDialog.Builder adb = new AlertDialog.Builder(AddActivity.this);
                    adb.setTitle("关于");
                    adb.setMessage("备忘录V1.0");
                    adb.setPositiveButton("确定",null);
                    adb.show();
                    break;
                //设置闹铃
                case 2:
                    Intent intent = new Intent(AddActivity.this,SetAlarm.class);
                    startActivity(intent);
                    break;
                //退出
                case 3:
                    AlertDialog.Builder adb2 = new AlertDialog.Builder(AddActivity.this);
                    adb2.setTitle("消息");
                    adb2.setMessage("真的要退出吗？");
                    adb2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭列表中所有Activity
                            am.exitAllprogress();
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
        //按键判断


        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当按键是返回键时
            if (keyCode == KeyEvent.KEYCODE_BACK){
                AlertDialog.Builder adb = new AlertDialog.Builder(AddActivity.this);
                adb.setTitle("消息");
                adb.setMessage("是否要保存？");
                adb.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //保存备忘录
                        saveNote();
                    }
                });
                adb.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent(AddActivity.this,MainActivity.class);
                        startActivity(intent2);
                    }
                });
                //显示对话框
                adb.show();

            }
            return super.onKeyDown(keyCode, event);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭数据库
        sdb.close();
    }
}













