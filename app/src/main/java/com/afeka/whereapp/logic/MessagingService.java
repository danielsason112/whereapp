package com.afeka.whereapp.logic;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.afeka.whereapp.dao.OnResponse;
import com.afeka.whereapp.dao.UserDao;
import com.afeka.whereapp.dao.firebase.FirebaseUserDao;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MessagingService {

    private final String TAG = "messaging_service";
    private final String BASE_URL = "https://us-central1-whereapp-298c2.cloudfunctions.net/sendMessage";

    private final FirebaseMessaging firebaseMessagingInstance;
    private UserDao userDao;

    public MessagingService() {
        firebaseMessagingInstance = FirebaseMessaging.getInstance();
        userDao = new FirebaseUserDao();
    }

    public void getToken(final String userId) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        final String token = task.getResult().getToken();
                        Log.d(TAG, "Token: " + token);

                        userDao.getTokenById(userId, new OnResponse<DataSnapshot>() {
                            @Override
                            public void onData(DataSnapshot data) {
                                // No User Token
                                if (data.getValue() == null) {
                                    userDao.updateTokenById(userId, token);
                                } else {
                                    // Different token. Resubscribing to topics
                                    String oldToken = data.getValue(String.class);
                                    if (!(oldToken.equals(token))) {
                                        userDao.updateTokenById(userId, token);
                                    }
                                }
                            }

                            @Override
                            public void onError(String msg) {
                                Log.d(TAG, msg);
                            }
                        });
                    }
                });
    }

    public void subscribeToGroup(final String userId, final String groupId) {
        firebaseMessagingInstance.subscribeToTopic(groupId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscribed!";
                        if (!task.isSuccessful()) {
                            msg = "error subscribing";
                            Log.d("subscribe", msg);
                            return;
                        }
                        Log.d("subscribe", msg);

                        userDao.addGroupById(userId, groupId);
                    }
                });
    }

    public void sendMessage(Context context, String msg, String userName, String groupId) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String query = "";
        try {
            query = "?msg=" + URLEncoder.encode(msg, "UTF-8")
                    + "&sender=" + URLEncoder.encode(userName, "UTF-8")
                    + "&group=" + URLEncoder.encode(groupId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = BASE_URL + query;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "response error: " + error.getMessage());
            }
        });

        queue.add(stringRequest);
    }
}
