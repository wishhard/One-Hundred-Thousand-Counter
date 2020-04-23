package com.wishhard.ohtc.odo_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import com.wishhard.ohtc.R;
import com.wishhard.ohtc.helpers.AnimatioHelper;
import com.wishhard.ohtc.helpers.NumberUnityHelper;
import java.util.ArrayList;
import java.util.List;



public class OdoMeter extends TableLayout {

    private static final int[] ODO_VIEWS_IDS = {R.id.unit_1,R.id.tens_10,R.id.hundred_100,R.id.thosand_1000,R.id.ten_thosand_10000};

    private AnimatioHelper animatioHelper;
    private Context context;

    private List<Odo> myOdoList;
    private int[] unitArray = new int[5];

    private OdoValueListener listener;

    private int count;


    private NumberUnityHelper numberUnityHelper;



    public OdoMeter(Context context) {
        super(context);
        init(context);

    }



    public OdoMeter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }



    private void init(Context context) {
        this.context = context;

        animatioHelper = new AnimatioHelper();



        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.odo_meter,this,true);

        numberUnityHelper = new NumberUnityHelper();

        setMyOdoList();
        initingDataForOdos();


    }


    //private methods
    private void setMyOdoList() {
        myOdoList = new ArrayList<>();

        myOdoList.add((Odo) findViewById(ODO_VIEWS_IDS[0]));
        myOdoList.add((Odo) findViewById(ODO_VIEWS_IDS[1]));
        myOdoList.add((Odo) findViewById(ODO_VIEWS_IDS[2]));
        myOdoList.add((Odo) findViewById(ODO_VIEWS_IDS[3]));
        myOdoList.add((Odo) findViewById(ODO_VIEWS_IDS[4]));

    }

    private void initingDataForOdos() {

        for (Odo odo : myOdoList) {
            odo.odoForewardAsignent();
        }

    }



    private void setUnitList() {
        numberUnityHelper.setCountered(getCount());

        unitArray[0] = numberUnityHelper.getUnit();
        unitArray[1] = numberUnityHelper.getTens();
        unitArray[2] = numberUnityHelper.getHuntred();
        unitArray[3] = numberUnityHelper.getThousand();
        unitArray[4] = numberUnityHelper.getTenThousand();

    }


    private void setCsForEachOdo() {
        int i = 0;

        for (Odo odo: myOdoList) {

            odo.setC(unitArray[i]);
            odo.odoForewardAsignent();

            if(animeOrNot(i)) {
                animatioHelper.goForward(odo.getTvList());
            } else {
                break;
            }
            i++;

        }

    }




    private int getCount() {
        return count;
    }


    public void setAllCounts(int count) {
        this.count = count;
        if(listener != null) {
            listener.odoCurrentValue(count);
        }
        setUnitList();
    }

    public void odoConpoundCb() {
        setCsForEachOdo();
    }

    public void odoConpoundCbReverse() {
        setCsForEachOdoReverse();

    }




    private boolean animeOrNot(int i) {

        if(i >= 1 && i <= 4) {
            return ((((unitArray[i]*10) + unitArray[i-1]) % 10) == 0);
        } else if (i == 0) {
            return true;
        }

        return false;
    }


    public void setCsForEachOdoReverse() {
        int i = 0;

        //max index
        int sh = (numberUnityHelper.isCounteredNumberInTens())? 1 : (numberUnityHelper
                .isCounteredNumberInHundred())? 2 : (numberUnityHelper.isCounteredNumberInThousand()
        )? 3 : (numberUnityHelper.isCounteredNumberInTenThousand())? 4 : 0;

        for (Odo odo: myOdoList) {

            odo.setC(unitArray[i]);
            odo.odoForewardAsignent();
            if(forReverse(i)) {
                animatioHelper.goReverse(odo.getTvList());
                if(count == 0) {
                    break;
                }
            } else if (i > sh) {

                break;
            }

            i++;

        }
    }


    private boolean forReverse(int i) {
        boolean b = false;
        if(i >= 1 && i <= 4) {
            if(i == 1) {
                b = (((unitArray[i] * 10) + unitArray[i - 1]) % 10 == 0);
            } else if(i == 2) {
                b = (count%100) == 0;
            } else if(i == 3) {
                b = (count%1000) == 0;
            } else if(i  == 4) {
                b = (count%10000) == 0;
            }

        } else if((i == 0) && (!b)) {

            b = true;
        }

        return b;
    }

    public void resetOdoMeter() {
        int i = 0;


        for (Odo odo : myOdoList) {

            odo.setC(unitArray[i]);
            odo.odoForewardAsignent();
            i++;

        }
    }


    public void setOdoValueListener(OdoValueListener listener) {
        this.listener = listener;
    }

    public interface OdoValueListener {
        void odoCurrentValue(int count);
    }
}
