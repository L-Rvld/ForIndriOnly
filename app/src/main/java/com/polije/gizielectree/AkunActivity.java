package com.polije.gizielectree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.polije.gizielectree.Utils.Sharedprefs;

public class AkunActivity extends AppCompatActivity {

    LinearLayout foc;
    Menu menus;
    MenuItem save, close, edit;
    boolean isedit = false;
    AlertDialog.Builder aleBuilder;
    TextInputEditText e, n, jk;
    Sharedprefs sharedprefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);
        foc = findViewById(R.id.linefos);

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
        } else {
            edit.setVisible(false);
            save.setVisible(true);
            close.setVisible(true);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}