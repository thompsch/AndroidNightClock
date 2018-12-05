package com.ca13b.nightclock;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    java.util.Date date;
    Boolean dotsOn = true;
    public Thread thread;
    private static ImageButton btnAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideAll();
        AlarmReceiver.setMainActivity(this);

        btnAlarm = findViewById(R.id.btnAlarm);
        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                AlarmPicker alert = new AlarmPicker(MainActivity.this);
                alert.showDialog(MainActivity.this);
        }});

        final Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmReceiver.Dismiss(getApplicationContext());
            }
        });

        final Button btnSnooze = findViewById(R.id.btnSnooze);
        btnSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmReceiver.Snooze();
            }
        });

        final TextView tv = findViewById(R.id.time);
        updateTime(tv);

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               updateTime(tv);
                        }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }

    public void hideAll(){
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void updateTime(TextView tv){

        String time = "";

        if (dotsOn)time = "HH:mm";
        else time = time = "HH mm";

        dotsOn = !dotsOn;

        date = Calendar.getInstance().getTime();
        tv.setText(DateFormat.format(time, date));
    }

    public static void SetAlarm(AppCompatActivity mainActivity, Calendar calendar) {

        Context context = mainActivity.getBaseContext();
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);

        AlarmReceiver.pendingIntent = pendingIntent;

        TextView tvAlarmTime = mainActivity.findViewById(R.id.alarmTime);
        tvAlarmTime.setText(DateFormat.format("HH:mm", calendar.getTime()));

        btnAlarm.setImageResource(R.drawable.ic_baseline_alarm_on_24px);
    }

    public void ClearAlarm(){
        TextView tvAlarmTime = findViewById(R.id.alarmTime);
        tvAlarmTime.setText("");
        ImageButton btnAlarm = findViewById(R.id.btnAlarm);
        btnAlarm.setImageResource(R.drawable.ic_baseline_alarm_add_24px);
        AlarmPicker.alarmIsSet = false;
    }
}
