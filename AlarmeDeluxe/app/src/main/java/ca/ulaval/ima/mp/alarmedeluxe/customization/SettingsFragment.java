package ca.ulaval.ima.mp.alarmedeluxe.customization;

import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.MainActivity;
import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.adapter.RingtoneAdapter;
import ca.ulaval.ima.mp.alarmedeluxe.persistence.DBHelper;

public class SettingsFragment extends Fragment {

    private RingtoneAdapter adapter;
    private List<Ringtone> ringtones = new ArrayList<>();
    private Spinner spn_ringtones;
    private DBHelper database;
    private SeekBar sb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        adapter = new RingtoneAdapter(getContext(), R.layout.alarm_types_subelement_row, ringtones);
        spn_ringtones = (Spinner)v.findViewById(R.id.spn_ringtones);
        spn_ringtones.setAdapter(adapter);
        spn_ringtones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                database.updateOrInsertSettings(null,String.valueOf(position),"ringtone");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ToggleButton toggle = (ToggleButton)v.findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    database.updateOrInsertSettings(null,"true","vibration");
                }
                else{
                    database.updateOrInsertSettings(null,"false","vibration");
                }
            }
        });
        database =((MainActivity)getActivity()).getDatabase();

        sb = (SeekBar)v.findViewById(R.id.seekBar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                double volume = (double)seekBar.getProgress()/seekBar.getMax();
                database.updateOrInsertSettings(null,String.valueOf(volume),"volume");
            }
        });

        setSettings();
        return v;
    }

    private void setSettings() {
        setVolume();
        setRingtone();
    }

    private void setRingtone() {
        String positionText = database.getSettings("ringtone");
        if(positionText.equals("")){
            spn_ringtones.setSelection(0);

        }else{
            int position = Integer.parseInt(positionText);
            spn_ringtones.setSelection(position);
        }
    }

    private void setVolume() {
        String volumeText = database.getSettings("volume");
        if(volumeText.equals("")){
            sb.setProgress(50);

        }else{
            int volume = (int)(Double.parseDouble(volumeText)*sb.getMax());
            sb.setProgress(volume);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);

        getAllRingtones();
    }

    public void getAllRingtones() {
        RingtoneManager ringtoneMgr = new RingtoneManager(getActivity());
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneMgr.getCursor();
        int alarmsCount = alarmsCursor.getCount();
        if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
            return;
        }
        Ringtone[] alarms = new Ringtone[alarmsCount];
        while(!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
            int currentPosition = alarmsCursor.getPosition();
            alarms[currentPosition] = ringtoneMgr.getRingtone(currentPosition);
        }
        alarmsCursor.close();
        ringtones.clear();
        ringtones.addAll(Arrays.asList(alarms));
        adapter.notifyDataSetChanged();
    }
}
