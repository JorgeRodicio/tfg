package com.tfg.hrv.ui.charts;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.github.mikephil.charting.charts.Chart;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.tfg.hrv.R;
import com.tfg.hrv.core.ChartService;
import com.tfg.hrv.core.XmlService;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class ChartsFragment extends Fragment {

    private static final String[] MONTHS = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private static final String HEART_RATE = "Frecuencia cardíaca";
    private static final String VARIABILITY = "Variabilidad";
    private static final Integer COLOR_PRIMARY = Color.argb(255, 240, 235, 55);
    private static final Integer COLOR_SECONDARY = Color.argb(255, 100, 195, 255);

    private ChartsViewModel mViewModel;
    private XmlService xmlService;
    private List<String> yearsSelected;
    private List<String> monthsSelected;
    private Boolean isActiveHeartRate;
    private Boolean isActiveVariability;
    private Boolean anyChipMonthChecked;

    private ChipGroup chipGroupYears;
    private ChipGroup chipGroupMonths;
    private ChipGroup chipGroupTypes;
    private Button buttonFilter;
    private Chart barChart;
    private Chart combinedChart;

    private ChartService chartService;

    public static ChartsFragment newInstance() {
        return new ChartsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        this.chipGroupYears = (ChipGroup) view.findViewById(R.id.cg_years);
        this.chipGroupMonths = (ChipGroup) view.findViewById(R.id.cg_months);
        this.chipGroupTypes = (ChipGroup) view.findViewById(R.id.cg_types);
        this.barChart = (Chart) view.findViewById(R.id.barChart);
        this.combinedChart = (Chart) view.findViewById(R.id.combinedChart);

        fillChipGroupYears(view);
        fillChipGroupMonths(view);
        fillChipGroupTypes(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChartsViewModel.class);
        this.xmlService = mViewModel.getXmlService();
        this.chartService = new ChartService();
        try {
            this.xmlService.loadXml();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        this.yearsSelected = new ArrayList<>();
        this.monthsSelected = new ArrayList<>();
        this.isActiveHeartRate = false;
        this.isActiveVariability = false;
        this.anyChipMonthChecked = false;
    }

    private void fillChipGroupYears(final View view){
        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);

        for (Integer i = year - 1; i < year + 2 ; i++){
            Chip chip = new Chip(view.getContext());
            chip.setText(i.toString());
            chip.setCheckable(true);
            chip.setClickable(true);
            this.chipGroupYears.addView(chip);

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {
                    if(isSelected == true){
                        yearsSelected.add(compoundButton.getText().toString());
                        anyChipMonthChecked = true;
                        fillBarChart();
                        //Toast.makeText(view.getContext(), yearsSelected.toString(), Toast.LENGTH_SHORT).show();
                    }else{
                        yearsSelected.remove(compoundButton.getText().toString());
                        anyChipMonthChecked = false;
                        fillBarChart();
                        //Toast.makeText(view.getContext(), yearsSelected.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void fillChipGroupMonths(final View view){

        for (int i = 0; i < MONTHS.length; i++){
            Chip chip = new Chip(view.getContext());
            chip.setText(MONTHS[i]);
            chip.setCheckable(true);
            chip.setClickable(true);

            this.chipGroupMonths.addView(chip);

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {
                    if(isSelected == true){
                        monthsSelected.add(compoundButton.getText().toString());
                        fillBarChart();
                        //Toast.makeText(view.getContext(), monthsSelected.toString(), Toast.LENGTH_SHORT).show();
                    }else{
                        monthsSelected.remove(compoundButton.getText().toString());
                        fillBarChart();
                        //Toast.makeText(view.getContext(), monthsSelected.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void fillChipGroupTypes(final View view){

        final Chip chipHR = new Chip(view.getContext());
        chipHR.setText(HEART_RATE);
        chipHR.setCheckable(true);
        chipHR.setClickable(true);

        final Chip chipVar = new Chip(view.getContext());
        chipVar.setText(VARIABILITY);
        chipVar.setCheckable(true);
        chipVar.setClickable(true);
        //this.chipGroupTypes.setSingleSelection(true);
        this.chipGroupTypes.addView(chipVar);
        this.chipGroupTypes.addView(chipHR);

        chipHR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {
                if(isSelected == true){
                    isActiveHeartRate = true;
                    fillBarChart();
                    //Toast.makeText(view.getContext(), isActiveHeartRate.toString(), Toast.LENGTH_SHORT).show();
                }else{
                    isActiveHeartRate = false;
                    fillBarChart();
                    //Toast.makeText(view.getContext(), isActiveHeartRate.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        chipVar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {
                if(isSelected == true){
                    isActiveVariability = true;
                    fillBarChart();
                    //Toast.makeText(view.getContext(), isActiveVariability.toString(), Toast.LENGTH_SHORT).show();
                }else{
                    isActiveVariability = false;
                    fillBarChart();
                    //Toast.makeText(view.getContext(), isActiveVariability.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fillBarChart(){
        if(isActiveHeartRate && isActiveVariability){
            this.barChart.setVisibility(View.GONE);
            this.combinedChart.setVisibility(View.VISIBLE);
            List<String> heartRateList = this.xmlService.getChartData(this.yearsSelected, this.monthsSelected, true, false);
            List<String> variabilityList = this.xmlService.getChartData(this.yearsSelected, this.monthsSelected, false, true);
            setCombinedData(heartRateList, variabilityList);

        }else{
            this.combinedChart.setVisibility(View.GONE);
            this.barChart.setVisibility(View.VISIBLE);
            List<String> data = this.xmlService.getChartData(this.yearsSelected, this.monthsSelected, isActiveHeartRate, isActiveVariability);
            setBarChartData(data);
        }
    }

    private void setCombinedData(List<String> heartRateList, List<String> variabilityList) {
        chartService.setCombinedData(heartRateList, variabilityList, this.combinedChart);
    }

    private void setBarChartData(List<String> list){
        chartService.setBarChartData(list, this.isActiveHeartRate, this.isActiveVariability, this.barChart);
    }

}
