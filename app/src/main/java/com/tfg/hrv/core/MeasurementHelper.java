package com.tfg.hrv.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeasurementHelper {


    public static List<Measurement> getMeasurementsFromMonth(List<Measurement> measurements, String month){
        List<Measurement> toret = new ArrayList<>();

        for (Measurement measurement: measurements) {
            if(getInfoFromDate(measurement.getDate()).get("month").equals(month)){
                toret.add(measurement);
            }
        }

        return toret;
    }

    public static List<Integer> getHeartRatesFromLastMonth(List<Measurement> measurements){
        List<Integer> toret = new ArrayList<>();

        for (Measurement measurement: measurements) {
            toret.add(measurement.getHeartRate());
        }

        return toret;
    }

    public static List<Integer> getVariabilitiesFromLastMonth(List<Measurement> measurements){
        List<Integer> toret = new ArrayList<>();

        for (Measurement measurement: measurements) {
            toret.add(measurement.getVariability());
        }

        return toret;
    }

    private static String getInfoFromDateStr(String date){
        String toret = "";
        String[] dateSplitted = date.split(" ");
        String firstPart = dateSplitted[0];

        return firstPart;
    }

    public static Map<String, String> getInfoFromDate(String date){
        Map<String, String> toret = new HashMap<>();
        String[] dateSplitted = date.split(" ");
        String firstPart = dateSplitted[0];
        String[] firstPartSplitted = firstPart.split("/");
        toret.put("day", firstPartSplitted[0]);
        toret.put("month", firstPartSplitted[1]);
        toret.put("year", firstPartSplitted[2]);

        return toret;
    }



    private List<String> monthNameToNumber(List<String> months){
        List<String> toret = new ArrayList();

        for (Integer i = 0; i < months.size(); i++){
            switch (months.get(i)){
                case "Enero":
                    toret.add("01");
                    break;
                case "Febrero":
                    toret.add("02");
                    break;
                case "Marzo":
                    toret.add("03");
                    break;
                case "Abril":
                    toret.add("04");
                    break;
                case "Mayo":
                    toret.add("05");
                    break;
                case "Junio":
                    toret.add("06");
                    break;
                case "Julio":
                    toret.add("07");
                    break;
                case "Agosto":
                    toret.add("08");
                    break;
                case "Septiembre":
                    toret.add("09");
                    break;
                case "Octubre":
                    toret.add("10");
                    break;
                case "Noviembre":
                    toret.add("11");
                    break;
                case "Diciembre":
                    toret.add("12");
                    break;
            }
        }

        return toret;
    }

    public static List<Measurement> getMeasurementsRange(List<Measurement> measurementList, String dateFrom, String dateTo){
        List toret = new ArrayList();
        String date = "";

        if(dateFrom == null && dateTo == null){
            for (Measurement measurement: measurementList) {
                toret.add(measurement);
            }
        }else{
            if(dateFrom != null && dateTo == null){
                for (Measurement measurement: measurementList) {
                    if(compareDates(dateFrom, getInfoFromDateStr(measurement.getDate())) <= 0){
                        toret.add(measurement);
                    }
                }
            }else{
                if(dateFrom == null && dateTo != null){
                    for (Measurement measurement: measurementList) {
                        if(compareDates(dateTo, getInfoFromDateStr(measurement.getDate())) >= 0){
                            toret.add(measurement);
                        }
                    }
                }else{
                    if(dateFrom != null && dateTo != null){
                        for (Measurement measurement: measurementList) {
                            if( (compareDates(dateFrom, getInfoFromDateStr(measurement.getDate())) <= 0)

                                    &&

                                    (compareDates(dateTo, getInfoFromDateStr(measurement.getDate())) >= 0)){

                                toret.add(measurement);
                            }
                        }
                    }
                }
            }
        }
        return  toret;
    }

    private static Integer compareDates(String date1, String date2){
        Integer toret = null;
        Map<String, String > infoDate1 = getInfoFromDate(date1);
        Map<String, String > infoDate2 = getInfoFromDate(date2);

        String strDate1 = infoDate1.get("year") + infoDate1.get("month") + infoDate1.get("day");
        String strDate2 = infoDate2.get("year") + infoDate2.get("month") + infoDate2.get("day");

        return strDate1.compareTo(strDate2);
    }
}
