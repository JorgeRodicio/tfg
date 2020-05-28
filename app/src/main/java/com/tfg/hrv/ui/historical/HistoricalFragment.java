package com.tfg.hrv.ui.historical;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.tfg.hrv.R;
import com.tfg.hrv.core.Measurement;
import com.tfg.hrv.core.MeasurementHelper;
import com.tfg.hrv.core.SQLite.MeasurementDbHelper;
import com.tfg.hrv.core.XmlService;

import java.util.List;

public class HistoricalFragment extends Fragment {

    private SQLiteDatabase db;
    private MeasurementDbHelper dbHelper;
    private List<Measurement> measurements;

    private String dateFrom;
    private String dateTo;

    private Button buttonFilterFrom;
    private Button buttonFilterTo;
    private RecyclerView recyclerView;
    private Chip chipFrom;
    private Chip chipTo;

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
        this.buttonFilterFrom = (Button) view.findViewById(R.id.bt_from);
        this.buttonFilterTo = (Button) view.findViewById(R.id.bt_to);
        this.chipFrom = (Chip) view.findViewById(R.id.chip_from);
        this.chipTo = (Chip) view.findViewById(R.id.chip_to);
        this.chipFrom.setVisibility(View.GONE);
        this.chipTo.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.dbHelper = new MeasurementDbHelper(getContext());
        this.db = dbHelper.getWritableDatabase();

        if(db != null){
            this.measurements = MeasurementDbHelper.getAllMeasurement(db);
        }

        updateMeasurementList();
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

                if(day < 10){
                    strDay = "0"+strDay;
                }

                if(month < 10){
                    strMonth = "0"+strMonth;
                }

                if(chip.equals(chipFrom)){
                    dateFrom = strDay + "/" + strMonth  + "/" + year;
                    chip.setText("Desde " + dateFrom);
                }

                if(chip.equals(chipTo)){
                    dateTo = strDay + "/" + strMonth  + "/" + year;

                    if(isToLowerThanFrom(dateFrom, dateTo) == false){
                        Toast.makeText(getContext(), "La fecha de fin es menor que la de inicio", Toast.LENGTH_SHORT).show();
                        dateTo = null;
                        chip.setVisibility(View.GONE);
                    }else{
                        chip.setText("Hasta " + dateTo);
                    }

                }

                System.out.println("Desde " + dateFrom + " hasta " + dateTo);

                chip.setCheckable(true);
                chip.setClickable(true);
                chip.setChecked(true);

                updateMeasurementList();

                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean selected) {
                        if(selected == false){
                            chip.setVisibility(View.GONE);

                            if(chip.equals(chipFrom)){
                                dateFrom = null;
                                System.out.println("Desde " + dateFrom + " hasta " + dateTo);
                            }

                            if(chip.equals(chipTo)){
                                dateTo = null;
                                System.out.println("Desde " + dateFrom + " hasta " + dateTo);
                            }

                            updateMeasurementList();
                        }

                    }
                });
            }
        });
    }


    private Boolean isToLowerThanFrom(String dateFrom, String dateTo){
        if(dateFrom != null && dateTo != null && dateFrom.compareTo(dateTo) == 1){
            System.out.println(dateFrom + " es mayor que " + dateTo);
            return false;
        }else{
            System.out.println(dateFrom + " es menor que " + dateTo);
            return true;
        }
    }


    private void updateMeasurementList(){
        List newMeasurementList = MeasurementHelper.getMeasurementsRange(this.measurements, this.dateFrom, this.dateTo);

        if(newMeasurementList != null){
            this.measurements = newMeasurementList;
        }
        HistoricalAdapter rvAdapter = new HistoricalAdapter(this.measurements, getContext(), this.db);
        this.recyclerView.setAdapter(rvAdapter);
    }


    private void setListeners(){
        this.buttonFilterFrom.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDialog(view, chipFrom);
                //System.out.println("Desde " + dateFrom + " hasta " + dateTo);

            }
        });

        this.buttonFilterTo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDialog(view, chipTo);
                //System.out.println("Desde " + dateFrom + " hasta " + dateTo);
            }
        });
    }
}
