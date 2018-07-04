package com.stuart.popularmoviesapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Stuart on 09/03/2018.
 */

public class FilmListLoader extends AsyncTaskLoader<List<Film>> {

    private String mUrl;

    public FilmListLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    @Override
    public List<Film> loadInBackground() {
        if (mUrl == null){
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Film> films = QueryUtils.fetchFilmData(mUrl);
        return films;
    }
}
