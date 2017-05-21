package com.example.hemendra.movieland.util;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hemendra.movieland.dao.MovieData;
import com.example.hemendra.movieland.data.MovieContract;
import com.example.hemendra.movieland.data.MovieDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MovieIntentService extends IntentService {
    private static final String TAG = MovieIntentService.class.getSimpleName();
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_RESPONSE = "com.example.hemendra.movieland.util.action.RESPONSE";
    public static final String ACTION_UPDATE = "com.example.hemendra.movieland.util.action.UPDATE";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM_IN = "com.example.hemendra.movieland.util.extra.PARAM";
    public static final String EXTRA_OUT_RESPONSE = "com.example.hemendra.movieland.util.extra.EXTRA_OUT_RESPONSE";
    public static final String EXTRA_OUT_UPDATE = "com.example.hemendra.movieland.util.extra.EXTRA_OUT_UPDATE";
    SQLiteDatabase db;
    Context context;
    private MovieData movieData;
    private String movieCat;
    private ArrayList<MovieData> movieLPopular = new ArrayList<>();
    private ArrayList<MovieData> movieLtoprated = new ArrayList<>();
    private ArrayList<MovieData> movieLupcoming = new ArrayList<>();
    private ArrayList<MovieData> movieLnoweplaying = new ArrayList<>();
    private Map<String, ArrayList<MovieData>> moviListmap = new HashMap<>();
    public static final String POPULAR_MOVIE = "POPULAR_MOVIE";
    public static final String TOPRATED_MOVIE = "TOPRATED_MOVIE";
    public static final String UPCOMING_MOVIE = "UPCOMING_MOVIE";
    public static final String NOWPLAYING_MOVIE = "NOWPLAYING_MOVIE";
    //json constants
    private static final String RESULTS = "results";
    private static final String PAGE = "page";
    private static final String COLUMN_NAME_MOVIE_CATEGORY = "movieCategory";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVER_VIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String MOVIE_ID = "id";
    private static final String TITLE = "title";
    private static final String VOTE_AVERAGE = "vote_average";


    public MovieIntentService() {
        super("MovieIntentService");
    }


    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionUpdate(Context context, String... param) {
        Intent intent = new Intent(context, MovieIntentService.class);
        intent.setAction(ACTION_UPDATE);
        intent.putExtra(EXTRA_PARAM_IN, param);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        MovieDbHelper movieDbHelper = new MovieDbHelper(getApplicationContext());
        db = movieDbHelper.getWritableDatabase();
       // createAdapter();
        if (intent != null) {
            String[] param = intent.getStringArrayExtra(EXTRA_PARAM_IN);
            handleActionUpdate(param);
        }
    }

    //handle progress bar update
    private void handleActionUpdate(String... param) {
        int i = 0;
        String update = "";
        Intent intentUpdate = new Intent();
        intentUpdate.setAction(ACTION_UPDATE);
        for (i = 0; i < param.length; i++) {
            movieCat = param[i];
            update = FetchData.fetch(movieCat);
            addtoDb(update);
            try {
                intentUpdate.putExtra(EXTRA_OUT_UPDATE, i);
                sendBroadcast(intentUpdate);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    void addtoDb(String jsonresponse) {
        long row = 0;

        try {
            JSONObject jMovieObj = new JSONObject(jsonresponse);
            JSONArray results = jMovieObj.getJSONArray(RESULTS);
            for (int i = 0; i < results.length(); i++) {
                ContentValues contentValues = new ContentValues();
                JSONObject oneMovie = results.getJSONObject(i);
                contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_MOVIE_CATEGORY, movieCat);
                contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH, oneMovie.getString(POSTER_PATH));
                contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, oneMovie.getString(OVER_VIEW));
                contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, oneMovie.getString(RELEASE_DATE));
                contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_ID, oneMovie.getString(MOVIE_ID));
                contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, oneMovie.getString(TITLE));
                contentValues.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, oneMovie.getString(VOTE_AVERAGE));

                String selection = MovieContract.MovieEntry.COLUMN_NAME_MOVIE_CATEGORY + "=?" + " AND " + MovieContract.MovieEntry.COLUMN_NAME_ID + "=?";

                String moviid = (String) contentValues.get(MovieContract.MovieEntry.COLUMN_NAME_ID);
                String[] projection = {MovieContract.MovieEntry.COLUMN_NAME_ID};
                String[] selectionArgs = new String[]{moviid, movieCat};
                row = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }
            if (row == -1)

//                 row=db.update(MovieEntry.TABLE_NAME, null, selection, selectionArgs);
                Log.i(TAG, "hhhhhhhhhhhhhhhh row inserted " + row);
//               row=db.insert(MovieEntry.TABLE_NAME,null,contentValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}