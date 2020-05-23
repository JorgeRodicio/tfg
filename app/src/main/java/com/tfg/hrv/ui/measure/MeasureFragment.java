package com.tfg.hrv.ui.measure;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenu;
import com.tfg.hrv.R;
import com.tfg.hrv.core.ChartService;
import com.tfg.hrv.core.Measurement;
import com.tfg.hrv.core.XmlService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;
import static com.tfg.hrv.R.id.mobile_navigation;
import static com.tfg.hrv.R.id.nav_view;

public class MeasureFragment extends Fragment {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static String HR_SENSOR_ADDRESS = "00:22:D0:2B:0C:E2";

    private UUID HEART_RATE_SERVICE_UUID = convertFromInteger(0x180D);
    private UUID HEART_RATE_MEASUREMENT_CHAR_UUID = convertFromInteger(0x2A37);
    private UUID HEART_RATE_CONTROL_POINT_CHAR_UUID = convertFromInteger(0x2A39);
    private UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = convertFromInteger(0x2902);
    private Integer COUNTDOWN_TIME = 10000;

    private XmlService xmlService;
    private Measurement measurement;
    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> deviceList;
    private BluetoothDevice device;
    private BluetoothGatt gatt;

    private ListView lvDevices;
    private Button btStartMeasure;
    private Button btSaveMeasurement;
    private TextView tvTitleMeasure;
    private TextView tvTitleChrono;
    private TextView tvChrono;
    private TextView tvTitleHeartRateChart;
    private TextView tvTitleRRChart;
    private ProgressBar progressBar;
    private ObjectAnimator animator;
    private CardView cardViewHeartRate;
    private CardView cardViewRR;
    private Chart chartHeartRate;
    private Chart chartRR;
    private CountDownTimer countDownTimer;
    private CardView cardViewInfoMeasure;
    private TextView tvTitleVariabilityInfo;
    private TextView tvVariabilityInfo;
    private TextView tvHeartRateInfo;
    private TextView tvAverageRR;
    private TextView tvSdnn;
    private TextView tvNn50;
    private TextView tvPnn50;
    private TextView tvRmssd;
    private TextView tvLnRmssd;
    private TextView tvHeartRateMaxMin;

    private List<Double> valueList;
    private Boolean isChronoFinished;
    private Boolean isCountdownStarted;
    private Boolean isMeasureSaved;
    private List<Integer> rrIntervalList;
    private List<Integer> heartRateList;
    private ChartService chartService;
    private Integer xAxisValue;



    public MeasureFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_measure, container, false);
        this.lvDevices = (ListView) view.findViewById(R.id.lv_devices);
        this.btStartMeasure = (Button) view.findViewById(R.id.bt_start_measure);
        this.btSaveMeasurement = (Button) view.findViewById(R.id.bt_save_measure);
        this.tvTitleMeasure = (TextView) view.findViewById(R.id.tv_title_measurement);
        this.tvTitleChrono = (TextView) view.findViewById(R.id.tv_title_chrono);
        this.tvChrono = (TextView) view.findViewById(R.id.tv_chrono);
        this.tvTitleHeartRateChart = (TextView) view.findViewById(R.id.tv_title_heart_rate_chart);
        this.tvTitleRRChart = (TextView) view.findViewById(R.id.tv_title_rr_chart);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.cardViewHeartRate = (CardView) view.findViewById(R.id.cv_chart_hr);
        this.cardViewRR = (CardView) view.findViewById(R.id.cv_chart_rr);
        this.chartHeartRate = (Chart) view.findViewById(R.id.lineChart_hr);
        this.chartRR = (Chart) view.findViewById(R.id.lineChart_rr);
        this.cardViewInfoMeasure = (CardView) view.findViewById(R.id.cv_info_measure);
        this.tvTitleVariabilityInfo = (TextView) view.findViewById(R.id.tv_title_variability_info);
        this.tvVariabilityInfo = (TextView) view.findViewById(R.id.tv_variablity_info);
        this.tvHeartRateInfo = (TextView) view.findViewById(R.id.tv_heart_rate_info);
        this.tvAverageRR = (TextView) view.findViewById(R.id.tv_average_RR);
        this.tvSdnn = (TextView) view.findViewById(R.id.tv_sdnn);
        this.tvNn50 = (TextView) view.findViewById(R.id.tv_nn50);
        this.tvPnn50 = (TextView) view.findViewById(R.id.tv_pnn50);
        this.tvRmssd = (TextView) view.findViewById(R.id.tv_rmssd);
        this.tvLnRmssd = (TextView) view.findViewById(R.id.tv_lnrmssd);
        this.tvHeartRateMaxMin = (TextView) view.findViewById(R.id.tv_hr_max_min);


        this.btStartMeasure.setVisibility(View.GONE);
        this.btSaveMeasurement.setVisibility(View.GONE);
        this.tvTitleChrono.setVisibility(View.GONE);
        this.tvChrono.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.GONE);
        this.cardViewHeartRate.setVisibility(View.GONE);
        this.cardViewRR.setVisibility(View.GONE);
        this.tvTitleHeartRateChart.setVisibility(View.GONE);
        this.tvTitleRRChart.setVisibility(View.GONE);
        this.cardViewInfoMeasure.setVisibility(View.GONE);

        this.tvVariabilityInfo.setText("");
        this.tvHeartRateInfo.setText("Frecuencia cardíaca:   ");
        this.tvAverageRR.setText("Media de intervalos R-R:   ");
        this.tvSdnn.setText("SDNN:   ");
        this.tvNn50.setText("NN50:   ");
        this.tvPnn50.setText("PNN50:   ");
        this.tvRmssd.setText("RMSSD:   ");
        this.tvLnRmssd.setText("ln(RMSSD):   ");
        this.tvHeartRateMaxMin.setText("Max(FC) - Min(FC):   ");

        this.isChronoFinished = false;
        this.isCountdownStarted = false;
        this.isMeasureSaved = false;

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try{
            this.xmlService = new XmlService(getContext());
            this.xmlService.loadXml();
            this.chartService =  new ChartService();
            this.heartRateList = new ArrayList<>();
            this.rrIntervalList = new ArrayList<>();
            this.isChronoFinished = false;
            this.isCountdownStarted = false;
            this.isMeasureSaved = false;
            this.xAxisValue = 0;
            this.heartRateList = new ArrayList<>();
            this.rrIntervalList = new ArrayList<>();
        }catch (Exception exc){
            System.err.println(exc);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeBluetooth();
        setDeviceList();
        setListViewAdapter();
        setListeners();
    }

    public UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void initializeBluetooth(){
        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        this.bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setDeviceList(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        this.deviceList = new ArrayList<BluetoothDevice>();

        for (BluetoothDevice device: pairedDevices) {
            this.deviceList.add(device);
            System.out.println(device.getAddress() + " " + device.getName() + " " + device.getType());
        }
    }

    private void setListViewAdapter(){
        ArrayList<String> namesDevices = new ArrayList<>();
        for (BluetoothDevice device: this.deviceList) {
            namesDevices.add(device.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, namesDevices);
        this.lvDevices.setAdapter(adapter);

        if(this.deviceList.size() > 0){
            this.lvDevices.setMinimumHeight(200);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setListeners(){
        lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                device = deviceList.get(i);

                for(int a = 0; a < adapterView.getChildCount(); a++)
                {
                    adapterView.getChildAt(a).setBackgroundColor(Color.WHITE);
                }

                view.setBackgroundColor(Color.LTGRAY);
                btStartMeasure.setText("Empezar a medir con " + device.getName());
                btStartMeasure.setVisibility(View.VISIBLE);
            }
        });

        btStartMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            public void onClick(View view) {
                try{
                    connectGatt();
                    buildChrono();

                    cardViewHeartRate.setVisibility(View.VISIBLE);
                    cardViewRR.setVisibility(View.VISIBLE);
                    setCharts();

                }catch (Exception ex){
                    Toast.makeText(getContext(), "Error al conectar con " + device.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        btSaveMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMeasureSaved = true;

                if(measurement != null){
                    xmlService.addMeasurement(measurement);
                }

                Toast.makeText(getContext(), "Datos guardados", Toast.LENGTH_SHORT).show();

                btSaveMeasurement.setVisibility(View.GONE);
            }
        });


        /*navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(!isMeasureSaved && isChronoFinished){
                    showSaveDialog();
                }

                return false;
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void connectGatt(){
        BluetoothGattCallback gattCallback =  new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == STATE_CONNECTED){
                    gatt.discoverServices();
                }

                if(newState == STATE_DISCONNECTED){
                    gatt.disconnect();
                    gatt.close();
                    System.out.println("DESCONECTADO");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                BluetoothGattCharacteristic characteristic =
                        gatt.getService(HEART_RATE_SERVICE_UUID)
                                .getCharacteristic(HEART_RATE_MEASUREMENT_CHAR_UUID);
                gatt.setCharacteristicNotification(characteristic, true);

                BluetoothGattDescriptor descriptor =
                        characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);

                descriptor.setValue(
                        BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                BluetoothGattCharacteristic characteristic =
                        gatt.getService(HEART_RATE_SERVICE_UUID)
                                .getCharacteristic(HEART_RATE_MEASUREMENT_CHAR_UUID);
                characteristic.setValue(new byte[]{1, 1});
                gatt.writeCharacteristic(characteristic);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

                if(characteristic.getUuid().equals(HEART_RATE_MEASUREMENT_CHAR_UUID)){
                    byte[] value = characteristic.getValue();

                    if(isCountdownStarted == false){
                        countDownTimer.start();
                        isCountdownStarted = true;
                        //animatePrgressBar();
                    }

                    calculateHRandRRIntervals(value);
                    setCharts();


                    if(isChronoFinished){
                        gatt.disconnect();
                        measurement = new Measurement(heartRateList, rrIntervalList);
                        System.out.println(measurement.toString());
                        setMeasurementInfo(measurement);
                    }
                }
            }
        };

        this.gatt = this.device.connectGatt(getContext(), true, gattCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDetach() {
        disconnect();
        super.onDetach();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onStop() {
        if(!isMeasureSaved && isChronoFinished){
            showSaveDialog();
        }
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDestroyView() {
        disconnect();
        super.onDestroyView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void buildChrono(){
        this.btStartMeasure.setVisibility(View.GONE);
        this.lvDevices.setVisibility(View.GONE);
        this.tvTitleMeasure.setVisibility(View.GONE);
        this.tvTitleMeasure.setTextSize(20);

        this.tvTitleChrono.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.tvChrono.setVisibility(View.VISIBLE);
        this.tvTitleHeartRateChart.setVisibility(View.VISIBLE);
        this.tvTitleRRChart.setVisibility(View.VISIBLE);

        this.tvTitleChrono.setText("Midiendo con " + device.getName());
        //this.tvChrono.setTextSize(20);
        this.tvTitleChrono.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.tvChrono.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //this.tvChrono.setTextSize(50);
        this.tvChrono.setText("30 seg");

        setChronoTime();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setChronoTime(){
        this.countDownTimer = new CountDownTimer(COUNTDOWN_TIME, 1000) {


            public void onTick(long millisUntilFinished) {
                tvChrono.setText(String.format(Locale.getDefault(), "%d sec.", millisUntilFinished / 1000L));
            }

            public void onFinish() {
                tvChrono.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                tvTitleChrono.setVisibility(View.GONE);

                tvVariabilityInfo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvTitleVariabilityInfo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                cardViewInfoMeasure.setVisibility(View.VISIBLE);
                btSaveMeasurement.setVisibility(View.VISIBLE);

                isChronoFinished = true;
                Toast.makeText(getContext(), "Medición realizada", Toast.LENGTH_LONG).show();
            }
        };
    }


    public void calculateHRandRRIntervals(byte[] value){
        if(value.length > 3){
            Byte flag = value[0];
            Byte hr = value[1];
            Byte rr1 = value[3];
            Byte rr2 = value[2];

            Integer intRR1 = Math.abs(new Integer(rr1.toString()));
            Integer intRR2 = Math.abs(new Integer(rr2.toString()));

            String binaryRR1 = Integer.toBinaryString(intRR1);
            String binaryRR2 = Integer.toBinaryString(intRR2);

            //Completar el número binario con ceros para que quede como un número de 8 bits
            String completeBinaryRR1 = addZerosToBinaryNumber(binaryRR1);
            String completeBinaryRR2 = addZerosToBinaryNumber(binaryRR2);

            String binaryRR = completeBinaryRR1 + completeBinaryRR2;
            Integer rrInterval = Integer.parseInt(binaryRR,2);

            System.out.println("RR: " + rrInterval + " ms");
            System.out.println("HR: " + hr);

            rrIntervalList.add(rrInterval);
            heartRateList.add(new Integer(hr));
        }
    }

    public String addZerosToBinaryNumber(String binaryNumber){
        if(binaryNumber.length() < 8){
            String zeros = "";
            Integer numZeros = 8 - binaryNumber.length();

            for(int i = 0; i < numZeros; i++){
                zeros += "0";
            }

            return  zeros + binaryNumber;
        }else{
            return  binaryNumber;
        }
    }


    private void setCharts(){
        chartService.setLineChartData(chartHeartRate, heartRateList, "Frecuencia cardiaca", Color.RED, xAxisValue);
        chartService.setLineChartData(chartRR, rrIntervalList, "Intervalo R-R", Color.BLUE, xAxisValue);

        chartHeartRate.notifyDataSetChanged();
        chartHeartRate.invalidate();
        chartRR.notifyDataSetChanged();
        chartRR.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void disconnect(){
        if(this.gatt != null){
            this.gatt.disconnect();
        }
    }

    private void setMeasurementInfo(Measurement measurement){
        this.tvVariabilityInfo.setText(measurement.getVariability().toString());
        this.tvHeartRateInfo.setText("Frecuencia cardíaca:   " + measurement.getHeartRate().toString() + " bpm");
        this.tvAverageRR.setText("Media de intervalos R-R:   " + measurement.getMeanRR().toString() + " ms");
        this.tvSdnn.setText("SDNN:   " + measurement.getSdnn().toString() + " ms");
        this.tvNn50.setText("NN50:   " + measurement.getNn50().toString());
        this.tvPnn50.setText("PNN50:   " + measurement.getPnn50().toString() + " %");
        this.tvRmssd.setText("RMSSD:   " + measurement.getRmssd().toString() + " ms");
        this.tvLnRmssd.setText("ln(RMSSD):   " + measurement.getLnRmssd().toString() + " ms");
        this.tvHeartRateMaxMin.setText("Max(FC) - Min(FC):   " + measurement.getHrMaxMinDifference().toString() + " bpm");
    }

    private void animatePrgressBar(){
        animator = ObjectAnimator.ofInt(progressBar, "progress", 0,100);
        animator.setDuration(COUNTDOWN_TIME);
        animator.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void showSaveDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("AlertDialog 2 botones");
        builder.setMessage("¿Quieres guardar la medición realizada?");

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isMeasureSaved = true;
                if(measurement != null){
                    xmlService.addMeasurement(measurement);
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onDetach();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }
}