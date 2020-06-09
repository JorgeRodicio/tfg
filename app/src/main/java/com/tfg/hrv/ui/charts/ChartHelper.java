package com.tfg.hrv.ui.charts;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ChartHelper {

    private static final Integer COLOR_RR_INTERVAL = Color.argb(255, 240, 235, 55);
    private static final Integer COLOR_HEART_RATE = Color.argb(255, 70, 210, 185);
    private static final Integer COLOR_MEAN_RR = Color.argb(255, 225, 140, 80);
    private static final Integer COLOR_SDNN = Color.argb(255, 95, 101, 225);
    private static final Integer COLOR_NN50 = Color.argb(255, 230, 90, 85);
    private static final Integer COLOR_PNN50 = Color.argb(255, 145, 85, 230);
    private static final Integer COLOR_RMSSD = Color.argb(255, 235, 90, 235);
    private static final Integer COLOR_LN_RMSSD = Color.argb(255, 145, 0, 15);
    private static final Integer COLOR_FC_MAX = Color.argb(255, 0, 145, 0);
    private static final Integer COLOR_FC_MIN = Color.argb(255, 0, 70, 145);
    private static final Integer COLOR_FC_MAXMIN = Color.argb(255, 145, 90, 0);

    private static final Integer COLOR_GREEN = Color.argb(255, 115, 225, 115);
    private static final Integer COLOR_ORANGE = Color.argb(255, 240, 160, 35);
    private static final Integer COLOR_WHITE = Color.argb(255, 255, 255, 255);
    private static final Integer COLOR_RED = Color.argb(255, 225, 50, 50);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void fillLineChart2(List<Integer> values, LineChart chart, String label, Integer typeColor){
        List<Entry> listEntry = new ArrayList<>();
        List<String> listNumbers = new ArrayList<>();
        Integer color = 0;

        for (Integer i = 0; i < values.size(); i++) {
            listEntry.add(new Entry(i, values.get(i)));
            listNumbers.add(i.toString());
        }

        LineDataSet dataSet = new LineDataSet(listEntry, label);

        //dataSet.setDrawFilled(true);
        //dataSet.setFillAlpha(20);
        if(typeColor == 1){
            color = COLOR_HEART_RATE;
        }else{
            color = COLOR_RR_INTERVAL;
        }
        dataSet.setFillColor(color);
        LineData data =  new LineData(dataSet);
        //chart.animateY(2000);
        chart.setData(data);

        setLineChartAppearence(chart);
        setLineDataSetAppearence2(dataSet, typeColor);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void fillBarChart(List<Integer> values, BarChart chart, String label, Integer typeColor){
        List<BarEntry> listEntry = new ArrayList<>();
        List<String> listNumbers = new ArrayList<>();

        for (Integer i = 0; i < values.size(); i++) {
            listEntry.add(new BarEntry(i, values.get(i)));
            listNumbers.add(i.toString());
        }

        BarDataSet dataSet = new BarDataSet(listEntry, label);

        /*dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(150);*/

        BarData data =  new BarData(dataSet);
        chart.setData(data);

        setBarChartAppearence(chart);
        setBarDataSetAppearence(dataSet, typeColor);
        chart.animateY(2000);
        //chart.animateX(2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void fillLineChart(List<Integer> values, LineChart chart, String label, Integer typeColor){
        List<Entry> listEntry = new ArrayList<>();
        List<String> listNumbers = new ArrayList<>();

        for (Integer i = 0; i < values.size(); i++) {
            listEntry.add(new Entry(i, values.get(i)));
            listNumbers.add(i.toString());
        }

        LineDataSet dataSet = new LineDataSet(listEntry, label);

        /*dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(150);*/

        LineData data =  new LineData(dataSet);
        chart.setData(data);

        setLineChartAppearence(chart);
        setLineDataSetAppearence(dataSet, typeColor);
        chart.animateX(2000);
        //chart.animateX(2000);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void fillCombinedChart(List<Integer> hrList, List<Integer> variabilityList, LineChart chart){
        List<Entry> listEntryHR = new ArrayList<>();
        List<Entry> listEntryVariability = new ArrayList<>();

        for (Integer i = 0; i < hrList.size(); i++) {
            listEntryHR.add(new Entry(i  , hrList.get(i)));
            listEntryVariability.add(new Entry(i  , variabilityList.get(i)));
        }
        LineDataSet dataSetHeartRate = new LineDataSet(listEntryHR, "Frecuencia cardiaca");
        LineDataSet dataSetVariability = new LineDataSet(listEntryVariability, "Variabilidad");
        dataSetHeartRate.setColor(COLOR_HEART_RATE);
        dataSetVariability.setColor(COLOR_RR_INTERVAL);
        LineData lineData =  new LineData(dataSetHeartRate, dataSetVariability);

        chart.setData(lineData);
        chart.animateY(2000);
        setLineChartAppearence(chart);
        setDoubleLineDataSetAppearence(dataSetHeartRate, dataSetVariability);
    }


    private static void setLineDataSetAppearence(LineDataSet dataSet, Integer typeColor){
        Integer color = 0;
        dataSet.setLineWidth(4f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);

        dataSet.setValueTextSize(8f);

        switch (typeColor){
            case 1:
                color = COLOR_RR_INTERVAL;
                break;

            case 2:
                color = COLOR_HEART_RATE;
                break;

            case 3:
                color = COLOR_MEAN_RR;
                break;

            case 4:
                color = COLOR_SDNN;
                break;

            case 5:
                color = COLOR_NN50;
                break;

            case 6:
                color = COLOR_PNN50;
                break;

            case 7:
                color = COLOR_RMSSD;
                break;

            case 8:
                color = COLOR_LN_RMSSD;
                break;

            case 9:
                color = COLOR_FC_MAX;
                break;

            case 10:
                color = COLOR_FC_MIN;
                break;

            case 11:
                color = COLOR_FC_MAXMIN;
                break;
        }

        dataSet.setColor(color);
        dataSet.setCircleColor(color);
    }


    private static void setLineDataSetAppearence2(LineDataSet dataSet, Integer typeColor){
        Integer color = 0;
        dataSet.setLineWidth(4f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);

        dataSet.setValueTextSize(8f);

        switch (typeColor){
            case 1:
                color = COLOR_HEART_RATE;
                break;

            case 2:
                color = COLOR_RR_INTERVAL;
                break;
        }

        dataSet.setColor(color);
        dataSet.setCircleColor(color);
    }


    private static void setBarDataSetAppearence(BarDataSet dataSet, Integer typeColor){
        Integer color = 0;
        dataSet.setValueTextSize(8f);

        switch (typeColor){
            case 1:
                color = COLOR_RR_INTERVAL;
                break;

            case 2:
                color = COLOR_HEART_RATE;
                break;

            case 3:
                color = COLOR_MEAN_RR;
                break;

            case 4:
                color = COLOR_SDNN;
                break;

            case 5:
                color = COLOR_NN50;
                break;

            case 6:
                color = COLOR_PNN50;
                break;

            case 7:
                color = COLOR_RMSSD;
                break;

            case 8:
                color = COLOR_LN_RMSSD;
                break;

            case 9:
                color = COLOR_FC_MAX;
                break;

            case 10:
                color = COLOR_FC_MIN;
                break;

            case 11:
                color = COLOR_FC_MAXMIN;
                break;
        }

        dataSet.setColor(color);
    }



    private static void setDoubleLineDataSetAppearence(LineDataSet dataSet1, LineDataSet dataSet2){
        dataSet1.setLineWidth(2f);
        dataSet1.setCircleRadius(2f);
        dataSet1.setDrawCircleHole(false);
        dataSet1.setValueTextSize(8f);
        dataSet1.setColor(COLOR_HEART_RATE);
        dataSet1.setCircleColor(COLOR_HEART_RATE);

        dataSet2.setLineWidth(2f);
        dataSet2.setCircleRadius(2f);
        dataSet2.setDrawCircleHole(false);
        dataSet2.setValueTextSize(8f);
        dataSet2.setColor(COLOR_RR_INTERVAL);
        dataSet2.setCircleColor(COLOR_RR_INTERVAL);

    }



    private static void setLineChartAppearence(LineChart chart){
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getXAxis().setEnabled(false);
        chart.getXAxis().setDrawGridLinesBehindData(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getXAxis().setDrawLimitLinesBehindData(false);
        YAxis yAxisRight = chart.getAxisRight();
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisRight.setEnabled(false);
        yAxisLeft.setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getData().setHighlightEnabled(false);
    }

    private static void setBarChartAppearence(BarChart chart){
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getXAxis().setEnabled(false);
        chart.getXAxis().setDrawGridLinesBehindData(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getXAxis().setDrawLimitLinesBehindData(false);
        YAxis yAxisRight = chart.getAxisRight();
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisRight.setEnabled(false);
        yAxisLeft.setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getData().setHighlightEnabled(false);
        chart.getLegend().setEnabled(false);
    }


    private static void setCombinedChartAppearence(CombinedChart chart){
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getXAxis().setEnabled(false);
        chart.getXAxis().setDrawGridLinesBehindData(false);
        chart.getXAxis().setDrawLabels(false);
        chart.getXAxis().setDrawLimitLinesBehindData(false);
        YAxis yAxisRight = chart.getAxisRight();
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisRight.setEnabled(false);
        yAxisLeft.setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getData().setHighlightEnabled(false);
    }


    public static void setPieChartChrono(PieChart chart, Integer total, Integer maxChrono){
        chart.setUsePercentValues(false);
        //chart.setDescription("");

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        //chart.setHoleColorTransparent(false);

        chart.setTransparentCircleColor(Color.WHITE);
//      chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(90f);
        //chart.setTransparentCircleRadius(10f);

        chart.setDrawCenterText(true);
        chart.setCenterText(total.toString());
        chart.setCenterTextSize(50f);
        //chart.setCenterTextColor(R.color.colorAccent);
        chart.setEntryLabelTextSize(30f);
        chart.setDrawSliceText(false);
        chart.setDrawMarkers(false);
        chart.setDrawEntryLabels(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        //chart.setRotationAngle(20);
        chart.setRotationEnabled(false);
        chart.setDrawRoundedSlices(false);
        chart.setDrawMarkers(false);

        List<PieEntry> pieEntryList = new ArrayList<>();
        pieEntryList.add(new PieEntry(total, 0));
        pieEntryList.add(new PieEntry(maxChrono - total, 0));

        List<Integer> colors = new ArrayList<Integer>();

        colors.add(COLOR_RED);
        colors.add(COLOR_WHITE);

        PieDataSet dataSet = new PieDataSet(pieEntryList, "");
        dataSet.setValueTextSize(0f);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        chart.setData(data);
        //chart.animateY(10000);

        //chart.setDrawEntryLabels(false);
        //chart.setLogEnabled(false);

    }

    public static void setPieChart2(PieChart chart, Integer total){
        chart.setUsePercentValues(false);
        //chart.setDescription("");

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        //chart.setHoleColorTransparent(false);

        chart.setTransparentCircleColor(Color.WHITE);
//      chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(80f);
        chart.setTransparentCircleRadius(90f);

        chart.setDrawCenterText(true);
        chart.setCenterText(total.toString());
        chart.setCenterTextSize(80f);
        //chart.setCenterTextColor(R.color.colorAccent);
        chart.setEntryLabelTextSize(30f);
        chart.setDrawSliceText(false);
        chart.setDrawMarkers(false);
        chart.setDrawEntryLabels(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        //chart.setRotationAngle(20);
        chart.setRotationEnabled(true);
        chart.setDrawRoundedSlices(false);
        chart.setDrawMarkers(false);

        List<PieEntry> pieEntryList = new ArrayList<>();
        pieEntryList.add(new PieEntry(total, 0));
        pieEntryList.add(new PieEntry(100 - total, 0));

        List<Integer> colors = new ArrayList<Integer>();

        if(total <= 100 && total >= 50){
            colors.add(COLOR_GREEN);
        }else{
            colors.add(COLOR_ORANGE);
        }

        colors.add(COLOR_WHITE);

        PieDataSet dataSet = new PieDataSet(pieEntryList, "");
        dataSet.setValueTextSize(0f);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        chart.setData(data);
        //chart.animateY(2000);

        //chart.setDrawEntryLabels(false);
        //chart.setLogEnabled(false);

    }

    public static void setPieChart(PieChart chart, Integer total){
        chart.setUsePercentValues(false);
        //chart.setDescription("");

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        //chart.setHoleColorTransparent(false);

        chart.setTransparentCircleColor(Color.WHITE);
//      chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(80f);
        chart.setTransparentCircleRadius(90f);

        chart.setDrawCenterText(true);
        chart.setCenterText(total.toString());
        chart.setCenterTextSize(80f);
        //chart.setCenterTextColor(R.color.colorAccent);
        chart.setEntryLabelTextSize(30f);
        chart.setDrawSliceText(false);
        chart.setDrawMarkers(false);
        chart.setDrawEntryLabels(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        //chart.setRotationAngle(20);
        chart.setRotationEnabled(true);
        chart.setDrawRoundedSlices(false);
        chart.setDrawMarkers(false);

        List<PieEntry> pieEntryList = new ArrayList<>();
        pieEntryList.add(new PieEntry(total, 0));
        pieEntryList.add(new PieEntry(100 - total, 0));

        List<Integer> colors = new ArrayList<Integer>();

        if(total <= 100 && total >= 50){
            colors.add(COLOR_GREEN);
        }else{
            colors.add(COLOR_ORANGE);
        }

        colors.add(COLOR_WHITE);

        PieDataSet dataSet = new PieDataSet(pieEntryList, "");
        dataSet.setValueTextSize(0f);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.animateY(2000);

        //chart.setDrawEntryLabels(false);
        //chart.setLogEnabled(false);

    }


    public static void setPieChartHist(PieChart chart, Integer total){
        chart.setUsePercentValues(false);
        //chart.setDescription("");

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        //chart.setHoleColorTransparent(false);

        chart.setTransparentCircleColor(Color.WHITE);
//      chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(80f);
        //chart.setTransparentCircleRadius(90f);

        chart.setDrawCenterText(true);
        chart.setCenterText(total.toString());
        chart.setCenterTextSize(30f);
        //chart.setCenterTextColor(R.color.colorAccent);
        chart.setEntryLabelTextSize(30f);
        chart.setDrawSliceText(false);
        chart.setDrawMarkers(false);
        chart.setDrawEntryLabels(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        //chart.setRotationAngle(20);
        chart.setRotationEnabled(true);
        chart.setDrawRoundedSlices(false);
        chart.setDrawMarkers(false);

        List<PieEntry> pieEntryList = new ArrayList<>();
        pieEntryList.add(new PieEntry(total, 0));
        pieEntryList.add(new PieEntry(100 - total, 0));

        List<Integer> colors = new ArrayList<Integer>();

        if(total <= 100 && total >= 50){
            colors.add(COLOR_GREEN);
        }else{
            colors.add(COLOR_ORANGE);
        }

        colors.add(COLOR_WHITE);

        PieDataSet dataSet = new PieDataSet(pieEntryList, "");
        dataSet.setValueTextSize(0f);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.animateY(2000);

        //chart.setDrawEntryLabels(false);
        //chart.setLogEnabled(false);

    }
}
