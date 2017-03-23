package com.example.hemendra.movieland;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hemendra.movieland.util.MovieAsyncTask;

public class BackendFragment extends Fragment {
    MovieAsyncTask movieAsyncTask;
    private Activity activity;
    public BackendFragment() {}
     public void beginTask(String...args){
         movieAsyncTask=new MovieAsyncTask(activity);
         movieAsyncTask.execute(args);
     }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity= (Activity) context;
        if(movieAsyncTask!=null){
            movieAsyncTask.onAttach(activity);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(movieAsyncTask!=null){
            movieAsyncTask.onDetach();
        }
    }
}
