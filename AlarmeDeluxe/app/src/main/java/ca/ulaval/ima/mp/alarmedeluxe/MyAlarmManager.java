package ca.ulaval.ima.mp.alarmedeluxe;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

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
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTime().getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
        }

        /*
                //Log.e(" jour",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                Log.e(" hours",String.valueOf(alarm.getHours()));
                Log.e(" minutes",String.valueOf(alarm.getMinutes()));
                Log.e("calendar jour",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                Log.e("calendar hours",String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
                Log.e("calendar minutes",String.valueOf(calendar.get(Calendar.MINUTE)));
                Log.e("Calendar",String.valueOf(calendar.getTimeInMillis()));
                Log.e("Calendar",String.valueOf(calendar.getTimeInMillis()));
                Log.e("System", String.valueOf(Calendar.getInstance().getTimeInMillis()));*/
    }
}
