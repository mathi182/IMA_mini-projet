package ca.ulaval.ima.mp.alarmedeluxe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;
import ca.ulaval.ima.mp.alarmedeluxe.DividerItemDecoration;
import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.adapter.AlarmAdapter;

public class HomeFragment extends Fragment {

    private RecyclerView alarmRecyclerView;
    private List<Alarm> alarmList = new ArrayList<>();
    private AlarmAdapter alarmAdapter;

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
                //TODO : This button will need to show up a new page instead of creating an empty alarm
                alarmList.add(new Alarm());
                alarmAdapter.notifyItemChanged(alarmList.size() - 1);
            }
        });
    }
}
