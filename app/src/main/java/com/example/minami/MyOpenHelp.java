package com.example.minami;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyOpenHelp extends SQLiteOpenHelper {
    public static final String CREATE_USER = "create table User(" +
            "id integer primary key autoincrement," +
            "name String," +
            "password String," +
            "flag int," +
            "learn_num int," +
            "word_book int)";

    public static final String CREATE_VOCABULARY = "create table Vocabulary(" +
            "id integer primary key autoincrement," +
            "cn String," +
            "en String)";

    private Context mContext;

    public MyOpenHelp(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_VOCABULARY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
