package com.example.administrator.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //显示备忘录文件
    private ListView listView;
    //数据库帮助类
    private SQLiteOpenHelper sdh;
    //每页显示的数目
    private static int page_size = 2;
    //初始化页数
    private static int page_no = 1,page_count = 0,count = 0;
    private Button btadd,btfirst,btend;
    private ImageButton imagebtpre,iamgebtnext;
    private SimpleAdapter simpleAdapter;
    //进度条
    private ProgressBar m_progressbar;
    private Intent intent;
    ActivityManager activityManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置显示进度条
        setProgressBarVisibility(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化activityManager
        activityManager = ActivityManager.getInstance();
        activityManager.addActivity(this);
        init();
        fenye();
        setview();
        int i = 2;
        int a = i++;
        Log.i("aaa",String.valueOf(a));

    }
    public void init(){
        btadd = (Button)findViewById(R.id.btadd);
        btfirst = (Button)findViewById(R.id.btfirst);
        btend = (Button)findViewById(R.id.btend);
        imagebtpre = (ImageButton) findViewById(R.id.imagebtpre);
        iamgebtnext = (ImageButton) findViewById(R.id.imagebtnext);
        m_progressbar = (ProgressBar) findViewById(R.id.progressbar);
        listView = (ListView)findViewById(R.id.listview);
        sdh = new SqliteDbHelp(MainActivity.this);
    }
    //获取数据库数据并分页显示
    public void fenye(){

        SQLiteDatabase sdb = sdh.getReadableDatabase();
        count = 0;
        //从数据库中查询数据，按升序排列
        Cursor c1 = sdb.query("note",new String[]{"noteId","noteName","noteTime"},null,null,null,null,"noteId asc");
        while (c1.moveToNext()){
            int noteid = c1.getInt(c1.getColumnIndex("noteId"));
            //保存数据的总数
            if (noteid > count)
                count = noteid;

        }
        c1.close();

        //取得总页数
        page_count = count % page_size == 0 ? count / page_size : count / page_size + 1;

        //到达首页
        if (page_no < 1)
            page_no = 1;

        //到达末页
        if (page_no >page_count)
            page_no = page_count;

        //查询指定页数的数据
        Cursor c = sdb.rawQuery("select noteId,noteName,noteTime from note limit ?,?", new String[]{(page_no - 1) * page_size + "",page_size +""});
        //Log.i("aa", String.valueOf(c));
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        //遍历循环取得所有数据，并存储到list中
        while (c.moveToNext()){
            Map<String,Object> map = new HashMap<String,Object>();
            //取备忘录名字
            String strName = c.getString(c.getColumnIndex("noteName"));
            //如果字数超过12字则去掉后面的字符用......代替
            if (strName.length() > 20){
                map.put("noteName",strName.substring(0,20) + "......");
            }else {
                map.put("noteName",strName);
            }
            //取得时间和ID信息存储到map中
            map.put("noteTime",c.getString(c.getColumnIndex("noteTime")));
            map.put("noteId",c.getInt(c.getColumnIndex("noteId")));
            //将map添加到list中
            list.add(map);
        }
        c.close();
        sdb.close();
        if (count > 0){
            //新建适配器
            simpleAdapter = new SimpleAdapter(MainActivity.this,list,R.layout.listview_main,new String[]{"noteName","noteTime"},new int[]{R.id.noteName,R.id.noteTime});
            listView.setAdapter(simpleAdapter);
        }

    }
    //菜单按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //添加菜单
        menu.add(0,1,1,"设置铃声");
        menu.add(0,2,2,"退出");
        return super.onCreateOptionsMenu(menu);
    }
    //为菜单按钮绑定按键监听器
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //设置铃声
            case 1:
                Intent intent = new Intent(MainActivity.this,SetAlarm.class);
                //跳转到设置铃声的界面
                startActivity(intent);
                break;
            //退出
            case 2:
                AlertDialog.Builder adb2 = new AlertDialog.Builder(MainActivity.this);
                adb2.setTitle("消息");
                adb2.setMessage("确定要退出吗？");
                adb2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //关闭所有activity
                        activityManager.exitAllprogress();

                    }
                });
                adb2.setNegativeButton("取消",null);
                //显示对话框，询问用户是否确定退出
                adb2.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    //当用户按键时触发
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果用户按下back键
        if (keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setTitle("消息");
            adb.setMessage("确定要退出吗？");
            adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activityManager.exitAllprogress();
                }
            });
            adb.setNegativeButton("取消",null);
            //显示对话框询问是否确定要退出
            adb.show();
        }
        return super.onKeyDown(keyCode, event);
    }
    //设置按键监听
    public void setview(){


        //设置listview 的按键监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> map = (Map<String,Object>) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                //传递备忘录的noteid
                intent.putExtra("noteId",map.get("noteId").toString());
                intent.setClass(MainActivity.this,Lookover.class);
                //查看备忘录
                startActivity(intent);
            }
        });
        //设置listview长按监听器
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Map<String,Object> map = (Map<String,Object>) parent.getItemAtPosition(position);
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle((CharSequence) map.get("noteName".toString()));
                //设置弹出选项
                adb.setItems(new String[]{"删除", "修改"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            //删除
                            case 0:
                                SQLiteDatabase sdb = sdh.getReadableDatabase();
                                sdb.delete("note","noteId=?",new String[]{map.get("noteId").toString()});
                                Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                sdb.close();
                                //刷新界面
                                fenye();
                                break;
                            case 1:
                                Intent intent = new Intent();
                                intent.putExtra("noteId",map.get("noteId").toString());
                                intent.setClass(MainActivity.this,AddActivity.class);
                                //进入编辑界面
                                startActivity(intent);
                                break;
                        }
                    }
                });
                //显示对话框
                adb.show();
                return true;
            }
        });
        //设置“添加”俺家监听器
        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示进度条
                m_progressbar.setVisibility(View.VISIBLE);
                m_progressbar.setProgress(0);
                intent = new Intent(MainActivity.this,AddActivity.class);
                //进入添加界面
                startActivity(intent);
            }
        });
        //进入首页
        btfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是首页，提示用户当前已经是首页了
                if (page_no == 1){
                    Toast.makeText(MainActivity.this,"已经是首页了",Toast.LENGTH_SHORT).show();

                }else {
                    //如果不是首页则将当前页码为1
                    page_no =1;

                }
                //刷新页面
                fenye();
            }
        });
        //设置“下一页”按钮监听器
        iamgebtnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果当前是最后一页
                if (page_no == page_count){
                    Toast.makeText(MainActivity.this,"已经是末页了", Toast.LENGTH_SHORT).show();

                }else {
                    //否则，当前的页码加1
                    page_no +=1;

                }
                //刷新页面
                fenye();
            }
        });
        //设置“上一页”按钮监听器
        imagebtpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果当前是第一页，则提示用户当前已经是首页
                if (page_no == 1){
                    Toast.makeText(MainActivity.this,"已经是首页了",Toast.LENGTH_SHORT).show();

                }else {
                    //否则当亲页码-1；
                    page_no -= 1;

                }
                //刷新界面
                fenye();
            }

        });
        //设置“末页”按钮监听器
        btend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果当前是最后一页，则提示用户当前已经是末页
                if (page_no == page_count){
                    Toast.makeText(MainActivity.this,"已经是末页了",Toast.LENGTH_SHORT).show();

                }else {
                    //否则将当前页这是为末页

                    page_no = page_count;

                }
                //刷新界面
                fenye();
            }

        });

    }

}
