package ca.ulaval.ima.mp.alarmedeluxe.customization;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.SearchResult;
import com.google.common.collect.Lists;

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

import static com.google.android.gms.internal.zzs.TAG;

public class YoutubeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, SearchView.OnQueryTextListener, YoutubeSearch.AsyncYoutubeResponse {

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView, txt_noYoutube_alarm;
    private Button  btn_newYoutubeAlarm;
    private int RC_SIGN_IN = 42;
    private YouTube youTube;
    private TextView currentRating;
    private SearchView searchView;
    private RecyclerView youtubeListSearch, alarmTypeList;
    private List<SearchResult> results = new ArrayList<>();
    private List<AlarmType> alarmTypes = new ArrayList<>();
    private YoutubeSearchListAdapter seachListAdapter;
    private AlarmTypeListAdapter typeListAdapter;
    private DBHelper database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //public GoogleSignInOptions.Builder requestScopes (Scope scope, Scope... scopes)
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(YouTubeScopes.YOUTUBE_FORCE_SSL))
                .requestEmail()
                .requestIdToken("752816531302-jmo22jf1v826ta5ei8lvuf7gv44kic29.apps.googleusercontent.com")
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");

        try {
            if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(new Scope("https://www.googleapis.com/auth/youtube"))
                        .requestEmail()
                        .requestIdToken("752816531302-jmo22jf1v826ta5ei8lvuf7gv44kic29.apps.googleusercontent.com")
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            }
        } catch (IllegalStateException ex) {

        }
        // Set the dimensions of the sign-in button.
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);
        database = new DBHelper(getContext());

        seachListAdapter = new YoutubeSearchListAdapter(results, getContext());
        youtubeListSearch = (RecyclerView)view.findViewById(R.id.youtube_list_search);
        youtubeListSearch.setAdapter(seachListAdapter);
        youtubeListSearch.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        youtubeListSearch.setItemAnimator(new DefaultItemAnimator());
        youtubeListSearch.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

        //signOutButton = (Button) view.findViewById(R.id.sign_out_button);
        //mStatusTextView = (TextView) view.findViewById(R.id.connection_status);

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

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    public void updateAlarmTypeList() {

    }
/*
    @Background
    public void getVideoRating(){

        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
        //Credential credential = Auth.authorize(scopes, "getrating");
        youTube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        })
                .setApplicationName(getResources().getString(R.string.app_name))

                //.setGoogleClientRequestInitializer(new YouTubeRequestInitializer(getResources().getString(R.string.server_client_id)))
                .build();
        try {

            YouTube.Videos.GetRating request = youTube.videos().getRating(url);
            request.setKey("lDMenZDhmxb385ddSx_rWDAE");
            request.setOauthToken("");
            request.setId("752816531302-jmo22jf1v826ta5ei8lvuf7gv44kic29.apps.googleusercontent.com");
            VideoGetRatingResponse response = request.execute();
            showRating(response.getItems().get(0).getRating());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    public void showRating(String rating){

        currentRating.setText(rating);
    }*/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("YOUTUBE", "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("YOUTUBE", "Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("YOUTUBE", "Failed");
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
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
