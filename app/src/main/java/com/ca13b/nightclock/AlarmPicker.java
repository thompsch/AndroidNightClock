package com.ca13b.nightclock;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class AlarmPicker {

    private MainActivity mainActivity;
    private static Calendar alarmCalendar;
    public static Boolean alarmIsSet = false;

    public AlarmPicker(MainActivity main){
        this.mainActivity = main;
    }

    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alarm_picker);

        if (alarmIsSet) {

            TextView text = (TextView) dialog.findViewById(R.id.alarmText);
            text.setText("Alarm set for " + DateFormat.format("HH:mm", alarmCalendar.getTime()));
            LinearLayoutCompat llAlarmText = dialog.findViewById(R.id.llAlarmText);
            llAlarmText.setVisibility(View.VISIBLE);

            dialog.findViewById(R.id.deleteAlarm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmIsSet = false;
                    mainActivity.ClearAlarm();
                    AlarmReceiver.RemoveAlarm();
                    TextView text = (TextView) dialog.findViewById(R.id.alarmText);
                    text.setText("Alarm set for " + DateFormat.format("HH:mm", alarmCalendar.getTime()));
                    LinearLayoutCompat llAlarmText = dialog.findViewById(R.id.llAlarmText);
                    llAlarmText.setVisibility(View.INVISIBLE);
                    dialog.dismiss();
                }
            });
        }

        TimePicker tp = dialog.findViewById(R.id.alarmTimePicker);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int selectedHour, int selectedMinute) {

                Calendar calNow = Calendar.getInstance();
                AlarmPicker.alarmCalendar = (Calendar) calNow.clone();

                AlarmPicker.alarmCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                AlarmPicker.alarmCalendar.set(Calendar.MINUTE, selectedMinute);
                AlarmPicker.alarmCalendar.set(Calendar.SECOND, 0);
                AlarmPicker.alarmCalendar.set(Calendar.MILLISECOND, 0);

                if (AlarmPicker.alarmCalendar.compareTo(calNow) <= 0) {
                    // Today Set time passed, count to tomorrow
                    AlarmPicker.alarmCalendar.add(Calendar.DATE, 1);
                }
            }

            //TODO: if dailyCheckBox is checked, make it recurring...and figure out how to
            //handling cancelling 1 or all
        });

        dialog.findViewById(R.id.dialogOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set alarm, etc.
                if (AlarmPicker.alarmCalendar==null) return;

                alarmIsSet = true;
                MainActivity.SetAlarm(mainActivity, AlarmPicker.alarmCalendar);
                dialog.dismiss();
                mainActivity.hideAll();
            }
        });

        dialog.findViewById(R.id.dialogCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mainActivity.hideAll();
            }
        });

        dialog.show();

    }
}