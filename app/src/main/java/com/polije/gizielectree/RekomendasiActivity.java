package com.polije.gizielectree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polije.gizielectree.AdapModel.AdapTemp;
import com.polije.gizielectree.AdapModel.AdapterRekomendasi;
import com.polije.gizielectree.AdapModel.AdapterSendData;
import com.polije.gizielectree.AdapModel.ModelData;
import com.polije.gizielectree.AdapModel.ModelForReceiver;
import com.polije.gizielectree.AdapModel.ModelRekomendasi;
import com.polije.gizielectree.Utils.Sharedprefs;
import com.polije.gizielectree.Utils.SqliteHelpers;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RekomendasiActivity extends AppCompatActivity {

    Sharedprefs sharedprefs;
    ProgressDialog progressDialog;
    Double bb, aktif, umur, tb;
    Double pagi, sp, siang, ss, malam;
    String api, jk;
    TextView kalori , kalfor, kp,ksp,ks,kss,km;
    WebApiService webApiService;
    RecyclerView recyPagi,recySiang,recyMalam,recySP,recySS;
    AdapterRekomendasi adapterRekomendasi;
    ArrayList<ModelRekomendasi> listP = new ArrayList<>();
    ArrayList<ModelRekomendasi> listS = new ArrayList<>();
    ArrayList<ModelRekomendasi> listM = new ArrayList<>();
    ArrayList<ModelRekomendasi> listSP = new ArrayList<>();
    ArrayList<ModelRekomendasi> listSS = new ArrayList<>();
    Dialog dialog;
    RecyclerView recylerek;
    SearchView searchView;
    ArrayList<ModelData> listData = new ArrayList<>();
    AdapterSendData adapterData;
    ModelForReceiver model = new ModelForReceiver();
    Integer rekomenergi = 0;
    SqliteHelpers helpers = new SqliteHelpers(this);
    int kalP, kalSP, kalS, kalSS, kalM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi);
        sharedprefs = new Sharedprefs(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Rekomendasi");

        jk = sharedprefs.getJK();
        bb = Double.parseDouble(sharedprefs.getBerat());
        aktif = Double.parseDouble(sharedprefs.getAktif());
        umur = Double.parseDouble(sharedprefs.getUmur());
        tb = Double.parseDouble(sharedprefs.getTinggi());
        webApiService = new WebApiService();
        kp = findViewById(R.id.rekkP);
        ksp= findViewById(R.id.rekkSP);
        ks = findViewById(R.id.rekkS);
        kss = findViewById(R.id.rekkSS);
        km = findViewById(R.id.rekkM);
        api = webApiService.getApi_url();
        kalori = findViewById(R.id.txtRekomKalori);
        kalfor= findViewById(R.id.txtRekomFor);

        dialog = new Dialog(this,android.R.style.ThemeOverlay_Material);
        dialog.setContentView(R.layout.layout_dialog);
        recylerek = dialog.findViewById(R.id.recycleData);
        searchView = dialog.findViewById(R.id.search_data);
        LinearLayout listt = dialog.findViewById(R.id.layoutDialogRecy);
        LinearLayout progress = dialog.findViewById(R.id.layoutprogressBar_data);
        recylerek.setHasFixedSize(true);
        recylerek.setLayoutManager(new LinearLayoutManager(this));
        ambilData(dialog, listt, progress);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String nextText) {
                nextText = nextText.toLowerCase();
                ArrayList<ModelData> lists = new ArrayList<>();
                for (ModelData data : listData) {
                    String dataM = data.getNama_bahan().toLowerCase();
                    if (dataM.contains(nextText)) {
                        lists.add(data);
                    }
                }
                adapterData.setFilter(lists);
                return true;
            }
        });

        init();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        int nilaijk;
        if (jk == "L") {
            nilaijk = 30;
        } else {
            nilaijk = 25;
        }
        getRekom(nilaijk, tb, aktif, umur);

        LocalBroadcastManager.getInstance(this).registerReceiver(mFromSendData,
                new IntentFilter("passData"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mFromRekom,
                new IntentFilter("sendFromRekom"));


    }

    public void showRekom() {
        List<AdapTemp> list = new ArrayList<>();
        list = helpers.getTemp();
        for (int j = 0; j < list.size(); j++) {
            String id = list.get(j).getId();
            String hari = list.get(j).getHari();
            Log.d(TAG, "showRekom: " + id + " :: " + hari);
            getInfoID(id, hari, false);
        }
        Log.d(TAG, "showRekom: PP");
        progressDialog.dismiss();
    }

    public void ambilData(Dialog dialog, LinearLayout line, LinearLayout progres){
        listData.clear();
        StringRequest ambilData = new StringRequest(Request.Method.GET, api + "getDataList.php", new Response.Listener<String>() {
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
                        listData.add(modelData);
                    }
                    adapterData = new AdapterSendData(RekomendasiActivity.this, listData, dialog);
                    recylerek.setAdapter(adapterData);
                    line.setVisibility(View.VISIBLE);
                    progres.setVisibility(View.GONE);
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

    public void getRekom(double jk, double tb, double aktif, double umur) {
        int kurangumur = 0;

        if (umur <40){
            kurangumur = 0;
        }else if (umur >40 && umur<60){
            kurangumur =5;
        }else if (umur >= 60 && umur <70){
            kurangumur = 10;
        }else if (umur>70){
            kurangumur = 20;
        }
        /////////////////////
        double tbt = (tb / 100.0);
        double BBI;
        double TB;
        if (jk == 25){
            TB = Math.pow(tbt,2);
            BBI = TB * 21.0;
        }else{
            TB = Math.pow(tbt,2);
            BBI = TB * 22.5;
        }

        double basal;

        if (jk == 25){
            basal = BBI * 25.0;
        }else{
            basal = BBI * 30.0;
        }

        double energi;
        double per = (aktif+10-kurangumur) / 100.0;
        energi = basal + (basal*per);

        kalori.setText(energi+" Kkal");
        Log.d(TAG, "getRekom: "+energi);

        pagi = energi * 25 / 100;
        sp = energi * 10 / 100;
        siang = energi * 30 / 100;
        ss = energi * 10 / 100;
        malam = energi * 25 / 100;

        if (sharedprefs.getRekom().equals("")) {
            progressDialog.setTitle("Mencari Rekomendasi Terbaik");
            progressDialog.show();
            sendRekom(pagi, siang, malam, sp, ss);
        } else {
            progressDialog.setTitle("Menampilkan Rekomendasi Anda");
            progressDialog.show();
            showRekom();
        }
    }

    public void sendRekom(final Double pagi, final Double siang, Double malam, Double sp, Double ss) {
        helpers.clear();
        String apiR = "?lowPagi=" + pagi + "&lowSiang=" + siang + "&lowMalam=" + malam + "&lowSP=" + sp + "&lowSS=" + ss;
        Log.d(TAG, "sendRekom: "+api+getString(R.string.rekomendasi) + apiR);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api + getString(R.string.rekomendasi) + apiR,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String[] object = {"pagi","siang","malam","snackpagi", "snacksiang"};
                    Log.d(TAG, "onResponsess: "+jsonObject);
                    getObject(jsonObject,object);
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("P", "onErrorResponse: ", error);
                    }
                }
        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void getObject(JSONObject jsonObject,String[] object){
        try {
            for (int i=0;i<object.length;i++) {
                JSONArray jsonArray = jsonObject.getJSONArray("" + object[i]);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String id = jsonArray.getString(j);
                    getInfoID(id,object[i],true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getInfoID(String id, String hari, boolean is){
        if (is==true){
            helpers.addTemp(hari,id);
        }
        final int[] energi = {0};
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api + getString(R.string.getDetail) + "?id="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject object = jsonArray.getJSONObject(0);
                    Log.d(TAG, "onResponse: "+jsonObject);
                    energi[0] = Integer.parseInt(object.getString("energi"));
                    rekomenergi += energi[0];
                    kalfor.setText(rekomenergi+" Kkal");
                    ModelRekomendasi modelRekomendasi = new ModelRekomendasi(object.getString("id"),object.getString("nama"),object.getString("jenis"),object.getString("energi"),object.getString("protein"),object.getString("lemak"),
                            object.getString("protein"),object.getString("bdd"));
                    switch (hari) {
                        case "pagi":
                            kalP += Integer.parseInt(object.getString("energi"));
                            kp.setText(kalP+" kkal");
                            listP.add(modelRekomendasi);
                            adapterRekomendasi = new AdapterRekomendasi(RekomendasiActivity.this, listP, hari);
                            recyPagi.setAdapter(adapterRekomendasi);
                            adapterRekomendasi.notifyDataSetChanged();
                            break;
                        case "siang":
                            kalS += Integer.parseInt(object.getString("energi"));
                            ks.setText(kalS+" kkal");
                            listS.add(modelRekomendasi);
                            adapterRekomendasi = new AdapterRekomendasi(RekomendasiActivity.this, listS,hari);
                            recySiang.setAdapter(adapterRekomendasi);
                            adapterRekomendasi.notifyDataSetChanged();
                            break;
                        case "malam":
                            kalM += Integer.parseInt(object.getString("energi"));
                            km.setText(kalM+" kkal");
                            listM.add(modelRekomendasi);
                            adapterRekomendasi = new AdapterRekomendasi(RekomendasiActivity.this, listM,hari);
                            recyMalam.setAdapter(adapterRekomendasi);
                            adapterRekomendasi.notifyDataSetChanged();
                            break;
                        case "snackpagi":
                            kalSP += Integer.parseInt(object.getString("energi"));
                            ksp.setText(kalSP+" kkal");
                            listSP.add(modelRekomendasi);
                            adapterRekomendasi = new AdapterRekomendasi(RekomendasiActivity.this, listSP,hari);
                            recySP.setAdapter(adapterRekomendasi);
                            adapterRekomendasi.notifyDataSetChanged();
                            break;
                        case "snacksiang":
                            kalSS += Integer.parseInt(object.getString("energi"));
                            kss.setText(kalSS+" kkal");
                            listSS.add(modelRekomendasi);
                            adapterRekomendasi = new AdapterRekomendasi(RekomendasiActivity.this, listSS,hari);
                            recySS.setAdapter(adapterRekomendasi);
                            adapterRekomendasi.notifyDataSetChanged();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("P", "onErrorResponse: ", error);
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void init(){
        recyPagi = findViewById(R.id.recyRekomPagi);
        recyPagi.setHasFixedSize(true);
        recyPagi.setLayoutManager(new LinearLayoutManager(RekomendasiActivity.this));

        recySiang = findViewById(R.id.recyRekomSiang);
        recySiang.setHasFixedSize(true);
        recySiang.setLayoutManager(new LinearLayoutManager(RekomendasiActivity.this));

        recyMalam = findViewById(R.id.recyRekomMalam);
        recyMalam.setHasFixedSize(true);
        recyMalam.setLayoutManager(new LinearLayoutManager(RekomendasiActivity.this));

        recySP = findViewById(R.id.recyRekomSP);
        recySP.setHasFixedSize(true);
        recySP.setLayoutManager(new LinearLayoutManager(RekomendasiActivity.this));

        recySS = findViewById(R.id.recyRekomSS);
        recySS.setHasFixedSize(true);
        recySS.setLayoutManager(new LinearLayoutManager(RekomendasiActivity.this));


    }
    public BroadcastReceiver mFromSendData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String hari = model.getHari();
            switch (hari) {
                case "pagi":
                    listP.remove(model.getPos());
                    helpers.removeTemp(model.getId());
                    getInfoID(intent.getStringExtra("id"), hari,true);
                    break;
                case "siang":
                    listS.remove(model.getPos());
                    helpers.removeTemp(model.getId());
                    getInfoID(intent.getStringExtra("id"), hari,true);
                    break;
                case "malam":
                    listM.remove(model.getPos());
                    helpers.removeTemp(model.getId());
                    getInfoID(intent.getStringExtra("id"), hari,true);
                    break;
                case "snackpagi":
                    listSP.remove(model.getPos());
                    helpers.removeTemp(model.getId());
                    getInfoID(intent.getStringExtra("id"), hari,true);
                    break;
                case "snacksiang":
                    listSS.remove(model.getPos());
                    helpers.removeTemp(model.getId());
                    getInfoID(intent.getStringExtra("id"), hari,true);
                    break;
            }
        }
    };
    public BroadcastReceiver mFromRekom = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dialog.show();
            model.setHari(intent.getStringExtra("hari"));
            model.setPos(intent.getIntExtra("position",0));
            model.setId(intent.getStringExtra("id"));
        }
    };

    public void saveRekom(){
        if (sharedprefs.getRekom().isEmpty()) {
            sharedprefs.saveRekom(sharedprefs.rekom, "save");
            progressDialog.setTitle("Menyimpan Rekomendasi ...");
            progressDialog.show();
            List<AdapTemp> list = new ArrayList<>();
            list = helpers.getTemp();
            for (int j = 0; j < list.size(); j++) {
                String id = list.get(j).getId();
                String hari = list.get(j).getHari();
                savePerId(id, hari);
            }
            progressDialog.dismiss();
            Toast.makeText(this, "Rekomendasi Anda Sudah Tersimpan", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Rekomendasi Anda Sudah Tersimpan", Toast.LENGTH_SHORT).show();
        }
    }
    public void savePerId(String id, String hari){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api + getString(R.string.getDetail) + "?id="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject object = jsonArray.getJSONObject(0);
                    helpers.addDataM(hari,object.getString("id"),object.getString("nama"),object.getString("jenis"),object.getString("energi"),object.getString("protein"),object.getString("lemak"),object.getString("karbo"),object.getString("bdd"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("P", "onErrorResponse: ", error);
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.rekom_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.rekomenuSave:
                saveRekom();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}