package com.tfg.hrv.ui.historical;

import androidx.annotation.RequiresApi;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.tfg.hrv.R;
import com.tfg.hrv.core.Measurement;
import com.tfg.hrv.core.MeasurementHelper;
import com.tfg.hrv.core.SQLite.DbHelper;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class HistoricalFragment extends Fragment {

    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private List<Measurement> measurements;

    private String dateFrom;
    private String dateTo;
    private RecyclerView recyclerView;
    private ImageView imageFilter;
    private ImageView imageClearFilter;
    private TextView tvFilterDate;

    public static HistoricalFragment newInstance() {
        return new HistoricalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historical, container, false);

        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        this.recyclerView.setLayoutManager(layoutManager);
        this.imageFilter = (ImageView) view.findViewById(R.id.icon_filter);
        this.imageClearFilter = (ImageView) view.findViewById(R.id.clear_filter);
        this.tvFilterDate = (TextView) view.findViewById(R.id.tv_filter_date);
        this.tvFilterDate.setText("");
        this.imageClearFilter.setVisibility(View.GONE);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.dbHelper = new DbHelper(getContext());
        this.db = dbHelper.getWritableDatabase();

        if(db != null){
            this.measurements = DbHelper.getAllMeasurement(db);
        }

        this.dateFrom = null;
        this.dateTo = null;

        HistoricalAdapter rvAdapter = new HistoricalAdapter(getActivity(), this.measurements, getContext(), this.db);
        this.recyclerView.setAdapter(rvAdapter);
        setListeners();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDialog(View view, final Chip chip){
        DatePickerDialog dialog = new DatePickerDialog(view.getContext());
        dialog.show();

        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                chip.setVisibility(View.VISIBLE);
                String strDay = String.valueOf(day);
                String strMonth = String.valueOf(month + 1);

            }
        });
    }


    private void updateMeasurementList(){
        List newMeasurementList = MeasurementHelper.getMeasurementsRange(this.measurements, this.dateFrom, this.dateTo);

        if(newMeasurementList != null){
            HistoricalAdapter rvAdapter = new HistoricalAdapter(getActivity(), newMeasurementList, getContext(), this.db);
            this.recyclerView.setAdapter(rvAdapter);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setListeners(){
        this.imageFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFilterDate.setText("");
                showDialogDateFrom();

            }
        });

        this.imageClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFrom = null;
                dateTo = null;
                tvFilterDate.setText("");
                imageClearFilter.setVisibility(View.GONE);
                updateMeasurementList();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDialogDateFrom(){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog  startTime =  new DatePickerDialog(Objects.requireNonNull(getContext()));

        startTime.setTitle("Fecha de inicio");
        startTime.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Toast.makeText(getContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
                String strDay = "";
                String strMonth = "";
                if(dayOfMonth < 10){
                    strDay = "0";
                }

                if(month < 10){
                    strMonth = "0";
                }

                dateFrom = strDay + dayOfMonth + "/" + strMonth + month + "/" + year;
                showDialogDateTo();
            }
        });

        startTime.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dateFrom = null;
                showDialogDateTo();
            }
        });

        startTime.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDialogDateTo(){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog  startTime =  new DatePickerDialog(Objects.requireNonNull(getContext()));

        startTime.setTitle("Fecha de fin");
        startTime.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Toast.makeText(getContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
                String strDay = "";
                String strMonth = "";
                if(dayOfMonth < 10){
                    strDay = "0";
                }

                if(month < 10){
                    strMonth = "0";
                }

                dateTo = strDay + dayOfMonth + "/" + strMonth + month + "/" + year;
                setFilterText();
                updateMeasurementList();
            }
        });

        startTime.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dateTo = null;
                setFilterText();
                updateMeasurementList();
            }
        });

        startTime.show();
    }

    private void setFilterText(){
        if(dateFrom != null){
            this.tvFilterDate.setText("Desde " + tvFilterDate.getText() + dateFrom + "   ");
            this.imageClearFilter.setVisibility(View.VISIBLE);
        }
        if(dateTo != null){
            this.tvFilterDate.setText(tvFilterDate.getText() + "Hasta "  + dateTo);
            this.imageClearFilter.setVisibility(View.VISIBLE);
        }
    }
}
