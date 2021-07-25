package com.polije.gizielectree;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GrafikBmi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_bmi);

        //
        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> jam = new ArrayList<>();
        jam.add(new BarEntry(2014, 2350));
        jam.add(new BarEntry(2015, 2350));
        jam.add(new BarEntry( 2016, 2353));
        jam.add(new BarEntry(2017, 2355));
        jam.add(new BarEntry(2018, 2353));
        jam.add(new BarEntry(2019, 2357));
        jam.add(new BarEntry(2020, 2352));

        BarDataSet barDataSet = new BarDataSet(jam,"Jam");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart Kalori");
        barChart.animateY(2000);


        //
        BarChart barChart2 = findViewById(R.id.barChart2);

        ArrayList<BarEntry> jam2 = new ArrayList<>();
        jam2.add(new BarEntry(2014, 240));
        jam2.add(new BarEntry( 2015, 242));
        jam2.add(new BarEntry( 2016, 243));
        jam2.add(new BarEntry( 2017, 220));
        jam2.add(new BarEntry(2018, 240));
        jam2.add(new BarEntry( 2019, 246));
        jam2.add(new BarEntry( 2020, 242));

        BarDataSet barDataSet2 = new BarDataSet(jam2,"Jam");
        barDataSet2.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet2.setValueTextColor(Color.BLACK);
        barDataSet2.setValueTextSize(16f);

        BarData barData2 = new BarData(barDataSet2);

        barChart2.setFitBars(true);
        barChart2.setData(barData2);
        barChart2.getDescription().setText("Bar Chart Gula Darah");
        barChart2.animateY(2000);
    }
}