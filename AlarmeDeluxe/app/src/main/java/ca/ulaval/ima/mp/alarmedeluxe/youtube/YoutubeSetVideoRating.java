package ca.ulaval.ima.mp.alarmedeluxe.youtube;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

public class YoutubeSetVideoRating extends AsyncTask<String, Void, Intent> {

    private YouTube youtube;
    public AsyncYoutubeResponse delegate = null;
    private String appName;
    private GoogleAccountCredential credential;

    public interface AsyncYoutubeResponse {
        void processFinish(Intent output);
    }

    public YoutubeSetVideoRating(AsyncYoutubeResponse response, String appName, GoogleAccountCredential credential) {
        delegate = response;
        this.appName = appName;
        this.credential = credential;
    }

    @Override
    protected Intent doInBackground(String... params) {
        try {
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
            .setApplicationName(appName)

            //.setGoogleClientRequestInitializer(new YouTubeRequestInitializer(getResources().getString(R.string.server_client_id)))
            .build();

            youtube.videos().rate(params[1], params[0]).execute();
            Log.e("YOP", "TEST");
            return null;
        } catch (UserRecoverableAuthIOException ex) {
            return ex.getIntent();
        } catch (Exception ex) {
            Log.e("EXCEPTION", ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Intent b) {
        delegate.processFinish(b);
    }
}