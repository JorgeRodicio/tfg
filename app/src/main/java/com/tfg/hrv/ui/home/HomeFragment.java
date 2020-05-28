package com.tfg.hrv.ui.home;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.tfg.hrv.R;
import com.tfg.hrv.core.ChartService;
import com.tfg.hrv.core.Measurement;
import com.tfg.hrv.core.MeasurementHelper;
import com.tfg.hrv.core.SQLite.MeasurementDbHelper;
import com.tfg.hrv.core.XmlService;
import com.tfg.hrv.ui.charts.ChartHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private final static String[] MONTHS = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    private SQLiteDatabase db;
    private MeasurementDbHelper dbHelper;
    private List<Measurement> measurements;

    private TextView tvDate;
    private TextView tvVariability;
    private TextView tvHeartRate;
    private TextView tvSubtitleVariability;
    private TextView tvSubtitleHeartRate;
    private TextView tvMeanRR;
    private TextView tvSdnn;
    private TextView tvNn50;
    private TextView tvPnn50;
    private TextView tvRmssd;
    private TextView tvLnRmssd;
    private TextView tvHrMax;
    private TextView tvHrMin;
    private TextView tvHrMaxMin;
    private Spinner spinnerMonth;

    private PieChart pieChartVariability;
    private LineChart chartRRInterval;
    private LineChart chartHeartRates;
    private LineChart chartLastMonth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_home, container, false);

        this.tvDate = (TextView) view.findViewById(R.id.tv_date_home);
        //this.tvVariability = (TextView) view.findViewById(R.id.tv_variability_home);
        this.tvHeartRate = (TextView) view.findViewById(R.id.tv_hr_home);
        this.tvSubtitleVariability = (TextView) view.findViewById(R.id.tv_subtitle_variability);
        this.tvSubtitleHeartRate = (TextView) view.findViewById(R.id.tv_subtitle_heart_rate);
        this.tvMeanRR = (TextView) view.findViewById(R.id.tv_mean_rr_home);
        this.tvSdnn = (TextView) view.findViewById(R.id.tv_sdnn_home);
        this.tvNn50 = (TextView) view.findViewById(R.id.tv_nn50_home);
        this.tvPnn50 = (TextView) view.findViewById(R.id.tv_pnn50_home);
        this.tvRmssd = (TextView) view.findViewById(R.id.tv_rmssd_home);
        this.tvLnRmssd = (TextView) view.findViewById(R.id.tv_lnrmssd_home);
        this.tvHrMax = (TextView) view.findViewById(R.id.tv_hrMax_home);
        this.tvHrMin = (TextView) view.findViewById(R.id.tv_hrMin_home);
        this.tvHrMaxMin = (TextView) view.findViewById(R.id.tv_hrMaxMin_home);
        this.spinnerMonth = (Spinner) view.findViewById(R.id.spinner_month_home);

        this.pieChartVariability = (PieChart) view.findViewById(R.id.pieChart_variability_home);
        this.chartRRInterval = (LineChart) view.findViewById(R.id.chart_rrInterval_home);
        this.chartHeartRates = (LineChart) view.findViewById(R.id.chart_heartRates_home);
        this.chartLastMonth = (LineChart) view.findViewById(R.id.chart_lastMonth_home);
        //this.combinedChart = (Chart) view.findViewById(R.id.combinedChart_home);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.dbHelper = new MeasurementDbHelper(getContext());
        this.db = dbHelper.getWritableDatabase();

        if(db != null){
            this.measurements = MeasurementDbHelper.getAllMeasurement(db);
        }

        Measurement lastMeasurement = this.measurements.get(this.measurements.size() - 1);
        lastMeasurement.setVariability(82);

        setTextViewInfo(lastMeasurement);
        setPieChart(lastMeasurement);
        setChartsLastMeasurement(lastMeasurement);
        setChartLastMonth("01");
        setSpinner();
    }

    /*private void fillBarChart(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        Map<String, String> dateMap = this.xmlService.getInfoFromDate(dateStr);

        List<String> heartRateList = this.xmlService.getChartData(dateMap.get("year"), dateMap.get("month"), true, false);
        List<String> variabilityList = this.xmlService.getChartData(dateMap.get("year"), dateMap.get("month"), false, true);
        this.chartService.setCombinedData(heartRateList, variabilityList, this.combinedChart);
    }*/

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setTextViewInfo(Measurement measurement){
        this.tvDate.setText(measurement.getDate().toString());
        //this.tvVariability.setText(measurement.getVariability().toString());
        this.tvHeartRate.setText(measurement.getHeartRate().toString());
        this.tvMeanRR.setText(this.tvMeanRR.getText() + " " + measurement.getMeanRR().toString() + " ms");
        this.tvSdnn.setText(this.tvSdnn.getText() + " " + measurement.getSdnn().toString() + " ms");
        this.tvNn50.setText(this.tvNn50.getText() + " " + measurement.getNn50().toString());
        this.tvPnn50.setText(this.tvPnn50.getText() + " " + measurement.getPnn50().toString() + "%");
        this.tvRmssd.setText(this.tvRmssd.getText() + " " + measurement.getRmssd().toString() + " ms");
        this.tvLnRmssd.setText(this.tvLnRmssd.getText() + " " + measurement.getLnRmssd().toString());
        this.tvHrMax.setText(this.tvHrMax.getText() + " " + measurement.getHrMax().toString() + " bpm");
        this.tvHrMin.setText(this.tvHrMin.getText() + " " + measurement.getHrMin().toString() + " bpm");
        this.tvHrMaxMin.setText(this.tvHrMaxMin.getText() + " " + measurement.getHrMaxMinDifference().toString() + " bpm");

        //this.tvVariability.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.tvHeartRate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.tvSubtitleVariability.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.tvSubtitleHeartRate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setChartsLastMeasurement(Measurement lastMeasurement){
        ChartHelper.fillLineChart(lastMeasurement.getRrIntervals(), this.chartRRInterval, "Intervalos R-R", 1);
        ChartHelper.fillLineChart(lastMeasurement.getHeartRateList(), this.chartHeartRates, "Frecuencias cardiacas", 2);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setChartLastMonth(String lastMonth){
        List<Measurement> lastMonthMeasurements = MeasurementHelper.getMeasurementsFromMonth(this.measurements, lastMonth);
        List<Integer> lastMonthHeartRates = MeasurementHelper.getHeartRatesFromLastMonth(lastMonthMeasurements);
        List<Integer> lastMonthVariabilities = MeasurementHelper.getVariabilitiesFromLastMonth(lastMonthMeasurements);
        ChartHelper.fillCombinedChart(lastMonthHeartRates, lastMonthVariabilities, this.chartLastMonth);
    }

    private void setListeners(){
        this.spinnerMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void setPieChart(Measurement measurement){
        ChartHelper.setPieChart(this.pieChartVariability, measurement.getVariability());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setSpinner(){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, MONTHS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerMonth.setAdapter(spinnerAdapter);

        this.spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String monthSelected = "";
                if(i < 10){
                    monthSelected = "0";
                }

                monthSelected = monthSelected + String.valueOf(i + 1);
                setChartLastMonth(monthSelected);
                chartLastMonth.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}