package ca.ulaval.ima.mp.alarmedeluxe;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
/**
 * Created by Jonathan on 3/12/2017.
 */

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

        Intent alarmIntent = new Intent(context,AlarmDialogPopUp.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra("AlarmID", intent.getIntExtra("AlarmID", -1));
        context.startActivity(alarmIntent);

    }
}
