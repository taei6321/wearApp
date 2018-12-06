package com.stonypark.util.weatheri.services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.stonypark.util.weatheri.data.NowWeather;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;

// app에서 data 받을 Listener
public class ListenerService extends WearableListenerService {

    private String MESSAGE_RECEIVED_PATH = "/weari";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals(MESSAGE_RECEIVED_PATH)) {
            final String message = new String(messageEvent.getData());
            Log.v("myTag", "Message path received on watch is: " + messageEvent.getPath());

            // Broadcast message to wearable activity for display
            Intent messageIntent = new Intent();
            messageIntent.setAction("message-forwarded-from-data-layer");
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);

        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }
}
