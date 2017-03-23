package com.example.hemendra.movieland;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hemendra.movieland.util.MovieIntentService;

import com.example.hemendra.movieland.dao.MovieData;
import com.example.hemendra.movieland.database.MovieContract.MovieEntry;
import com.example.hemendra.movieland.database.MovieDbHelper;
import com.example.hemendra.movieland.util.MovieIntentService;
import com.example.hemendra.movieland.util.MoviesAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BackendFragment fragment;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewpopular, recyclerViewtoprated,
            recyclerViewupcomming, recyclerViewnowplaying;
    private ArrayList<MovieData> movieLPopular = new ArrayList<>();
    private ArrayList<MovieData> movieLtoprated = new ArrayList<>();
    private ArrayList<MovieData>  movieLupcoming= new ArrayList<>();
    private ArrayList<MovieData> movieLnoweplaying = new ArrayList<>();
    private SQLiteDatabase db;
    MovieDbHelper movieDbHelper = new MovieDbHelper(this);
    private MyBroadcastReceiver myBroadcastReciever;
    private MyBroadcastUpdateReceiver myBroadcastUpdateRecieve;

    String msgToIntentService = "Android-er";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       db = movieDbHelper.getReadableDatabase();
        createAdapter();
        recyclerViewpopular = (RecyclerView) findViewById(R.id.recycler_view_popular);
        recyclerViewtoprated = (RecyclerView) findViewById(R.id.recycler_view_top_rated);
        recyclerViewupcomming= (RecyclerView) findViewById(R.id.recycler_view_up_coming);
        recyclerViewnowplaying= (RecyclerView) findViewById(R.id.recycler_view_nowplaying);

        LinearLayoutManager layoutManagerPopular
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerToprated
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerUpcoming
                = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManagerNowPlaying
                = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewpopular.setLayoutManager(layoutManagerPopular);
        recyclerViewtoprated.setLayoutManager(layoutManagerToprated);
        recyclerViewupcomming.setLayoutManager(layoutManagerUpcoming);
        recyclerViewnowplaying.setLayoutManager(layoutManagerNowPlaying);
        MoviesAdapter movieAdapterpopular = new MoviesAdapter(this, movieLPopular);
        MoviesAdapter movieAdapterToprated=new MoviesAdapter(this,movieLtoprated);
        MoviesAdapter moviesAdapterUpcoming=new MoviesAdapter(this,movieLupcoming);
        MoviesAdapter moviesAdapterNowPlaying=new MoviesAdapter(this,movieLnoweplaying);
        recyclerViewpopular.setAdapter(movieAdapterpopular);
        recyclerViewtoprated.setAdapter(movieAdapterToprated);
        recyclerViewupcomming.setAdapter(moviesAdapterUpcoming);
        recyclerViewnowplaying.setAdapter(moviesAdapterNowPlaying);
        movieAdapterpopular.notifyDataSetChanged();
        movieAdapterToprated.notifyDataSetChanged();
        moviesAdapterUpcoming.notifyDataSetChanged();
        moviesAdapterNowPlaying.notifyDataSetChanged();

        myBroadcastReciever =new MyBroadcastReceiver();
        myBroadcastUpdateRecieve =new MyBroadcastUpdateReceiver();
         //register broadcast reciever
        IntentFilter intentFilterResponse=new IntentFilter(MovieIntentService.ACTION_MovieResponse);
        intentFilterResponse.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(myBroadcastReciever,intentFilterResponse);
        IntentFilter intentFilterUpadte=new IntentFilter(MovieIntentService.ACTION_MyUpdate);
        intentFilterUpadte.addCategory(Intent.CATEGORY_DEFAULT) ;
        registerReceiver(myBroadcastUpdateRecieve,intentFilterUpadte);
        if (savedInstanceState == null) {
            fragment = new BackendFragment();
            getSupportFragmentManager().beginTransaction().add(fragment, "BackendFragment").commit();

        } else {
            fragment = (BackendFragment) getSupportFragmentManager().findFragmentByTag("BackendFragment");

        }
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);
        if (fragment != null) {
            if (fragment.movieAsyncTask != null && fragment.movieAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregister broadcast receiver
        unregisterReceiver(myBroadcastReciever);
        unregisterReceiver(myBroadcastUpdateRecieve);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            Intent startIntentService=new Intent(this,MovieIntentService.class);
//            startIntentService.putExtra(MovieIntentService.EXTRA_KEY_IN, msgToIntentService);
//            this.startService(startIntentService);

            fragment.beginTask("popular","top_rated","now_playing","upcoming");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showProgressBarBeforeDownloading() {
        if (fragment.movieAsyncTask != null) {
            if (progressBar.getVisibility() != View.VISIBLE && progressBar.getProgress() != progressBar.getMax()) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideProgressBarAfterDownloading() {
        if (fragment.movieAsyncTask != null) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    public void updateProgress(int progress) {
        progressBar.setProgress(progress);
    }

    void createAdapter() {

        String sort = MovieEntry.COLUMN_NAME_ID + " DESC";

        Cursor cursor = db.query(MovieEntry.TABLE_NAME, null, null, null, null, null, sort);
        while (cursor.moveToNext()) {
            MovieData movieData = new MovieData();
            movieData.setMovieCategory(cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_MOVIE_CATEGORY)));
            movieData.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_POSTER_PATH)));
            movieData.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_OVERVIEW)));
            movieData.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_RELEASE_DATE)));
            movieData.setId(cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_ID)));
            movieData.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_TITLE)));
            movieData.setVote_average(cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_NAME_VOTE_AVERAGE)));
            String category=movieData.getMovieCategory();
            switch (category) {
                case "popular":
                    movieLPopular.add(movieData);
                        break;
                case "top_rated":
                    movieLtoprated.add(movieData);
                    break;
                case "now_playing":
                    movieLnoweplaying.add(movieData);
                case "upcoming":
                    movieLupcoming.add(movieData);
                    default: break;
            }

        }

        cursor.close();
    }
public class MyBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        String msg=intent.getStringExtra(MovieIntentService.EXTRA_KEY_OUT);
        Toast.makeText(getApplicationContext(),"hoello",Toast.LENGTH_SHORT).show();
        Log.i(TAG,"hhhh================hhh"+msg);

    }
}

    public class MyBroadcastUpdateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int updateprog=intent.getIntExtra(MovieIntentService.EXTRA_UPDATE,0);
            progressBar.setProgress(updateprog);
        }
    }

}
