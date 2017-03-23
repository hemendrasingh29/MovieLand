package com.example.hemendra.movieland.util;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hemendra.movieland.R;
import com.example.hemendra.movieland.dao.MovieData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by hemendra on 3/17/2017.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();
   private ArrayList<MovieData> moviDataArrayList;
    Context context;



    public MoviesAdapter( Context context,ArrayList<MovieData> moviDataList){
        this.moviDataArrayList=moviDataList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view,parent,false);
        return new MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MyViewHolder holder, int position) {
       MovieData movieData=moviDataArrayList.get(position);
        String posterPath=movieData.getPoster_path();
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185//"+posterPath).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return moviDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(View rowView) {
            super(rowView);
            imageView=(ImageView)rowView.findViewById(R.id.poster_view);
        }
    }
}
