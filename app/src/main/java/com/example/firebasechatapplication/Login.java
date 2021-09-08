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

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    TextView tvRegister;
    EditText etLoginUsername, etLoginPassword;
    Button btnLogin;
    String strUsername, strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvRegister = (TextView) findViewById(R.id.tv_register);
        etLoginUsername = (EditText) findViewById(R.id.et_login_username);
        etLoginPassword = (EditText) findViewById(R.id.et_login_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        tvRegister.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));

        btnLogin.setOnClickListener(v -> {
            strUsername = etLoginUsername.getText().toString();
            strPassword = etLoginPassword.getText().toString();

            if (strUsername.equals("")) {
                etLoginUsername.setError("can't be blank");
            } else if (strPassword.equals("")) {
                etLoginPassword.setError("can't be blank");
            } else {
                String url = "https://fir-chat-application-f3907-default-rtdb.firebaseio.com/users.json";
                final ProgressDialog pd = new ProgressDialog(Login.this);
                pd.setMessage("Loading...");
                pd.show();

                StringRequest request = new StringRequest(Request.Method.GET, url, s -> {
                    if (s.equals("null")) {
                        Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(strUsername)) {
                                Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                            } else if (obj.getJSONObject(strUsername).getString("password").equals(strPassword)) {
                                UserDetails.username = strUsername;
                                UserDetails.password = strPassword;
                                startActivity(new Intent(Login.this, Users.class));
                                finish();
                            } else {
                                Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    pd.dismiss();
                }, volleyError -> {
                    System.out.println("" + volleyError);
                    pd.dismiss();
                });

                RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                rQueue.add(request);
            }

        });
    }
}
