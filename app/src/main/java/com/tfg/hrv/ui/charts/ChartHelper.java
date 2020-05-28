package com.tfg.hrv.ui.charts;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChartHelper {

    private static final Integer COLOR_YELLOW = Color.argb(255, 240, 235, 55);
    private static final Integer COLOR_BLUE = Color.argb(255, 70, 210, 185);
    private static final Integer COLOR_BLUE_2 = Color.argb(255, 70, 210, 185);
    private static final Integer COLOR_WHITE = Color.argb(255, 255, 255, 255);

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
        dataSet.setFillAlpha(150);
        dataSet.setFillColor(COLOR_YELLOW);*/
        LineData data =  new LineData(dataSet);
        chart.animateY(2000);
        chart.setData(data);

        setLineChartAppearence(chart);
        setLineDataSetAppearence(dataSet, typeColor);

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
        dataSetHeartRate.setColor(COLOR_BLUE);
        dataSetVariability.setColor(COLOR_YELLOW);
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
                color = COLOR_YELLOW;
                break;

            case 2:
                color = COLOR_BLUE;
                break;
        }

        dataSet.setColor(color);
        dataSet.setCircleColor(color);
    }


    private static void setDoubleLineDataSetAppearence(LineDataSet dataSet1, LineDataSet dataSet2){
        dataSet1.setLineWidth(2f);
        dataSet1.setCircleRadius(2f);
        dataSet1.setDrawCircleHole(false);
        dataSet1.setValueTextSize(8f);
        dataSet1.setColor(COLOR_BLUE);
        dataSet1.setCircleColor(COLOR_BLUE);

        dataSet2.setLineWidth(2f);
        dataSet2.setCircleRadius(2f);
        dataSet2.setDrawCircleHole(false);
        dataSet2.setValueTextSize(8f);
        dataSet2.setColor(COLOR_YELLOW);
        dataSet2.setCircleColor(COLOR_YELLOW);

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


    public static void setPieChart(PieChart chart, Integer total){
        chart.setUsePercentValues(false);
        //chart.setDescription("");

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        //chart.setHoleColorTransparent(false);

        chart.setTransparentCircleColor(Color.WHITE);
//      chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(80f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);
        chart.setCenterText(total.toString());
        chart.setCenterTextSize(80f);
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
            colors.add(COLOR_BLUE);
        }else{
            colors.add(COLOR_YELLOW);
        }

        colors.add(COLOR_WHITE);

        PieDataSet dataSet = new PieDataSet(pieEntryList, "");
        dataSet.setValueTextSize(0f);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.animateY(3000);


        //chart.setDrawEntryLabels(false);
        //chart.setLogEnabled(false);

    }
}
