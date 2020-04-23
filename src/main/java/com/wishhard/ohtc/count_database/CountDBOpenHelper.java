package com.wishhard.ohtc.count_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class CountDBOpenHelper extends SQLiteOpenHelper {

    private static  final String DATABASE_NAME = "count.db";
    private static  final int  DB_VERSION = 1;

    public CountDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseQueriesAndConsts.DB_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL("DROP TABLE IF EXISTS " + DataBaseQueriesAndConsts.TABLE_NAME);
         onCreate(db);
    }
}
