package com.tfg.hrv.ui.historical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tfg.hrv.R;
import com.tfg.hrv.core.Measurement;

import java.util.List;

public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder> {

    private List<Measurement> measurements;
    private final Context context;

    public HistoricalAdapter(List<Measurement> measurements, Context context){
        this.measurements = measurements;
        this.context = context;
    }

    public static class HistoricalViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        Toolbar toolbarDate;
        TextView tvHeartRate;
        TextView tvVariability;
        TextView tvComment;
        ImageView personPhoto;

        HistoricalViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView2);
            toolbarDate = (Toolbar)itemView.findViewById(R.id.toolbar_date);
            tvHeartRate = (TextView)itemView.findViewById(R.id.tv_heartRate);
            tvVariability = (TextView)itemView.findViewById(R.id.tv_variability_home);
            tvComment = (TextView)itemView.findViewById(R.id.tv_comment);


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

    @Override
    public void onBindViewHolder(HistoricalViewHolder historicalViewHolder, final int i) {
        historicalViewHolder.toolbarDate.setTitle(measurements.get(i).getDate());
        historicalViewHolder.tvHeartRate.setText(measurements.get(i).getHeartRate().toString());
        historicalViewHolder.tvVariability.setText(measurements.get(i).getVariability().toString());
        historicalViewHolder.tvComment.setText(measurements.get(i).getComment());

        historicalViewHolder.toolbarDate.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mi_delete:
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
