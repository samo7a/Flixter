package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Headers;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

public class DetailActivity extends YouTubeBaseActivity {
    private static final String YOUTUBE_API = "AIzaSyCuFwQ11JMe4ZWuFmwZwuVgvmKdoGlsFTk";
    private static final String URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView detailTitle;
    TextView overview;
    RatingBar rating;
    YouTubePlayerView youTubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailTitle = findViewById(R.id.detailTitle);
        overview = findViewById(R.id.overview);
        rating = findViewById(R.id.ratingBar);
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        detailTitle.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        rating.setRating((float) movie.getRating());


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray array = json.jsonObject.getJSONArray("results");
                    if (array.length() == 0) return;
                    String ytkey = array.getJSONObject(0).getString("key");
                    initializeYT(ytkey);

                } catch (JSONException j) {
                    j.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });



    }

    private void initializeYT(final String ytkey) {
        youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(YOUTUBE_API,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(ytkey);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}