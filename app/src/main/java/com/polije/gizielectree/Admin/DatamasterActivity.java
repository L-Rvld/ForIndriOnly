package com.polije.gizielectree.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.polije.gizielectree.AdapModel.AdapterData;
import com.polije.gizielectree.AdapModel.AdapterDataMaster;
import com.polije.gizielectree.AdapModel.ModelData;
import com.polije.gizielectree.DataActivity;
import com.polije.gizielectree.Login;
import com.polije.gizielectree.R;
import com.polije.gizielectree.RegisterUser;
import com.polije.gizielectree.Utils.ToastBaseCust;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatamasterActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    ArrayList<ModelData> list = new ArrayList<>();
    WebApiService apiService;
    AdapterDataMaster adapterDataMaster;
    Dialog dialog;
    ImageButton closes;
    Button add;
    TextInputEditText nama, jns, enrg, sumber, bdd, protein, lemak, karbo;
    String[] kategori,kodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamaster);
        apiService = new WebApiService();
        dialog = new Dialog(this,android.R.style.ThemeOverlay_Material);
        dialog.setContentView(R.layout.tambah_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        kategori = getResources().getStringArray(R.array.kategori);
        kodes = getResources().getStringArray(R.array.kode);

        searchView = findViewById(R.id.searchViewDataMas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Data Master Makanan & Minuman");

        recyclerView = findViewById(R.id.recyDataMaster);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        getDataM(false);

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
                adapterDataMaster.setFilter(lists);
                return true;
            }
        });
    }

    private void getDataM(boolean isreload) {
        if (!isreload) {
            list.clear();
            progressDialog.setTitle("Mengambil Data");
            progressDialog.show();
        }
        StringRequest ambilData = new StringRequest(Request.Method.GET, apiService.getApi_url() + "getDataList.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TASS", "onResponse: "+response);
                String s;
                try {
                    progressDialog.dismiss();
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
                    adapterDataMaster = new AdapterDataMaster(DatamasterActivity.this, list, progressDialog, dialog);
                    recyclerView.setAdapter(adapterDataMaster);
                    adapterDataMaster.notifyDataSetChanged();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("TAG", "onErrorResponse: " + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(ambilData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_datamaster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tambah_menu) {
            openDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initD(){
        add = dialog.findViewById(R.id.btnAddda);
        closes = dialog.findViewById(R.id.closeDialog);

        nama = dialog.findViewById(R.id.eDMNama);
        jns = dialog.findViewById(R.id.eDMJenis);
        enrg = dialog.findViewById(R.id.eDMEnergi);
        sumber = dialog.findViewById(R.id.eDMSumber);
        bdd = dialog.findViewById(R.id.eDMBDD);
        protein = dialog.findViewById(R.id.eDMProtein);
        lemak = dialog.findViewById(R.id.eDMLemak);
        karbo = dialog.findViewById(R.id.eDMKH);
    }

    private void openDialog() {
        dialog.show();
        final String[] koded = new String[1];
        initD();
        jns.setFocusable(false);

        final ArrayAdapter<String> adapterjenis = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,kategori );
        jns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(dialog.getContext()).setTitle("Pilih Kategori").setAdapter(adapterjenis, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jns.setText(kategori[i].toString());
                        koded[0] = kodes[i];
                        Log.d("KODED", "onClick: geting = "+kodes[i]);
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(nama.getText())&&!TextUtils.isEmpty(jns.getText())&&!TextUtils.isEmpty(enrg.getText())
                        &&!TextUtils.isEmpty(sumber.getText())&&!TextUtils.isEmpty(bdd.getText())){

                    getSeq(koded[0]);

                }else if (TextUtils.isEmpty(nama.getText())&&TextUtils.isEmpty(jns.getText())&&TextUtils.isEmpty(enrg.getText())
                        && TextUtils.isEmpty(sumber.getText())&&TextUtils.isEmpty(bdd.getText())){
                    nama.setError("Isi Nama Makanan / Minuman");
                    jns.setError("Pilih Jenis Makanan / Minuman");
                    enrg.setError("Isi Data Energi");
                    sumber.setError("Isi Sumber Makanan / Minuman");
                    bdd.setError("Isi BDD");
                }else if (TextUtils.isEmpty(nama.getText())){
                    nama.setError("Isi Nama Makanan / Minuman");
                }else if (TextUtils.isEmpty(jns.getText())){
                    jns.setError("Pilih Jenis Makanan / Minuman");
                }else if (TextUtils.isEmpty(enrg.getText())){
                    enrg.setError("Isi Data Energi");
                }else if (TextUtils.isEmpty(sumber.getText())){
                    sumber.setError("Isi Sumber Makanan / Minuman");
                }else if (TextUtils.isEmpty(bdd.getText())){
                    bdd.setError("Isi BDD");
                }

            }
        });
        closes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                clearingText();
            }
        });
    }
    public void getSeq(String id){
        progressDialog.setTitle("Menambah Data ...");
        progressDialog.show();
        StringRequest seq = new StringRequest(Request.Method.GET, apiService.getApi_url() + "getSeq.php"+"?id="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TASsssS", "onResponse: "+response);
                String s;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String idnya = jsonObject.getString("result");
                    int idx = Integer.parseInt(idnya)+1;
                    String nextID = id+""+idx;

                    adding(t(nama),nextID,t(enrg),t(sumber),t(bdd),t(protein), t(lemak), t(karbo));

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: " + error);
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(seq);
    }
    public void adding(String nama, String ID, String energi, String jenis, String bdd, String protein, String lemak, String karbo){
        StringRequest adder = new StringRequest(Request.Method.POST, apiService.getApi_url() + getString(R.string.admin_master), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("TAG", "APINYA: " + apiService.getApi_url() + getString(R.string.admin_master));
                Log.d("TAG", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        new ToastBaseCust(getApplicationContext(),getWindow().getDecorView(),"SUKSES",R.drawable.logoapp);
                        dialog.dismiss();
                        clearingText();
                        getDataM(true);
                    }else {
                        new ToastBaseCust(getApplicationContext(),getWindow().getDecorView(),"GAGAL",R.drawable.logoapp);
                        dialog.dismiss();
                        clearingText();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("TAG", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("nama",nama);
                param.put("enrg",energi);
                param.put("bdd",bdd);
                param.put("protein",protein);
                param.put("lemak",lemak);
                param.put("action","add");
//                param.put("",);
                Log.d("TAG", "getParams: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(adder);
    }

    public String t (TextInputEditText s){
        return s.getText().toString();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void clearingText(){
        nama.setText("");
        jns.setText("");
        enrg.setText("");
        sumber.setText("");
        bdd.setText("");
        protein.setText("0");
        lemak.setText("0");
        karbo.setText("0");
    }
}