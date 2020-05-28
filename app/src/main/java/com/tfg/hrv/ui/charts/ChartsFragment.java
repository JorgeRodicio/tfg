package com.tfg.hrv.ui.charts;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.tfg.hrv.MainActivity;
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
    private ChartService chartService;

    public static ChartsFragment newInstance() {
        return new ChartsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
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


    }










}
