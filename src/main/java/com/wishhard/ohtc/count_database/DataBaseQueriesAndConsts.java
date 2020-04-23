package com.wishhard.ohtc.count_database;



public class DataBaseQueriesAndConsts {

    public static final String TABLE_NAME = "saved_count";
    public static final String ID = "_id";
    public static final String COUNT_TITLE = "count_title";
    public static final String TOTEL_COUNT = "totel_count";
    public static final String COUNT_CREATED_TIME = "count_created_time";

    public static final String DB_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " TEXT PRIMARY KEY, "
            + COUNT_TITLE + " TEXT, " + TOTEL_COUNT + " INTEGER, " + COUNT_CREATED_TIME + " TEXT )";
}
