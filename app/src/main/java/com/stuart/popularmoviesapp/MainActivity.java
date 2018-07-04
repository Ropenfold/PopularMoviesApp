package com.stuart.popularmoviesapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Film>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static String MOST_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=ad0550bad192b8c963c53f60ccc01331&language=en-US&page=1";

    private String mMovieAPIFeed = "";

    //Adapter for list of movies
    private FilmAdapter mFilmAdapter;

    private RecyclerView mRecyclerView;

    //constant value for the film Loader
    private static final int FILM_LOADER_ID = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_images);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        setSharePreferences();
        Log.i("movie list feed", mMovieAPIFeed);
        loaderManagerStartup();

    }

    public void loaderManagerStartup(){

        /**
         *  When the device is is rotated the view data is lost. This is because when the activity is created
         *  or recreated), the activity does not know there is a loader running.
         *  An initLoader()method is needed on onCreate() of MainActivity to connect to the Loader.
         */

        if(getLoaderManager().getLoader(FILM_LOADER_ID) != null) {
            getLoaderManager().initLoader(FILM_LOADER_ID, null, this);
        }

        //Get reference to the ConnectivityManager to check the state of network activity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            //Get a reference to the LoaderManager, in order to interact with Loaders.
            LoaderManager loaderManager = getLoaderManager();

            /**
             * Initialize the loader. Pass in the int ID constant defined above and pass in null for
             * the bundle. Pass in this activityfor the LoaderCallbacks parameter.
             */
            loaderManager.initLoader(FILM_LOADER_ID, null, this);
        }

    }

    public void setmMovieAPIFeed(String mMovieAPIFeed){

        if (mMovieAPIFeed.equals("Highest Rated")) {
            Intent favouritesActivity = new Intent(this, HighestRated.class);
            startActivity(favouritesActivity);

        }

        if(mMovieAPIFeed.equals("Now Showing")) {
            Intent favouritesActivity = new Intent(this, NowShowing.class);
            startActivity(favouritesActivity);

        }

    }


    @Override
    public Loader<List<Film>> onCreateLoader(int i, Bundle bundle) {
        return new FilmListLoader(this, MOST_POPULAR_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Film>> loader, List<Film> filmList) {

        mFilmAdapter = new FilmAdapter(this, filmList);
        // Clear the adapter of previous film data
        //mFilmAdapter.clear();

        //If there is a valid list of {@link Films}, then add them to the
        //the adapter's data set. This will trigger the ListView to update.
         if(filmList != null && !filmList.isEmpty()){
             mRecyclerView.setAdapter(mFilmAdapter);

        } else {
            //No news items found
            Toast.makeText(this, "No adapter Loaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Film>> loader) {
        //Loader reset, so we can clear out our existing data
        mFilmAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.visualizer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void setSharePreferences () {

        SharedPreferences sharePreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadMoviesFromPreference(sharePreferences);

    }

    public void loadMoviesFromPreference(SharedPreferences sharedPreferences){
        mMovieAPIFeed = sharedPreferences.getString(getString(R.string.movie_key),
                getString(R.string.pref_movie_now_showing));
            setmMovieAPIFeed(mMovieAPIFeed);
    }



    @Override
    protected void onResume() {
        setSharePreferences();
        Log.i("onResume sharedPref", mMovieAPIFeed);
        super.onResume();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.movie_key))) {
            mMovieAPIFeed = sharedPreferences.getString(getString(R.string.movie_key),
                    getString(R.string.pref_movie_now_showing));
            Log.i("sharedpreferencechanged", mMovieAPIFeed);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
