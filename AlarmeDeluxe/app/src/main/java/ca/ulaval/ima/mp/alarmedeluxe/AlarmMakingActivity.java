package ca.ulaval.ima.mp.alarmedeluxe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import ca.ulaval.ima.mp.alarmedeluxe.domain.AccelerometerAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;
import ca.ulaval.ima.mp.alarmedeluxe.domain.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.GeolocationAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.MathsAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.StandardAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.YoutubeAlarmType;

public class AlarmMakingActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private EditText title, description;
    private Spinner alarmTypeSpinner;
    private ArrayAdapter<AlarmType> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_making);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Hides the keyboard when activity starts
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        spinnerAdapter = new ArrayAdapter<AlarmType>(this,
                android.R.layout.simple_spinner_item, new AlarmType[] {
                new StandardAlarmType(),
                new AccelerometerAlarmType(),
                new GeolocationAlarmType(),
                new MathsAlarmType(),
                new YoutubeAlarmType()
        });
        alarmTypeSpinner = (Spinner)findViewById(R.id.spinner);
        alarmTypeSpinner.setAdapter(spinnerAdapter);
    }

    public void onButtonClick(View v){
        View rootView = v.getRootView();
        timePicker = (TimePicker)rootView.findViewById(R.id.timePicker);
        title = (EditText)rootView.findViewById(R.id.txtTitle);
        description = (EditText)rootView.findViewById(R.id.txtDescription);

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
