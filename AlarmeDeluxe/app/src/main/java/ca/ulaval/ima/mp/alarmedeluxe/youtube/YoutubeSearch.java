package ca.ulaval.ima.mp.alarmedeluxe.youtube;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class YoutubeSearch extends AsyncTask<String, Void, List<SearchResult>> {

    private YouTube youtube;
    public static final long MAX_RESULTS = 10;
    public AsyncYoutubeResponse delegate = null;

    public interface AsyncYoutubeResponse {
        void processFinish(List<SearchResult> output);
    }

    public YoutubeSearch(AsyncYoutubeResponse response) {
        delegate = response;
    }

    @Override
    protected List<SearchResult> doInBackground(String... params) {
        try {
            Properties properties = new Properties();
            String apiKey = properties.getProperty("youtube.apikey");

            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-geolocationsearch-sample").build();

            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setKey("AIzaSyDXI4UtteomaHKSJQqFLVNYXtuvytlZmT0");
            search.setQ(params[0]);
            search.setType("video");
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(MAX_RESULTS);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();

            return searchResultList;
        } catch (Exception ex) {
            Log.e("EXCEPTION", ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<SearchResult> list) {
        delegate.processFinish(list);
    }
}
