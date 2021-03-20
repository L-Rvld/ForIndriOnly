package com.polije.gizielectree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polije.gizielectree.AdapModel.AdapterData;
import com.polije.gizielectree.AdapModel.AdapterSendData;
import com.polije.gizielectree.AdapModel.ModelData;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterData adapterData;
    ArrayList<ModelData> list = new ArrayList<>();
    WebApiService apiService;
    SearchView searchView;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        bundle = getIntent().getExtras();

        apiService = new WebApiService();
        recyclerView = findViewById(R.id.recyData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DataActivity.this));
        searchView = findViewById(R.id.searchView);


        getData();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                nextText = nextText.toLowerCase();
                ArrayList<ModelData> lists = new ArrayList<>();
                for (ModelData data : list) {
                    String dataM = data.getNama_bahan().toLowerCase();
                    if (dataM.contains(nextText)) {
                        lists.add(data);
                    }
                }
                adapterData.setFilter(lists);
                return true;
            }
        });
        
    }

    public void getData() {
        StringRequest ambilData = new StringRequest(Request.Method.GET, apiService.getApi_url() + "getDataList.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String s;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.getString("bdd").equals("0")) {
                            s = "Porsi : - ";
                        } else {
                            s = "Porsi : " + jsonObject1.getString("bdd") + " gr";
                        }
                        ModelData modelData = new ModelData(jsonObject1.getString("id"), jsonObject1.getString("nama"), "Energi : " + jsonObject1.getString("energi") + " Kkal", s);
                        list.add(modelData);
                    }
                    adapterData = new AdapterData(DataActivity.this, list);
                    recyclerView.setAdapter(adapterData);
                    adapterData.notifyDataSetChanged();
                } catch (JSONException e) {
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
}