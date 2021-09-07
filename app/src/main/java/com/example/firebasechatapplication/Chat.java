package com.example.firebasechatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    LinearLayout llChat;
    ImageView ivSend;
    EditText etMessageArea;
    ScrollView scrollViewChat;
    Firebase reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle(getResources().getString(R.string.chat_with, UserDetails.chatWith));

        llChat = (LinearLayout) findViewById(R.id.ll_chat);
        ivSend = (ImageView) findViewById(R.id.iv_send);
        etMessageArea = (EditText) findViewById(R.id.et_message_area);
        scrollViewChat = (ScrollView) findViewById(R.id.scroll_view_chat);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://fir-chat-application-f3907-default-rtdb.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://fir-chat-application-f3907-default-rtdb.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        ivSend.setOnClickListener(v -> {
            String messageText = etMessageArea.getText().toString();

            if (!messageText.equals("")) {
                Map<String, String> map = new HashMap<>();
                map.put("message", messageText);
                map.put("user", UserDetails.username);
                reference1.push().setValue(map);
                reference2.push().setValue(map);
                etMessageArea.setText(null);
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map;
                map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(UserDetails.username)) {
                    addMessageBox("You:\n" + message, 1);
                } else {
                    addMessageBox(UserDetails.chatWith + ":\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type) {
        TextView tvMessage = new TextView(Chat.this);
        tvMessage.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        tvMessage.setLayoutParams(lp);

        if (type == 1) {
            lp.gravity = Gravity.END;
            tvMessage.setGravity(Gravity.FILL);
            tvMessage.setTextColor(getResources().getColor(R.color.primaryTextColor));
            tvMessage.setBackgroundResource(R.drawable.rounded_corner1);
        } else {
            lp.gravity = Gravity.START;
            tvMessage.setGravity(Gravity.FILL);
            tvMessage.setTextColor(getResources().getColor(R.color.secondaryTextColor));
            tvMessage.setBackgroundResource(R.drawable.rounded_corner2);
        }

        llChat.addView(tvMessage);
        scrollViewChat.fullScroll(View.FOCUS_DOWN);
    }
}