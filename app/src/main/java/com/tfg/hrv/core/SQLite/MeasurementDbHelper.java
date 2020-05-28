package com.tfg.hrv.core.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tfg.hrv.core.Measurement;
import com.thoughtworks.xstream.XStream;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MeasurementDbHelper extends SQLiteOpenHelper {

    private List<Measurement> measurementList;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public MeasurementDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MEASUREMENT);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String SQL_CREATE_TABLE_MEASUREMENT =
            "CREATE TABLE " + MeasurementDbContract.Measurement.TABLE_NAME + " (" +
                    MeasurementDbContract.Measurement._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MeasurementDbContract.Measurement.DATE + " TEXT," +
                    MeasurementDbContract.Measurement.RR_INTERVALS + " TEXT," +
                    MeasurementDbContract.Measurement.HEART_RATES + " TEXT," +
                    MeasurementDbContract.Measurement.MEAN_HEART_RATE + " TEXT," +
                    MeasurementDbContract.Measurement.VARIABILITY + " TEXT," +
                    MeasurementDbContract.Measurement.MEAN_RR + " TEXT," +
                    MeasurementDbContract.Measurement.SDNN + " TEXT," +
                    MeasurementDbContract.Measurement.NN50 + " TEXT," +
                    MeasurementDbContract.Measurement.PNN50 + " TEXT," +
                    MeasurementDbContract.Measurement.RMSSD + " TEXT," +
                    MeasurementDbContract.Measurement.LN_RMSSD + " TEXT," +
                    MeasurementDbContract.Measurement.HR_MAX + " TEXT," +
                    MeasurementDbContract.Measurement.HR_MIN + " TEXT" +
                    ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MeasurementDbContract.Measurement.TABLE_NAME;

    private static final String SQL_DELETE_FROM =
            "DELETE FROM " + MeasurementDbContract.Measurement.TABLE_NAME;


    public static void insertMeasurement(SQLiteDatabase db, Measurement measurement){
        ContentValues cv = new ContentValues();
        cv.put(MeasurementDbContract.Measurement.DATE, measurement.getDate());
        cv.put(MeasurementDbContract.Measurement.RR_INTERVALS, rrIntervalListToXml(measurement.getRrIntervals()));
        cv.put(MeasurementDbContract.Measurement.HEART_RATES, heartRateListToXml(measurement.getHeartRateList()));
        cv.put(MeasurementDbContract.Measurement.MEAN_HEART_RATE, measurement.getHeartRate());
        cv.put(MeasurementDbContract.Measurement.VARIABILITY, measurement.getVariability());
        cv.put(MeasurementDbContract.Measurement.MEAN_RR, measurement.getMeanRR());
        cv.put(MeasurementDbContract.Measurement.SDNN, measurement.getSdnn().toString());
        cv.put(MeasurementDbContract.Measurement.NN50, measurement.getNn50());
        cv.put(MeasurementDbContract.Measurement.PNN50, measurement.getPnn50().toString());
        cv.put(MeasurementDbContract.Measurement.RMSSD, measurement.getRmssd().toString());
        cv.put(MeasurementDbContract.Measurement.LN_RMSSD, measurement.getLnRmssd().toString());
        cv.put(MeasurementDbContract.Measurement.HR_MAX, measurement.getHrMax());
        cv.put(MeasurementDbContract.Measurement.HR_MIN, measurement.getHrMin());

        db.insert(MeasurementDbContract.Measurement.TABLE_NAME, null, cv);
    }


    public static void deleteMeasurement(SQLiteDatabase db, String date){
        String args [] = new String[]{date};
        //db.delete(MeasurementDbContract.Measurement.TABLE_NAME, "DATE=?", args);

        db.execSQL("DELETE FROM " + MeasurementDbContract.Measurement.TABLE_NAME + " WHERE " + MeasurementDbContract.Measurement.DATE + "=?", args);
    }

    public static void deleteTable(SQLiteDatabase db){
        db.execSQL("DELETE FROM " + MeasurementDbContract.Measurement.TABLE_NAME);
    }

    public static Measurement getMeasurement(SQLiteDatabase db, String date){
        String args [] = new String[]{date};
        Cursor cursor = db.rawQuery("SELECT * FROM " + MeasurementDbContract.Measurement.TABLE_NAME + " WHERE " + MeasurementDbContract.Measurement.DATE + "=?", args);

        if(cursor.moveToFirst()){
            List<Integer> rrIntervals = stringToList(cursor.getString(2)) ;
            List<Integer> heartRates = stringToList(cursor.getString(3)) ;
            Integer meanHeartRate = new Integer(cursor.getString(4));
            Integer variability = new Integer(cursor.getString(5));
            Integer meanRR = new Integer(cursor.getString(6));
            BigDecimal sdnn = new BigDecimal(cursor.getString(7));
            Integer nn50 = new Integer(cursor.getString(8));
            BigDecimal pnn50 = new BigDecimal(cursor.getString(9));
            BigDecimal rmssd = new BigDecimal(cursor.getString(10));
            BigDecimal lnRmssd = new BigDecimal(cursor.getString(11));
            Integer hrMax = new Integer(cursor.getString(12));
            Integer hrMin = new Integer(cursor.getString(13));

            Measurement measurement = new Measurement(date, meanHeartRate, variability, rrIntervals, heartRates, meanRR, sdnn, nn50, pnn50, rmssd, lnRmssd, hrMax, hrMin);

            return measurement;

        }else{
            return null;
        }
    }

    public static Measurement getLastMeasurement(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM " + MeasurementDbContract.Measurement.TABLE_NAME + " WHERE " + MeasurementDbContract.Measurement._ID + "=(SELECT MAX(_id) FROM "  + MeasurementDbContract.Measurement.TABLE_NAME + ")", null);

        if(cursor.moveToFirst()){
            String date = cursor.getString(1);
            List<Integer> rrIntervals = stringToList(cursor.getString(2));
            List<Integer> heartRates = stringToList(cursor.getString(3));
            Integer meanHeartRate = new Integer(cursor.getString(4));
            Integer variability = new Integer(cursor.getString(5));
            Integer meanRR = new Integer(cursor.getString(6));
            BigDecimal sdnn = new BigDecimal(cursor.getString(7));
            Integer nn50 = new Integer(cursor.getString(8));
            BigDecimal pnn50 = new BigDecimal(cursor.getString(9));
            BigDecimal rmssd = new BigDecimal(cursor.getString(10));
            BigDecimal lnRmssd = new BigDecimal(cursor.getString(11));
            Integer hrMax = new Integer(cursor.getString(12));
            Integer hrMin = new Integer(cursor.getString(13));

            Measurement measurement = new Measurement(date, meanHeartRate, variability, rrIntervals, heartRates, meanRR, sdnn, nn50, pnn50, rmssd, lnRmssd, hrMax, hrMin);

            return measurement;

        }else{
            return null;
        }
    }

    public static List<Measurement> getAllMeasurement(SQLiteDatabase db){
        List<Measurement> toret = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + MeasurementDbContract.Measurement.TABLE_NAME , null);

        if(cursor.moveToFirst()){
           toret = new ArrayList<>();
            do{
                String date = cursor.getString(1);
                List<Integer> rrIntervals = stringToList(cursor.getString(2)) ;
                List<Integer> heartRates = stringToList(cursor.getString(3)) ;
                Integer meanHeartRate = new Integer(cursor.getString(4));
                Integer variability = new Integer(cursor.getString(5));
                Integer meanRR = new Integer(cursor.getString(6));
                BigDecimal sdnn = new BigDecimal(cursor.getString(7));
                Integer nn50 = new Integer(cursor.getString(8));
                BigDecimal pnn50 = new BigDecimal(cursor.getString(9));
                BigDecimal rmssd = new BigDecimal(cursor.getString(10));
                BigDecimal lnRmssd = new BigDecimal(cursor.getString(11));
                Integer hrMax = new Integer(cursor.getString(12));
                Integer hrMin = new Integer(cursor.getString(13));

                Measurement measurement = new Measurement(date, meanHeartRate, variability, rrIntervals, heartRates, meanRR, sdnn, nn50, pnn50, rmssd, lnRmssd, hrMax, hrMin);
                toret.add(measurement);

            }while (cursor.moveToNext());

        }
        return toret;
    }


    private static String rrIntervalListToXml(List<Integer> rrIntervalList){
        StringBuilder toret = new StringBuilder();

        for(int i = 0; i < rrIntervalList.size(); i++){
            toret.append(rrIntervalList.get(i));
            toret.append(",");
        }

        return toret.toString();
    }

    private static String heartRateListToXml(List<Integer> heartRateList){
        StringBuilder toret = new StringBuilder();

        for(int i = 0; i < heartRateList.size(); i++){
            toret.append(heartRateList.get(i));
            toret.append(",");
        }

        return toret.toString();
    }

    private static List<Integer> stringToList(String string){
        String string2 = string.replace(" ", "");
        String [] arrayStr = string2.split(",");

        List<Integer> toret = new ArrayList<>();

        for(int i = 0; i < arrayStr.length; i++){
            toret.add(new Integer(arrayStr[i]));
        }

        return toret;
    }

    public static void insertMeasurementList(SQLiteDatabase db, List<Measurement> measurementList){
        for (Measurement measurement: measurementList) {
            insertMeasurement(db, measurement);
        }
    }

}
