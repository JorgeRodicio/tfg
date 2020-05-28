package com.tfg.hrv;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tfg.hrv.core.Measurement;
import com.tfg.hrv.core.SQLite.MeasurementDbHelper;
import com.tfg.hrv.core.XmlService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private MeasurementDbHelper dbHelper;
    private XmlService xmlService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_measure,
                R.id.navigation_historical,
                R.id.navigation_charts)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        this.dbHelper = new MeasurementDbHelper(this);
        this.db = dbHelper.getWritableDatabase();

        if(db != null){
            List<Measurement> measurements = MeasurementDbHelper.getAllMeasurement(db);
            /*if(measurements != null && !measurements.isEmpty()){
                for (Measurement measure: measurements) {
                    System.out.println(measure.toString());
                }
            }*/
        }
    }

    @Override
    protected void onStop() {
        //xmlService.saveXml();
        super.onStop();
    }
}
