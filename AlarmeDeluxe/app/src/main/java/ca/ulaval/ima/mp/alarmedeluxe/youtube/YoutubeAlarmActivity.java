package ca.ulaval.ima.mp.alarmedeluxe.youtube;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.common.collect.Lists;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;
import ca.ulaval.ima.mp.alarmedeluxe.auth.OnTokenAcquired;
import ca.ulaval.ima.mp.alarmedeluxe.domain.Alarm;

import static com.google.android.gms.internal.zzs.TAG;

@EActivity
public class YoutubeAlarmActivity extends YouTubeBaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, YoutubeVideoRatingFetch.AsyncYoutubeResponse, OnTokenAcquired.AsyncTokenRequest, YoutubeSetVideoRating.AsyncYoutubeResponse {
    private static final int AUTH_PHASE_TWO = 200;
    private static final int RC_REAUTHORIZE = 300;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 400;
    private Alarm alarm;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private String url = "a4NT5iBFuZs";
    private TextView currentRating;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 42;
    private Button btn_like, btn_dislike, btn_neutral;
    private GoogleAccountCredential credential;
    private String userEmail = "";
    private SignInButton btn_signIn;
    private List<String> scopes = Lists.newArrayList(YouTubeScopes.YOUTUBE);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_alarm);
        alarm = getIntent().getParcelableExtra("alarm");

        btn_dislike = (Button)findViewById(R.id.btnDislike);
        btn_like = (Button)findViewById(R.id.btnLike);
        btn_neutral =(Button)findViewById(R.id.btnNeutral);
        btn_like.setOnClickListener(this);
        btn_dislike.setOnClickListener(this);
        btn_signIn = (SignInButton)findViewById(R.id.sign_in_button);
        btn_signIn.setOnClickListener(this);
        btn_neutral.setOnClickListener(this);

        // Show the activity over the lock screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(YouTubeScopes.YOUTUBE_FORCE_SSL))
                .requestEmail()
                //.requestIdToken("752816531302-jmo22jf1v826ta5ei8lvuf7gv44kic29.apps.googleusercontent.com")
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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


        youTubePlayerView.initialize(getResources().getString(R.string.api_key),onInitializedListener);

        updateUI(mGoogleApiClient.isConnected());
    }

    @Background
    public void getVideoRating(){
        new YoutubeVideoRatingFetch(this, getApplicationContext(),userEmail, credential).execute(alarm.getType().getURL());

    }

    @UiThread
    public void showRating(String rating){
        currentRating = (TextView)findViewById(R.id.lblRating);
        currentRating.setText(rating);
    }

    @Override
    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    signIn();
                    break;
                case R.id.btnLike:
                    new YoutubeSetVideoRating(this, userEmail, credential).execute(new String[]{"like", alarm.getType().getURL()});
                    showRating("like");
                    break;
                case R.id.btnDislike:
                    new YoutubeSetVideoRating(this, userEmail, credential).execute(new String[]{"dislike", alarm.getType().getURL()});
                    showRating("dislike");
                    break;
                case R.id.btnNeutral:
                    new YoutubeSetVideoRating(this, userEmail, credential).execute(new String[]{"none", alarm.getType().getURL()});
                    showRating("none");
                    break;
            }
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
        showRating(output);
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
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            signIn();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        boolean b = result.isSuccess();

        if (b) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            userEmail = acct.getEmail();

            credential = GoogleAccountCredential.usingOAuth2(this, scopes);
            credential.setSelectedAccountName(userEmail);

            getVideoRating();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean b) {
        if (b) {
            btn_signIn.setVisibility(View.INVISIBLE);
            btn_like.setVisibility(View.VISIBLE);
            btn_dislike.setVisibility(View.VISIBLE);
            btn_neutral.setVisibility(View.VISIBLE);
        } else {
            btn_signIn.setVisibility(View.VISIBLE);
            btn_like.setVisibility(View.INVISIBLE);
            btn_dislike.setVisibility(View.INVISIBLE);
            btn_neutral.setVisibility(View.INVISIBLE);
        }
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

    @Override
    public void processFinish(Intent output) {
        if (output != null) {
            startActivityForResult(output, RC_REAUTHORIZE);
        }
    }
}
