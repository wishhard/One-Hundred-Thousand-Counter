package com.wishhard.ohtc.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wishhard.ohtc.count_database.DataSource;
import com.wishhard.ohtc.model.Count;


import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class LoadDataService extends IntentService {

    private DataSource dataSource;
    private  List<Count> counts;

    public LoadDataService() {
        super("LoadDataService");
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

            dataSource = new DataSource(getApplicationContext());
            dataSource.DBOpen();

        counts = dataSource.getAllCounts();
        dataSource.DBClose();

            EventBus.getDefault().post(counts);

    }


}
