package ca.ulaval.ima.mp.alarmedeluxe.customization;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.VideoGetRatingResponse;
import com.google.common.collect.Lists;


import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;

import static com.google.android.gms.internal.zzs.TAG;

public class YoutubeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private SignInButton signInButton;
    private Button signOutButton;
    int RC_SIGN_IN =42;
    private YouTube youTube;
    private String url = "a4NT5iBFuZs";
    private TextView currentRating;
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

         mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Set the dimensions of the sign-in button.
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);
        currentRating = (TextView)view.findViewById(R.id.lblRating);
        signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
        signOutButton = (Button) view.findViewById(R.id.sign_out_button);
        mStatusTextView = (TextView) view.findViewById(R.id.connection_status);
        return view;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);

            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }
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

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            // ...
        }
    }
}
