package com.polije.gizielectree;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.polije.gizielectree.AdapModel.AdapTemp;
import com.polije.gizielectree.AdapModel.AdapterRekomendasi;
import com.polije.gizielectree.AdapModel.ModelSave;
import com.polije.gizielectree.Utils.Sharedprefs;
import com.polije.gizielectree.Utils.SqliteHelpers;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CatatanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ProgressBar pg;
    ProgressDialog progressDialog;
    TextView kcalmu, kcalreq, txt, kp,ksp,ks,kss,km,ksm;
    Sharedprefs sharedprefs;
    SqliteHelpers helpers;
    List<ModelSave> list;
    Integer rekomenergi = 0;
    SwipeRefreshLayout refreshLayout;
    double totalK, aktif, bb, umur;
    int nilaijk;
    int kalP, kalSP, kalS, kalSS, kalM, kalSM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_catatan, container, false);
        pg = root.findViewById(R.id.pgCal);
        kcalmu = root.findViewById(R.id.kcalMu);
        kcalreq = root.findViewById(R.id.kcalReq);
        progressDialog = new ProgressDialog(getContext());
        helpers = new SqliteHelpers(getContext());
        sharedprefs = new Sharedprefs(getContext());
        String jk = sharedprefs.getJK();
        list = new ArrayList<>();
        kp = root.findViewById(R.id.kkalP);
        ksp= root.findViewById(R.id.kkalSP);
        ks = root.findViewById(R.id.kkalS);
        kss = root.findViewById(R.id.kkalSS);
        km = root.findViewById(R.id.kkalM);
        ksm = root.findViewById(R.id.kkalSM);
        txt = root.findViewById(R.id.txtTidakAdaSave);
        refreshLayout = root.findViewById(R.id.resfreshCatatan);
        refreshLayout.setOnRefreshListener(this);

        bb = Double.parseDouble(sharedprefs.getBerat());
        aktif = Double.parseDouble(sharedprefs.getAktif());
        umur = Double.parseDouble(sharedprefs.getUmur());


        if (jk == "L") {
            nilaijk = 30;
        } else {
            nilaijk = 25;
        }

        getRekom(nilaijk, bb, aktif, umur);

        return root;
    }

    @Override
    public void onRefresh() {
        getRekom(nilaijk, bb, aktif, umur);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 5000);
    }

    public void getRekom(double jk, double bb, double aktif, double umur) {
        totalK = 0;
        rekomenergi =0;
        kalP = 0;
        kalSP = 0;
        kalS = 0;
        kalSS = 0;
        kalM = 0;
        kalS = 0;
        int kurangumur = 0;
        if (umur < 40) {
            kurangumur = 0;
        } else if (umur > 40 && umur < 60) {
            kurangumur = 5;
        } else if (umur >= 60 && umur < 70) {
            kurangumur = 10;
        } else if (umur > 70) {
            kurangumur = 20;
        }
        double jkbb = jk * bb;
        double kBasal = (jkbb * aktif) / 100;
        double Kalori = jkbb + kBasal;
        double ku = (Kalori * kurangumur) / 100;
        totalK = Kalori - ku;
        kcalmu.setText(totalK + " Kkal");

        progressDialog.setTitle("Menampilkan Rekomendasi Anda");
        progressDialog.show();
        showRekom();
    }

    public void showRekom() {
        list = helpers.getRekomSave();
        if (list.isEmpty()) {
            txt.setVisibility(View.VISIBLE);
        } else {
            txt.setVisibility(View.GONE);
            getAllData(list);
        }
        progressDialog.dismiss();
    }

    public void getAllData(List<ModelSave> lists) {
        final int[] energi = {0};
        for (int i = 0; i < lists.size(); i++) {
            energi[0] = Integer.parseInt(lists.get(i).getEnergi());
            rekomenergi += energi[0];
            int persen = (int) ((rekomenergi / totalK) * 100);
            pg.setProgress(persen);
            kcalreq.setText(rekomenergi + " Kkal");

            String hari = lists.get(i).getHari();
            switch (hari){
                case "pagi":
                    kalP += Integer.parseInt(lists.get(i).getEnergi());
                    kp.setText(kalP+" kkal");
                    break;
                case "siang":
                    kalS += Integer.parseInt(lists.get(i).getEnergi());
                    ks.setText(kalS+" kkal");
                    break;
                case "malam":
                    kalM += Integer.parseInt(lists.get(i).getEnergi());
                    km.setText(kalM+" kkal");
                    break;
                case "snackpagi":
                    kalSP += Integer.parseInt(lists.get(i).getEnergi());
                    ksp.setText(kalSP+" kkal");
                    break;
                case "snacksiang":
                    kalSS += Integer.parseInt(lists.get(i).getEnergi());
                    kss.setText(kalSS+" kkal");
                    break;
                case "snackmalam":
                    kalSM += Integer.parseInt(lists.get(i).getEnergi());
                    km.setText(kalSM+" kkal");
                    break;
            }
        }
    }
}