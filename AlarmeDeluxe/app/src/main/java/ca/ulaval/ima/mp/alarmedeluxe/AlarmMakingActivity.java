package ca.ulaval.ima.mp.alarmedeluxe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

/**
 * Created by Jonathan on 3/11/2017.
 */

public class AlarmMakingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_making);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onButtonClick(View v){
        View rootView = v.getRootView();
        TimePicker timePicker = (TimePicker)rootView.findViewById(R.id.timePicker);
        EditText title = (EditText)rootView.findViewById(R.id.txtTitle);
        EditText description = (EditText)rootView.findViewById(R.id.txtDescription);



        Alarm alarm = new Alarm();
        alarm.setDescription(description.getText().toString());
        alarm.setTitle(title.getText().toString());
        alarm.setHours(timePicker.getCurrentHour());
        alarm.setMinutes(timePicker.getCurrentMinute());


        Intent resultIntent = new Intent();
        resultIntent.putExtra("alarm", alarm);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
