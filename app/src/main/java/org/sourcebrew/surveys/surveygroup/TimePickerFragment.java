package org.sourcebrew.surveys.surveygroup;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by John on 12/11/2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        java.util.Calendar cal = java.util.Calendar.getInstance();

        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        //TextView tv = (TextView) getActivity().findViewById(R.id.tv);
        //Set a message for user
        //tv.setText("Your chosen time is...\n\n");
        //Display the user changed time on TextView
        //tv.setText(tv.getText()+ "Hour : " + String.valueOf(hourOfDay)
        //        + "\nMinute : " + String.valueOf(minute) + "\n");
    }

}
