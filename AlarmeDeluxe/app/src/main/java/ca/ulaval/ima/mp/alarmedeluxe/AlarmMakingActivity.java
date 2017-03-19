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
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.adapter.AlarmTypeSpinnerAdapter;
import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;
import ca.ulaval.ima.mp.alarmedeluxe.types.AccelerometerAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.types.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.types.GeolocationAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.types.MathsAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.types.StandardAlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.types.YoutubeAlarmType;

public class AlarmMakingActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private EditText title, description;
    private Spinner alarmTypeSpinner;
    private ToggleButton btn_sunday, btn_monday, btn_tuesday, btn_wednesday, btn_thursday, btn_friday, btn_saturday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_making);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Hides the keyboard when activity starts
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        List<AlarmType> alarmTypes = new ArrayList<>();
        alarmTypes.add(new StandardAlarmType());
        alarmTypes.add(new AccelerometerAlarmType());
        alarmTypes.add(new GeolocationAlarmType());
        alarmTypes.add(new MathsAlarmType());
        alarmTypes.add(new YoutubeAlarmType());

        AlarmTypeSpinnerAdapter adapter = new AlarmTypeSpinnerAdapter(this, R.layout.alarmtype_spinner_row, alarmTypes);
        alarmTypeSpinner = (Spinner)findViewById(R.id.spinner);
        alarmTypeSpinner.setAdapter(adapter);
    }

    public void onButtonClick(View v){
        View rootView = v.getRootView();
        timePicker = (TimePicker)rootView.findViewById(R.id.timePicker);
        title = (EditText)rootView.findViewById(R.id.txtTitle);
        description = (EditText)rootView.findViewById(R.id.txtDescription);
        btn_sunday = (ToggleButton)rootView.findViewById(R.id.btn_sunday);
        btn_monday = (ToggleButton)rootView.findViewById(R.id.btn_monday);
        btn_tuesday = (ToggleButton)rootView.findViewById(R.id.btn_tuesday);
        btn_wednesday = (ToggleButton)rootView.findViewById(R.id.btn_wednesday);
        btn_thursday = (ToggleButton)rootView.findViewById(R.id.btn_thursday);
        btn_friday = (ToggleButton)rootView.findViewById(R.id.btn_friday);
        btn_saturday = (ToggleButton)rootView.findViewById(R.id.btn_saturday);

        boolean[] days = new boolean[] {
                btn_sunday.isChecked(),
                btn_monday.isChecked(),
                btn_tuesday.isChecked(),
                btn_wednesday.isChecked(),
                btn_thursday.isChecked(),
                btn_friday.isChecked(),
                btn_saturday.isChecked()
        };

        Alarm alarm = new Alarm();
        alarm.setDescription(description.getText().toString());
        alarm.setTitle(title.getText().toString());
        alarm.setTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
        alarm.setType((AlarmType)alarmTypeSpinner.getSelectedItem());
        alarm.setDays(days);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("alarm", alarm);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
