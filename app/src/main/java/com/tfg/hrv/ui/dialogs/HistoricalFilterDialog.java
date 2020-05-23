package com.tfg.hrv.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;

import androidx.fragment.app.DialogFragment;

import com.tfg.hrv.R;


public class HistoricalFilterDialog extends DialogFragment {
    public String firstDate;
    public String lastDate;

    public CalendarView calendarView;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        this.calendarView = (CalendarView) this.getActivity().findViewById(R.id.calendar_view);
        this.calendarView = new CalendarView(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_historical_filter, null));

        this.calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
            }
        });

        return builder.create();
    }

}
