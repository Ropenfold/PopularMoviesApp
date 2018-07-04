package com.stuart.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stuart on 01/03/2018.
 */

public class Film implements Parcelable{

    private String mId;
    private String mVoteAverage;
    private String mTitle;
    private String mPoster_path;
    private String mOverview;
    private String mRelease_date;

    private static String IMDB_POSTER_LINK = "https://image.tmdb.org/t/p/w185";


    public Film(String id, String voteAverage, String title, String poster_path, String overview, String release_date) {
        this.mId = id;
        this.mVoteAverage = voteAverage;
        this.mTitle = title;
        this.mPoster_path = poster_path;
        this.mOverview = overview;
        this.mRelease_date = release_date;
    }


    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    protected Film(Parcel in) {

        mId = in.readString();
        mVoteAverage = in.readString();
        mTitle = in.readString();
        mPoster_path = in.readString();
        mOverview = in.readString();
        mRelease_date = in.readString();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int i) {
            return new Film[0];
        }
    };

        public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPoster_path() {

        return IMDB_POSTER_LINK + mPoster_path;
    }

    public void setmPoster_path(String mPoster_path) {this.mPoster_path = mPoster_path;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmRelease_date() {
        return mRelease_date;
    }

    public void setmRelease_date(String mRelease_date) {
        this.mRelease_date = mRelease_date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mVoteAverage);
        parcel.writeString(mTitle);
        parcel.writeString(mPoster_path);
        parcel.writeString(mOverview);
        parcel.writeString(mRelease_date);
    }
}
