package ca.ulaval.ima.mp.alarmedeluxe;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

import static android.content.Context.POWER_SERVICE;

public class AlarmReceiver extends WakefulBroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {

        //Turn the screen on when the alarm is ringing
        PowerManager.WakeLock screenLock = ((PowerManager)context.getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        Alarm alarm = ParcelableUtil.unmarshall(intent.getByteArrayExtra("alarm"), Alarm.CREATOR);
        Intent alarmIntent = new Intent(context, alarm.getType().getAlarmActivity().getClass());
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra("AlarmID", intent.getIntExtra("AlarmID", -1));
        context.startActivity(alarmIntent);

        screenLock.release();
    }
}
