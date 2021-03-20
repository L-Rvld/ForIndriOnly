package com.polije.gizielectree.AdapModel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.polije.gizielectree.DataActivity;
import com.polije.gizielectree.DetailActivity;
import com.polije.gizielectree.R;
import com.polije.gizielectree.RekomendasiActivity;

import java.util.ArrayList;

public class AdapterRekomendasi extends RecyclerView.Adapter<AdapterRekomendasi.DataViewHolder> {
    Context context;
    public ArrayList<ModelRekomendasi> dataList;
    String hari;

    public AdapterRekomendasi(Context context, ArrayList<ModelRekomendasi> dataList, String hari) {
        this.context = context;
        this.hari = hari;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AdapterRekomendasi.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_rekom,parent,false);
        return new DataViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRekomendasi.DataViewHolder holder, int position) {
        holder.ID.setText(dataList.get(position).getId());
        holder.nama.setText(dataList.get(position).getNama());
        holder.sumber.setText(dataList.get(position).getSumber());
        holder.air.setText(dataList.get(position).getAir());
        holder.energi.setText("Energi : "+dataList.get(position).getEnergi()+" Kkal");
        holder.protein.setText(dataList.get(position).getProtein());
        holder.lemak.setText(dataList.get(position).getLemak());
        holder.kh.setText(dataList.get(position).getKh());
        holder.serat.setText(dataList.get(position).getSerat());
        holder.abu.setText(dataList.get(position).getAbu());
        holder.kalsium.setText(dataList.get(position).getKalsium());
        holder.fosfor.setText(dataList.get(position).getFosfor());
        holder.besi.setText(dataList.get(position).getBesi());
        holder.natrium.setText(dataList.get(position).getNatrium());
        holder.kalium.setText(dataList.get(position).getKalium());
        holder.tembaga.setText(dataList.get(position).getTembaga());
        holder.seng.setText(dataList.get(position).getSeng());
        holder.retinol.setText(dataList.get(position).getRetinol());
        holder.b_kar.setText(dataList.get(position).getBkar());
        holder.kar_total.setText(dataList.get(position).getKartot());
        holder.thiamin.setText(dataList.get(position).getThiamin());
        holder.riboflavin.setText(dataList.get(position).getRiboflavin());
        holder.niasin.setText(dataList.get(position).getNiasin());
        holder.vitc.setText(dataList.get(position).getVitc());
        String s;
        if (dataList.get(position).getBdd().equals("0")){
            s  = "Porsi : - ";
        }else {
            s = "Porsi : "+dataList.get(position).getBdd()+" gr";
        }
        holder.ganti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(position, dataList.get(position).getId());
            }
        });
        holder.bdd.setText(s);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DetailActivity.class)
                        .putExtra("id",dataList.get(position).getId())
                        .putExtra("nama",dataList.get(position).getNama())
                        .putExtra("sumber",dataList.get(position).getSumber())
                        .putExtra("air",dataList.get(position).getAir())
                        .putExtra("energi",dataList.get(position).getEnergi())
                        .putExtra("protein",dataList.get(position).getProtein())
                        .putExtra("lemak",dataList.get(position).getLemak())
                        .putExtra("kh",dataList.get(position).getKh())
                        .putExtra("serat",dataList.get(position).getSerat())
                        .putExtra("kalsium",dataList.get(position).getKalsium())
                        .putExtra("fosfor",dataList.get(position).getFosfor())
                        .putExtra("besi",dataList.get(position).getBesi())
                        .putExtra("natrium",dataList.get(position).getNatrium())
                        .putExtra("kalium",dataList.get(position).getKalium())
                        .putExtra("tembaga",dataList.get(position).getTembaga())
                        .putExtra("seng",dataList.get(position).getSeng())
                        .putExtra("retinol",dataList.get(position).getRetinol())
                        .putExtra("bkar",dataList.get(position).getBkar())
                        .putExtra("kartot",dataList.get(position).getKartot())
                        .putExtra("thiamin",dataList.get(position).getThiamin())
                        .putExtra("riboflavin",dataList.get(position).getRiboflavin())
                        .putExtra("niasin",dataList.get(position).getNiasin())
                        .putExtra("vitc",dataList.get(position).getVitc())
                        .putExtra("bdd",dataList.get(position).getBdd()));
            }
        });

    }

    public void openDialog(int position, String id){
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(new Intent("sendFromRekom")
                        .putExtra("hari",hari)
                .putExtra("position",position)
                .putExtra("id",id));
    }

    @Override
    public int getItemCount() {
        if (dataList != null){
            return dataList.size();
        }
        return 0;
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        TextView ID, nama, sumber, air, energi,protein,lemak,kh,serat,abu,kalsium,fosfor,besi,natrium,kalium,tembaga,seng,retinol,b_kar, kar_total,thiamin,riboflavin,niasin,vitc,bdd;
        LinearLayout layout, ganti;
        public DataViewHolder(View itemView) {
            super(itemView);
            ganti = itemView.findViewById(R.id.rekomGanti);
            layout = itemView.findViewById(R.id.layGoDetail);
            ID = itemView.findViewById(R.id.rekomID);
            nama = itemView.findViewById(R.id.rekomNama);
            sumber = itemView.findViewById(R.id.rekomSumber);
            air = itemView.findViewById(R.id.rekomAir);
            energi = itemView.findViewById(R.id.rekomEnergi);
            protein = itemView.findViewById(R.id.rekomProtein);
            lemak = itemView.findViewById(R.id.rekomLemak);
            serat = itemView.findViewById(R.id.rekomSerat);
            abu = itemView.findViewById(R.id.rekomAbu);
            kh = itemView.findViewById(R.id.rekomKh);
            kalsium = itemView.findViewById(R.id.rekomKalsium);
            fosfor = itemView.findViewById(R.id.rekomFosfor);
            natrium = itemView.findViewById(R.id.rekomNatrium);
            kalium = itemView.findViewById(R.id.rekomKalium);
            tembaga = itemView.findViewById(R.id.rekomTembaga);
            seng = itemView.findViewById(R.id.rekomSeng);
            retinol = itemView.findViewById(R.id.rekomRetinol);
            b_kar = itemView.findViewById(R.id.rekomBkar);
            kar_total = itemView.findViewById(R.id.rekomKarTot);
            thiamin = itemView.findViewById(R.id.rekomThiamin);
            besi = itemView.findViewById(R.id.rekomBesi);
            riboflavin = itemView.findViewById(R.id.rekomRiboflavin);
            niasin = itemView.findViewById(R.id.rekomNiasin);
            vitc = itemView.findViewById(R.id.rekomVitC);
            bdd = itemView.findViewById(R.id.rekomPorsi);
        }
    }
}
