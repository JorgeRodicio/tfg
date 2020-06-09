package com.tfg.hrv.ui.charts;

import androidx.annotation.RequiresApi;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.tfg.hrv.R;
import com.tfg.hrv.core.Measurement;
import com.tfg.hrv.core.MeasurementHelper;
import com.tfg.hrv.core.SQLite.DbHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChartsFragment extends Fragment {

    private static final String[] MONTHS = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private static final String[] YEARS = {"", "", "", "", ""};
    private static final String[] TYPE_DATA = {"Variabilidad", "Frecuencia cardíaca", "Media intervalos R-R", "SDNN", "NN50", "PNN50", "RMSSD", "LN (RMSSD)", "Frecuencia cardiaca máxima", "Frecuencia cardíaca mínima", "Diferencia frecuencia cardíaca máxima y mínima"};

    private NumberPicker pickerMonths;
    private NumberPicker pickerYears;
    private Spinner spinnerTypeData;
    private String yearSelected;
    private String monthSelected;
    private Integer typeData;

    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private List<Measurement> measurements;
    private BarChart chart;

    public static ChartsFragment newInstance() {
        return new ChartsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        this.pickerMonths = (NumberPicker) view.findViewById(R.id.picker_months);
        this.pickerYears = (NumberPicker) view.findViewById(R.id.picker_years);
        this.spinnerTypeData = (Spinner) view.findViewById(R.id.spinner_typeData_chart);
        this.chart = (BarChart) view.findViewById(R.id.chart_chart);

        this.monthSelected = "Enero";

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.dbHelper = new DbHelper(getContext());
        this.db = dbHelper.getWritableDatabase();

        if(db != null){
            this.measurements = DbHelper.getAllMeasurement(db);
        }

        monthSelected = "01";

        setPickerYear();
        setPickerMonth();
        setSpinner();
        setChartData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    private void setPickerMonth(){
        this.pickerMonths.setMinValue(1);
        this.pickerMonths.setMaxValue(12);
        this.pickerMonths.setWrapSelectorWheel(true);
        this.pickerMonths.setDisplayedValues(MONTHS);
        this.monthSelected = MONTHS[0];

        this.pickerMonths.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                System.out.println("MONTH: " + newVal);
                monthSelected = MeasurementHelper.formatMonthNumber(String.valueOf(newVal));
                setChartData();
            }
        });
    }

    private void setPickerYear(){
        Date date = new Date();
        CharSequence yearStr  = DateFormat.format("yyyy", date.getTime());
        final Integer year = Integer.parseInt(yearStr.toString());


        YEARS[0] = Integer.toString(year - 2);
        YEARS[1] = Integer.toString(year - 1);
        YEARS[2] = Integer.toString(year);
        YEARS[3] = Integer.toString(year + 1);
        YEARS[4] = Integer.toString(year + 2);

        this.pickerYears.setMinValue(Integer.parseInt(YEARS[0]));
        this.pickerYears.setMaxValue(Integer.parseInt(YEARS[YEARS.length - 1]));
        this.pickerYears.setWrapSelectorWheel(true);
        this.pickerYears.setDisplayedValues(YEARS);
        this.yearSelected = YEARS[0];

        this.pickerYears.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                System.out.println("YEAR : " + newVal);
                yearSelected = String.valueOf(newVal);
                setChartData();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setSpinner(){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, TYPE_DATA);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTypeData.setAdapter(spinnerAdapter);
        this.typeData = 1;

        this.spinnerTypeData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("SPINNER " + i + " " + TYPE_DATA[i]);
                typeData = i + 1;
                setChartData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                typeData = 1;
                setChartData();
            }
        });
    }

    private void setChartData() {
        List<Measurement> filteredMeasurements = MeasurementHelper.getMeasurementsFromYearMonth(this.measurements, this.yearSelected, this.monthSelected);
        List<Integer> chartDataList = new ArrayList<>();
        String label = "";

        if(!filteredMeasurements.isEmpty()){
            if(this.chart.getVisibility() == View.GONE){
                this.chart.setVisibility(View.VISIBLE);
            }
            switch (typeData) {
                //Variabilidad
                case 1:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add(measurement.getVariability());
                    }
                    label = "Variabilidad";
                    break;

                //Frecuencia cardiaca
                case 2:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add(measurement.getHeartRate());
                    }
                    label = "Frecuencia cardiaca";
                    break;

                //Media intervalos RR
                case 3:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add(measurement.getMeanRR());
                    }
                    label = "Media intervalos R-R";
                    break;

                //SDNN
                case 4:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add( Math.round(Float.parseFloat(measurement.getSdnn().toString())));
                    }
                    label = "SDNN";
                    break;

                //NN50
                case 5:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add(measurement.getNn50());
                    }
                    label = "NN50";
                    break;

                //PNN50
                case 6:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add(Math.round(Float.parseFloat(measurement.getPnn50().toString())));
                    }
                    label = "PNN50";
                    break;

                //RMSSD
                case 7:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add(Math.round(Float.parseFloat(measurement.getRmssd().toString())));
                    }
                    label = "RMSSD";
                    break;

                //LN_RMSSD
                case 8:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add(Math.round(Float.parseFloat(measurement.getLnRmssd().toString())));
                    }
                    label = "LN (RMSSD)";
                    break;
                //FC MAX
                case 9:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add(measurement.getHrMax());
                    }
                    label = "Frecuencia cardíaca máxima";
                    break;
                //FC MIN
                case 10:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add(measurement.getHrMin());
                    }
                    label = "Frecuencia cardíaca mínima";
                    break;
                //FC MAX MIN
                case 11:
                    for (Measurement measurement : filteredMeasurements) {
                        chartDataList.add(measurement.getHrMaxMinDifference());
                    }
                    label = "Diferencia frecuencia cardíaca máxima y mínima";
                    break;
            }

            ChartHelper.fillBarChart(chartDataList, this.chart, label, this.typeData);
        }else{
            //Toast.makeText(getContext(), "No hay datos para este mes", Toast.LENGTH_SHORT).show();
            this.chart.setVisibility(View.GONE);
        }
    }

}
