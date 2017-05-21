package com.example.hemendra.movieland.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hemendra on 3/18/2017.
 */
public final class MovieContract {
    //base uri constants
    public static final String SCHEME="content://";
    public static final String AUTHORITY= "com.example.hemendra.movieland";
    public static final Uri BASE_CONTENT_URI=Uri.parse(SCHEME+AUTHORITY);
    public static final String PATH_MOVIE="movieTable";



    private MovieContract() {
    }
    public static class MovieEntry implements BaseColumns {
        //constants foor table and column
        public static final Uri CONTENT_URI= BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
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

