package ca.ulaval.ima.mp.alarmedeluxe.customization;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.youtube.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.adapter.AlarmTypeListAdapter;
import ca.ulaval.ima.mp.alarmedeluxe.adapter.YoutubeSearchListAdapter;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.AlarmType;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.AlarmTypeFactory;
import ca.ulaval.ima.mp.alarmedeluxe.domain.types.DividerItemDecoration;
import ca.ulaval.ima.mp.alarmedeluxe.persistence.DBHelper;
import ca.ulaval.ima.mp.alarmedeluxe.youtube.YoutubeSearch;

public class YoutubeFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener, YoutubeSearch.AsyncYoutubeResponse {

    private GoogleApiClient mGoogleApiClient;
    private TextView txt_noYoutube_alarm;
    private Button  btn_newYoutubeAlarm;
    private int RC_SIGN_IN = 42;
    private SearchView searchView;
    private RecyclerView youtubeListSearch, alarmTypeList;
    private List<SearchResult> results = new ArrayList<>();
    private List<AlarmType> alarmTypes = new ArrayList<>();
    private YoutubeSearchListAdapter seachListAdapter;
    private AlarmTypeListAdapter typeListAdapter;
    private DBHelper database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);
        database = new DBHelper(getContext());

        seachListAdapter = new YoutubeSearchListAdapter(results, getContext());
        youtubeListSearch = (RecyclerView)view.findViewById(R.id.youtube_list_search);
        youtubeListSearch.setAdapter(seachListAdapter);
        youtubeListSearch.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        youtubeListSearch.setItemAnimator(new DefaultItemAnimator());
        youtubeListSearch.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

        txt_noYoutube_alarm = (TextView)view.findViewById(R.id.txt_no_youtube_alarms);
        searchView = (SearchView)view.findViewById(R.id.txt_youtube_search);
        searchView.setOnQueryTextListener(this);

        alarmTypes.addAll(database.getAllAlarmTypes(AlarmTypeFactory.YOUTUBE_ALARM_TYPE_NAME));
        typeListAdapter = new AlarmTypeListAdapter(alarmTypes, getContext());
        alarmTypeList = (RecyclerView)view.findViewById(R.id.list_youtube_alarms);
        alarmTypeList.setAdapter(typeListAdapter);
        alarmTypeList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        alarmTypeList.setItemAnimator(new DefaultItemAnimator());
        alarmTypeList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

        btn_newYoutubeAlarm = (Button)view.findViewById(R.id.btn_newyoutube);
        btn_newYoutubeAlarm.setOnClickListener(this);

        if (alarmTypes.isEmpty()) {
            txt_noYoutube_alarm.setVisibility(View.VISIBLE);
        } else {
            txt_noYoutube_alarm.setVisibility(View.GONE);
        }

        return view;
    }

    public void updateAlarmTypeList() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_newyoutube:
                AlarmType youtubeAlarmType = seachListAdapter.getSelectedAlarmType();

                if (youtubeAlarmType == null) {
                    Toast.makeText(getContext(), "You must select a video.", Toast.LENGTH_SHORT).show();
                    return;
                }

                long id = database.insertAlarmType(youtubeAlarmType, null);
                youtubeAlarmType.setAlarmId((int)id);
                alarmTypes.add(youtubeAlarmType);
                typeListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return onQueryTextChange(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        new YoutubeSearch(this).execute(newText);

        return true;
    }

    @Override
    public void processFinish(List<SearchResult> results) {
        this.results.clear();
        this.results.addAll(results);
        seachListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
}
