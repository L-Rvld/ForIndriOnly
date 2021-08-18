package com.polije.gizielectree;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    String id;
    ProgressDialog progressDialog;
    String api;
    TextView nama, jenis, energi, protein, lemak, karbo, bdd;
    WebApiService webApiService ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        webApiService = new WebApiService();
        api     = webApiService.getApi_url();
        nama    = findViewById(R.id.txtNamaM);
        jenis   = findViewById(R.id.txtJenisM);
        energi  = findViewById(R.id.txtenergiM);
        protein = findViewById(R.id.txtproteinM);
        lemak   = findViewById(R.id.txtlemakM);
        karbo   = findViewById(R.id.txtkarboM);
        bdd     = findViewById(R.id.txtbddM);

        id = getIntent().getExtras().getString("id");


        StringRequest stringRequest = new StringRequest(Request.Method.GET, api + getString(R.string.getDetail) + "?id=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject object = jsonArray.getJSONObject(0);
                    nama.setText(object.getString("nama"));
                    if (object.getString("jenis").equals("1")){
                        jenis.setText("(Karbohidrat)");
                    } else if (object.getString("jenis").equals("2")){
                        jenis.setText("(Sayuran)");
                    } else if (object.getString("jenis").equals("3")){
                        jenis.setText("(Protein Hewani)");
                    } else if (object.getString("jenis").equals("4")){
                        jenis.setText("(Protein Nabati)");
                    } else if (object.getString("jenis").equals("5")){
                        jenis.setText("(Buah - Buahan)");
                    }
                    energi.setText("Energi  :  "+object.getString("energi")+" Kkal");
                    protein.setText("Protein  :  "+object.getString("protein")+" gr");
                    lemak.setText("Lemak  :  "+object.getString("lemak")+" gr");
                    karbo.setText("Karbohidrat  :  "+object.getString("karbo")+" gr");
                    bdd.setText("Porsi  :  per "+object.getString("bdd")+" gr");

                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("P", "onErrorResponse: ", error);
                        progressDialog.dismiss();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}