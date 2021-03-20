package com.polije.gizielectree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.polije.gizielectree.Admin.AdminMainActivity;
import com.polije.gizielectree.Utils.Sharedprefs;

public class Splashscreen extends AppCompatActivity {
    Sharedprefs sharedprefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        sharedprefs = new Sharedprefs(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedprefs.getFirst().equals(false)) {
                    startActivity(new Intent(Splashscreen.this, FirstSetup.class));
                    finish();
                }else{
                    if (sharedprefs.getonLogin().equals(true)) {
                        if (sharedprefs.getLevel().equals(false)) {
                            startActivity(new Intent(Splashscreen.this, MainActivity.class));
                            finish();
                        }else{
                            startActivity(new Intent(Splashscreen.this, AdminMainActivity.class));
                            finish();
                        }
                    } else {
                        startActivity(new Intent(Splashscreen.this, Login.class));
                        finish();
                    }
                }
            }
        },1500);
    }
}