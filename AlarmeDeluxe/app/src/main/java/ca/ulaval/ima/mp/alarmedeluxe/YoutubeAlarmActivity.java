package ca.ulaval.ima.mp.alarmedeluxe;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.*;
import com.google.api.services.youtube.model.VideoGetRatingResponse;
import com.google.common.collect.Lists;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

/**
 * Created by Jonathan on 3/19/2017.
 */

@EActivity
public class YoutubeAlarmActivity extends YouTubeBaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    private Alarm alarm;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private String url = "a4NT5iBFuZs";
    private YouTube youTube;
    private TextView currentRating;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN =42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_alarm);
        alarm = getIntent().getParcelableExtra("alarm");

        // Show the activity over the lock screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(YouTubeScopes.YOUTUBE_FORCE_SSL))
                .requestEmail()
                .requestIdToken("752816531302-jmo22jf1v826ta5ei8lvuf7gv44kic29.apps.googleusercontent.com")
                .build();

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage((FragmentActivity) this /* FragmentActivity *///, this /* OnConnectionFailedListener */)
                //.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                //.build();*/

        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_player);
        onInitializedListener = new YouTubePlayer.OnInitializedListener(){

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(url);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        getVideoRating();

        youTubePlayerView.initialize(getResources().getString(R.string.api_key),onInitializedListener);
    }


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
        currentRating = (TextView)findViewById(R.id.lblRating);
        currentRating.setText(rating);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
