package com.polije.gizielectree.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polije.gizielectree.AdapModel.AdapterDataMaster;
import com.polije.gizielectree.AdapModel.AdapterUser;
import com.polije.gizielectree.AdapModel.ModelData;
import com.polije.gizielectree.AdapModel.ModelUser;
import com.polije.gizielectree.R;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListuseronlyActivity extends AppCompatActivity {
    AdapterUser adapterUser;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    ArrayList<ModelUser> list = new ArrayList<>();
    WebApiService apiService;
    String api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listuseronly);
        apiService = new WebApiService();
        api=apiService.getApi_url();

        recyclerView = findViewById(R.id.recyDataUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Data User [Read-Only]");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Mengambil Data");
        progressDialog.show();

        getData();
    }
    public String senstext(String text, Boolean isMail){
        if (isMail){
            String seperate = text.substring(0, 2);

            return "Email : "+seperate + "***@***.***";
        }else {
            String seperate = text.substring(0, 3);

            return seperate + "***";
        }
    }
    private void getData() {
        StringRequest ambilData = new StringRequest(Request.Method.GET, apiService.getApi_url() + "getUser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TASS", "onResponse: "+response);
                String s = null, a = null;
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.getString("jk_user").equals("L")) {
                            s = "Laki - Laki";
                        } else if (object.getString("jk_user").equals("P")) {
                            s = "Perempuan";
                        }
                        ModelUser modelUser = new ModelUser(senstext(object.getString("email_user"),true),senstext(object.getString("nama_user"),false),s,object.getString("umur_user")+" thn"
                        ,object.getString("tinggi_user")+" cm",object.getString("berat_user")+" kg",object.getString("aktifitas_user"));
                        list.add(modelUser);
                    }
                    adapterUser = new AdapterUser(ListuseronlyActivity.this, list);
                    recyclerView.setAdapter(adapterUser);
                    adapterUser.notifyDataSetChanged();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: " + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(ambilData);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}