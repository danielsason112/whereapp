package com.afeka.whereapp;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.afeka.whereapp.data.Message;
import com.afeka.whereapp.logic.DataService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;
    private static final String TAG = "FMService";

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> data = remoteMessage.getData();

            // Broadcast data to ChatActivity
            Intent intent = new Intent("MyData");
            intent.putExtra("msg", data.get("msg"));
            intent.putExtra("sender", data.get("sender"));
            intent.putExtra("group", data.get("group"));
            broadcaster.sendBroadcast(intent);

            DataService dataService = new DataService(getApplicationContext());

            Message msg = new Message(UUID.randomUUID().toString(), data.get("msg"),
                    data.get("sender"), data.get("group"), new Date().getTime());
            dataService.saveMessage(msg);


        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }


}
