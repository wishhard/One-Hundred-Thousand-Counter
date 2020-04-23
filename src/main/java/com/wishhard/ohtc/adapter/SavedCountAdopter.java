package com.wishhard.ohtc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wishhard.ohtc.R;
import com.wishhard.ohtc.model.Count;
import java.util.ArrayList;
import java.util.List;



public class SavedCountAdopter extends RecyclerView.Adapter<SavedCountAdopter.ViewHolder>  {

    private List<Count> mCounts;
    private Context mContext;
    private ViewHolder viewHolder;

    private ArrayList<Boolean> selections = new ArrayList<>();


    public SavedCountAdopter(Context mContext,List<Count> mCounts) {
        this.mCounts = mCounts;
        this.mContext = mContext;

        for (int i = 0; i < mCounts.size();i++) {
            selections.add(false);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.saved_counts_list,parent,false);
        viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Count count = mCounts.get(position);

        String totel_count = String.valueOf(count.getTotel_count());

        holder.countTitle.setText(count.getTitle());
        holder.totelCount.setText(String.valueOf(totel_count));
        holder.countCreatedTime.setText(count.getCount_created_time());

           if(selections.get(position)) {
               if(!holder.mView.isSelected()) {
                   holder.mView.setSelected(true);
               }
           } else {
               holder.mView.setSelected(false);
           }
    }


    @Override
    public int getItemCount() {
        return mCounts.size();
    }

      public void select(int pos,boolean b) {
          selections.remove(pos);
          selections.add(pos,b);
      }

      public void clearSelection() {
          for(int i =0; i < selections.size();i++) {
              if(selections.get(i)) {
                  selections.remove(i);
                  selections.add(i,false);
              }
          }
          notifyDataSetChanged();
      }



    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView countTitle,totelCount,countCreatedTime;
        public View mView;



        public ViewHolder(View itemView) {
            super(itemView);

            countTitle =  itemView.findViewById(R.id.count_title);
            totelCount =  itemView.findViewById(R.id.totel_count);
            countCreatedTime =  itemView.findViewById(R.id.count_created_time);
            mView = itemView;

        }

    }
}
