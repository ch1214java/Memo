package com.example.administrator.memo;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/18.
 */
public class SetAlarm extends AppCompatActivity{
    //显示音乐文件的列表
    private ListView listView;
    //列表适配器
    private SimpleAdapter sim;
    //音乐文件搜索路径
    private static final String MUSIC_PATH = new String("/sdcard/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setalarm);
        init();
        setview();
    }
    public void init(){
        listView = (ListView) findViewById(R.id.list);
        //显示音乐文件
        musicList();
    }
    public void setview(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> map = (Map<String,Object>) parent.getItemAtPosition(position);
                //取得音乐文件名称
                final String name = (String) map.get("musicName");
                //创建对话框
                AlertDialog.Builder adb = new AlertDialog.Builder(SetAlarm.this);
                adb.setTitle("提示消息");
                adb.setMessage("确定要将"+name+"设置为默认闹铃铃声吗？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(MUSIC_PATH + name);
                        //设置闹铃的路径
                        ActivityManager.setUri(uri);
                        Toast.makeText(SetAlarm.this,"设置成功",Toast.LENGTH_SHORT).show();
                        //关闭当前页面
                        finish();
                    }
                });
                adb.setNegativeButton("取消",null);
                //显示
                adb.show();
            }
        });
    }

    //显示音乐文件
    private void musicList() {
        //取得需要便利的文件目录
        File home = new File(MUSIC_PATH);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        //便利文件目录
        /*if (home.listFiles(new MusicFilter()).length > 0){
            for (File file : home.listFiles(new MusicFilter())){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("musicName",file.getName());
                list.add(map);
            }
            sim = new SimpleAdapter(SetAlarm.this,list,R.layout.listview_setalarm,new String[]{"musicName"},new int[]{R.id.musicNmae});
            listView.setAdapter(sim);;

        }*/
    }
    //过滤所有不是以。MP3结尾的文件
    class MusicFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3"));
        }
    }
}
