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
    Bundle bundle;
    String TAG = "[RLog]";
    LinearLayout lBMI,lRec, foc;
    boolean isedit = false;
    Menu menus;
    String req;
    MenuItem save,close,edit;
    TextInputEditText jkl, jkp, ai,as,ar,ab,asb;
    TextInputEditText p,sp,s,ss,m,sm;
    AlertDialog.Builder aleBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        foc = findViewById(R.id.linefocus);
        lBMI = findViewById(R.id.lineBMI);
        lRec = findViewById(R.id.lineRec);
        aleBuilder = new AlertDialog.Builder(this,R.style.AlertDialog);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bundle = getIntent().getExtras();
        req = bundle.getString("req");
        if (req.equals("bmi")){
            getSupportActionBar().setTitle("Index BMI");
            openBMIMenu();
        }else
            if (req.equals("rec")){
                getSupportActionBar().setTitle("Index Rekomendasi");
                openRecMenu();
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void openBMIMenu(){
        lBMI.setVisibility(View.VISIBLE);
        lRec.setVisibility(View.GONE);

        jkl = findViewById(R.id.eBMIJKL);
        jkp = findViewById(R.id.eBMIJKP);
        ai = findViewById(R.id.eBMII);
        as = findViewById(R.id.eBMIS);
        ar = findViewById(R.id.eBMIR);
        ab = findViewById(R.id.eBMIB);
        asb  = findViewById(R.id.eBMISB);

    }

    public void openRecMenu(){
        lRec.setVisibility(View.VISIBLE);
        lBMI.setVisibility(View.GONE);

        p = findViewById(R.id.eRecP);
        sp = findViewById(R.id.eRecSP);
        s = findViewById(R.id.eRecS);
        ss = findViewById(R.id.eRecSS);
        m = findViewById(R.id.eRecM);
        sm = findViewById(R.id.eRecSM);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_index, menu);
        menus = menu;
        save = menus.findItem(R.id.saveindex);
        close = menus.findItem(R.id.closecancel);
        edit = menus.findItem(R.id.editindex);
        if (!isedit){
            save.setVisible(false);
            close.setVisible(false);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
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
                closemode(req);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closemode(String req) {
        if (req.equals("bmi")) {
            jkl.setEnabled(false);
            jkp.setEnabled(false);
            ai.setEnabled(false);
            as.setEnabled(false);
            ar.setEnabled(false);
            ab.setEnabled(false);
            asb.setEnabled(false);
        }else if (req.equals("rec")){
            p.setEnabled(false);
            sp.setEnabled(false);
            s.setEnabled(false);
            ss.setEnabled(false);
            m.setEnabled(false);
            sm.setEnabled(false);
        }
        foc.requestFocus();
    }

    private void editmode(String req) {
        if (req.equals("bmi")) {
            jkl.setEnabled(true);
            jkp.setEnabled(true);
            ai.setEnabled(true);
            as.setEnabled(true);
            ar.setEnabled(true);
            ab.setEnabled(true);
            asb.setEnabled(true);
        }else if (req.equals("rec")){
            p.setEnabled(true);
            sp.setEnabled(true);
            s.setEnabled(true);
            ss.setEnabled(true);
            m.setEnabled(true);
            sm.setEnabled(true);
        }
    }

    private void saveindex(String req) {
        if (req.equals("bmi")) {
            jkl.setEnabled(true);
            jkp.setEnabled(true);
            ai.setEnabled(true);
            as.setEnabled(true);
            ar.setEnabled(true);
            ab.setEnabled(true);
            asb.setEnabled(true);
        }else if (req.equals("rec")){
            p.setEnabled(true);
            sp.setEnabled(true);
            s.setEnabled(true);
            ss.setEnabled(true);
            m.setEnabled(true);
            sm.setEnabled(true);
        }
        foc.requestFocus();
    }

    public void updateview(){
        if (!isedit){
            edit.setVisible(true);
            save.setVisible(false);
            close.setVisible(false);
        }else {
            edit.setVisible(false);
            save.setVisible(true);
            close.setVisible(true);
        }
    }

    public void openalert(String mode){
        if (mode.equals("edit")) {
            aleBuilder.setTitle("Edit Index");
            aleBuilder
                    .setMessage("Edit index sangat sensitif, Apakah anda yakin lanjut edit index?")
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Ya, Lanjut", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            editmode(req);
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
                            saveindex(req);
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
    //after get, set to text temp for cancel

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}