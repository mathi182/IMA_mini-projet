package ca.ulaval.ima.mp.alarmedeluxe.customization;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.AlarmMakingActivity;
import ca.ulaval.ima.mp.alarmedeluxe.AlarmReceiver;
import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.adapter.AlarmAdapter;
import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;
import ca.ulaval.ima.mp.alarmedeluxe.domain.DividerItemDecoration;
import ca.ulaval.ima.mp.alarmedeluxe.persistence.DBHelper;

import static android.content.Context.ALARM_SERVICE;
import static ca.ulaval.ima.mp.alarmedeluxe.MyAlarmManager.updateAlarmManager;

public class HomeFragment extends Fragment {

    private static final int MAKE_NEW_ALARM_REQUEST = 666;
    private RecyclerView alarmRecyclerView;
    private List<Alarm> alarmList = new ArrayList<>();
    private AlarmAdapter alarmAdapter;
    private AlarmManager am;
    private DBHelper database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        alarmAdapter = new AlarmAdapter(alarmList, getContext());
        alarmRecyclerView = (RecyclerView)getActivity().findViewById(R.id.list_alarms);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        alarmRecyclerView.setLayoutManager(layoutManager);
        alarmRecyclerView.setItemAnimator(new DefaultItemAnimator());
        alarmRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        alarmRecyclerView.setAdapter(alarmAdapter);

        database = new DBHelper(getActivity());
        List<Alarm> alarms = database.getAllAlarms();

        for (Alarm alarm : alarms) {
            alarmList.add(alarm);
        }
        alarmAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.btn_addClock);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), AlarmMakingActivity.class);
                startActivityForResult(in, MAKE_NEW_ALARM_REQUEST);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MAKE_NEW_ALARM_REQUEST){
            if(resultCode == getActivity().RESULT_OK){
                Alarm alarm = data.getExtras().getParcelable("alarm");
                alarmList.add(alarm);
                alarmAdapter.notifyItemChanged(alarmList.size() - 1);
                updateAlarmManager(alarm);

                long id = database.insertAlarm(alarm);
                alarm.setId(id);
            }
        }
    }
}
