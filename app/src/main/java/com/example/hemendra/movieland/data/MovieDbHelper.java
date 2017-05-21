package com.example.hemendra.movieland.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hemendra.movieland.data.MovieContract.MovieEntry;
/**
 * Created by hemendra on 3/18/2017.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MovieEntry.db";

    private static final String SQL_MOVIE_TABLE =
            "CREATE TABLE "+MovieEntry.TABLE_NAME+" (" +
                    MovieEntry._ID + " INTEGER," +
                    MovieEntry.COLUMN_NAME_MOVIE_CATEGORY + " TEXT,"+
                    MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT," +
                    MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT," +
                    MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT," +
                    MovieEntry.COLUMN_NAME_ID + " TEXT," +
                    MovieEntry.COLUMN_NAME_TITLE + " TEXT," +
                    MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " TEXT,"+
                     "PRIMARY KEY ("+ MovieEntry.COLUMN_NAME_MOVIE_CATEGORY +","+
                       MovieEntry.COLUMN_NAME_ID +"))";

    private static final String SQL_DELETE_MOVIE =
            "DROP TABLE ID EXISTS" + MovieEntry.TABLE_NAME;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_MOVIE_TABLE);

    }

        @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_MOVIE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
