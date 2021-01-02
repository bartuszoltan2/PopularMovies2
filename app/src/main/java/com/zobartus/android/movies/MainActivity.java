package com.zobartus.android.movies;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.zobartus.android.movies.database.AppDatabase;
import com.zobartus.android.movies.model.Movie;
import com.zobartus.android.movies.utils.Constants;
import com.zobartus.android.movies.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AppDatabase mDb;
    private int selectedItem;
    private MenuItem menuItem;
    private Movie[] movies;
    private ImageAdapter mImageAdapter;
    private Parcelable mListState;

    @SuppressLint({"WrongConstant", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);

        mLayoutManager = new GridLayoutManager(this, Constants.GRID_NUM_OF_COLUMNS);
        mRecyclerView.getRecycledViewPool().clear();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt("OPTION");
        }
        new FetchDataAsyncTask().execute(Constants.POPULAR_QUERY_PARAM);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("OPTION", selectedItem);
        // Save list state
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("LIST_STATE_KEY", mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        selectedItem = outState.getInt("OPTION");

        // Retrieve list state and list/item positions
        if (outState != null)
            mListState = outState.getParcelable("LIST_STATE_KEY");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preference_menu, menu);
        switch (selectedItem) {
            case R.id.popular_setting:
                menuItem = menu.findItem(R.id.popular_setting);
                menuItem.setChecked(true);
                break;

            case R.id.top_rated_setting:
                menuItem = menu.findItem(R.id.top_rated_setting);
                menuItem.setChecked(true);
                break;

            case R.id.favorite_movie_setting:
                menuItem = menu.findItem(R.id.popular_setting);
                menuItem.setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular_setting) {
            selectedItem = id;
            item.setVisible(true);
            new FetchDataAsyncTask().execute(Constants.POPULAR_QUERY_PARAM);
            return true;
        }
        if (id == R.id.top_rated_setting) {
            selectedItem = id;
            item.setVisible(true);
            new FetchDataAsyncTask().execute(Constants.TOP_RATED_QUERY_PARAM);
            return true;
        }
        if (id == R.id.favorite_movie_setting) {
            selectedItem = id;
            item.setVisible(true);
            setUpViewModel(); // Favorite Movies
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, (Movie[] movies1) -> {
            mImageAdapter.notifyDataSetChanged();
            mImageAdapter.setMovies(movies1);
        });
    }

    public Movie[] makeMoviesDataToArray(String moviesJsonResults) throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonResults);
        JSONArray resultsArray = moviesJson.getJSONArray(Constants.RESULTS_QUERY_PARAM);

        movies = new Movie[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {
            movies[i] = new Movie();

            JSONObject movieInfo = resultsArray.getJSONObject(i);

            movies[i].setOriginalTitle(movieInfo.getString(Constants.ORIGINAL_TITLE_QUERY_PARAM));
            movies[i].setPosterPath(Constants.MOVIE_DB_IMAGE_BASE_URL + movieInfo.getString(Constants.POSTER_PATH_QUERY_PARAM));
            movies[i].setOverview(movieInfo.getString(Constants.OVERVIEW_QUERY_PARAM));
            movies[i].setVoterAverage(movieInfo.getDouble(Constants.VOTER_AVERAGE_QUERY_PARAM));
            movies[i].setReleaseDate(movieInfo.getString(Constants.RELEASE_DATE_QUERY_PARAM));
            movies[i].setMovieId(movieInfo.getInt(Constants.MOVIE_ID_QUERY_PARAM));
        }
        return movies;
    }

    public class FetchDataAsyncTask extends AsyncTask<String, Void, Movie[]> {
        public FetchDataAsyncTask() {
            super();
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            String movieSearchResults;

            try {
                URL url = JsonUtils.buildUrl(params);
                movieSearchResults = JsonUtils.getResponseFromHttpUrl(url);

                if (movieSearchResults == null) {
                    return null;
                }
                return makeMoviesDataToArray(movieSearchResults);
            } catch (IOException e) {
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Movie[] movies) {
            mImageAdapter = new ImageAdapter(getApplicationContext(), movies);
            mRecyclerView.setAdapter(mImageAdapter);
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec(Constants.INTERNET_CHECK_COMMAND);
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}