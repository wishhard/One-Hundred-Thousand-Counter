package com.wishhard.ohtc.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.wishhard.ohtc.count_database.DataSource;
import com.wishhard.ohtc.model.Count;
import java.util.List;

import static com.wishhard.ohtc.service.ServiceOpConsts.*;


public class CountDBOpsService extends IntentService {

    private DataSource dataSource;

    private Count countObj;

    private String count_id;
    private List<String> count_ids;

    private int totalCount;
    private String newName;


    public CountDBOpsService() {
        super("CountDBOpsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        dataSource = new DataSource(getApplicationContext());
        dataSource.DBOpen();


        char op = intent.getCharExtra(OPERATION, WANA_DEFAULT_VALUE);

          assignValues(intent,op);


        switch (op) {
            case WANA_DELETE:
                dataSource.deleteCount(count_id);
                dataSource.DBClose();
                break;
            case WANA_SAVE:
                 dataSource.createCount(countObj);
                 dataSource.DBClose();
                break;
            case WANA_UPDATE_TOTAL_COUNT:
                dataSource.updateTotelCount(count_id,totalCount);
                dataSource.DBClose();
                break;
            case WANA_RENAME:
                dataSource.renameCount(count_id,newName);
                dataSource.DBClose();
                break;
            case WANA_LIST_DELETE:
                dataSource.deleteCount(count_ids);
                dataSource.DBClose();
                startLoadingService();
                break;
        }

    }

    private void assignValues(Intent i,char op) {

        switch (op) {
            case WANA_DELETE:
                 count_id =  i.getStringExtra(COUNT_ID_FOR_OP);
                break;
            case WANA_SAVE:
                 countObj = i.getParcelableExtra(THE_COUNT_OBJECT_TO_SAVE);
                break;
            case WANA_UPDATE_TOTAL_COUNT:
                count_id =  i.getStringExtra(COUNT_ID_FOR_OP);
                totalCount = i.getIntExtra(THE_COUNT_VALUE_FOR_UPDAED,0);
                break;
            case WANA_RENAME:
                count_id =  i.getStringExtra(COUNT_ID_FOR_OP);
                newName = i.getStringExtra(THE_COUNT_TITEL_RENAME);
                break;
            case WANA_LIST_DELETE:
                count_ids = i.getStringArrayListExtra(COUNT_ID_LIST_FOR_OP);
                break;
        }
    }

    private void startLoadingService() {
        Intent i = new Intent(CountDBOpsService.this,LoadDataService.class);
        startService(i);
    }
}
