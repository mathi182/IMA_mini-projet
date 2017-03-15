package ca.ulaval.ima.mp.alarmedeluxe;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class AlarmDialogPopUp extends AppCompatActivity {
    private int m_alarmId;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        // Show the activity over the lock screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        //Ringtone ringtone = RingtoneManager.getRingtone(this, alarmUri);
        mediaPlayer = MediaPlayer.create(this,alarmUri);
        mediaPlayer.start();
        // Get the alarm ID from the intent extra data
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            m_alarmId = extras.getInt("AlarmID", -1);
        } else {
            m_alarmId = -1;
        }

        // Show the popup dialog
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        super.onCreateDialog(id);

        // Build the dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("ALARM REMINDER");
        alert.setMessage("Its time for the alarm ");
        alert.setCancelable(false);

        alert.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mediaPlayer.stop();
                mediaPlayer.release();
                AlarmDialogPopUp.this.finish();
            }
        });

        // Create and return the dialog
        AlertDialog dlg = alert.create();

        return dlg;
    }
}
