package com.polije.gizielectree.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.polije.gizielectree.Login;
import com.polije.gizielectree.R;
import com.polije.gizielectree.Register;
import com.polije.gizielectree.RegisterUser;
import com.polije.gizielectree.Utils.Sharedprefs;
import com.polije.gizielectree.Utils.SqliteHelpers;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminMainActivity extends AppCompatActivity {
    Button chngPass, setPass;
    ImageButton close;
    TextInputEditText edtOldPass,edtNewPass, edtVPass;
    CardView gotoList, gotoMaster;
    Dialog dialog;
    Sharedprefs sharedprefs;
    SqliteHelpers helpers;
    WebApiService webApiService;
    String api;
    AlertDialog.Builder aleBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        chngPass = findViewById(R.id.btnChangePass);
        gotoList = findViewById(R.id.gotoListUser);
        gotoMaster = findViewById(R.id.gotoDataMaster);
        dialog = new Dialog(this,android.R.style.ThemeOverlay_Material);
        dialog.setContentView(R.layout.admin_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        sharedprefs = new Sharedprefs(this);
        helpers = new SqliteHelpers(this);
        webApiService = new WebApiService();
        api = webApiService.getApi_url();
        aleBuilder = new AlertDialog.Builder(this);

        getSupportActionBar().setTitle("GiziElectree - [ADMIN]");

        chngPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

//        indxB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(AdminMainActivity.this,IndexActivity.class)
//                        .putExtra("req","bmi"));
//            }
//        });
        gotoMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this,DatamasterActivity.class));
            }
        });
        gotoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this,ListuseronlyActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutAdmin) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        aleBuilder.setTitle("Logout");

        aleBuilder
                .setMessage("Anda yakin keluar dari Admin ?\nKlik Ya untuk keluar")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        sharedprefs.clearing();
                        helpers.clear();
                        startActivity(new Intent(AdminMainActivity.this, Login.class));
                        finish();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = aleBuilder.create();
        alertDialog.show();

    }
    public void openDialog(){
        dialog.show();
        chngPass = dialog.findViewById(R.id.btnsetnewpass);
        close = dialog.findViewById(R.id.btnClose);
        edtNewPass = dialog.findViewById(R.id.etnewpass);
        edtOldPass = dialog.findViewById(R.id.etoldpass);
        edtVPass = dialog.findViewById(R.id.etnewpassver);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        chngPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtOldPass.getText().toString())&&!TextUtils.isEmpty(edtNewPass.getText().toString())&&!TextUtils.isEmpty(edtOldPass.getText().toString())) {
                    if (edtNewPass.getText().toString().equals(edtVPass.getText().toString())) {
                        Log.d("[RLOG]", "SetPass trigerred");
                        setNewPass(edtNewPass.getText().toString());
                    } else {
                        Toast.makeText(AdminMainActivity.this, "Password Baru tidak sesuai, ulangi", Toast.LENGTH_SHORT).show();
                    }
                }else if (TextUtils.isEmpty(edtOldPass.getText().toString())
                        &&TextUtils.isEmpty(edtNewPass.getText().toString())
                        &&TextUtils.isEmpty(edtOldPass.getText().toString()))
                {
                    edtOldPass.setError("Isi Password Lama");
                    edtNewPass.setError("Isi Password Baru");
                    edtVPass.setError("Isi Ulang Password Baru");
                }else if (TextUtils.isEmpty(edtOldPass.getText().toString())) {
                    edtOldPass.setError("Isi Password Lama");
                }else if (TextUtils.isEmpty(edtNewPass.getText().toString())) {
                    edtNewPass.setError("Isi Password Baru");
                }else if (TextUtils.isEmpty(edtVPass.getText().toString())) {
                    edtVPass.setError("Isi Ulang Password Baru");
                }
            }
        });
    }
    public void setNewPass(String newPass){
        StringRequest setnewPass = new StringRequest(Request.Method.POST, api + "/" + getString(R.string.newPass), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("true")) {
                        edtNewPass.setText("");
                        edtOldPass.setText("");
                        edtVPass.setText("");
                        dialog.dismiss();
                        Toast.makeText(AdminMainActivity.this, "Password Baru Berhasil Diganti", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminMainActivity.this, "Password Baru Gagal Diganti, Ulangi", Toast.LENGTH_SHORT).show();
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
                param.put("newpass",newPass);
                Log.d("TAG", "getParams: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(setnewPass);
    }
}