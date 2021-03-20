package com.polije.gizielectree;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
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

public class BMI extends AppCompatActivity {
    EditText pekerjaan, usia, tb,bb;
    Button hitung;
    TextView poin, kkal, imb, txtimb;
    RadioGroup jk;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Hitung Kalori");

        pekerjaan = findViewById(R.id.Aktifitas);
        poin = findViewById(R.id.aktifpoin);
        usia =findViewById(R.id.usia);
        tb = findViewById(R.id.tinggi);
        jk = findViewById(R.id.opsijenk);
        bb =findViewById(R.id.berat);
        hitung = findViewById(R.id.hitungbmi);
        kkal = findViewById(R.id.txtKalor);
        imb = findViewById(R.id.txtBMI);
        txtimb = findViewById(R.id.txtketBmi);
        cardView = findViewById(R.id.cardafterhitung);

        hitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tb.getText())){
                    Toast.makeText(BMI.this, "Tinggi Badan Kosong", Toast.LENGTH_SHORT).show();
                    tb.setError("Masukan Tinggi Badan");
                }else if (TextUtils.isEmpty(bb.getText())){
                    Toast.makeText(BMI.this, "Berat Badan Kosong", Toast.LENGTH_SHORT).show();
                    bb.setError("Masukan Berat Badan");
                }else if (TextUtils.isEmpty(usia.getText())){
                    Toast.makeText(BMI.this, "Umur Kosong", Toast.LENGTH_SHORT).show();
                    usia.setError("Masukan Umur");
                } else if(jk.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(BMI.this, "Jenis Kelamin belum di isi", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(pekerjaan.getText())){
                    Toast.makeText(BMI.this, "Aktifitas Kosong", Toast.LENGTH_SHORT).show();
                    pekerjaan.setError("Masukan Aktifitas");
                }if (!TextUtils.isEmpty(tb.getText())&&!TextUtils.isEmpty(bb.getText())&&!TextUtils.isEmpty(usia.getText())&&jk.getCheckedRadioButtonId() != -1&&!TextUtils.isEmpty(pekerjaan.getText())){
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
                    hitungBMI(Integer.parseInt(tb.getText().toString()),Integer.parseInt(bb.getText().toString()),Integer.parseInt(usia.getText().toString()),jnskl,Integer.parseInt(poin.getText().toString()));
                }
            }
        });

        final String [] namaAktif ={"Istirahat","Ringan","Sedang","Berat","Sangat Berat"};
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
//                        Toast.makeText(BMI.this, poin.getText().toString(), Toast.LENGTH_SHORT).show();
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
    public void hitungBMI(int tb, int bb, int umur, int jk, int aktif){
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

        double tbt = (tb / 100)^2;
        double BMI = bb / tbt;

        double jkbb = jk * bb;
        double kBasal = (jkbb * aktif) / 100;
        double Kalori =  jkbb + kBasal;
        double ku = (Kalori * kurangumur) / 100;
        double totalK = Kalori - ku;

        imb.setText(""+BMI);
        kkal.setText(totalK+" Kkal");

        if (BMI<17){
            txtimb.setText("Kurus, kekurangan berat badan berat");
        }else if (BMI>17&&BMI<18.4) {
            txtimb.setText("Kurus, kekurangan berat badan ringan");
        }else if (BMI>18.5&&BMI<25){
            txtimb.setText("Normal");
        } else if (BMI>25.1&&BMI<27){
            txtimb.setText("Gemuk, kelebihan berat badan tingkat ringan");
        }else if (BMI>27){
            txtimb.setText("Gemuk, kelebihan berat badan tingkat berat");
        }

        cardView.setVisibility(View.VISIBLE);
    }
}
