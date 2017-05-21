package com.example.hemendra.movieland.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hemendra.movieland.MovieDetailActivity;
import com.example.hemendra.movieland.R;
import com.example.hemendra.movieland.dao.MovieData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by hemendra on 3/17/2017.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();
    public static final String imageViewTag="imageViewTag";

    private ArrayList<MovieData> moviDataArrayList;
    private Context context;


    public void setData(ArrayList<MovieData> mlist) {

        this.moviDataArrayList = mlist;

    }

    public MoviesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MyViewHolder holder, int position) {
        MovieData movieData = moviDataArrayList.get(position);
        String posterPath = movieData.getPoster_path();
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185//" + posterPath).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return moviDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public MyViewHolder(View rowView) {
            super(rowView);
            imageView = (ImageView) rowView.findViewById(R.id.poster_view);
            imageView.setTag("imageViewTag");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "hello" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("INDEX",getAdapterPosition());
                    intent.putExtra("MOVIE_LIST",moviDataArrayList);
                    context.startActivity(intent);
                }
            });

        }
    }
}
