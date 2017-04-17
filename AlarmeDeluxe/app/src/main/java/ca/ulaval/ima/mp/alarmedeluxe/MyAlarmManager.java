package ca.ulaval.ima.mp.alarmedeluxe;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.Calendar;

import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

import static android.content.Context.ALARM_SERVICE;

public final class MyAlarmManager {

    private static AlarmManager alarmManager;
    private static Activity mainActivity;

    private MyAlarmManager(Activity mainActivity) {
        alarmManager = (AlarmManager)mainActivity.getSystemService(ALARM_SERVICE);
        this.mainActivity = mainActivity;
    }

    public static void createAlarmManager(Activity activity) {
        new MyAlarmManager(activity);
    }

    public static void updateAlarmManager(Alarm alarm) {
        Intent in = new Intent(mainActivity, AlarmReceiver.class);
        byte[] alarmBytes = ParcelableUtil.marshall(alarm);
        in.putExtra("alarm", alarmBytes);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mainActivity,alarm.getId(),in,PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarm.isActive()) {
            if (alarm.isRepeating()) {
                Calendar now = Calendar.getInstance();
                int dayOfWeek = Calendar.SUNDAY; //Week starts on Sunday

                while (alarm.getTime().compareTo(now) < 0) {
                    alarm.getTime().add(Calendar.DATE, 1);
                }

                for (boolean b : alarm.getDays()) {
                    if (b) {
                        while (alarm.getTime().get(Calendar.DAY_OF_WEEK) != dayOfWeek ) {
                            alarm.getTime().add(Calendar.DATE, 1);
                        }
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTime().getTimeInMillis(), pendingIntent);
                    }
                    dayOfWeek++;
                }
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTime().getTimeInMillis(), pendingIntent);
            }
        } else {
            alarmManager.cancel(pendingIntent);
        }
    }
}
