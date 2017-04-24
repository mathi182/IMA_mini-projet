package ca.ulaval.ima.mp.alarmedeluxe.youtube;

import android.content.Context;
import android.os.AsyncTask;

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

    @Override
    protected String doInBackground(String... params) {
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
        //Credential credential = YoutubeAuth.authorize(scopes, "getrating");
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        })
                .setApplicationName(context.getString(R.string.app_name))

                //.setGoogleClientRequestInitializer(new YouTubeRequestInitializer(getResources().getString(R.string.server_client_id)))
                .build();
        try {

            YouTube.Videos.GetRating request = youtube.videos().getRating(params[0]);
            request.setKey("lDMenZDhmxb385ddSx_rWDAE");
            request.setOauthToken("");
            request.setId("752816531302-jmo22jf1v826ta5ei8lvuf7gv44kic29.apps.googleusercontent.com");
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

    public YoutubeVideoRatingFetch(AsyncYoutubeResponse response, Context context) {
        delegate = response;
        this.context = context;
    }
}
