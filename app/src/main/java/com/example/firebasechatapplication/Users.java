package com.example.firebasechatapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Users extends AppCompatActivity implements UsersAdapter.ItemClickListener {

    UsersAdapter adapter;
    RecyclerView rvUsersList;
    TextView tvNoUsers;
    ArrayList<String> alUsers = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        tvNoUsers = (TextView) findViewById(R.id.tv_no_users);
        rvUsersList = (RecyclerView) findViewById(R.id.rv_user_list);
        rvUsersList.setLayoutManager(new LinearLayoutManager(this));

        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://fir-chat-application-f3907-default-rtdb.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        }, volleyError -> System.out.println("" + volleyError));

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

    }

    public void doOnSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key;

            while (i.hasNext()) {
                key = i.next().toString();

                if (!key.equals(UserDetails.username)) {
                    alUsers.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (totalUsers <= 1) {
            tvNoUsers.setVisibility(View.VISIBLE);
            rvUsersList.setVisibility(View.GONE);
        } else {
            tvNoUsers.setVisibility(View.GONE);
            rvUsersList.setVisibility(View.VISIBLE);
            adapter = new UsersAdapter(this, alUsers);
            adapter.setClickListener(this);
            rvUsersList.setAdapter(adapter);
        }

        pd.dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        UserDetails.chatWith = alUsers.get(position);
        startActivity(new Intent(Users.this, Chat.class));
    }
}