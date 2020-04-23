package com.wishhard.ohtc;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wishhard.ohtc.adapter.CountDividerDecoration;
import com.wishhard.ohtc.adapter.SavedCountAdopter;
import com.wishhard.ohtc.model.Count;
import com.wishhard.ohtc.service.CountDBOpsService;
import com.wishhard.ohtc.service.LoadDataService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.wishhard.ohtc.service.ServiceOpConsts.COUNT_ID_LIST_FOR_OP;
import static com.wishhard.ohtc.service.ServiceOpConsts.OPERATION;
import static com.wishhard.ohtc.service.ServiceOpConsts.WANA_LIST_DELETE;

public class SavedCountListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SavedCountAdopter adopter;
    private List<Integer>  selectedPos = new ArrayList<>();

    private boolean isItemSelectionModeOn = false;

    private Toolbar toolbar;
    private ProgressDialog pd;
    private TextView emplyTv;

    List<Count> counts = new ArrayList<>();

    private List<String> count_ids = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_count_list);

        Intent i = new Intent(this, LoadDataService.class);
        startService(i);


        EventBus.getDefault().register(this);

        toolbar = (Toolbar) findViewById(R.id.save_count_list_toolbar);
        setSupportActionBar(toolbar);


        emplyTv = (TextView) findViewById(R.id.emptyTv);


        recyclerView = (RecyclerView) findViewById(R.id.counts_recycle_view);


        noItemSetectedToolbar();

        pd = new ProgressDialog(this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();


        adopter = new SavedCountAdopter(this,counts);
        recyclerView.setAdapter(adopter);
        CountDividerDecoration dividerDecoration = new CountDividerDecoration(this);
        recyclerView.addItemDecoration(dividerDecoration);


        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {

                selectItems(position,v);
                invalidate();

                return true;
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if(!isItemSelectionModeOn) {
                    sendCountAsParcel(position);
                }

                if(selectedPos.size() != 0) {
                    selectItems(position,v);
                    invalidate();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.saved_count_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(counts.isEmpty()) {
            menu.findItem(R.id.delete_couunt_item).setVisible(false);
            menu.findItem(R.id.sort_by_count).setVisible(false);
            menu.findItem(R.id.sort_alphabetically).setVisible(false);
        } else {

            if (selectedPos.isEmpty()) {
                menu.findItem(R.id.delete_couunt_item).setVisible(false);
                menu.findItem(R.id.sort_by_count).setVisible(true);
                menu.findItem(R.id.sort_alphabetically).setVisible(true);
            } else if (!selectedPos.isEmpty()) {
                menu.findItem(R.id.delete_couunt_item).setVisible(true);
                menu.findItem(R.id.sort_by_count).setVisible(false);
                menu.findItem(R.id.sort_alphabetically).setVisible(false);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.delete_couunt_item) {
            unselectSlashDelete(false,true);
            invalidate();
            return true;
        } else if(id == R.id.sort_alphabetically) {
            sortAlpha();
            return true;
        } else if(id == R.id.sort_by_count) {
            sortByMaxCounting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbarNavBtnAction();

    }



    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(selectedPos.size() != 0) {
            unselectSlashDelete(true, false);
            invalidate();
        } else {
            finish();
        }
    }

    private void invalidate() {
               if(selectedPos.size() == 1) {
                   invalidateOptionsMenu();
                   itemSelectedToolbar();
               } else if(selectedPos.size() == 0) {
                   invalidateOptionsMenu();
                   noItemSetectedToolbar();
               }
           }


             private void noItemSetectedToolbar() {
                 isItemSelectionModeOn = false;
                 toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

                 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                 getSupportActionBar().setDisplayShowHomeEnabled(true);
                 getSupportActionBar().setDisplayShowTitleEnabled(true);
                 getSupportActionBar().setTitle("Saved Counts");
             }

         private void itemSelectedToolbar() {
             isItemSelectionModeOn = true;
             toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_grey));
             getSupportActionBar().setDisplayShowTitleEnabled(false);

             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             getSupportActionBar().setDisplayShowHomeEnabled(true);


         }


    private void selectItems(int position, View view) {
        boolean content = selectedPos.contains(position);
        if(!content) {
            view.setSelected(true);
            adopter.select(position,true);
            selectedPos.add(position);
            count_ids.add(counts.get(position).get_id());
        } else {
            view.setSelected(false);
            adopter.select(position,false);
            selectedPos.remove((Integer) position);
            count_ids.remove(counts.get(position).get_id());
        }

    }

      private void unselectSlashDelete(boolean unselect,boolean delete) {

          if(unselect && !delete) {
              pd.show();
              adopter.clearSelection();
              pd.dismiss();


              selectedPos.clear();
              count_ids.clear();
          } else if(delete && !unselect) {

              pd.show();

                      Collections.sort(count_ids,StringDescComparator);
                      Intent del = new Intent(SavedCountListActivity.this, CountDBOpsService.class);
                      del.putStringArrayListExtra(COUNT_ID_LIST_FOR_OP, (ArrayList<String>) count_ids);
                      del.putExtra(OPERATION,WANA_LIST_DELETE);
                      startService(del);


              selectedPos.clear();
              count_ids.clear();

          }

      }



      private void toolbarNavBtnAction() {
          toolbar.setNavigationOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 onBackPressed();
              }
          });
      }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void allCountsFromDB(List<Count> c) {
          counts = c;
              if(counts.isEmpty()) {
                  emplyTv.setVisibility(View.VISIBLE);
                  recyclerView.setVisibility(View.GONE);
              } else {
                  emplyTv.setVisibility(View.GONE);
                  recyclerView.setVisibility(View.VISIBLE);
                  adopter = new SavedCountAdopter(this, counts);
                  recyclerView.setAdapter(adopter);
              }
              pd.dismiss();

         invalidate();
    }


    private void sendCountAsParcel(int pos) {

       Intent i = new Intent(this,CountActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(CountActivity.PARCELABLE_COUNT,counts.get(pos));
        startActivity(i);

    }


    public static Comparator<String> StringDescComparator = new Comparator<String>() {
        public int compare(String app1, String app2) {

            String stringName1 = app1;
            String stringName2 = app2;

            return stringName2.compareToIgnoreCase(stringName1);
        }
    };

    public static Comparator<Integer> IntegerDescComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer integer1, Integer integer2) {
            return integer2.compareTo(integer1);
        }
    };


    private void sortAlpha() {
      //  Thread t = new Thread("waq");

        pd.show();

       Collections.sort(counts, new Comparator<Count>() {
           @Override
           public int compare(Count count, Count t1) {
               return count.getTitle().compareTo(t1.getTitle());
           }
       });

       recyclerView.getAdapter().notifyItemRangeChanged(0,counts.size());

       pd.dismiss();
    }

    private void sortByMaxCounting() {
        pd.show();

        Collections.sort(counts, new Comparator<Count>() {
            @Override
            public int compare(Count count, Count count2) {
                int val = count.getTotel_count();
                int val2 = count2.getTotel_count();

                return val2-val;
            }
        });

        recyclerView.getAdapter().notifyItemRangeChanged(0,counts.size());

        pd.dismiss();
    }

}
