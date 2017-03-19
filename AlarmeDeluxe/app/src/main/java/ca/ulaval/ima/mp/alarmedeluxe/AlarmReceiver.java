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
       /* Reminder inst = Reminder.instance();
        inst.setAlarmText("");*/

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        /*Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();*/

        //this will send a notification message
        /*ComponentName comp = new ComponentName(context.getPackageName(),
                MainActivity.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);*/

        //Turn the screen on when the alarm is ringing
        PowerManager.WakeLock screenLock = ((PowerManager)context.getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        Alarm alarm = intent.getParcelableExtra("alarm");
        Intent alarmIntent = new Intent(context, alarm.getType().getAlarmActivity().getClass());
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra("AlarmID", intent.getIntExtra("AlarmID", -1));
        context.startActivity(alarmIntent);

        screenLock.release();
    }
}
