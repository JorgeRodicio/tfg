package com.tfg.hrv.core;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlService {
    private String xml;
    private List<Measurement> measurementList;
    private Context context;

    private final static String FILE_NAME = "measurements.xml";
    private final static String TAG_MEASUREMENTS = "MEASUREMENTS";
    private final static String TAG_MEASUREMENT = "MEASUREMENT";
    private final static String TAG_DATE = "DATE";
    private final static String TAG_VARIABILITY = "VARIABILITY";
    private final static String TAG_MEAN_HEART_RATE = "MEAN_HEART_RATE";
    private final static String TAG_HEART_RATES = "HEART_RATES";
    private final static String TAG_HEART_RATE = "HEART_RATE";
    private final static String TAG_RR_INTERVALS = "RR_INTERVALS";
    private final static String TAG_RR_INTERVAL = "RR_INTERVAL";
    private final static String TAG_MEAN_RR = "MEAN_RR";
    private final static String TAG_SDNN = "SDNN";
    private final static String TAG_NN50 = "NN50";
    private final static String TAG_PNN50 = "PNN50";
    private final static String TAG_RMSSD = "RMSSD";
    private final static String TAG_LN_RMSSD = "LN_RMSSD";
    private final static String TAG_HR_MAX = "HR_MAX";
    private final static String TAG_HR_MIN = "HR_MIN";
    private final static String TAG_COMMENT = "COMMENT";

    public XmlService(Context context){
        this.measurementList = new ArrayList<>();
        this.context = context;
        this.xml = "";
    }

    public void saveXml(){
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        String result = "";

        try{
            serializer.setOutput(writer);
            serializer.startTag("",TAG_MEASUREMENTS);

            for(int i = 0; i < this.measurementList.size(); i++){
                serializer.startTag("", TAG_MEASUREMENT);

                //fecha
                serializer.startTag("", TAG_DATE);
                serializer.text(this.measurementList.get(i).getDate());
                serializer.endTag("", TAG_DATE);

                //VFC
                serializer.startTag("", TAG_VARIABILITY);
                serializer.text(this.measurementList.get(i).getVariability().toString());
                serializer.endTag("", TAG_VARIABILITY);

                //HR
                serializer.startTag("", TAG_MEAN_HEART_RATE);
                serializer.text(this.measurementList.get(i).getHeartRate().toString());
                serializer.endTag("", TAG_MEAN_HEART_RATE);

                //HR list
                serializer.startTag("", TAG_HEART_RATES);
                for (Integer heartRate: this.measurementList.get(i).getHeartRateList()) {
                    serializer.startTag("",TAG_HEART_RATE);
                    serializer.text(heartRate.toString());
                    serializer.endTag("", TAG_HEART_RATE);
                }
                serializer.endTag("", TAG_HEART_RATES);

                //RR intervals
                serializer.startTag("",TAG_RR_INTERVALS);
                for (Integer rrInterval: this.measurementList.get(i).getRrIntervals()) {
                    serializer.startTag("",TAG_RR_INTERVAL);
                    serializer.text(rrInterval.toString());
                    serializer.endTag("", TAG_RR_INTERVAL);
                }
                serializer.endTag("", TAG_RR_INTERVALS);

                //Mean RR
                serializer.startTag("", TAG_MEAN_RR);
                serializer.text(this.measurementList.get(i).getMeanRR().toString());
                serializer.endTag("", TAG_MEAN_RR);

                //SDNN
                serializer.startTag("", TAG_SDNN);
                serializer.text(this.measurementList.get(i).getSdnn().toString());
                serializer.endTag("", TAG_SDNN);

                //NN50
                serializer.startTag("", TAG_NN50);
                serializer.text(this.measurementList.get(i).getNn50().toString());
                serializer.endTag("", TAG_NN50);

                //PNN50
                serializer.startTag("", TAG_PNN50);
                serializer.text(this.measurementList.get(i).getPnn50().toString());
                serializer.endTag("", TAG_PNN50);

                //RMSSD
                serializer.startTag("", TAG_RMSSD);
                serializer.text(this.measurementList.get(i).getRmssd().toString());
                serializer.endTag("", TAG_RMSSD);

                //LN_RMSSD
                serializer.startTag("", TAG_LN_RMSSD);
                serializer.text(this.measurementList.get(i).getLnRmssd().toString());
                serializer.endTag("", TAG_LN_RMSSD);

                //HR MAX
                serializer.startTag("", TAG_HR_MAX);
                serializer.text(this.measurementList.get(i).getHrMax().toString());
                serializer.endTag("", TAG_HR_MAX);

                //HR MIN
                serializer.startTag("", TAG_HR_MIN);
                serializer.text(this.measurementList.get(i).getHrMin().toString());
                serializer.endTag("", TAG_HR_MIN);

                //Comentario
                if(this.measurementList.get(i).getComment() != null){
                    serializer.startTag("",TAG_COMMENT);
                    serializer.text(this.measurementList.get(i).getComment());
                    serializer.endTag("", TAG_COMMENT);
                }
                serializer.endTag("", TAG_MEASUREMENT);
            }

            serializer.endTag("", TAG_MEASUREMENTS);
            serializer.endDocument();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(serializer.toString());
            result = stringBuilder.toString();
            writer.close();

            OutputStreamWriter fout = new OutputStreamWriter(this.context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(result));
            bufferedWriter.write(result);

            fout.write(result);
            fout.close();

            System.out.println("XML guardado");

        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            System.err.println("Error al escribir fichero a memoria interna");
        }
    }

    public void addMeasurement(Measurement measurement){
        boolean isNotSaved = true;

        for (Measurement measure: this.measurementList) {
            if(measure.getDate().equals(measurement.getDate())){
                isNotSaved = false;
            }
        }

        if(isNotSaved){
            this.measurementList.add(measurement);
            saveXml();
        }
    }


    public void loadXml() throws ParserConfigurationException, IOException, SAXException {

        String date = "";
        Integer variability = new Integer(0);
        Integer meanHeartRate = null;
        List<Integer> heartRateList = new ArrayList<>();
        List<Integer> rrIntervalList = new ArrayList<>();
        Integer meanRR = null;
        BigDecimal sdnn = null;
        Integer nn50 = null;
        BigDecimal pnn50 = null;
        BigDecimal rmssd = null;
        BigDecimal lnRmssd = null;
        Integer hrMax = null;
        Integer hrMin = null;
        String comment = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //Obtenemos la referencia al fichero XML de entrada
        FileInputStream fil = this.context.openFileInput(FILE_NAME);

        //Creamos un nuevo parser DOM
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Realizamos lalectura completa del XML
        Document dom = builder.parse(fil);

        //Nos posicionamos en el nodo principal del árbol (<rss>)
        Element root = dom.getDocumentElement();

        //Localizamos todos los elementos <item>
        NodeList items = root.getElementsByTagName(TAG_MEASUREMENT);
        int  num = 1;

        this.measurementList = new ArrayList<>();

        for(int i = 0; i < items.getLength(); i++){
            Node item = items.item(i);
            NodeList datosMedicion = item.getChildNodes();

            for(int j = 0; j < datosMedicion.getLength(); j++){
                Node data = datosMedicion.item(j);
                String tag = data.getNodeName();

                //Date
                if(tag.equals(TAG_DATE)){
                    date = getText(data);
                }

                //HRV
                else if(tag.equals(TAG_VARIABILITY)){
                    variability = new Integer(getText(data));
                }

                //Mean HR
                else if(tag.equals(TAG_MEAN_HEART_RATE)){
                    meanHeartRate = new Integer(getText(data));
                }


                //HR list
                else if(tag.equals(TAG_HEART_RATES)){
                    NodeList heartRateNodeList = data.getChildNodes();
                    for(int k = 0; k < heartRateNodeList.getLength(); k++){
                       Node heartRateNode = heartRateNodeList.item(k);
                       String heartRateTag = heartRateNode.getNodeName();

                       if(heartRateTag.equals(TAG_HEART_RATE)){
                           Integer heartRate = new Integer(getText(heartRateNode));
                           heartRateList.add(heartRate);
                       }
                   }
                }

                //RR intervals
                else if(tag.equals(TAG_RR_INTERVALS)){
                    NodeList rrIntervalNodeList = data.getChildNodes();
                    for(int l = 0; l < rrIntervalNodeList.getLength(); l++){
                        Node rrIntervalNode = rrIntervalNodeList.item(l);
                        String rrIntervalTag = rrIntervalNode.getNodeName();

                        if(rrIntervalTag.equals(TAG_RR_INTERVAL)){
                            Integer rrInterval = new Integer(getText(rrIntervalNode));
                            rrIntervalList.add(rrInterval);
                        }
                    }
                }

                //Mean RR
                else if(tag.equals(TAG_MEAN_RR)){
                    meanRR = new Integer(getText(data));
                }

                //SDNN
                else if(tag.equals(TAG_SDNN)){
                    sdnn = new BigDecimal(getText(data));
                }

                //NN50
                else if(tag.equals(TAG_NN50)){
                    nn50 = new Integer(getText(data));
                }

                //PNN50
                else if(tag.equals(TAG_PNN50)){
                    pnn50 = new BigDecimal(getText(data));
                }

                //RMSSD
                else if(tag.equals(TAG_RMSSD)){
                    rmssd = new BigDecimal(getText(data));
                }

                //LN_RMSSD
                else if(tag.equals(TAG_LN_RMSSD)){
                    lnRmssd = new BigDecimal(getText(data));
                }

                //HR MAX
                else if(tag.equals(TAG_HR_MAX)){
                    hrMax = new Integer(getText(data));
                }

                //HR MIN
                else if(tag.equals(TAG_HR_MIN)){
                    hrMin = new Integer(getText(data));
                }

                //Comment
                else if(tag.equals(TAG_COMMENT)){
                    comment = getText(data);
                }

            }

            if(date != null && meanHeartRate != null && variability != null && rrIntervalList != null && heartRateList != null & meanRR != null && sdnn != null && nn50 != null && pnn50 != null && rmssd != null && lnRmssd != null && hrMax != null && hrMin != null){
                Measurement measurement = new Measurement(date, meanHeartRate, variability, rrIntervalList, heartRateList, meanRR, sdnn, nn50, pnn50, rmssd, lnRmssd, hrMax, hrMin);

                this.measurementList.add(measurement);
            }
        }

        System.out.println("XML leidoooooooooooo");

    }

    private String getText(Node dato)
    {
        StringBuilder texto = new StringBuilder();
        NodeList fragmentos = dato.getChildNodes();

        for (int k = 0; k < fragmentos.getLength(); k++)
        {
            texto.append(fragmentos.item(k).getNodeValue());
        }

        return texto.toString();
    }


    public void deleteMeasurement(Integer position){
        this.measurementList.remove(position);
        saveXml();
    }

    public void modifyMeasurement(Integer position, String comment){
        this.measurementList.get(position).setComment(comment);
        saveXml();
    }


    public List<Measurement> getMeasurementList() {
        return measurementList;
    }

    public void setMeasurementList(List<Measurement> measurementList) {
        this.measurementList = measurementList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void mockData(){
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        String result = "";

        for(Integer i = 1; i < 7; i++){
            for(Integer j = 1; j < 20; j++){

                Random r = new Random();
                int random = r.nextInt(100);

                Measurement measurement = new Measurement();
                String month = "";
                String day= "";

                if(i < 10){
                    month = "0"+i;
                }else{
                    month = i.toString();
                }

                if(j < 10){
                    day = "0"+j;
                }else{
                    day = j.toString();
                }


                measurement.setDate(day + "/" + month + "/2020 10:00:00");
                measurement.setHeartRate(random);
                random = r.nextInt(100);
                measurement.setVariability(new Integer(random));
                List<Integer> rrIntervals = new ArrayList<>();
                List<Integer> heartRateList = new ArrayList<>();

                for(int k = 0; k < 20; k++){
                    rrIntervals.add(r.nextInt(1000));
                    heartRateList.add(r.nextInt(100));
                }

                measurement.setRrIntervals(rrIntervals);
                measurement.setHeartRateList(heartRateList);

                Integer meanRR = r.nextInt(100);
                measurement.setMeanRR(meanRR);
                BigDecimal sdnn = new BigDecimal(r.nextInt(100));
                measurement.setSdnn(sdnn);
                Integer nn50 = r.nextInt(50);
                measurement.setNn50(nn50);
                BigDecimal pnn50 = new BigDecimal(r.nextInt(100));
                measurement.setPnn50(pnn50);
                BigDecimal rmssd = new BigDecimal(r.nextInt(1000));
                measurement.setRmssd(rmssd);
                BigDecimal lnRmssd = new BigDecimal(r.nextInt(10));
                measurement.setLnRmssd(lnRmssd);
                Integer hrMax = r.nextInt(100) + 10;;
                measurement.setHrMax(hrMax);
                Integer hrMin = 50;
                measurement.setHrMin(hrMin);
                measurement.setHrMaxMinDifference(hrMax - hrMin);
                measurement.setComment("Me siento bien");

                this.measurementList.add(measurement);

            }
        }
        saveXml();
    }


    public List getChartData(List yearsSelected, List monthsSelected, Boolean isHeartRateSelected, Boolean isVariabilitySelected){
        List<String> toret = new ArrayList<>();

        List numberMonthsSelected = monthNameToNumber(monthsSelected);

        for (Measurement measurement: this.measurementList) {
            Map<String, String> infoMap = getInfoFromDate(measurement.getDate());
            String day = infoMap.get("day");
            String month = infoMap.get("month");
            String year = infoMap.get("year");

            if(yearsSelected.contains(year) && numberMonthsSelected.contains(month)){
                if(isHeartRateSelected){
                    toret.add(measurement.getHeartRate().toString());
                }

                if(isVariabilitySelected){
                    toret.add(measurement.getVariability().toString());
                }
            }
        }

        return toret;
    }


    public List getChartData(String yearSelected, String monthSelected, Boolean isHeartRateSelected, Boolean isVariabilitySelected){
        List<String> toret = new ArrayList<>();


        for (Measurement measurement: this.measurementList) {
            Map<String, String> infoMap = getInfoFromDate(measurement.getDate());
            String day = infoMap.get("day");
            String month = infoMap.get("month");
            String year = infoMap.get("year");

            if(yearSelected.equals(year) && monthSelected.equals(month)){
                if(isHeartRateSelected){
                    toret.add(measurement.getHeartRate().toString());
                }

                if(isVariabilitySelected){
                    toret.add(measurement.getVariability().toString());
                }
            }
        }

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

    public Map<String, String> getInfoFromDate(String date){
        Map<String, String> toret = new HashMap<>();
        String[] dateSplitted = date.split(" ");
        String firstPart = dateSplitted[0];
        String[] firstPartSplitted = firstPart.split("/");
        toret.put("day", firstPartSplitted[0]);
        toret.put("month", firstPartSplitted[1]);
        toret.put("year", firstPartSplitted[2]);

        return toret;
    }

    private String getInfoFromDateStr(String date){
        String toret = "";
        String[] dateSplitted = date.split(" ");
        String firstPart = dateSplitted[0];

        return firstPart;
    }


    public List<Measurement> getMeasurementsRange(String dateFrom, String dateTo){
        List toret = new ArrayList();
        String date = "";

        if(dateFrom == null && dateTo == null){
            for (Measurement measurement: this.measurementList) {
                toret.add(measurement);
            }
        }else{
            if(dateFrom != null && dateTo == null){
                for (Measurement measurement: this.measurementList) {
                    if(compareDates(dateFrom, getInfoFromDateStr(measurement.getDate())) <= 0){
                        toret.add(measurement);
                    }
                }
            }else{
                if(dateFrom == null && dateTo != null){
                    for (Measurement measurement: this.measurementList) {
                        if(compareDates(dateTo, getInfoFromDateStr(measurement.getDate())) >= 0){
                            toret.add(measurement);
                        }
                    }
                }else{
                    if(dateFrom != null && dateTo != null){
                        for (Measurement measurement: this.measurementList) {
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

    private Integer compareDates(String date1, String date2){
        Integer toret = null;
        Map<String, String > infoDate1 = getInfoFromDate(date1);
        Map<String, String > infoDate2 = getInfoFromDate(date2);

        String strDate1 = infoDate1.get("year") + infoDate1.get("month") + infoDate1.get("day");
        String strDate2 = infoDate2.get("year") + infoDate2.get("month") + infoDate2.get("day");

        return strDate1.compareTo(strDate2);
    }

    public Measurement getLastMeasuerement(){
        if(measurementList != null && measurementList.size() == 0){
            return null;
        }else{
            return this.measurementList.get(measurementList.size() - 1);
        }
    }

    public static List<Measurement> randomMeasurement(){
        List<Measurement> toret = new ArrayList<>();

        for(int monthIndex = 1; monthIndex < 5; monthIndex ++){
            for(int  dayIndex = 1; dayIndex < 25; dayIndex ++){
                Random r = new Random();
                int random = r.nextInt(100);

                Measurement measurement = new Measurement();
                String month = "";
                String day = "";

                if(monthIndex < 10){
                    month = "0";
                }

                if(dayIndex < 10){
                    day = "0";
                }

                month = month + monthIndex;
                day = day + dayIndex;

                measurement.setDate(day+ "/" + month +"/2020 10:00:00");
                measurement.setHeartRate(random);
                random = r.nextInt(100);
                measurement.setVariability(new Integer(random));
                List<Integer> rrIntervals = new ArrayList<>();
                List<Integer> heartRateList = new ArrayList<>();

                for(int k = 0; k < 20; k++){
                    rrIntervals.add(r.nextInt(1000));
                    heartRateList.add(r.nextInt(100));
                }

                measurement.setRrIntervals(rrIntervals);
                measurement.setHeartRateList(heartRateList);

                Integer meanRR = r.nextInt(100);
                measurement.setMeanRR(meanRR);
                BigDecimal sdnn = new BigDecimal(r.nextInt(100));
                measurement.setSdnn(sdnn);
                Integer nn50 = r.nextInt(50);
                measurement.setNn50(nn50);
                BigDecimal pnn50 = new BigDecimal(r.nextInt(100));
                measurement.setPnn50(pnn50);
                BigDecimal rmssd = new BigDecimal(r.nextInt(1000));
                measurement.setRmssd(rmssd);
                BigDecimal lnRmssd = new BigDecimal(r.nextInt(10));
                measurement.setLnRmssd(lnRmssd);
                Integer hrMax = r.nextInt(100) + 10;;
                measurement.setHrMax(hrMax);
                Integer hrMin = 50;
                measurement.setHrMin(hrMin);
                measurement.setHrMaxMinDifference(hrMax - hrMin);
                measurement.setComment("Me siento bien");

                toret.add(measurement);
            }
        }
        return toret;
    }
}
