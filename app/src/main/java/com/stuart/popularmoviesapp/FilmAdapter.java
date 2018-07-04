package com.stuart.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Stuart on 28/02/2018.
 */

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.MyViewHolder> {

    private Context mContext;
    private List <Film> mFilmList;

    public FilmAdapter(Context context, List<Film> filmList) {

        mContext = context;
        mFilmList = filmList;
    }

    @Override
    public FilmAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View filmView = inflater.inflate(R.layout.item_view, parent, false);
        FilmAdapter.MyViewHolder viewHolder = new FilmAdapter.MyViewHolder(filmView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Film filmPoster = mFilmList.get(position);
        ImageView imageView = holder.mFilmImageView;

        Glide.with(mContext)
                .load(filmPoster.getmPoster_path())
                .placeholder(R.drawable.ic_cloud_off_red)
                .into(imageView);

    }

    public void clear() {
        final int size = mFilmList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mFilmList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public int getItemCount() {

        return mFilmList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView mFilmImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mFilmImageView = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Film filmObject = mFilmList.get(position);
                Intent intent = new Intent(mContext, SingleFilmActivity.class);
                intent.putExtra(SingleFilmActivity.DETAILED_FILM_DESCRIPTION, filmObject);
                mContext.startActivity(intent);
            }

        }
    }
}