package com.tfg.hrv.ui.historical;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.tfg.hrv.R;
import com.tfg.hrv.core.Measurement;
import com.tfg.hrv.core.SQLite.MeasurementDbHelper;
import com.tfg.hrv.core.XmlService;
import com.tfg.hrv.ui.charts.ChartHelper;

import java.util.List;

public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder> {

    private SQLiteDatabase db;
    private MeasurementDbHelper dbHelper;
    private List<Measurement> measurements;
    private final Context context;

    public HistoricalAdapter(List<Measurement> measurements, Context context, SQLiteDatabase db){
        this.context = context;
        this.measurements = measurements;
        this.db = db;
    }

    public static class HistoricalViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        Toolbar toolbarDate;
        TextView tvHeartRate;
        TextView tvVariability;
        PieChart chart;
        //TextView tvComment;
        ImageView personPhoto;

        HistoricalViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView)itemView.findViewById(R.id.cardView2);
            this.toolbarDate = (Toolbar)itemView.findViewById(R.id.toolbar_date);
            this.tvHeartRate = (TextView)itemView.findViewById(R.id.tv_heart_rate_hist);
            this.tvVariability = (TextView)itemView.findViewById(R.id.tv_variability_hist);
            this.chart = (PieChart) itemView.findViewById(R.id.pieChart_variability_hist);
            //tvComment = (TextView)itemView.findViewById(R.id.tv_comment);
        }
    }

    @Override
    public int getItemCount() {
        return this.measurements.size();
    }

    @Override
    public HistoricalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row, viewGroup, false);
        HistoricalViewHolder historicalViewHolder = new HistoricalViewHolder(v);
        historicalViewHolder.toolbarDate.inflateMenu(R.menu.historic_card_menu);

        return historicalViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onBindViewHolder(HistoricalViewHolder historicalViewHolder, final int i) {
        historicalViewHolder.toolbarDate.setTitle(measurements.get(i).getDate());
        historicalViewHolder.tvHeartRate.setText(measurements.get(i).getHeartRate().toString());
        historicalViewHolder.tvVariability.setText(measurements.get(i).getVariability().toString());

        historicalViewHolder.tvHeartRate.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        historicalViewHolder.tvVariability.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ChartHelper.setPieChart(historicalViewHolder.chart, measurements.get(i).getVariability());
        //historicalViewHolder.tvComment.setText(measurements.get(i).getComment());

        historicalViewHolder.toolbarDate.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mi_delete:
                        MeasurementDbHelper.deleteMeasurement(db, measurements.get(i).getDate());
                        measurements.remove(i);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Medición eliminada", Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }


}
