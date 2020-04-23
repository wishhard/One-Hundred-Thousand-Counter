package com.wishhard.ohtc;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wishhard.ohtc.dialogs.NamingRulesDialogs;
import com.wishhard.ohtc.dialogs.RenameCountDialog;
import com.wishhard.ohtc.model.Count;
import com.wishhard.ohtc.odo_view.OdoMeter;
import com.wishhard.ohtc.remember.RememberSharePref;
import com.wishhard.ohtc.service.CountDBOpsService;
import com.wishhard.ohtc.service.ServiceOpConsts;
import com.wishhard.ohtc.views.FullyCountedTv;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CountActivity extends AppCompatActivity implements RenameCountDialog.RenameDialogListener,OdoMeter.OdoValueListener{
    public static String REMEBER_PREF = "remember";

    public static final String PARCELABLE_COUNT = "BLA_BLA";



    private ProgressDialog pd;
    private RememberSharePref pref;




    private int count;
    private boolean saved = false;
    private String count_title;
    private String count_id;

    private boolean logoPressed = false;

    private Count countObj = null;

    private OdoMeter odoMeter;


    private NamingRulesDialogs namingRulesDialogs;
    private RenameCountDialog renameCountDialog;

    private Toolbar toolbar;
    private EditText scEditText;
    private TextView countTitleTextView;
    private FullyCountedTv fullyCountedTv;
    private Button plusBtn,minusBtn;


    private Intent countIntent;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);


        pref = new RememberSharePref(this);

        namingRulesDialogs = new NamingRulesDialogs();

        fullyCountedTv = (FullyCountedTv) findViewById(R.id.fully_countedTv);


        countIntent = getIntent();
        try {
            countObj = countIntent.getParcelableExtra(PARCELABLE_COUNT);
        } catch (Exception e) {

        }

        countObjToPref();

                pd = new ProgressDialog(this);
                pd.setMessage("Please wait.");
                pd.setCancelable(false);


         toolbar = (Toolbar) findViewById(R.id.countActivity_toolbar);
         setSupportActionBar(toolbar);

        plusBtn = (Button) findViewById(R.id.plus_one_btn);
        minusBtn = (Button) findViewById(R.id.minus_one_btn);



        countTitleTextView = (TextView) findViewById(R.id.count_title);
        scEditText = (EditText) findViewById(R.id.sc_et);

        prefLoader();
        setVisiablity(saved);


        plusBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                   int action = motionEvent.getAction();
                 if(action == MotionEvent.ACTION_DOWN) {
                    plusBtn.setTextColor(Color.BLACK);
                 } else if(action == MotionEvent.ACTION_UP) {
                     plusBtn.setTextColor(Color.WHITE);
                 }
                return false;
            }
        });

        minusBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                if(action == MotionEvent.ACTION_DOWN) {
                    minusBtn.setTextColor(Color.BLACK);
                } else if(action == MotionEvent.ACTION_UP) {
                    minusBtn.setTextColor(Color.WHITE);
                }
                return false;
            }
        });

         scEditText.setInputType(0);

         scEditText.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View view, MotionEvent motionEvent) {
                  if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                      scEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                  }

                 return false;
             }
         });



         odoMeter = (OdoMeter) findViewById(R.id.odo_meter);
         odoMeter.setOdoValueListener(this);
         odoMeter.setAllCounts(count);
         odoMeter.resetOdoMeter();

          toolbarState2();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.count_meun,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();

        if(itemID == R.id.save) {
             hideKeyBoard();
             saveTheCount();
             setVisiablity(saved);
            return true;

        } else if(itemID == R.id.ca_delete) {
            newCountOrDeleteCount(true);
            return true;

        } else if(itemID == R.id.new_count) {
            newCountOrDeleteCount(false);
            return true;

        } else if(itemID == R.id.rest_count) {
            resetOdoCount();
            return true;
        } else if(itemID == R.id.rename) {
            renameCount();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(saved) {
            menu.findItem(R.id.save).setVisible(false);
            menu.findItem(R.id.new_count).setVisible(true);
            menu.findItem(R.id.ca_delete).setVisible(true);
            menu.findItem(R.id.rename).setVisible(true);
        } else {
            menu.findItem(R.id.save).setVisible(true);
            menu.findItem(R.id.new_count).setVisible(false);
            menu.findItem(R.id.ca_delete).setVisible(false);
            menu.findItem(R.id.rename).setVisible(false);
        }

        if(count <= 0) {
            menu.findItem(R.id.rest_count).setVisible(false);
        } else {
            menu.findItem(R.id.rest_count).setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        if(pref.isRememberPrefEmpty() && countTitleTextView.getVisibility() == View.VISIBLE) {
            newCountOrDeleteCount(false);
        }

        logoPressed = false;

        super.onResume();

    }



    @Override
    protected void onStop() {

        pref.remember(saved,count_id,count_title,count);

        if(!isMyServiceRunning(CountDBOpsService.class) && !logoPressed && saved) {
            updateTotalCountToDB();
        }
        super.onStop();
    }


    public void onPlusPassed(View view) {
         if(plusBtn.isEnabled()) {
                 count++;
                     odoMeter.post(new Runnable() {
                         @Override
                         public void run() {
                             odoMeter.setAllCounts(count);
                             odoMeter.odoConpoundCb();
                         }
                     });
         }
    }

    public void onMinusPassed(View view) {

        if(minusBtn.isEnabled()) {
            count--;
                odoMeter.post(new Runnable() {
                    @Override
                    public void run() {
                        odoMeter.setAllCounts(count);
                        odoMeter.odoConpoundCbReverse();
                    }
                });

        }

    }




    private void toolbarState2() {
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);


        View logo = toolbar.getChildAt(1);


        logo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoPressed =  true;
                if(!pref.isRememberPrefEmpty()) {
                    updateTotalCountToDB();
                }

                Intent i = new Intent(CountActivity.this,SavedCountListActivity.class);
                startActivity(i);
            }
        });

    }


    private void setVisiablity(boolean saved) {
        if(saved) {
            scEditText.setText("");
            scEditText.setVisibility(View.INVISIBLE);
            countTitleTextView.setVisibility(View.VISIBLE);
        } else {
            scEditText.setVisibility(View.VISIBLE);
            countTitleTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void saveTheCount() {
        count_title = scEditText.getText().toString().trim();


        if(isCountTitleValid()) {
            countTitleTextView.setText(count_title);
            saved = true;

            countObj = new Count(count_title, count, saveCountTimeStr());
            count_id = countObj.get_id();

            pd.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    countIntent = new Intent(CountActivity.this, CountDBOpsService.class);
                    countIntent.putExtra(ServiceOpConsts.THE_COUNT_OBJECT_TO_SAVE, countObj);
                    countIntent.putExtra(ServiceOpConsts.OPERATION, ServiceOpConsts.WANA_SAVE);
                    startService(countIntent);

                    while (isMyServiceRunning(CountDBOpsService.class)) {

                    }

                }
            }).start();
            pd.dismiss();

            pref.remember(saved, count_id, count_title, count);

            invalidateOptionsMenu();
        } else {
            namingRulesDialogs.show(getSupportFragmentManager(),"naming_rules");
            scEditText.setText("");
        }

    }

    private void newCountOrDeleteCount(boolean b) {

        if(b) {
            countIntent = new Intent(CountActivity.this, CountDBOpsService.class);
            countIntent.putExtra(ServiceOpConsts.COUNT_ID_FOR_OP,pref.getCountId());
            countIntent.putExtra(ServiceOpConsts.OPERATION,ServiceOpConsts.WANA_DELETE);

            startService(countIntent);
        } else if(!b && saved) {
            updateTotalCountToDB();
            pref.emptyRemember();
        }

            saved = false;
            setVisiablity(saved);
            resetOdoCount();

        invalidateOptionsMenu();
    }




    private void resetOdoCount() {
        count = 0;
        pref.setTotalCount(count);
        odoMeter.setAllCounts(count);
        odoMeter.resetOdoMeter();
        invalidateOptionsMenu();

    }

    private void renameCount() {
        showRenameDialog();
    }


    private void prefLoader() {
        saved = pref.getSaved();

        if(saved) {
            count_id = pref.getCountId();
            count_title = pref.getCountTitle();
            count = pref.getTotalCount();

            countTitleTextView.setText(count_title);
        } else {
            count = pref.getTotalCount();
        }
    }


    private void countObjToPref() {
        if(countObj != null) {
            pref.remember(true,countObj.get_id(),countObj.getTitle(),countObj.getTotel_count());
        }
    }


    private String saveCountTimeStr() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d/MMM/yy h:m a");
        return sdf.format(c.getTime());
    }


    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    private boolean isCountTitleValid() {
        count_title.trim();
        count_title.replaceAll("\\s{2,}"," ");

        if (count_title.length() < 3 || count_title.length() > 60 || count_title.isEmpty()) {
            return false;
        }
        return true;
    }

    private void updateTotalCountToDB() {
        countIntent = new Intent(CountActivity.this,CountDBOpsService.class);
        countIntent.putExtra(ServiceOpConsts.COUNT_ID_FOR_OP,count_id);
        countIntent.putExtra(ServiceOpConsts.THE_COUNT_VALUE_FOR_UPDAED,count);
        countIntent.putExtra(ServiceOpConsts.OPERATION,ServiceOpConsts.WANA_UPDATE_TOTAL_COUNT);
        startService(countIntent);
    }


    private void showRenameDialog() {
        renameCountDialog = RenameCountDialog.newInstance(count_title);
        renameCountDialog.show(getSupportFragmentManager(),"rename_dialog");
    }

    @Override
    public void changedName(String forChange) {

        count_title =  forChange;

        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                countIntent = new Intent(CountActivity.this, CountDBOpsService.class);
                countIntent.putExtra(ServiceOpConsts.COUNT_ID_FOR_OP, count_id);
                countIntent.putExtra(ServiceOpConsts.THE_COUNT_TITEL_RENAME,count_title);
                countIntent.putExtra(ServiceOpConsts.OPERATION, ServiceOpConsts.WANA_RENAME);
                startService(countIntent);

                while (isMyServiceRunning(CountDBOpsService.class)) {

                }

            }
        }).start();
        pd.dismiss();

        countTitleTextView.setText(count_title);
    }


    @Override
    public void odoCurrentValue(int count) {
        if(count == 0) {
            minusBtn.setTextColor(Color.GRAY);
            minusBtn.setEnabled(false);
            invalidateOptionsMenu();
        }

        if (count == 1) {
            minusBtn.setTextColor(Color.WHITE);
            minusBtn.setEnabled(true);
            invalidateOptionsMenu();
        }

        if(count < 100000 && !plusBtn.isEnabled()) {
            odoMeter.setVisibility(View.VISIBLE);
            fullyCountedTv.setVisibility(View.INVISIBLE);
            plusBtn.setTextColor(Color.WHITE);
            plusBtn.setEnabled(true);
        }

        if (count == 100000) {
            odoMeter.setVisibility(View.INVISIBLE);
            fullyCountedTv.setVisibility(View.VISIBLE);
            plusBtn.setTextColor(Color.GRAY);
            plusBtn.setEnabled(false);
            pref.setTotalCount(count);
        }

    }
}
