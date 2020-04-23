package com.wishhard.ohtc.odo_view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wishhard.ohtc.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Odo extends LinearLayout {

    private static final float PERCENTAGE = 20.0f;
    private static final float ONE_100 = 100.0f;



    private Context context;
    private int c;

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    private List<TextView> tvList = new ArrayList<>(); //list of tet views
    private final List<String> NUMBER_VLUES = Collections.unmodifiableList(Arrays.asList("0","1","2","3","4","5","6","7","8","9")); //to store sting array of numbers


    public Odo(Context context) {
        super(context);
        init(context);

    }

    public Odo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public Odo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void init(Context context) {
        this.context = context;

        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        addTextViewToOdo();

    }


    private TextView createTextView() {
        TextView tv = new TextView(context);
        final Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/even_stevens.ttf");
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTypeface(font);
        tv.setSingleLine(true);
        tv.setScaleX(1.1f);
        tv.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_size));
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setIncludeFontPadding(false);
        return tv;
    }

    private ArrayList<TextView> tvList() {

        for (int i = 0; i < 3;i++) {
            tvList.add(createTextView());
        }
        return (ArrayList<TextView>) tvList;
    }


    public List<TextView> getTvList() {
        return tvList;
    }

    private void addTextViewToOdo() {
        tvList();

        for (TextView tv : tvList) {
            this.addView(tv);
        }
    }



    private void startOrResetValues() {
        tvList.get(0).setText(NUMBER_VLUES.get(9));
        tvList.get(1).setText(NUMBER_VLUES.get(0));
        tvList.get(2).setText(NUMBER_VLUES.get(1));
    }

    private void endingValues() {
        tvList.get(0).setText(NUMBER_VLUES.get(8));
        tvList.get(1).setText(NUMBER_VLUES.get(9));
        tvList.get(2).setText(NUMBER_VLUES.get(0));
    }

    public void odoForewardAsignent() {

        if(c >= 1 && c <= 8) {
            tvList.get(0).setText(NUMBER_VLUES.get(c-1));
            tvList.get(1).setText(NUMBER_VLUES.get(c));
            tvList.get(2).setText(NUMBER_VLUES.get(c+1));
        } else if(c == 9) {
            endingValues();
        } else {
            startOrResetValues();
        }

    }






}
