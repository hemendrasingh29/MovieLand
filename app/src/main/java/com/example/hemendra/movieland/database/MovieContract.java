package com.example.hemendra.movieland.database;

import android.content.Context;
import android.provider.BaseColumns;

import java.lang.ref.SoftReference;

/**
 * Created by hemendra on 3/18/2017.
 */
public final class MovieContract {

    private MovieContract() {
    }

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movieTable";
        public static final String COLUMN_NAME_MOVIE_CATEGORY = "movieCategory";
        public static final String COLUMN_NAME_POSTER_PATH = "posterPath";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_NAME_ID = "movieId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "voteAverage";

    }
}

