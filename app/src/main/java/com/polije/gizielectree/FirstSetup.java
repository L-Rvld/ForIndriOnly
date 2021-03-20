package com.polije.gizielectree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polije.gizielectree.Utils.Sharedprefs;
import com.polije.gizielectree.Utils.SliderAdapter;

public class FirstSetup extends AppCompatActivity {
    private ViewPager viewpager;
    private LinearLayout liner;
    private SliderAdapter myadapter;

    private TextView[] mdots;
    private Button next,back;

    private int mCureentPage;

    Sharedprefs sharedprefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);
        viewpager=(ViewPager)findViewById(R.id.viewpager);
        liner=(LinearLayout)findViewById(R.id.dots);
        sharedprefs = new Sharedprefs(this);

        next=(Button)findViewById(R.id.nextBtn);
        back=(Button)findViewById(R.id.backBtn);

        myadapter=new SliderAdapter(this);
        viewpager.setAdapter(myadapter);
        adddots(0);

        viewpager.addOnPageChangeListener(viewlistener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(mCureentPage+1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(mCureentPage-1);
            }
        });
    }

    public void adddots(int i){

        mdots=new TextView[4];
        liner.removeAllViews();

        for (int x=0;x<mdots.length;x++){

            mdots[x]=new TextView(this);
            mdots[x].setText(Html.fromHtml("&#8226;"));
            mdots[x].setTextSize(35);
            mdots[x].setTextColor(getResources().getColor(R.color.colorGrey));

            liner.addView(mdots[x]);
        }
        if (mdots.length>0){

            mdots[i].setTextColor(getResources().getColor(R.color.colorWhite));

        }

    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            adddots(position);
            mCureentPage = position;

            if (position==0){
                next.setEnabled(true);
                back.setEnabled(false);
                back.setVisibility(View.INVISIBLE);

                next.setText("NEXT");
                back.setText("");
            }
            else if(position==mdots.length-1){

                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);

                next.setText("FINISH");
                back.setText("BACK");
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedprefs.saveFirst(sharedprefs.first,true);
                        if (sharedprefs.getonLogin().equals(true)) {
                            startActivity(new Intent(FirstSetup.this, MainActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(FirstSetup.this, Login.class));
                            finish();
                        }
                    }
                });

            }
            else {
                next.setEnabled(true);
                back.setEnabled(true );
                back.setVisibility(View.VISIBLE);

                next.setText("NEXT");
                back.setText("BACK");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}