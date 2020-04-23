package com.wishhard.ohtc.helpers;


public class NumberUnityHelper {

    private static final int UNIT_MIN_LIMIT = 0;
    private static final int UNIT_MAX_LIMLT = 9;

    private static final int TENS_MIN_LIMIT = 10;
    private static final int TENS_MAX_LIMIT = 99;

    private static final int HUNDRED_MIN_LIMIT = 100;
    private static final int HUNDRED_MAX_LIMIT = 999;

    private static final int THOSAND_MIN_LIMIT = 1000;
    private static final int THOSAND_MAX_LIMIT = 9999;

    private static final int TEN_THOSAND_MIN_LIMIT = 10000;
    private static final int TEN_THOSAND_MAX_LIMIT = 99999;

    private int countered;

    public void setCountered(int countered) {
        this.countered = (countered < 0)? 0 : countered;
    }


    public boolean isCounteredNumberInUnit() {
        return (countered >= UNIT_MIN_LIMIT && countered <= UNIT_MAX_LIMLT)? true : false;
    }

    public boolean isCounteredNumberInTens() {
        return (countered >= TENS_MIN_LIMIT && countered <= TENS_MAX_LIMIT)? true : false;
    }

    public boolean isCounteredNumberInHundred() {
        return (countered >= HUNDRED_MIN_LIMIT && countered <= HUNDRED_MAX_LIMIT)? true : false ;
    }

    public boolean isCounteredNumberInThousand() {
        return (countered >= THOSAND_MIN_LIMIT && countered <= THOSAND_MAX_LIMIT)? true : false;
    }

    public boolean isCounteredNumberInTenThousand() {
        return (countered >= TEN_THOSAND_MIN_LIMIT && countered <= TEN_THOSAND_MAX_LIMIT)? true : false;
    }

    //can be called to compare to click whether countered has reach the max value of 99999
    public int getMaxCountableValue() {
        return TEN_THOSAND_MAX_LIMIT;
    }



    public int getUnit() {
        return (!isCounteredNumberInUnit())? countered%TENS_MIN_LIMIT : countered;
    }

    public int getTens() {
        return (!isCounteredNumberInTens())? (countered%HUNDRED_MIN_LIMIT)/TENS_MIN_LIMIT : countered/TENS_MIN_LIMIT;
    }

    public int getHuntred() {
        if(isCounteredNumberInThousand() || isCounteredNumberInTenThousand()) {
            return (countered%THOSAND_MIN_LIMIT)/HUNDRED_MIN_LIMIT;
        }

        return countered/HUNDRED_MIN_LIMIT;
    }

    public int getThousand() {
        return (!isCounteredNumberInThousand())? (countered%TEN_THOSAND_MIN_LIMIT)/THOSAND_MIN_LIMIT : countered/THOSAND_MIN_LIMIT;
    }

    public int getTenThousand() {
        return countered/TEN_THOSAND_MIN_LIMIT;
    }


}
