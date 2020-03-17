package com.afeka.whereapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afeka.whereapp.ChatActivity;
import com.afeka.whereapp.R;
import com.afeka.whereapp.data.Group;

import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment {

    private LinearLayout layout;
    private Map<String, TextView> groupsList = new HashMap<String, TextView>();
    private String userName;

    public ChatFragment() {
    }

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        layout = v.findViewById(R.id.chat_fragment_layout);
        for (TextView tv : groupsList.values()) {
            if(tv.getParent() != null) {
                ((ViewGroup)tv.getParent()).removeView(tv);
            }
            layout.addView(tv);
        }

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void updateUserName(String name) {
        userName = name;
    }

    public void addGroup(final Context context, final Group group) {
        if (!groupsList.containsKey(group.getId())) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(group.getName());
            textView.setTextSize(22.0f);
            textView.setPadding(10, 10, 10, 10);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra(ChatActivity.GROUP_ID_EXTRA, group.getId());
                    intent.putExtra(ChatActivity.USER_NAME_EXTRA, userName);
                    startActivity(intent);
                }
            });
            groupsList.put(group.getId(), textView);
        }

    }
}
