package com.tfg.hrv.ui.historical;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.tfg.hrv.R;
import com.tfg.hrv.core.Measurement;
import com.tfg.hrv.core.SQLite.DbHelper;
import com.tfg.hrv.ui.charts.ChartHelper;

import java.util.List;

public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder> {

    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private List<Measurement> measurements;
    private final Context context;
    private Activity activity;

    public HistoricalAdapter(Activity activity , List<Measurement> measurements, Context context, SQLiteDatabase db){
        this.activity = activity;
        this.context = context;
        this.measurements = measurements;
        this.db = db;
    }

    public static class HistoricalViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        Toolbar toolbarDate;
        PieChart chart;
        TextView tvHeartRate;
        TextView tvMeanRR;
        TextView tvRmssd;

        HistoricalViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView)itemView.findViewById(R.id.cardView2);
            this.toolbarDate = (Toolbar)itemView.findViewById(R.id.toolbar_date);
            this.tvHeartRate = (TextView)itemView.findViewById(R.id.tv_mean_heartRate_hist);
            this.tvMeanRR = (TextView)itemView.findViewById(R.id.tv_meanRR_hist);
            this.tvRmssd = (TextView)itemView.findViewById(R.id.tv_rmssd_hist);
            this.chart = (PieChart) itemView.findViewById(R.id.pieChart_variability_hist);
            //tvComment = (TextView)itemView.findViewById(R.id.tv_comment);
        }
    }

    @Override
    public int getItemCount() {
        return this.measurements.size();
    }

    @Override
    public HistoricalViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row, viewGroup, false);
        HistoricalViewHolder historicalViewHolder = new HistoricalViewHolder(v);


        return historicalViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onBindViewHolder(HistoricalViewHolder historicalViewHolder, final int i) {

        historicalViewHolder.toolbarDate.inflateMenu(R.menu.historic_card_menu);

        historicalViewHolder.toolbarDate.setTitle(measurements.get(i).getDate());
        historicalViewHolder.tvHeartRate.setText("Frecuencia cardiaca:" + "  " + measurements.get(i).getHeartRate().toString() + " bpm");
        historicalViewHolder.tvMeanRR.setText("Media intervalos R-R:" + "  " + measurements.get(i).getMeanRR().toString() + " ms");
        historicalViewHolder.tvRmssd.setText("RMSSD:" + "  " + measurements.get(i).getRmssd().toString() + " ms");

        ChartHelper.setPieChartHist(historicalViewHolder.chart, measurements.get(i).getVariability());

        historicalViewHolder.toolbarDate.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mi_delete:
                        DbHelper.deleteMeasurement(db, measurements.get(i).getDate());
                        measurements.remove(i);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Medición eliminada", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.mi_details:
                        showDialog(measurements.get(i));
                        break;
                }
                return true;
            }
        });

        historicalViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(measurements.get(i));
                //Toast.makeText(context, measurements.get(i).getDate() , Toast.LENGTH_SHORT).show();
            }
        });

        /*historicalViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "aaaaaa", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void showDialog(Measurement measurement){
        /*// con este tema personalizado evitamos los bordes por defecto
        Dialog customDialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.dialog_hist_measurement_info);
        View view2 = customDialog.getCurrentFocus();*/


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_hist_measurement_info, null);

        builder.setView(view);

        Toolbar toolbarDate = (Toolbar) view.findViewById(R.id.toolbar_date_dialog_hist);
        TextView tvSubtitleVariability = (TextView) view.findViewById(R.id.tv_subtitle_variability_hist_dialog);
        TextView tvSubtitleHeartRate = (TextView) activity.findViewById(R.id.tv_subtitle_hr_hist_dialog);
        PieChart pieChart = (PieChart) view.findViewById(R.id.pieChart_hist_dialog);
        TextView tvHeartRate = (TextView) view.findViewById(R.id.tv_hr_hist_dialog);
        LineChart chartHearRate = (LineChart) view.findViewById(R.id.chart_heart_rates_hist_dialog);
        LineChart chartRRInterval = (LineChart) view.findViewById(R.id.chart_rrInterval_hist_dialog);
        TextView tvMeanRR = (TextView) view.findViewById(R.id.tv_meanRR_hist_dialog);
        TextView tvSdnn = (TextView) view.findViewById(R.id.tv_sdnn_hist_dialog);
        TextView tvNn50 = (TextView) view.findViewById(R.id.tv_nn50_hist_dialog);
        TextView tvPnn50 = (TextView) view.findViewById(R.id.tv_pnn50_hist_dialog);
        TextView tvRmssd = (TextView) view.findViewById(R.id.tv_rmssd_hist_dialog);
        TextView tvLnRmssd = (TextView) view.findViewById(R.id.tv_lnrmssd_hist_dialog);
        TextView tvHrMax = (TextView) view.findViewById(R.id.tv_hrMax_hist_dialog);
        TextView tvHrMin = (TextView) view.findViewById(R.id.tv_hrMin_hist_dialog);
        TextView tvHrMaxMin = (TextView) view.findViewById(R.id.tv_hrMaxMin_hist_dialog);

        toolbarDate.setTitle(measurement.getDate().toString());
        //this.tvVariability.setText(measurement.getVariability().toString());
        tvHeartRate.setText(measurement.getHeartRate().toString());
        tvMeanRR.setText(tvMeanRR.getText() + " " + measurement.getMeanRR().toString() + " ms");
        tvSdnn.setText(tvSdnn.getText() + " " + measurement.getSdnn().toString() + " ms");
        tvNn50.setText(tvNn50.getText() + " " + measurement.getNn50().toString());
        tvPnn50.setText(tvPnn50.getText() + " " + measurement.getPnn50().toString() + "%");
        tvRmssd.setText(tvRmssd.getText() + " " + measurement.getRmssd().toString() + " ms");
        tvLnRmssd.setText(tvLnRmssd.getText() + " " + measurement.getLnRmssd().toString());
        tvHrMax.setText(tvHrMax.getText() + " " + measurement.getHrMax().toString() + " bpm");
        tvHrMin.setText(tvHrMin.getText() + " " + measurement.getHrMin().toString() + " bpm");
        tvHrMaxMin.setText(tvHrMaxMin.getText() + " " + measurement.getHrMaxMinDifference().toString() + " bpm");

        //this.tvVariability.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        /*tvHeartRate.setTextAlignment(view.TEXT_ALIGNMENT_CENTER);
        tvSubtitleVariability.setTextAlignment(view.TEXT_ALIGNMENT_CENTER);
        tvSubtitleHeartRate.setTextAlignment(view.TEXT_ALIGNMENT_CENTER);*/

        ChartHelper.fillLineChart(measurement.getRrIntervals(), chartRRInterval, "Intervalos R-R", 1);
        ChartHelper.fillLineChart(measurement.getHeartRateList(), chartHearRate, "Frecuencias cardiacas", 2);
        ChartHelper.setPieChart(pieChart, measurement.getVariability());

        builder.create().show();
    }


}
