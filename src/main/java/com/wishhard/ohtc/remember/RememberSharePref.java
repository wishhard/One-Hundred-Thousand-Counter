package com.wishhard.ohtc.remember;


import android.content.Context;
import android.content.SharedPreferences;

import com.wishhard.ohtc.CountActivity;

public class RememberSharePref {

    private static final String COUNT_ID = "count_id";
    private static final String SAVED = "saved";
    private static final String TOTAL_COUNT = "tc";
    private static final String COUNT_TITLE = "title";

    private Context context;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public RememberSharePref(Context context) {
        this.context = context;


        preferences = context.getSharedPreferences(CountActivity.REMEBER_PREF,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    public void remember(boolean saved,String count_id,String count_title,int total_count) {
        editor.putBoolean(SAVED,saved);
        editor.putString(COUNT_ID,count_id);
        editor.putString(COUNT_TITLE,count_title);
        editor.putInt(TOTAL_COUNT,total_count);
        editor.commit();

    }


    public boolean getSaved() {
         return preferences.getBoolean(SAVED,false);
    }

    public String getCountId() {
        return preferences.getString(COUNT_ID,"");
    }

    public String getCountTitle() {
        return preferences.getString(COUNT_TITLE,"");
    }


    public void setTotalCount(int total_Count) {
        editor.putInt(TOTAL_COUNT,total_Count);
        editor.commit();
    }

    public int getTotalCount() {
        return preferences.getInt(TOTAL_COUNT,0);
    }

    public boolean isRememberPrefEmpty() {
        if(!getSaved() && getCountTitle().endsWith("")) {
            return true;
        }

        return false;
    }

    public void emptyRemember() {
        editor.clear().commit();
    }
}
