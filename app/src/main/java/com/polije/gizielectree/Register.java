package com.polije.gizielectree;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.kusu.loadingbutton.LoadingButton;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends Activity {
    Button gotolog,btnregister;
    String api;
    WebApiService webApiService;
    TextInputEditText email, username, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        webApiService = new WebApiService();
        api = webApiService.getApi_url();
        email= findViewById(R.id.etEmailReg);
        username = findViewById(R.id.etUserReg);
        pass = findViewById(R.id.etPassReg);
        btnregister = findViewById(R.id.lanjutRegister);
        gotolog = findViewById(R.id.btn_gotolog);
        gotolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(email.getText().toString(),username.getText().toString(),pass.getText().toString());
            }
        });
    }

    public void register(final String email, final String user, final String pass) {
        StringRequest register = new StringRequest(Request.Method.POST, api + "/" + getString(R.string.register), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("true")) {
                        startActivity(new Intent(Register.this, RegisterUser.class)
                        .putExtra("email",email)
                        .putExtra("user",user)
                        .putExtra("pass",pass));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email Sudah Digunakan", Toast.LENGTH_SHORT).show();
                        Log.d("hhh", "onResponse: "+response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Periksa Lagi Koneksi Anda", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email",email);
                Log.d("TAG", "getParams: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(register);
    }
}
