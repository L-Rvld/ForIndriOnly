package com.polije.gizielectree.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.polije.gizielectree.Login;
import com.polije.gizielectree.R;

public class IndexActivity extends AppCompatActivity {
    String TAG = "[RLog]";
    LinearLayout lBMI, foc;
    boolean isedit = false;
    Menu menus;
    MenuItem save, close, edit, list;
    TextInputEditText i, r, s, b, sb;
    AlertDialog.Builder aleBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        foc = findViewById(R.id.linefocus);
        lBMI = findViewById(R.id.lineBMI);
        i = findViewById(R.id.eBMII);
        r = findViewById(R.id.eBMIR);
        s = findViewById(R.id.eBMIS);
        b = findViewById(R.id.eBMIB);
        sb = findViewById(R.id.eBMISB);
        aleBuilder = new AlertDialog.Builder(this, R.style.AlertDialog);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_index, menu);
        menus = menu;
        save = menus.findItem(R.id.saveindex);
        close = menus.findItem(R.id.closecancel);
        edit = menus.findItem(R.id.editindex);
        list = menus.findItem(R.id.listing);
        if (!isedit) {
            save.setVisible(false);
            close.setVisible(false);
        }
        return true;
    }

    public void openList(){
        startActivity(new Intent(IndexActivity.this,ListingAktifActivity.class));
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
                return true;

            case R.id.closecancel:
                isedit = false;
                updateview();
                closemode();
                return true;

            case R.id.listing:
                openList();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closemode() {
        i.setEnabled(false);
        r.setEnabled(false);
        s.setEnabled(false);
        b.setEnabled(false);
        sb.setEnabled(false);
        foc.requestFocus();
    }

    private void editmode() {
        i.setEnabled(true);
        s.setEnabled(true);
        r.setEnabled(true);
        b.setEnabled(true);
        sb.setEnabled(true);
    }

    private void saveindex() {
        i.setEnabled(true);
        s.setEnabled(true);
        r.setEnabled(true);
        b.setEnabled(true);
        sb.setEnabled(true);
        foc.requestFocus();
    }

    public void updateview() {
        if (!isedit) {
            edit.setVisible(true);
            save.setVisible(false);
            close.setVisible(false);
        } else {
            edit.setVisible(false);
            save.setVisible(true);
            close.setVisible(true);
        }
    }

    public void openalert(String mode) {
        if (mode.equals("edit")) {
            aleBuilder.setTitle("Edit Index");
            aleBuilder
                    .setMessage("Edit index sangat sensitif, Apakah anda yakin lanjut edit index?")
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
            aleBuilder.setTitle("Simpan Index");
            aleBuilder
                    .setMessage("Index akan berubah dan sangat sensitif, Apakah anda yakin lanjut menyimpan index terbaru?")
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Ya, Lanjut", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}