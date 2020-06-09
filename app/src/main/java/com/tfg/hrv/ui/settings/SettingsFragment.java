package com.tfg.hrv.ui.settings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.tfg.hrv.BuildConfig;
import com.tfg.hrv.R;
import com.tfg.hrv.core.Measurement;
import com.tfg.hrv.core.MeasurementHelper;
import com.tfg.hrv.core.SQLite.DbHelper;


import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class SettingsFragment extends Fragment {

    private static final String[] TIMES = {"1 min", "2 min", "3 min", "4 min", "5 min"};

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private List<Measurement> measurements;
    boolean sdDisponible = false;
    boolean sdAccesoEscritura = false;

    private Toolbar toolbarTime;
    private Toolbar toolbarFileOptions;
    //private NumberPicker pickerTime;
    private Integer oldTime;
    private Integer timeSelected;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private Button btExportXml;
    private Button btExportJson;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_settings, container, false);

        this.toolbarTime = (Toolbar) view.findViewById(R.id.toolbar_time_settings);
        this.toolbarFileOptions = (Toolbar) view.findViewById(R.id.toolbar_xml_settings);
        //this.pickerTime = (NumberPicker) view.findViewById(R.id.picker_time_settings);
        this.radioGroup = (RadioGroup) view.findViewById(R.id.rg_settings);
        this.radioButton1 = (RadioButton) view.findViewById(R.id.rb_1_settings);
        this.radioButton2 = (RadioButton) view.findViewById(R.id.rb_2_settings);
        this.radioButton3 = (RadioButton) view.findViewById(R.id.rb_3_settings);
        this.radioButton4 = (RadioButton) view.findViewById(R.id.rb_4_settings);
        this.btExportXml = (Button) view.findViewById(R.id.bt_export_xml_settings);
        this.btExportJson = (Button) view.findViewById(R.id.bt_export_json_settings);

        this.dbHelper = new DbHelper(getContext());
        this.db = dbHelper.getWritableDatabase();
        //this.dbHelper.onUpgrade(db, 1, 2);
        //DbHelper.insertTime(db, "60");

        this.oldTime = Integer.valueOf(DbHelper.getTime(this.db));
        this.measurements = DbHelper.getAllMeasurement(db);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.timeSelected = 60;
        setText();
        setListeners();
        super.onViewCreated(view, savedInstanceState);

    }

    private void setText(){
        this.toolbarTime.setTitle("Tiempo de medición");
        this.toolbarFileOptions.setTitle("Opciones de exportación de datos");
    }


    private void setListeners(){
        setRadioGroupListener();
        setButtonListeners();
    }

    private void setRadioGroupListener(){

        switch (oldTime){
            case 60:
                this.radioButton1.performClick();
                break;
            case 120:
                this.radioButton2.performClick();
                break;
            case 180:
                this.radioButton3.performClick();
                break;
            case 240:
                this.radioButton4.performClick();
                break;
        }

        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_1_settings:
                        timeSelected = 60;
                        break;
                    case R.id.rb_2_settings:
                        timeSelected = 120;
                        break;
                    case R.id.rb_3_settings:
                        timeSelected = 180;
                        break;
                    case R.id.rb_4_settings:
                        timeSelected = 240;
                        break;
                }

                DbHelper.updateTime(db, oldTime.toString(), timeSelected.toString());

                System.out.println(timeSelected);
            }
        });

    }

    private void setButtonListeners(){
        this.btExportXml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String xml = MeasurementHelper.measurementListToXml(measurements);
                /*ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Xml", xml);
                clipboard.setPrimaryClip(clip);*/
                saveXmlFile(xml);
                shareXmlFile();
            }
        });

        this.btExportJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json =  MeasurementHelper.measuerementListToJson(measurements);
                System.out.println(json);
                saveJsonFile(json);
                shareJsonFile();
            }
        });
    }


    private void saveXmlFile(String xml){

        try {
            OutputStreamWriter fout = new OutputStreamWriter(getContext().openFileOutput("measurements.xml", Context.MODE_PRIVATE));
            fout.write(xml);
            fout.close();
            //Toast.makeText(getContext(),"Archivo guardado ", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveJsonFile(String json){

        try {
            OutputStreamWriter fout = new OutputStreamWriter(getContext().openFileOutput("measurements.json", Context.MODE_PRIVATE));
            fout.write(json);
            fout.close();
            //Toast.makeText(getContext(),"Archivo guardado ", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareXmlFile(){
        File outFile = getContext().getFileStreamPath("measurements.xml");

        Intent intent = new Intent(Intent.ACTION_VIEW);

        // set flag to give temporary permission to external app to use your FileProvider
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID, outFile);

        // I am opening a PDF file so I give it a valid MIME type
        intent.setDataAndType(uri, "text/plain");

        // validate that the device can open your File!
        PackageManager pm = getActivity().getPackageManager();
        if (intent.resolveActivity(pm) != null) {
            startActivity(intent);
        }
    }

    private void shareJsonFile(){
        File outFile = getContext().getFileStreamPath("measurements.json");

        Intent intent = new Intent(Intent.ACTION_VIEW);

        // set flag to give temporary permission to external app to use your FileProvider
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // generate URI, I defined authority as the application ID in the Manifest, the last param is file I want to open
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID, outFile);

        // I am opening a PDF file so I give it a valid MIME type
        intent.setDataAndType(uri, "text/plain");

        // validate that the device can open your File!
        PackageManager pm = getActivity().getPackageManager();
        if (intent.resolveActivity(pm) != null) {
            startActivity(intent);
        }
    }

}