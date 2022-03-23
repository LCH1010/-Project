package com.example.project1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PodcastDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "heat.db";
    public static final String TABLE_NAME = "podcastdb";

    public PodcastDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (NAME TEXT, TEL TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

// 셀렉트는 공통으로 해주고
// 없으면 켜줄때 인설트
// 있으면 삭제할려고 딜리트