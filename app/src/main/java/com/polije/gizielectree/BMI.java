package com.polije.gizielectree;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.polije.gizielectree.AdapModel.AdapTemp;
import com.polije.gizielectree.AdapModel.ModelGula;
import com.polije.gizielectree.Utils.Sharedprefs;
import com.polije.gizielectree.Utils.SqliteHelpers;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BMI extends AppCompatActivity {
    EditText pekerjaan, usia, tb,bb, kgul;
    Button hitung;
    TextView poin, kkal, imb, txtimb, txtgul;
    RadioGroup jk;
    CardView cardView;
    Sharedprefs sharedprefs;
    RadioButton p, l;
    SqliteHelpers helpers;
    WebApiService apiService ;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Hitung Kalori");
        helpers = new SqliteHelpers(this);
        apiService = new WebApiService();

        p = findViewById(R.id.jkPer);
        l = findViewById(R.id.jkLaki);

        sharedprefs = new Sharedprefs(this);
        final String [] namaAktif ={"Istirahat","Ringan","Sedang","Berat","Sangat Berat"};

        pekerjaan = findViewById(R.id.Aktifitas);
        poin = findViewById(R.id.aktifpoin);
        kgul = findViewById(R.id.kadargul);
        usia =findViewById(R.id.usia);
        tb = findViewById(R.id.tinggi);
        jk = findViewById(R.id.opsijenk);
        bb =findViewById(R.id.berat);
        hitung = findViewById(R.id.hitungbmi);
        kkal = findViewById(R.id.txtKalor);
        imb = findViewById(R.id.txtBMI);
        txtimb = findViewById(R.id.txtketBmi);
        txtgul = findViewById(R.id.txtketGul);
        cardView = findViewById(R.id.cardafterhitung);

        int di = Integer.parseInt(sharedprefs.getAktif()) / 10;
        pekerjaan.setText(namaAktif[di-1]);
        poin.setText(sharedprefs.getAktif());

        usia.setText(sharedprefs.getUmur());
        tb.setText(sharedprefs.getTinggi());
        if (sharedprefs.getJK().equals("25")){
            jk.check(p.getId());
        }else {
            jk.check(l.getId());
        }
        if (sharedprefs.getGula().isEmpty()){
            kgul.setError("Masukan Kadar Gula");
        }else {
            kgul.setText(sharedprefs.getGula());
        }

        bb.setText(sharedprefs.getBerat());

        hitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tb.getText())){
                    Toast.makeText(BMI.this, "Tinggi Badan Kosong", Toast.LENGTH_SHORT).show();
                    tb.setError("Masukan Tinggi Badan");
                }else if (TextUtils.isEmpty(bb.getText())){
                    Toast.makeText(BMI.this, "Berat Badan Kosong", Toast.LENGTH_SHORT).show();
                    bb.setError("Masukan Berat Badan");
                }else if (TextUtils.isEmpty(kgul.getText())){
                    Toast.makeText(BMI.this, "Kadar Gula Kosong", Toast.LENGTH_SHORT).show();
                    kgul.setError("Masukan Kadar Gula");
                }else if (TextUtils.isEmpty(usia.getText())){
                    Toast.makeText(BMI.this, "Umur Kosong", Toast.LENGTH_SHORT).show();
                    usia.setError("Masukan Umur");
                } else if(jk.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(BMI.this, "Jenis Kelamin belum di isi", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(pekerjaan.getText())){
                    Toast.makeText(BMI.this, "Aktifitas Kosong", Toast.LENGTH_SHORT).show();
                    pekerjaan.setError("Masukan Aktifitas");
                }if (!TextUtils.isEmpty(tb.getText())&&!TextUtils.isEmpty(bb.getText())&&!TextUtils.isEmpty(usia.getText())&&!TextUtils.isEmpty(kgul.getText())&&jk.getCheckedRadioButtonId() != -1&&!TextUtils.isEmpty(pekerjaan.getText())){
                    int selected=jk.getCheckedRadioButtonId();
                    RadioButton gender=(RadioButton) findViewById(selected);
                    int jnskl;
                    if (gender.getText().toString().equals("Perempuan")){
                        jnskl = 25;
                    }else {
                        jnskl = 30;
                    }
                    InputMethodManager inputManager = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    hitungBMI(Integer.parseInt(tb.getText().toString()),Integer.parseInt(bb.getText().toString()),Integer.parseInt(usia.getText().toString()),jnskl,Integer.parseInt(poin.getText().toString()),Integer.parseInt(kgul.getText().toString()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        addGul(Integer.parseInt(kgul.getText().toString()));
                    }
                }
            }
        });

        final ArrayAdapter<String> adapterAktif = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, namaAktif);
        pekerjaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(BMI.this).setTitle("Pilih aktifitas anda :").setAdapter(adapterAktif, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id = i+1;
                        int aktifno = id * 10;
                        pekerjaan.setText(namaAktif[i].toString());
                        poin.setText(""+aktifno);
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addGul(int gula){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd");
        LocalDateTime now = LocalDateTime.now();
        helpers.addGula(String.valueOf(dtf.format(now)),gula);

    }

    public void addToDb(double tb, double bb, double umur,double jk, double aktif){
        StringRequest adder = new StringRequest(Request.Method.POST, apiService.getApi_url() + "user_master.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(BMI.this, "BERHASIL", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        poin.requestFocus();
                    }else {
                        Toast.makeText(BMI.this, "GAGAL", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        poin.requestFocus();
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
                param.put("action","saveProfBMI");
                param.put("tinggi", String.valueOf(tb));
                param.put("berat", String.valueOf(bb));
                param.put("usia", String.valueOf(umur));
                param.put("jk", String.valueOf(jk));
                param.put("aktif", String.valueOf(aktif));
                param.put("email", sharedprefs.getEmail());

                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(adder);
    }

    public void saveShare(){

    }

    public void hitungBMI(int tb, int bb, int umur, int jk, int aktif, int kadargula){
        sharedprefs.saveGula("gula",String.valueOf(kadargula));

        try {
            addToDb(tb, bb, umur, jk, aktif);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            
            sharedprefs.saveTinggi(sharedprefs.tinggi, String.valueOf(tb));
            sharedprefs.saveBerat(sharedprefs.berat, String.valueOf(bb));
            sharedprefs.saveUmur(sharedprefs.umur, String.valueOf(umur));
            sharedprefs.saveJK(sharedprefs.jk, String.valueOf(jk));
            sharedprefs.saveAktif(sharedprefs.aktif, String.valueOf(aktif));

            int kurangumur = 0;

            if (umur < 40) {
                kurangumur = 0;
            } else if (umur > 40 && umur < 60) {
                kurangumur = 5;
            } else if (umur >= 60 && umur < 70) {
                kurangumur = 10;
            } else if (umur > 70) {
                kurangumur = 20;
            }
            /////////////////////
            double tbt = (tb / 100.0);
            double BBI;
            double TB;
            if (jk == 25) {
                TB = Math.pow(tbt, 2);
                BBI = TB * 21.0;
            } else {
                TB = Math.pow(tbt, 2);
                BBI = TB * 22.5;
            }

            double basal;

            if (jk == 25) {
                basal = BBI * 25.0;
            } else {
                basal = BBI * 30.0;
            }

            double energi;
            double per = (aktif + 10 - kurangumur) / 100.0;
            energi = basal + (basal * per);

            double BMI = bb / TB;

            /////////////////////


            imb.setText("" + BMI);
            kkal.setText(energi + " Kkal");

            if (BMI < 17) {
                txtimb.setText("Kurus, kekurangan berat badan berat");
            } else if (BMI > 17 && BMI < 18.4) {
                txtimb.setText("Kurus, kekurangan berat badan ringan");
            } else if (BMI > 18.5 && BMI < 25) {
                txtimb.setText("Normal");
            } else if (BMI > 25.1 && BMI < 27) {
                txtimb.setText("Gemuk, kelebihan berat badan tingkat ringan");
            } else if (BMI > 27) {
                txtimb.setText("Gemuk, kelebihan berat badan tingkat berat");
            }


            if (kadargula < 90) {
                txtgul.setText("Gula Darah Rendah");
            } else if (kadargula >= 90 && kadargula <= 120) {
                txtgul.setText("Gula Darah Normal");
            } else if (kadargula > 120 && kadargula <= 240) {
                txtgul.setText("Gula Darah Tinggi");
            } else if (kadargula > 240) {
                txtgul.setText("Gula Darah Sangat Tinggi");
            }
            cardView.setVisibility(View.VISIBLE);
        }
    }
}
