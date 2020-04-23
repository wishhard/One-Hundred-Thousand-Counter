package com.wishhard.ohtc.model;


import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.wishhard.ohtc.count_database.DataBaseQueriesAndConsts;

import java.util.UUID;

public class Count implements Parcelable {

    private String _id;
    private String title;
    private int totel_count;
    private String count_created_time;

    public Count(String title, int totel_count, String count_created_time) {
        _id = UUID.randomUUID().toString();
        this.title = title;
        this.totel_count = totel_count;
        this.count_created_time = count_created_time;
    }



    public Count() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotel_count() {
        return totel_count;
    }

    public void setTotel_count(int totel_count) {
        this.totel_count = totel_count;
    }

    public String getCount_created_time() {
        return count_created_time;
    }

    public void setCount_created_time(String count_created_time) {
        this.count_created_time = count_created_time;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        Count countOdj = (Count) obj;
        if(countOdj.get_id().equals(this.get_id())) {
            return true;
        }

        if(countOdj.getTitle().equalsIgnoreCase(this.getTitle())) {
            return true;
        }

        return false;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues();

        values.put(DataBaseQueriesAndConsts.ID, _id);
        values.put(DataBaseQueriesAndConsts.COUNT_TITLE, title);
        values.put(DataBaseQueriesAndConsts.TOTEL_COUNT, totel_count);
        values.put(DataBaseQueriesAndConsts.COUNT_CREATED_TIME, count_created_time);

        return values;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.title);
        dest.writeInt(this.totel_count);
        dest.writeString(this.count_created_time);
    }

    protected Count(Parcel in) {
        this._id = in.readString();
        this.title = in.readString();
        this.totel_count = in.readInt();
        this.count_created_time = in.readString();
    }

    public static final Creator<Count> CREATOR = new Creator<Count>() {
        @Override
        public Count createFromParcel(Parcel source) {
            return new Count(source);
        }

        @Override
        public Count[] newArray(int size) {
            return new Count[size];
        }
    };
}
