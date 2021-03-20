    package com.polije.gizielectree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.polije.gizielectree.Admin.AdminMainActivity;
import com.polije.gizielectree.Utils.Sharedprefs;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button register,login;
    String api;
    WebApiService webApiService;
    TextInputEditText email, pass;
    Sharedprefs sharedprefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        register = findViewById(R.id.btn_gotoreg);
        email = findViewById(R.id.logUser);
        pass = findViewById(R.id.logPass);
        webApiService = new WebApiService();
        api = webApiService.getApi_url();
        sharedprefs = new Sharedprefs(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText())&&!TextUtils.isEmpty(pass.getText())) {
                    if (email.getText().toString().equals("admin_")){
                        reqAdm(pass.getText().toString());
                    }else {
                        reqLog(email.getText().toString(), pass.getText().toString());
                    }
                }else if (TextUtils.isEmpty(email.getText())){
                    Toast.makeText(Login.this, "Email Kosong", Toast.LENGTH_SHORT).show();
                    email.setError("Masukan Email");
                }else if (TextUtils.isEmpty(pass.getText())){
                    Toast.makeText(Login.this, "Password Kosong", Toast.LENGTH_SHORT).show();
                    pass.setError("Masukan Password");
                }
            }
        });
    }
    public void reqAdm(String pass){
        StringRequest admin = new StringRequest(Request.Method.POST, api + "/" + getString(R.string.admin), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        sharedprefs.savelogin("onLogin",true);
                        sharedprefs.saveLevel("is_admin",true);
                        startActivity(new Intent(Login.this, AdminMainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Gagal Login Admin", Toast.LENGTH_SHORT).show();
                        Log.d("hhh", "onResponse: "+response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("password",pass);
                Log.d("TAG", "getParams: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(admin);
    }
    public void reqLog(final String email, final String pass){
        StringRequest register = new StringRequest(Request.Method.POST, api + "/" + getString(R.string.login), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        sharedprefs.savelogin("onLogin",true);
                        sharedprefs.saveLevel("is_admin",false);
                        sharedprefs.saveID(sharedprefs.id,jsonObject.getJSONObject("user").getString("id"));
                        sharedprefs.saveusername(sharedprefs.user,jsonObject.getJSONObject("user").getString("username"));
                        sharedprefs.saveJK(sharedprefs.jk,jsonObject.getJSONObject("user").getString("jk"));
                        sharedprefs.saveTinggi(sharedprefs.tinggi,jsonObject.getJSONObject("user").getString("tinggi"));
                        sharedprefs.saveBerat(sharedprefs.berat,jsonObject.getJSONObject("user").getString("berat"));
                        sharedprefs.saveUmur(sharedprefs.umur,jsonObject.getJSONObject("user").getString("umur"));
                        sharedprefs.saveAktif(sharedprefs.aktif,jsonObject.getJSONObject("user").getString("aktifitas"));
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Gagal Login, parameter", Toast.LENGTH_SHORT).show();
                        Log.d("hhh", "onResponse: "+response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email",email);
                param.put("password",pass);
                Log.d("TAG", "getParams: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(register);
    }
}
