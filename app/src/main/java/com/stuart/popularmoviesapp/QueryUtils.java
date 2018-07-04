package com.stuart.popularmoviesapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stuart on 09/03/2018.
 */

public final class QueryUtils {

    /**Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * private constructor so no one should ever create {@Link QueryUtils}
     * object. This class holds only static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     *
     */
    private QueryUtils() {
    }


    public static List<Film> fetchFilmData (String requestUrl) {

        //Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        //Extract relevant fields from the JSON
        List<Film> films = extractFeatureFromJson(jsonResponse);

        return films;
    }

    //Returns new URL object from the given String URL
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }

        return url;
    }

    //Make a HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null, then return early
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        //used to connect to the URL
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If the request was successful (response 200),
            //the read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
        } finally {
            if (urlConnection !=null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                /**Closing the input stream could throw an IOException, which is why
                 * the makeHttpRequest(URL url) method signature specifies than an IOException
                 * could be thrown.
                 */
                inputStream.close();
            }
        }

        //Log.e("JSON Response", jsonResponse);
        return jsonResponse;
    }

    /**
     * Converts the {@Link Inputstream} into a String which is the complete
     * JSON response that you have requested. This is the entire stream, you will
     * then use this String and parse this into the relevant object
     * @param inputStream
     */

    private static String readFromStream (InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     *
     * Returns a list from the string @param jsonResponse, this is where the news
     * objects are created and then they are returned as an object
     * @return the Films list
     */

    private static List <Film> extractFeatureFromJson (String filmJSON) {

        //If the JSON string is empty or null, return the String early.
        if (TextUtils.isEmpty(filmJSON)){
            return null;
        }

        //Create an empty ArrayList which will be used to add film objects to array.
        List<Film> filmList = new ArrayList<>();

        /**
         * Use the newsJSON String and parse this into objects. If there is an error
         * in how this formatted, an exception will be thrown. As with any tests such as this,
         * a try/catch statement will catch any errors made in parsing the String
         */

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(filmJSON);

            //Extract the JSONObject from the object called "results"
            JSONArray filmArray = baseJsonResponse.getJSONArray("results");

            //For each film result object in the filmArray, create an {@Link Film} object
            for(int i = 0; i < filmArray.length(); i++) {

                //Get a Film object at position i within the list of films in the JSON list
                JSONObject currentFilmObject = filmArray.getJSONObject(i);

                //Get a Film object, extract the JSONObject associated with key called "id"
                String filmId = currentFilmObject.getString("id");

                //Get a Film object, extract the JSONObject associated with key called "vote_average"
                String voteAverage = currentFilmObject.getString("vote_average");

                //Get a Film object, extract the JSONObject associated with the key called "title"
                String filmTitle = currentFilmObject.getString("title");

                //Get a Film object, extract the JSONObject associated with the key called "poster_path"
                String filmPosterPath = currentFilmObject.getString("poster_path");

                //Get a Film object, extract the JSONObject associated with the key called "overview"
                String filmOverview = currentFilmObject.getString("overview");

                //Get a Film object, extract the JSONObject associated with the key called "poster_path"
                String filmReleaseDate = currentFilmObject.getString("release_date");


                filmList.add(new Film(filmId, voteAverage, filmTitle, filmPosterPath, filmOverview, filmReleaseDate));



            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing the film JSON results", e);
        }

        int filmListArraySize = filmList.size();
        String filmListSize = Integer.toString(filmListArraySize);

        return filmList;
    }

}
