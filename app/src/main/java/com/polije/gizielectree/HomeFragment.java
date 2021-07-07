package com.polije.gizielectree;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.polije.gizielectree.Utils.Sharedprefs;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeFragment extends Fragment {
    CardView rekomendasi,tentang, bmi,data, akun;
    CarouselView carouselView;
    TextView nama;
    Sharedprefs sharedprefs;
    int[] imageP= {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        rekomendasi = root.findViewById(R.id.cardRekom);
        data = root.findViewById(R.id.cardData);
        akun = root.findViewById(R.id.cardAcc);
        bmi = root.findViewById(R.id.cardBmi);
        tentang = root.findViewById(R.id.cardInfo);
        carouselView = root.findViewById(R.id.carouselView);
        carouselView.setPageCount(imageP.length);
        carouselView.setImageListener(imageListener);
        nama = root.findViewById(R.id.txtHomeName);
        sharedprefs = new Sharedprefs(getContext());
        nama.setText(sharedprefs.getuser());


        rekomendasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),RekomendasiActivity.class));
            }
        });
        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BMI.class));
            }
        });
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),DataActivity.class).putExtra("sendData","null"));
            }
        });
        tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),TentangActivity.class));
            }
        });
        akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AkunActivity.class));
            }
        });
        return root;
    }
    public ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(imageP[position]);
        }
    };
}