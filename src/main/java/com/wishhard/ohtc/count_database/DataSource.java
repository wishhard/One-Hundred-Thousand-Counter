package com.wishhard.ohtc.count_database;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.wishhard.ohtc.CountActivity;
import com.wishhard.ohtc.model.Count;
import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private Context context;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDBHelper;



    public DataSource(Context context) {
        this.context = context;
        mDBHelper = new CountDBOpenHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
    }


    public void DBOpen() {
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public void DBClose() {
        mDBHelper.close();
    }

    public void createCount(Count count) {
        ContentValues values = count.toValues();
        mDatabase.insert(DataBaseQueriesAndConsts.TABLE_NAME,null,values);
    }

    public List<Count> getAllCounts() {

        List<Count> counts = new ArrayList<>();

        String queryString = "SELECT * FROM saved_count";

        Cursor cursor = mDatabase.rawQuery(queryString,null);

        try {
            while (cursor.moveToNext()) {
                Count count = new Count(cursor.getString(cursor.getColumnIndex(DataBaseQueriesAndConsts.COUNT_TITLE)),
                        cursor.getInt(cursor.getColumnIndex(DataBaseQueriesAndConsts.TOTEL_COUNT)),
                        cursor.getString(cursor.getColumnIndex(DataBaseQueriesAndConsts.COUNT_CREATED_TIME))
                        );

               count.set_id(cursor.getString(cursor.getColumnIndex(DataBaseQueriesAndConsts.ID)));

                counts.add(count);
            }
        }finally {

            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return counts;

    }

    public long getCountDBitemCount() {
         return DatabaseUtils.queryNumEntries(mDatabase, DataBaseQueriesAndConsts.TABLE_NAME);
    }



    public void deleteCount(String countId) {
        String selrction = DataBaseQueriesAndConsts.ID + " = ?";
        String[] selectionAgrs = {countId};


        SharedPreferences sp = context.getSharedPreferences(CountActivity.REMEBER_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sp.edit();

        if(countId.equals(sp.getString("count_id",""))) {
            editor.clear().commit();
        }


        mDatabase.delete(DataBaseQueriesAndConsts.TABLE_NAME,selrction,selectionAgrs);
    }

    public void deleteCount(List<String> countIds) {
        for (String countId:countIds) {
            deleteCount(countId);
        }
    }


    public void updateTotelCount(String countId,int totelCount) {

        ContentValues values = new ContentValues();
        values.put(DataBaseQueriesAndConsts.ID, countId);
        values.put(DataBaseQueriesAndConsts.TOTEL_COUNT, totelCount);

        String whereClase = DataBaseQueriesAndConsts.ID + " = ?";

        String[] slectionArgs = {countId};

        mDatabase.update(DataBaseQueriesAndConsts.TABLE_NAME,values,whereClase,slectionArgs);

    }

    public void renameCount(String countId,String countTitel) {

        ContentValues values = new ContentValues();
        values.put(DataBaseQueriesAndConsts.ID, countId);
        values.put(DataBaseQueriesAndConsts.COUNT_TITLE, countTitel);

        String whereClase = DataBaseQueriesAndConsts.ID + " = ?";

        String[] slectionArgs = {countId};

        mDatabase.update(DataBaseQueriesAndConsts.TABLE_NAME,values,whereClase,slectionArgs);

    }




}
