package com.example.android.restful.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.android.restful.model.DataItem;
import com.example.android.restful.utils.HttpHelper;
import com.google.gson.Gson;

import java.io.IOException;

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";
    public static final String MY_SERVICE_EXCEPTION = "myServiceException";
    public static final String USER = "nadias";
    public static final String PASSWORD = "NadiasPassword";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        String response;
        try {
            response = HttpHelper.downloadUrl(uri.toString(), USER, PASSWORD);

        } catch (IOException e) {
            /**Broadcast the exception*/
            e.printStackTrace();
            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(MY_SERVICE_EXCEPTION, e.getMessage());
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            return;
        }

        Gson gson = new Gson();
        /**I'm not passing in an array of data items, I'm simply telling Gson, this is the
         * class I want you to use, to instantiate each time you see a new object.*/
        DataItem[] dataItems = gson.fromJson(response, DataItem[].class);

//        DataItem[] dataItems = MyXMLParser.parseFeed(response);

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

}
