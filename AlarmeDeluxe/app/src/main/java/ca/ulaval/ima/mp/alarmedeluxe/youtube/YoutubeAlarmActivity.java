package ca.ulaval.ima.mp.alarmedeluxe.youtube;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.auth.OnError;
import ca.ulaval.ima.mp.alarmedeluxe.auth.OnTokenAcquired;
import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

@EActivity
public class YoutubeAlarmActivity extends YouTubeBaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, YoutubeVideoRatingFetch.AsyncYoutubeResponse, OnTokenAcquired.AsyncTokenRequest {
    private static final int AUTH_PHASE_TWO = 200;
    private Alarm alarm;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private String url = "a4NT5iBFuZs";
    private YouTube youTube;
    private TextView currentRating;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 42;
    private Button btn_like, btn_dislike;
    private String scope = "https://www.googleapis.com/auth/youtube.force-ssl";
    private AccountManager am;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_alarm);
        alarm = getIntent().getParcelableExtra("alarm");
        am = AccountManager.get(this);

        btn_dislike = (Button)findViewById(R.id.btnDislike);
        btn_like = (Button)findViewById(R.id.btnLike);
        btn_like.setOnClickListener(this);
        btn_dislike.setOnClickListener(this);

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
                youTubePlayer.loadVideo(alarm.getType().getURL());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        getVideoRating();

        youTubePlayerView.initialize(getResources().getString(R.string.api_key),onInitializedListener);
    }

    private boolean createAccountManager() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            // Should we show an explanation?
            if (this.shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {

            } else {

                // No explanation needed, we can request the permission.


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        Bundle options = new Bundle();
        Account[] accounts = am.getAccountsByType(AccountManager.KEY_AUTHTOKEN); //TODO

        am.getAuthToken(
                accounts[0],                     // Account retrieved using getAccountsByType()
                scope,                           // Auth scope
                options,                        // Authenticator-specific options
                this,                           // Your activity
                new OnTokenAcquired(this),          // Callback called when a token is successfully acquired
                new Handler(new OnError()));    // Callback called if an error occurs


        return true;
    }


    @Background
    public void getVideoRating(){
        new YoutubeVideoRatingFetch(this, getApplicationContext()).execute(url);

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
            case R.id.btnLike:
                    createAccountManager();
                break;
            case R.id.btnDislike:

                break;
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

    @Override
    public void processFinish(String output) {
        Log.e("WORKS", "YoutubeAlarmActivity");
    }

    @Override
    public void processFinish(Bundle bundle) { // After OnTokenAcquired
        Intent intent = bundle.getParcelable("intent");

        if (intent != null) {
            startActivityForResult(intent, AUTH_PHASE_TWO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == AUTH_PHASE_TWO) {
            Log.e("OnResult", "AUTH_PHASE_TWO");
        }
    }
}
