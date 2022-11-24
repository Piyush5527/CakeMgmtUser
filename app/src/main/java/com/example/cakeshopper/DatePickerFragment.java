package com.example.cakeshopper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        DatePickerDialog dpd=new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)getTargetFragment(),year,month,day);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis()+(86400000*2));
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis()+(86400000*10));
        return dpd;
    }
}
