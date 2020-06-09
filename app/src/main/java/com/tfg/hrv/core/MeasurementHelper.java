package com.tfg.hrv.core;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeasurementHelper {


    public static List<Measurement> getMeasurementsFromYearMonth(List<Measurement> measurements, String year, String month){
        List<Measurement> toret = new ArrayList<>();

        for (Measurement measurement: measurements) {
            if(getInfoFromDate(measurement.getDate()).get("year").equals(year) && getInfoFromDate(measurement.getDate()).get("month").equals(month)){
                toret.add(measurement);
            }
        }
        return toret;
    }

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


    public static String formatMonthNumber(String month) {
        String toret = "";
        switch (month){
            case "1":
                toret = "01";
                break;
            case "2":
                toret = "02";
                break;
            case "3":
                toret = "03";
                break;
            case "4":
                toret = "04";
                break;
            case "5":
                toret = "05";
                break;
            case "6":
                toret = "06";
                break;
            case "7":
                toret = "07";
                break;
            case "8":
                toret = "08";
                break;
            case "9":
                toret = "09";
                break;
            case "10":
                toret = "10";
                break;
            case "11":
                toret = "11";
                break;
            case "12":
                toret = "12";
                break;
        }


        return toret;
    }

    public static String measurementListToXml(List<Measurement> measurementList){
        StringBuilder toret = new StringBuilder();
        toret.append("<MEASUREMENTS>");
        for (Measurement measurement : measurementList) {
            toret.append("<MEASUREMENT>");

            toret.append("<HEART_RATES>");
            for (Integer heartRate : measurement.getHeartRateList()) {
                toret.append("<HEART_RATE>");
                toret.append(heartRate);
                toret.append("</HEART_RATE>");
            }
            toret.append("</HEART_RATES>");


            toret.append("<RR_INTERVALS>");
            for (Integer rrInterval : measurement.getRrIntervals()) {
                toret.append("<RR_INTERVAL>");
                toret.append(rrInterval);
                toret.append("</RR_INTERVAL>");
            }
            toret.append("</RR_INTERVALS>");

            toret.append("<VARIABILITY>");
            toret.append(measurement.getVariability());
            toret.append("</VARIABILITY>");

            toret.append("<HEART_RATE_MEAN>");
            toret.append(measurement.getHeartRate());
            toret.append("</HEART_RATE_MEAN>");

            toret.append("<RR_INTERVALS_MEAN>");
            toret.append(measurement.getMeanRR());
            toret.append("</RR_INTERVALS_MEAN>");

            toret.append("<SDNN>");
            toret.append(measurement.getSdnn());
            toret.append("</SDNN>");

            toret.append("<NN50>");
            toret.append(measurement.getNn50());
            toret.append("</NN50>");

            toret.append("<PNN50>");
            toret.append(measurement.getPnn50());
            toret.append("</PNN50>");

            toret.append("<RMSSD>");
            toret.append(measurement.getRmssd());
            toret.append("</RMSSD>");

            toret.append("<LN_RMSSD>");
            toret.append(measurement.getLnRmssd());
            toret.append("</LN_RMSSD>");

            toret.append("<HEART_RATE_MAX>");
            toret.append(measurement.getHrMax());
            toret.append("</HEART_RATE_MAX>");

            toret.append("<HEART_RATE_MIN>");
            toret.append(measurement.getHrMin());
            toret.append("</HEART_RATE_MIN>");

            toret.append("<HEART_RATE_MAX_MIN_DIFF>");
            toret.append(measurement.getHrMaxMinDifference());
            toret.append("</HEART_RATE_MAX_MIN_DIFF>");

            toret.append("</MEASUREMENT>");
        }

        toret.append("</MEASUREMENTS>");

        return toret.toString();
    }


    public static String measuerementListToJson(List<Measurement> listMeasurement){
        Gson gson =  new Gson();
        String json = gson.toJson(listMeasurement);

        return json;
    }

}
