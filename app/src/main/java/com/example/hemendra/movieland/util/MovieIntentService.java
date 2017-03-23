package com.example.hemendra.movieland.util;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hemendra on 3/22/2017.
 */
public class MovieIntentService extends IntentService {
    public static final String TAG = MovieIntentService.class.getName();
    public static final String ACTION_MovieResponse = "com.example.hemendra.movieland.RESPONSE";
    public static final String ACTION_MyUpdate = "com.example.hemendra.movieland.UPDATE";
    public static final String EXTRA_KEY_IN = "EXTRA_IN";
    public static final String EXTRA_KEY_OUT = "EXTRA_OUT";
    public static final String EXTRA_UPDATE = "EXTRA_UPDATE";
    String msgFromActivity;
    String extraOut;
    ArrayList<String> list=new ArrayList<>();


    public MovieIntentService() {
        super(TAG);
    }

    public MovieIntentService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
//Get input from activity
        msgFromActivity = intent.getStringExtra(EXTRA_KEY_IN);
        extraOut = "hello" + msgFromActivity;
        Log.i(TAG,"===================="+extraOut);

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //send update to activity
            Intent intentUpdate = new Intent();
            intent.setAction(ACTION_MyUpdate);
            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(EXTRA_UPDATE, i);
            sendBroadcast(intentUpdate);
        }
        //return response
        Intent intentResponse = new Intent();
        intentResponse.setAction(ACTION_MovieResponse);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(EXTRA_KEY_OUT, extraOut);
        sendBroadcast(intentResponse);
    }
}


