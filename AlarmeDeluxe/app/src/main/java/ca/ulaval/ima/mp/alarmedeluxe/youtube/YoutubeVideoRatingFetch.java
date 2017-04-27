package ca.ulaval.ima.mp.alarmedeluxe.youtube;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoGetRatingResponse;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

import ca.ulaval.ima.mp.alarmedeluxe.R;

public class YoutubeVideoRatingFetch extends AsyncTask<String, Void, String> {

    public AsyncYoutubeResponse delegate = null;
    private YouTube youtube;
    private Context context;
    private String appName;
    private GoogleAccountCredential credential;

    @Override
    protected String doInBackground(String... params) {
        youtube = new YouTube.Builder(new NetHttpTransport(),new JacksonFactory(),credential)
                .setApplicationName(appName)

                .build();
        try {
            SystemClock.sleep(6000);
            YouTube.Videos.GetRating request = youtube.videos().getRating(params[0]);
            VideoGetRatingResponse response = request.execute();
            return response.getItems().get(0).getRating();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(s);
    }

    public interface AsyncYoutubeResponse {
        void processFinish(String output);
    }

    public YoutubeVideoRatingFetch(AsyncYoutubeResponse response, Context context,String appName, GoogleAccountCredential credential) {
        delegate = response;
        this.context = context;
        this.appName = appName;
        this.credential = credential;
    }
}
