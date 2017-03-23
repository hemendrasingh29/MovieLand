package com.example.hemendra.movieland.util;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hemendra.movieland.MainActivity;
import com.example.hemendra.movieland.dao.MovieData;
import com.example.hemendra.movieland.database.MovieDbHelper;
import com.example.hemendra.movieland.database.MovieContract.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hemendra on 3/17/2017.
 */
public class MovieAsyncTask extends AsyncTask<String, Integer, ArrayList<MovieData>> {
    private static final String TAG = MovieAsyncTask.class.getSimpleName();
    private int contentLength = -1;
    private int counter = 0;
    private int calculateProgress = 0;
    private Activity activity;
    private ArrayList<MovieData> mDataList = new ArrayList<MovieData>();
    SQLiteDatabase db;
    private String movieCat;
    private ArrayList<MovieData> mlist;

    public MovieAsyncTask(Activity activity) {


        onAttach(activity);
    }

    private static final String PAGE = "page";
    private static final String RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVER_VIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String MOVIE_ID = "id";
    private static final String TITLE = "title";
    private static final String VOTE_AVERAGE = "vote_average";

    ArrayList<MovieData> result(String jsonData) {

        long row = 0;

        try {
            JSONObject jMovieObj = new JSONObject(jsonData);
            JSONArray results = jMovieObj.getJSONArray(RESULTS);
            for (int i = 0; i < results.length(); i++) {
                ContentValues contentValues = new ContentValues();

                JSONObject oneMovie = results.getJSONObject(i);
                contentValues.put(MovieEntry.COLUMN_NAME_MOVIE_CATEGORY, movieCat);
                contentValues.put(MovieEntry.COLUMN_NAME_POSTER_PATH, oneMovie.getString(POSTER_PATH));
                contentValues.put(MovieEntry.COLUMN_NAME_OVERVIEW, oneMovie.getString(OVER_VIEW));
                contentValues.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, oneMovie.getString(RELEASE_DATE));
                contentValues.put(MovieEntry.COLUMN_NAME_ID, oneMovie.getString(MOVIE_ID));
                contentValues.put(MovieEntry.COLUMN_NAME_TITLE, oneMovie.getString(TITLE));
                contentValues.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, oneMovie.getString(VOTE_AVERAGE));


                String[] projection = {MovieEntry.COLUMN_NAME_ID};
                String selection = MovieEntry.COLUMN_NAME_MOVIE_CATEGORY + "=?" + " AND " + MovieEntry.COLUMN_NAME_ID + "=?";
                String moviid = (String) contentValues.get(MovieEntry.COLUMN_NAME_ID);
                String[] selectionArgs = new String[]{moviid, movieCat};
                row = db.insertWithOnConflict(MovieEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
//                if(row==-1)
//                    row=db.update(MovieEntry.TABLE_NAME, null, selection, selectionArgs);
//
//               row=db.insert(MovieEntry.TABLE_NAME,null,contentValues);

            }
            Log.i(TAG, "hhhhhhhhhhhhhhhh row inserted " + row);

        } catch (JSONException e) {
            e.printStackTrace();


        }
        return mDataList;
    }

    public void onAttach(Activity activity) {
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        if (activity != null) {
            ((MainActivity) activity).showProgressBarBeforeDownloading();
            MovieDbHelper movieDbHelper = new MovieDbHelper(activity.getApplicationContext());
            db = movieDbHelper.getWritableDatabase();
            db = movieDbHelper.getReadableDatabase();
        }
    }

    @Override
    protected ArrayList<MovieData> doInBackground(String... params) {
        for(int i=0;i<params.length;i++) {
            movieCat = params[i];
            publishProgress(counter);
            mlist=result(FetchData.fetch(movieCat));
        }
        return mlist;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        if (activity != null) {
            calculateProgress = (int) (((double) values[0] / contentLength) * 100);
            ((MainActivity) activity).updateProgress(calculateProgress);
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieData> result) {
        if (activity != null) {
            ((MainActivity) activity).hideProgressBarAfterDownloading();
        }
    }

    public void onDetach() {
        activity = null;
    }
}



