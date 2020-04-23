package com.wishhard.ohtc.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;



public class FullyCountedTv extends android.support.v7.widget.AppCompatTextView {

    public FullyCountedTv(Context context) {
        super(context);
        init();
    }

    public FullyCountedTv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FullyCountedTv(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/montague.ttf");
        setTypeface(tf);

         setTextColor(Color.BLACK);

        setPadding(0,0,0,0);
        setIncludeFontPadding(false);
    }
}
