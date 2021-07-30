package com.polije.gizielectree;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.polije.gizielectree.AdapModel.ModelGula;
import com.polije.gizielectree.Utils.SqliteHelpers;

import java.util.ArrayList;
import java.util.List;

public class CatatanFragment extends Fragment {
    SqliteHelpers helpers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_catatan, container, false);

        BarChart barChart = root.findViewById(R.id.barChart);
        helpers = new SqliteHelpers(getContext());

        ArrayList<BarEntry> kgul = new ArrayList<>();

        List<ModelGula> list = new ArrayList<>();
        list = helpers.getGula();
        for (int j = 0; j < list.size(); j++) {
            String tgl = list.get(j).getTgl();
            int gula = list.get(j).getGula();
            kgul.add(new BarEntry(Integer.parseInt(tgl),gula));
        }
        BarDataSet barDataSet = new BarDataSet(kgul,"Gula Darah");

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Gula Darah / Tanggal");
        barChart.animateY(2000);

        return root;
    }
}