package com.polije.gizielectree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.polije.gizielectree.Utils.Sharedprefs;
import com.polije.gizielectree.Utils.ToastBaseCust;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AkunActivity extends AppCompatActivity {

    LinearLayout foc;
    Menu menus;
    MenuItem save, close, edit;
    boolean isedit = false;
    AlertDialog.Builder aleBuilder;
    TextInputEditText e, n, jk;
    Sharedprefs sharedprefs;
    WebApiService apiService;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        foc = findViewById(R.id.linefos);
        apiService = new WebApiService();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        sharedprefs = new Sharedprefs(this);

        e = findViewById(R.id.profilemail);
        n = findViewById(R.id.profilnama);
        jk = findViewById(R.id.profiljk);

        n.setText(sharedprefs.getuser());
        if (sharedprefs.getJK().equals("25")){
            jk.setText("Perempuan");
        }else {
            jk.setText("Laki - Laki");
        }

        closemode();
        e.requestFocus();
        e.setText(sharedprefs.getEmail());

        aleBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Akun");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_profil, menu);
        menus = menu;
        save = menus.findItem(R.id.saveindex);
        close = menus.findItem(R.id.closecancel);
        edit = menus.findItem(R.id.editindex);
        if (!isedit) {
            save.setVisible(false);
            close.setVisible(false);
        }
        return true;
    }

    private void closemode() {
        n.setEnabled(false);
        jk.setEnabled(false);
        foc.requestFocus();
    }

    private void editmode() {
        n.setEnabled(true);
        jk.setEnabled(true);
    }

    private void saveindex() {
        n.setEnabled(true);
        jk.setEnabled(true);
        foc.requestFocus();
    }

    public void updateview() {
        if (!isedit) {
            edit.setVisible(true);
            save.setVisible(false);
            close.setVisible(false);
            foc.requestFocus();
        } else {
            edit.setVisible(false);
            save.setVisible(true);
            close.setVisible(true);
            foc.requestFocus();
        }
    }

    public void openalert(String mode) {
        if (mode.equals("edit")) {
            aleBuilder.setTitle("Edit Profile");
            aleBuilder
                    .setMessage("Apakah anda yakin lanjut mengubah profile?")
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Ya, Lanjut", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            editmode();
                            updateview();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        } else if (mode.equals("save")) {
            aleBuilder.setTitle("Simpan Profile");
            aleBuilder
                    .setMessage("Apakah anda yakin lanjut menyimpan profile terbaru?")
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Ya, Lanjut", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saving(n.getText().toString(),e.getText().toString());
                            saveindex();
                            updateview();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }
        AlertDialog alertDialog = aleBuilder.create();
        alertDialog.show();
    }

    public void saving(String snama, String semail){
        StringRequest adder = new StringRequest(Request.Method.POST, apiService.getApi_url() + "user_master.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(AkunActivity.this, "OKE", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        foc.requestFocus();
                    }else {
                        Toast.makeText(AkunActivity.this, "GAGAL", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        foc.requestFocus();
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
                param.put("action","saveProfile");
                param.put("nama",snama);
                param.put("email",semail);
                Log.d("TAG", "getParams: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(adder);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editindex:
                isedit = true;
                openalert("edit");
                return true;

            case R.id.saveindex:
                isedit = false;
                openalert("save");
                updateview();
                closemode();
                return true;

            case R.id.closecancel:
                isedit = false;
                updateview();
                closemode();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}