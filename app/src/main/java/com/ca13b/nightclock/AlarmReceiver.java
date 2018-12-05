package com.ca13b.nightclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Calendar;


public class AlarmReceiver extends BroadcastReceiver {

    private static WeakReference<MainActivity> mActivityRef;
    private static Ringtone ringtone;
    private static Integer snoozeTime = 7;

    public static PendingIntent pendingIntent;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message

        ConstraintLayout main = mActivityRef.get().findViewById(R.id.mainScreen);
        main.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundOnWake));

        ConstraintLayout cl = mActivityRef.get().findViewById(R.id.buttonPanel);
        cl.setVisibility(View.VISIBLE);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.setLooping(true);
        ringtone.play();
    }

    public static void setMainActivity(MainActivity main) {
        mActivityRef = new WeakReference<>(main);
    }

    public static void RemoveAlarm() {
        if (pendingIntent != null) {
            pendingIntent.cancel();

        }
        mActivityRef.get().ClearAlarm();
    }

    public static void Snooze(){
        Dismiss(mActivityRef.get().getApplicationContext());
        Calendar calNow = Calendar.getInstance();
        calNow.add(Calendar.MINUTE, snoozeTime);
        MainActivity.SetAlarm(mActivityRef.get(), calNow);
    }

    public static void Dismiss(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (ringtone.isPlaying()) ringtone.stop();

        if (pendingIntent != null) {
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);
        }

        ConstraintLayout main = mActivityRef.get().findViewById(R.id.mainScreen);
        main.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
        ConstraintLayout cl = mActivityRef.get().findViewById(R.id.buttonPanel);
        cl.setVisibility(View.INVISIBLE);

        mActivityRef.get().ClearAlarm();
    }
}
