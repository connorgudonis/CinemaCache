package com.example.cinemacachefinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class WatchListActivity extends AppCompatActivity {

        private RecyclerView recyclerView;
        private MovieListAdapter movieListAdapter;
        private ArrayList<String> userWatchList;
        private SharedPreferences sharedPreferences;
        public static final String SHARED_PREF_NAME = "USER";


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_watch_list);
            sharedPreferences = getSharedPreferences(
                SHARED_PREF_NAME,
                MODE_PRIVATE);
            userWatchList = getUserWatchlist();
            recyclerView = findViewById(R.id.recycler_watchlist);
            movieListAdapter = new MovieListAdapter(this, getMovieList());
            recyclerView.setAdapter(movieListAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.navigation_top_movies) {
            Intent movieListIntent = new Intent(this, TopMovieListActivity.class);
            startActivity(movieListIntent);
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.navigation_watchlist) {
            Intent movieListIntent = new Intent(this, WatchListActivity.class);
            startActivity(movieListIntent);
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.navigation_reviews){
            Intent reviewIntent = new Intent(this, ReviewListActivity.class);
            startActivity(reviewIntent);
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.navigation_find_movies){
            Intent findMoviesIntent = new Intent(this, MainActivity.class);
            startActivity(findMoviesIntent);
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.navigation_logout){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return true;
        }

        return false;
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return true;
        }


        /**
         * Getting the data from json file
         */
        public ArrayList<Movie> getMovieList() {
            ArrayList<Movie> movieArrayList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(loadMoviesFromJSON());
                JSONArray jsonArray = jsonObject.getJSONArray("movies");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject movieData = jsonArray.getJSONObject(i);
                    String movieTitle = movieData.getString("movieTitle");
                    if (userWatchList.contains(movieTitle)) {
                        String releaseDate = movieData.getString("releaseDate");
                        String director = movieData.getString("director");
                        String description = movieData.getString("description");
                        String rating = movieData.getString("rating");
                        String genre = movieData.getString("genre");
                        String posterFileName = movieData.getString("profileFileName");
                        Resources resources = this.getResources();
                        int resourceId = resources.getIdentifier(posterFileName, "drawable", getPackageName());
                        Movie m = new Movie(movieTitle, releaseDate, director, description, rating, genre, resourceId);
                        movieArrayList.add(m);
                    }
                }
            } catch (
                    JSONException e) {
                e.printStackTrace();
            }
            return movieArrayList;
        }

        public ArrayList<String> getUserWatchlist() {
            ArrayList<String> userWatchListArray = new ArrayList<>();
            String watchlistData = sharedPreferences.getString("WATCHLIST", null);
            String[] watchlistList = watchlistData.split("-break-");
            for (String movieTitle : watchlistList) {
                userWatchListArray.add(movieTitle);
            }
            return userWatchListArray;
        }


        private String loadMoviesFromJSON() {
            String json = null;
            try {
                InputStream inputStream = getAssets().open("Movies.json");
                int fileSize = inputStream.available();
                byte[] bufferData = new byte[fileSize];
                inputStream.read(bufferData);
                json = new String(bufferData, "UTF-8");
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return json;
        }


    }
