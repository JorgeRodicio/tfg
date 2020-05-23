package com.tfg.hrv.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.Chart;
import com.tfg.hrv.R;
import com.tfg.hrv.core.ChartService;
import com.tfg.hrv.core.Measurement;
import com.tfg.hrv.core.XmlService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private XmlService xmlService;
    private ChartService chartService;
    private List<Measurement> measurements;
    private Measurement lastMeasurement;

    private CardView cardNewMeasurement;
    private CardView cardHeartRate;
    private CardView cardVariability;
    private TextView tvHeartRate;
    private TextView tvVariability;
    private TextView tvLastMeasurementTitle;
    private Chart combinedChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        this.cardHeartRate = (CardView) view.findViewById(R.id.cv_heartRate);
        this.cardVariability = (CardView) view.findViewById(R.id.cv_variability);
        this.tvHeartRate = (TextView) view.findViewById(R.id.tv_heart_rate_home);
        this.tvVariability = (TextView) view.findViewById(R.id.tv_variability_home);
        this.tvLastMeasurementTitle = (TextView) view.findViewById(R.id.tv_lastMeasurement);
        this.combinedChart = (Chart) view.findViewById(R.id.combinedChart_home);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        this.xmlService = mViewModel.getXmlService();
        this.chartService = new ChartService();
        //xmlService.fillHistoricalDemo();
        //xmlService.saveXml();
        try {
            xmlService.loadXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.measurements = xmlService.getMeasurementList();


        this.lastMeasurement = xmlService.getLastMeasuerement();

        if(lastMeasurement != null){
            this.tvLastMeasurementTitle.setText(tvLastMeasurementTitle.getText() + ":  " + lastMeasurement.getDate());
            this.tvHeartRate.setText(this.lastMeasurement.getHeartRate().toString());
            this.tvVariability.setText(this.lastMeasurement.getVariability().toString());
            fillBarChart();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void fillBarChart(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        Map<String, String> dateMap = this.xmlService.getInfoFromDate(dateStr);

        List<String> heartRateList = this.xmlService.getChartData(dateMap.get("year"), dateMap.get("month"), true, false);
        List<String> variabilityList = this.xmlService.getChartData(dateMap.get("year"), dateMap.get("month"), false, true);
        this.chartService.setCombinedData(heartRateList, variabilityList, this.combinedChart);
    }
}