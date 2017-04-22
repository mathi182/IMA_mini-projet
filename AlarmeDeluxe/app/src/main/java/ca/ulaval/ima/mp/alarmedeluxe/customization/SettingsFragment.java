package ca.ulaval.ima.mp.alarmedeluxe.customization;

import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.adapter.RingtoneAdapter;

public class SettingsFragment extends Fragment {

    private RingtoneAdapter adapter;
    private List<Ringtone> ringtones = new ArrayList<>();
    private Spinner spn_ringtones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        adapter = new RingtoneAdapter(getContext(), R.layout.alarm_types_subelement_row, ringtones);
        spn_ringtones = (Spinner)v.findViewById(R.id.spn_ringtones);
        spn_ringtones.setAdapter(adapter);

        return v;
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
