package fr.antoinebaudot.lab1mad;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MyDatePickerFragment extends DialogFragment {

    private int val ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        this.val = getArguments().getInt("VAL");


        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }


    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {

                    final Calendar c = Calendar.getInstance();
                    view.setMinDate(c.getTimeInMillis());

                    DialogData act = (DialogData) getActivity();
                    String date = view.getDayOfMonth() + "-" + (view.getMonth()+1) + "-" + view.getYear();
                    act.getDialogData(date,val);


                }
            };

    public  interface DialogData {
        void getDialogData(String date,int val);
    }


}



