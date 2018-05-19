package com.example.administrator.memo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/4/18.
 */

public class SqliteDbHelp extends SQLiteOpenHelper {

    public SqliteDbHelp(Context context) {
        super(context, "NotePad", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Table brfore Create");
        db.execSQL("create table note(noteId integer primary key," +
                "noteName varchar(20)," +
                "noteTime varchar(20)," +
                "noteContext varchar(400))");
        System.out.println("Table after Create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        // TODO 每次成功打开数据库后首先被执行
        super.onOpen(db);
    }
}
