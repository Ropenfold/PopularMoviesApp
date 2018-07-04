package com.stuart.popularmoviesapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class SingleFilmActivity extends AppCompatActivity {

    public static final String DETAILED_FILM_DESCRIPTION = "SingleFilmActivity.DETAILED_FILM_DESCRIPTION";

    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mFilmDescription;
    private TextView mFilmAverage;
    private TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_film);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageView = (ImageView) findViewById(R.id.image);
        mTitleView = (TextView) findViewById(R.id.film_title);
        mFilmDescription = (TextView) findViewById(R.id.film_description);
        mFilmAverage = (TextView) findViewById(R.id.film_review_score);
        mReleaseDate = (TextView) findViewById(R.id.film_release_date);
        Film film = getIntent().getParcelableExtra(DETAILED_FILM_DESCRIPTION);

        String filmTitle = film.getmTitle();
        mTitleView.setText(filmTitle);

        String filmReleaseDate = film.getmRelease_date();
        mReleaseDate.setText(filmReleaseDate);

        String filmDescription = film.getmOverview();
        mFilmDescription.setText(filmDescription);

        String filmAverageScore = film.getmVoteAverage();
        mFilmAverage.setText(filmAverageScore);




        Glide.with(this)
                .load(film.getmPoster_path())
                .asBitmap()
                .error(R.drawable.ic_cloud_off_red)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean b, boolean b1) {

                            onPalette(Palette.from(resource).generate());
                            mImageView.setImageBitmap(resource);

                        return false;
                    }

                    public void onPalette (Palette palette) {
                        if (null != palette) {
                            ViewGroup parent = (ViewGroup) mImageView.getParent().getParent();
                            parent.setBackgroundColor(palette.getLightMutedColor(Color.GRAY));
                        }
                    }


                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
