package com.tfg.hrv.core;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChartService {

    private static final String[] MONTHS = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private static final String HEART_RATE = "Frecuencia cardíaca";
    private static final String VARIABILITY = "Variabilidad";
    private static final Integer COLOR_PRIMARY = Color.argb(255, 240, 235, 55);
    private static final Integer COLOR_SECONDARY = Color.argb(255, 100, 195, 255);

    private List<String> makeDaysList(List list){
        List<String> toret = new ArrayList();

        for (Integer i = 0; i < list.size(); i++){
            Integer result = i + 1;
            toret.add(result.toString());
        }

        return toret;
    }

    public void setCombinedData(List<String> heartRateList, List<String> variabilityList, Chart combinedChart) {
        ArrayList barEntries = new ArrayList();
        ArrayList lineEntries = new ArrayList();
        List<String> xAxisData = makeDaysList(heartRateList);
        String labelHR = "Frecuencia cardiaca";
        String labelVar = "Variabilidad";
        Integer colorHR = COLOR_PRIMARY;
        Integer colorVR = COLOR_SECONDARY;


        for (Integer i = 0; i < heartRateList.size(); i++) {
            barEntries.add(new BarEntry(Float.parseFloat(xAxisData.get(i)), Float.parseFloat(heartRateList.get(i))));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, labelHR);
        barDataSet.setColors(Collections.singletonList(colorHR));
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        //LineChart
        for (Integer i = 0; i < heartRateList.size(); i++) {
            lineEntries.add(new Entry(Float.parseFloat(xAxisData.get(i)), Float.parseFloat(variabilityList.get(i))));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, labelVar);
        lineDataSet.setColors(Collections.singletonList(colorVR));
        //lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(3);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        combinedData.setData(lineData);

        combinedChart.setData(combinedData);
        Description description = new Description();
        description.setText("Dia");
        combinedChart.setDescription(description);
        combinedChart.animateY(2000);
        combinedChart.getXAxis().setDrawGridLines(false);
    }


    public void setBarChartData(List<String> list, Boolean isActiveHeartRate, Boolean isActiveVariability, Chart barChart){
        ArrayList barEntries = new ArrayList();
        List<String> xAxisData = makeDaysList(list);
        String label = "";
        Integer color = 0;

        if(isActiveHeartRate){
            label = "Frecuencia cardiaca";
            color = COLOR_PRIMARY;
        }

        if(isActiveVariability){
            label = "Variabilidad";
            color = COLOR_SECONDARY;
        }

        for (Integer i = 0; i < list.size(); i++) {
            barEntries.add(new BarEntry(Float.parseFloat(xAxisData.get(i)), Float.parseFloat(list.get(i))));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, label);
        barChart.animateY(2000);
        BarData data = new BarData(barDataSet);
        barDataSet.setColors(Collections.singletonList(color));
        barChart.setData(data);
        Description description = new Description();
        description.setText("Dia");
        barChart.setDescription(description);
        barChart.getXAxis().setDrawGridLines(false);
    }

    public void setLineChartData(Chart chart, List<Integer> data, String labelVar, Integer colorVR, Integer xAxisValue){
        ArrayList lineEntries = new ArrayList();

        for (Integer i = 0; i < data.size(); i++) {
            lineEntries.add(new Entry(xAxisValue, data.get(i)));
            xAxisValue += 1;
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, labelVar);
        lineDataSet.setColors(Collections.singletonList(colorVR));
        //lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(3);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);

        chart.setData(lineData);
        chart.getXAxis().setDrawGridLines(false);
    }

}
