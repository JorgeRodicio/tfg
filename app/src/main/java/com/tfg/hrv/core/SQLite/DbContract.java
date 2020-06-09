package com.tfg.hrv.core.SQLite;

import android.provider.BaseColumns;

public class DbContract {

    private DbContract() {}

    //Measurement class
    public static class Measurement implements BaseColumns {
        public static final String TABLE_NAME = "MEASUREMENT";
        public static final String DATE = "DATE";
        public static final String RR_INTERVALS = "RR_INTERVALS";
        public static final String HEART_RATES = "HEART_RATES";
        public static final String MEAN_HEART_RATE = "MEAN_HEART_RATE";
        public static final String VARIABILITY = "VARIABILITY";
        public static final String MEAN_RR = "MEAN_RR";
        public static final String SDNN = "SDNN";
        public static final String NN50 = "NN50";
        public static final String PNN50 = "PNN50";
        public static final String RMSSD = "RMSSD";
        public static final String LN_RMSSD = "LN_RMSSD";
        public static final String HR_MAX = "HR_MAX";
        public static final String HR_MIN = "HR_MIN";
    }


    public static class Time implements BaseColumns {
        public static final String TABLE_NAME = "TIME";
        public static final String TIME_VALUE = "TIME_VALUE";
    }

}
