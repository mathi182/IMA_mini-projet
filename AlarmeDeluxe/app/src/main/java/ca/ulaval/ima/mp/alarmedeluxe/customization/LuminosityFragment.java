package ca.ulaval.ima.mp.alarmedeluxe.customization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.adapter.AlarmTypeListAdapter;
import ca.ulaval.ima.mp.alarmedeluxe.domain.DividerItemDecoration;
import ca.ulaval.ima.mp.alarmedeluxe.persistence.DBHelper;
import ca.ulaval.ima.mp.alarmedeluxe.types.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.types.AlarmTypeFactory;
import ca.ulaval.ima.mp.alarmedeluxe.types.LuminosityAlarmType;

public class LuminosityFragment extends Fragment {

    private RecyclerView alarmRecyclerView;
    private AlarmTypeListAdapter adapter;
    private List<AlarmType> alarmTypes = new ArrayList<>();
    private TextView txt_noAlarms;
    private EditText txt_newName;
    private SeekBar skb_intensity;
    private Button btn_newAlarmType;
    private DBHelper database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_luminosity, container, false);

        txt_noAlarms = (TextView)v.findViewById(R.id.txt_no_luminosity_alarms);
        txt_newName = (EditText)v.findViewById(R.id.txt_newluminosity_name);
        skb_intensity = (SeekBar)v.findViewById(R.id.skb_newluminosity_strength);
        btn_newAlarmType = (Button)v.findViewById(R.id.btn_newluminosity);
        btn_newAlarmType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmType luminosity = new LuminosityAlarmType();
                Bundle bundle = new Bundle();

                bundle.putString("description", txt_newName.getText().toString());
                bundle.putDouble("strength", skb_intensity.getProgress()/skb_intensity.getMax());
                bundle.putBoolean("default", false);

                luminosity.buildFromBundle(bundle);

                long id = database.insertAlarmType(luminosity, null);
                luminosity.setAlarmId((int)id);

                alarmTypes.add(luminosity);
                adapter.notifyDataSetChanged();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        database = new DBHelper(getContext());
        alarmTypes = database.getAllAlarmTypes(AlarmTypeFactory.LUMINOSITY_ALARM_TYPE_NAME);

        if (alarmTypes.isEmpty()) {
            txt_noAlarms.setVisibility(View.VISIBLE);
        } else {
            txt_noAlarms.setVisibility(View.GONE);
        }

        adapter = new AlarmTypeListAdapter(alarmTypes, getContext());
        alarmRecyclerView = (RecyclerView)getActivity().findViewById(R.id.list_luminosity_alarms);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        alarmRecyclerView.setLayoutManager(layoutManager);
        alarmRecyclerView.setItemAnimator(new DefaultItemAnimator());
        alarmRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        alarmRecyclerView.setAdapter(adapter);

        database = new DBHelper(getContext());
    }
}
