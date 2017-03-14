package ca.ulaval.ima.mp.alarmedeluxe.fragment;

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
import ca.ulaval.ima.mp.alarmedeluxe.DividerItemDecoration;
import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.adapter.AlarmAdapter;
import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

import static android.content.Context.ALARM_SERVICE;

public class HomeFragment extends Fragment {

    private RecyclerView alarmRecyclerView;
    private List<Alarm> alarmList = new ArrayList<>();
    private AlarmAdapter alarmAdapter;
    private AlarmManager am;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        alarmAdapter = new AlarmAdapter(alarmList);
        alarmRecyclerView = (RecyclerView)getActivity().findViewById(R.id.list_alarms);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        alarmRecyclerView.setLayoutManager(layoutManager);
        alarmRecyclerView.setItemAnimator(new DefaultItemAnimator());
        alarmRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        alarmRecyclerView.setAdapter(alarmAdapter);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.btn_addClock);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), AlarmMakingActivity.class);
                startActivityForResult(in,666);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 666){
            if(resultCode == getActivity().RESULT_OK){
                Alarm alarm = data.getExtras().getParcelable("alarm");
                alarmList.add(alarm);
                alarmAdapter.notifyItemChanged(alarmList.size() - 1);

                Intent in = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),0,in,PendingIntent.FLAG_UPDATE_CURRENT);
                am = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,alarm.getHours());
                calendar.set(Calendar.MINUTE,alarm.getMinutes());
                //Log.e(" jour",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                Log.e(" hours",String.valueOf(alarm.getHours()));
                Log.e(" minutes",String.valueOf(alarm.getMinutes()));
                Log.e("calendar jour",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                Log.e("calendar hours",String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
                Log.e("calendar minutes",String.valueOf(calendar.get(Calendar.MINUTE)));
                Log.e("Calendar",String.valueOf(calendar.getTimeInMillis()));
                Log.e("Calendar",String.valueOf(calendar.getTimeInMillis()));
                Log.e("System", String.valueOf(Calendar.getInstance().getTimeInMillis()));
                am.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            }
        }

    }
}
