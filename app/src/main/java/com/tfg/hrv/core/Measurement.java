package com.tfg.hrv.core;


import androidx.annotation.NonNull;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Measurement {

    private String date;

    private Integer heartRate;

    private Integer variability;

    private List<Integer> rrIntervals;

    private List<Integer> heartRateList;

    //Promedio R-R (ms): es la media de los intervalos R-R.
    // Este dato se obtiene dividiendo la sumatoria de todos los intervalos entre el total de intervalos.
    private Integer meanRR;

    //SDNN (ms): desviación estándar todos los intervalos R-R.
    // Esta variable muestra la variación en cortos y largos periodos en cuanto a la variación en los intervalos R-R (HRV).
    private BigDecimal sdnn;

    //NN50: es el número de intervalos adyacentes que varían por más de 50ms.
    private Integer nn50;

    //pNN50 (%): es el número de intervalos adyacentes que varían por más de 50ms expresado en porcentaje.
    private BigDecimal pnn50;

    //rMSSD (ms): es el cuadrado de la raíz media de la unión de los intervalos R-R adyacentes.
    // Provee un indicador del control cardiaco vagal (tono parasimpático).
    private BigDecimal rmssd;

    private BigDecimal lnRmssd;

    //Diferencia entre el
    private Integer hrMax;

    private Integer hrMin;

    //Diferencia entre el
    private Integer hrMaxMinDifference;

    private String comment;

    public Measurement(){

        calculateDate();
    }

    public Measurement(Integer heartRate, Integer variability, String comment){
        calculateDate();
        this.heartRate = heartRate;
        this.variability = variability;
        this.comment = comment;
    }

    public Measurement(String date, Integer heartRate, Integer variability, String comment){
        this.date = date;
        this.heartRate = heartRate;
        this.variability = variability;
        this.comment = comment;
    }

    public Measurement(List<Integer> heartRateList, List<Integer> rrIntervals){
        this.rrIntervals = rrIntervals;
        this.heartRateList = heartRateList;
        calculateDate();
        calculateHeartRate();
        calculateMeanRR();
        calculateSdnn();
        calculateNN50();
        calculatePnn50();
        calculateRmssd();
        calculateLnRssd();
        calculateVariability();
        calculateHrMaxMinDifference();
    }

    public Measurement(String date, Integer heartRate, Integer variability, List<Integer> rrIntervals, List<Integer> heartRateList, Integer meanRR, BigDecimal sdnn, Integer nn50, BigDecimal pnn50, BigDecimal rmssd, BigDecimal lnRmssd, Integer hrMax, Integer hrMin) {
        this.date = date;
        this.heartRate = heartRate;
        this.variability = variability;
        this.rrIntervals = rrIntervals;
        this.heartRateList = heartRateList;
        this.meanRR = meanRR;
        this.sdnn = sdnn;
        this.nn50 = nn50;
        this.pnn50 = pnn50;
        this.rmssd = rmssd;
        this.lnRmssd = lnRmssd;
        this.hrMax = hrMax;
        this.hrMin = hrMin;

        if(hrMax != null && hrMin != null){
            this.hrMaxMinDifference = hrMax - hrMin;
        }

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getVariability() {
        return variability;
    }

    public void setVariability(Integer variability) {
        this.variability = variability;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Integer> getRrIntervals() {
        return rrIntervals;
    }

    public void setRrIntervals(List<Integer> rrIntervals) {
        this.rrIntervals = rrIntervals;
    }

    public Integer getMeanRR() {
        return meanRR;
    }

    public void setMeanRR(Integer averageRR) {
        this.meanRR = averageRR;
    }

    public BigDecimal getSdnn() {
        return sdnn;
    }

    public void setSdnn(BigDecimal sdnn) {
        this.sdnn = sdnn;
    }

    public Integer getNn50() {
        return nn50;
    }

    public void setNn50(Integer nn50) {
        this.nn50 = nn50;
    }

    public BigDecimal getPnn50() {
        return pnn50;
    }

    public void setPnn50(BigDecimal pnn50) {
        this.pnn50 = pnn50;
    }

    public BigDecimal getRmssd() {
        return rmssd;
    }

    public void setRmssd(BigDecimal rmssd) {
        this.rmssd = rmssd;
    }

    public BigDecimal getLnRmssd() {
        return lnRmssd;
    }

    public void setLnRmssd(BigDecimal lnRmssd) {
        this.lnRmssd = lnRmssd;
    }

    public Integer getHrMax() {
        return hrMax;
    }

    public void setHrMax(Integer hrMax) {
        this.hrMax = hrMax;
    }

    public Integer getHrMin() {
        return hrMin;
    }

    public void setHrMin(Integer hrMin) {
        this.hrMin = hrMin;
    }

    public Integer getHrMaxMinDifference() {
        return hrMaxMinDifference;
    }

    public void setHrMaxMinDifference(Integer hrMaxMinDifference) {
        this.hrMaxMinDifference = hrMaxMinDifference;
    }

    public List<Integer> getHeartRateList() {
        return heartRateList;
    }

    public void setHeartRateList(List<Integer> heartRateList) {
        this.heartRateList = heartRateList;
    }

    private void calculateDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        this.date = dateFormat.format(date);
    }

    private void calculateMeanRR(){
        Integer toret = 0;

        if(this.rrIntervals != null && this.rrIntervals.size() > 0){
            for (Integer rr : this.rrIntervals) {
                toret += rr;
            }

            this.meanRR = Math.abs(toret / this.rrIntervals.size());
        }
    }

    private void calculateSdnn(){
        if(this.meanRR != null && this.rrIntervals != null && this.rrIntervals.size() > 0){
            Double sum = 0.0;
            Integer n = rrIntervals.size();

            for (int i = 0; i < n; i++){
                sum += Math.pow(rrIntervals.get(i) - meanRR, 2);
            }

            this.sdnn = new BigDecimal(Math.sqrt ( sum / new Double(n) )).setScale(2, RoundingMode.HALF_UP);
        }
    }

    private void calculateNN50(){
        this.nn50 = 0;

        if(this.rrIntervals != null && this.rrIntervals.size() > 0){
            for(int i = 0; i < this.rrIntervals.size() -1 ; i++){
                if( Math.abs(this.rrIntervals.get(i) - this.rrIntervals.get(i + 1)) > 50){
                    this.nn50 = this.nn50 + 1;
                }
            }
        }
    }

    private void calculatePnn50(){
        if(this.nn50 != null && this.nn50 > 0){
            Double total = new Double(this.rrIntervals.size());
            this.pnn50 = new BigDecimal(this.nn50 * 100 / total).setScale(2, RoundingMode.HALF_UP);
        }else{
            this.pnn50 = new BigDecimal(0);
        }
    }

    private void calculateRmssd(){
        Integer sum = 0;
        Integer count = 0;

        if(this.heartRateList != null && !this.heartRateList.isEmpty()){
            for(int i = 0; i < this.rrIntervals.size() -1 ; i++){
                Integer diff = this.rrIntervals.get(i) - this.rrIntervals.get(i + 1);
                Integer diff2 = diff * diff;
                sum = sum + diff2;
                count++;
            }
        }

        this.rmssd = new BigDecimal(Math.sqrt (sum / count)).setScale(2, RoundingMode.HALF_UP);
    }

    private void calculateLnRssd(){
        if(this.rmssd != null){
            Double value = Double.parseDouble(this.rmssd.toString());
            this.lnRmssd = new BigDecimal(Math.log(value)).setScale(1, RoundingMode.HALF_UP);
        }
    }

    private void calculateVariability(){
        if(this.lnRmssd != null){
            Double lnRmssdDouble = new Double(this.lnRmssd.toString());
            Double result = lnRmssdDouble * 100 / 6.5;
            this.variability = new Integer((int) Math.round(result));
        }
    }

    private void calculateHeartRate(){
        Integer heartRateAverage = 0;
        if(this.heartRateList != null && !this.heartRateList.isEmpty()){
            for (Integer heartRate : this.heartRateList) {
                heartRateAverage += heartRate;
            }
            this.heartRate = heartRateAverage / this.heartRateList.size();
        }
    }

    private void calculateHrMaxMinDifference(){
        Integer max = Integer.MIN_VALUE;
        Integer min = Integer.MAX_VALUE;

        for (Integer heartRate: this.heartRateList) {
            if(heartRate > max){
                max = heartRate;
            }

            if(heartRate < min){
                min = heartRate;
            }
        }

        this.hrMax = max;
        this.hrMin = min;
        this.hrMaxMinDifference = max - min;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder toret = new StringBuilder();

        toret.append("Date: " + this.date);
        toret.append("\nRR Intervals: ");
        for (Integer interval: this.rrIntervals) {
            toret.append(interval + " ");
        }
        toret.append("\nHeart Rates: ");
        for (Integer heartRate: this.heartRateList) {
            toret.append(heartRate + " ");
        }
        toret.append("\nVariability: " + this.variability);
        toret.append("\nHeart Rate Mean: " + this.heartRate);
        toret.append("\nAverage RR: " + this.meanRR + " ms");
        toret.append("\nSDNN: " + this.sdnn + " ms");
        toret.append("\nNN50: " + this.nn50);
        toret.append("\nPNN50: " + this.pnn50 + " %");
        toret.append("\nRMSSD: " + this.rmssd + " ms");
        toret.append("\nLN_RMSSD: " + this.lnRmssd + " ms");
        toret.append("\nHR Max: " + this.hrMax + " bpm");
        toret.append("\nHR Min: " + this.hrMin + " bpm");
        toret.append("\nHR Max - HR Min: " + this.hrMaxMinDifference + " bpm");

        return toret.toString();
    }


}