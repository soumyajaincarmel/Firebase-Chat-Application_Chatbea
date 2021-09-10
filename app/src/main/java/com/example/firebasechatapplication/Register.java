package com.example.firebasechatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    EditText etRegisterUsername, etRegisterPassword;
    Button btnRegister;
    String strUsername, strPassword;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterUsername = (EditText) findViewById(R.id.et_register_username);
        etRegisterPassword = (EditText) findViewById(R.id.et_register_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        tvLogin = (TextView) findViewById(R.id.tv_login);

        Firebase.setAndroidContext(this);

        tvLogin.setOnClickListener(v -> startActivity(new Intent(Register.this, Login.class)));

        btnRegister.setOnClickListener(v -> {
            strUsername = etRegisterUsername.getText().toString();
            strPassword = etRegisterPassword.getText().toString();

            if (strUsername.equals("")) {
                etRegisterUsername.setError("can't be blank");
            } else if (strPassword.equals("")) {
                etRegisterPassword.setError("can't be blank");
            } else if (!strUsername.matches("[A-Za-z0-9]+")) {
                etRegisterUsername.setError("only alphabet or number allowed");
            } else if (strUsername.length() < 5) {
                etRegisterUsername.setError("at least 5 characters long");
            } else if (strPassword.length() < 5) {
                etRegisterPassword.setError("at least 5 characters long");
            } else {
                final ProgressDialog pd = new ProgressDialog(Register.this);
                pd.setMessage("Loading...");
                pd.show();

                String url = "https://fir-chat-application-f3907-default-rtdb.firebaseio.com/users.json";

                StringRequest request = new StringRequest(Request.Method.GET, url, s -> {
                    Firebase reference = new Firebase("https://fir-chat-application-f3907-default-rtdb.firebaseio.com/users");

                    if (s.equals("null")) {
                        reference.child(strUsername).child("password").setValue(strPassword);
                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Register.this, Login.class));
                        finish();
                    } else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(strUsername)) {
                                reference.child(strUsername).child("password").setValue(strPassword);
                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Register.this, "Username already exists, Login!", Toast.LENGTH_LONG).show();
                            }
                            startActivity(new Intent(Register.this, Login.class));
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    pd.dismiss();
                }, volleyError -> {
                    System.out.println("" + volleyError);
                    pd.dismiss();
                });

                RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                rQueue.add(request);
            }
        });
    }
}