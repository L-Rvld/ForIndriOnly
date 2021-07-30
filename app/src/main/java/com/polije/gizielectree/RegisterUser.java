package com.polije.gizielectree;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    TextInputEditText user, email, pass, repass, nama, umur, tinggi,berat;
    EditText pekerjaan;
    RadioGroup jk;
    String api;
    TextView poin;
    Button btnregister;
    WebApiService webApiService;
    Bundle bundle;
    ArrayList<String> aktifitas = new ArrayList<>();
    ArrayList<String> point = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        user = findViewById(R.id.etUserUser);
        email = findViewById(R.id.etEmailUser);
        pass = findViewById(R.id.etPassUser);
        repass = findViewById(R.id.etPassRepUser);
        nama = findViewById(R.id.etNamaUser);
        umur = findViewById(R.id.etUmurUser);
        tinggi = findViewById(R.id.etTinggiUser);
        berat = findViewById(R.id.etBeratUser);
        pekerjaan = findViewById(R.id.etPekerjaanUser);
        jk = findViewById(R.id.opsiJK);
        btnregister = findViewById(R.id.btnRegUser);
        poin = findViewById(R.id.poin);

        webApiService = new WebApiService();
        api = webApiService.getApi_url();
        bundle = getIntent().getExtras();

        getPekerjaan();

        if (!bundle.isEmpty()) {
            user.setText(bundle.getString("user"));
            email.setText(bundle.getString("email"));
            pass.setText(bundle.getString("pass"));
        }else {
            Toast.makeText(this, "Terjadi Kesalahan. Ulangi lagi", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        final ArrayAdapter<String> adapterAktif = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, aktifitas);
        pekerjaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(RegisterUser.this).setTitle("Pilih aktifitas anda :").setAdapter(adapterAktif, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pekerjaan.setText(aktifitas.get(i).toString());
                        int p = Integer.parseInt(point.get(i))*10;
                        poin.setText(""+p);
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText())){
                    Toast.makeText(RegisterUser.this, "Email Kosong", Toast.LENGTH_SHORT).show();
                    email.setError("Masukan Email");
                }else if (TextUtils.isEmpty(user.getText())){
                    Toast.makeText(RegisterUser.this, "Username Kosong", Toast.LENGTH_SHORT).show();
                    user.setError("Masukan Username");
                }else if (TextUtils.isEmpty(pass.getText())){
                    Toast.makeText(RegisterUser.this, "Password Kosong", Toast.LENGTH_SHORT).show();
                    pass.setError("Masukan Password");
                }else if (TextUtils.isEmpty(repass.getText())) {
                    Toast.makeText(RegisterUser.this, "Re-Password Kosong", Toast.LENGTH_SHORT).show();
                    repass.setError("Masukan Ulang Password");
                }else if (TextUtils.isEmpty(nama.getText())){
                    Toast.makeText(RegisterUser.this, "Nama Kosong", Toast.LENGTH_SHORT).show();
                    nama.setError("Masukan Nama");
                }else if(jk.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterUser.this, "Jenis Kelamin belum di isi", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(umur.getText())){
                    Toast.makeText(RegisterUser.this, "Umur Kosong", Toast.LENGTH_SHORT).show();
                    umur.setError("Masukan Umur");
                }else if (TextUtils.isEmpty(tinggi.getText())){
                    Toast.makeText(RegisterUser.this, "Tinggi Kosong", Toast.LENGTH_SHORT).show();
                    tinggi.setError("Masukan Tinggi");
                }else if (TextUtils.isEmpty(berat.getText())){
                    Toast.makeText(RegisterUser.this, "Berat Kosong", Toast.LENGTH_SHORT).show();
                    berat.setError("Masukan Berat");
                }else if (TextUtils.isEmpty(pekerjaan.getText())){
                    Toast.makeText(RegisterUser.this, "Pekerjaan Kosong", Toast.LENGTH_SHORT).show();
                    pekerjaan.setError("Masukan Pekerjaan");
                }if (repass.getText().toString().equals(pass.getText().toString())&&!TextUtils.isEmpty(email.getText())&&!TextUtils.isEmpty(user.getText())&&!TextUtils.isEmpty(pass.getText())&&!TextUtils.isEmpty(nama.getText())
                    &&(jk.getCheckedRadioButtonId() != -1)&&!TextUtils.isEmpty(umur.getText())&&!TextUtils.isEmpty(tinggi.getText())&&!TextUtils.isEmpty(berat.getText())&&!TextUtils.isEmpty(pekerjaan.getText())){
                    int selected=jk.getCheckedRadioButtonId();
                    RadioButton gender=(RadioButton) findViewById(selected);
                    String jnskl;
                    if (gender.getText().toString().equals("Perempuan")){
                        jnskl = "P";
                    }else {
                        jnskl = "L";
                    }
                    reqReg(email.getText().toString(), user.getText().toString(), pass.getText().toString(),nama.getText().toString(),jnskl
                    ,umur.getText().toString(),tinggi.getText().toString(),berat.getText().toString(),poin.getText().toString());
                }else {
                    repass.setError("Password Tidak Sama");
                }
            }
        });
    }
    public void getPekerjaan(){
        StringRequest ambilData = new StringRequest(Request.Method.GET, webApiService.getApi_url() + "getPekerjaan.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        aktifitas.add(jsonObject1.getString("pek"));
                        point.add(jsonObject1.getString("nilai"));
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
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(ambilData);
    }

    public void reqReg(final String email, final String user, final String pass, final String nama, final String jk, final String umur, final String tinggi, final String berat, final String aktif){
        StringRequest register = new StringRequest(Request.Method.POST, api + "/" + getString(R.string.registerUser), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("true")) {
                        startActivity(new Intent(RegisterUser.this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Gagal register", Toast.LENGTH_SHORT).show();
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
                param.put("username",user);
                param.put("password",pass);
                param.put("nama",nama);
                param.put("jk",jk);
                param.put("umur",umur);
                param.put("tinggi",tinggi);
                param.put("berat",berat);
                param.put("aktifitas",aktif);
                Log.d("TAG", "getParams: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(register);
    }
}