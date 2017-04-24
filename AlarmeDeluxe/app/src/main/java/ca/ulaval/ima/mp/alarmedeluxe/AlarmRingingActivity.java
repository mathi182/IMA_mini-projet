package ca.ulaval.ima.mp.alarmedeluxe;

import android.app.FragmentTransaction;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

import static ca.ulaval.ima.mp.alarmedeluxe.MyAlarmManager.updateAlarmManager;

public class AlarmRingingActivity extends AppCompatActivity {

    private Alarm alarm;
    private boolean backPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);

        alarm = getIntent().getParcelableExtra("alarm");
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        if (alarm.isRepeating()) {
            updateAlarmManager(alarm);
        }

        // Show the activity over the lock screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.alarm_ringing_frame, alarm.getType().getFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            alarm.stop();
            finish();
        } else {
            backPressed = true;
            Toast.makeText(this, "Appuyez de nouveau pour retarder l'alarme.", Toast.LENGTH_SHORT).show();
        }
    }
}
