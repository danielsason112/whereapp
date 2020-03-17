package com.afeka.whereapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.afeka.whereapp.data.Message;
import com.afeka.whereapp.logic.DataService;
import com.afeka.whereapp.logic.MessagingService;

import java.lang.ref.WeakReference;
import java.util.List;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatActivity extends AppCompatActivity {

    public final String GROUP_ID_EXTRA = "group_id";
    public final String USER_NAME_EXTRA = "user_name";

    private ChatView chatView;
    private String groupId;
    private String userName;

    private MessagingService messagingService;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getExtras().get("group").equals(groupId)) {
                ChatMessage.Type type;
                String text = "";
                if (intent.getExtras().get("sender").equals(userName)) {
                    type = ChatMessage.Type.SENT;
                } else {
                    type = ChatMessage.Type.RECEIVED;
                    text += intent.getExtras().get("sender") + ": ";
                }
                text += intent.getExtras().get("msg");
                ChatMessage chatMessage = new ChatMessage(text, System.currentTimeMillis(), type);
                chatView.addMessage(chatMessage);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        groupId = (String) getIntent().getExtras().get(GROUP_ID_EXTRA);
        userName = (String) getIntent().getExtras().get(USER_NAME_EXTRA);

        messagingService = new MessagingService();

        final Context context = this;
        chatView = findViewById(R.id.chat_view);
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                messagingService.sendMessage(context, chatMessage.getMessage(), userName, groupId);
                return false;
            }
        });

        loadLocalMessages task = new loadLocalMessages(this, userName, groupId, chatView);
        task.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData"));
    }

    private static class loadLocalMessages extends AsyncTask<Void, Void, List<Message>> {

        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private String userName;
        private ChatView chatView;
        private String groupId;
        private DataService dataService;

        public loadLocalMessages(Activity activity, String userName, String groupId, ChatView chatView) {
            weakActivity = new WeakReference<>(activity);
            this.dataService = new DataService(activity);
            this.userName = userName;
            this.chatView = chatView;
            this.groupId = groupId;
        }

        @Override
        protected List<Message> doInBackground(Void... params) {
            return dataService.getAllMessagesByGroupId(groupId);
        }
        private ChatMessage getChatMessage(String msg, String sender, long timestamp) {
            ChatMessage.Type type;
            String text = "";
            if (sender.equals(userName)) {
                type = ChatMessage.Type.SENT;
            } else {
                type = ChatMessage.Type.RECEIVED;
                text += sender + ": ";
            }
            text += msg;
            return new ChatMessage(text, timestamp, type);
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            Activity activity = weakActivity.get();
            if(activity == null) {
                return;
            }

            for (Message m : messages) {
                ChatMessage chatMessage = getChatMessage(m.getText(), m.getSender(), m.getTimestamp());
                chatView.addMessage(chatMessage);
            }

            Log.d("chat", messages.toString());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("chat", intent.getExtras().toString());
    }
}


