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
        holder.jenis.setText(dataList.get(position).getJenis());
        holder.energi.setText("Energi : "+dataList.get(position).getEnergi()+" Kkal");
        holder.protein.setText(dataList.get(position).getProtein());
        holder.lemak.setText(dataList.get(position).getLemak());
        holder.karbo.setText(dataList.get(position).getKarbo());
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
                        .putExtra("jenis",dataList.get(position).getJenis())
                        .putExtra("energi",dataList.get(position).getEnergi())
                        .putExtra("protein",dataList.get(position).getProtein())
                        .putExtra("lemak",dataList.get(position).getLemak())
                        .putExtra("karbo",dataList.get(position).getKarbo())
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
        TextView ID, nama, jenis, energi,protein,lemak,karbo,bdd;
        LinearLayout layout, ganti;
        public DataViewHolder(View itemView) {
            super(itemView);
            ganti = itemView.findViewById(R.id.rekomGanti);
            layout = itemView.findViewById(R.id.layGoDetail);
            ID = itemView.findViewById(R.id.rekomID);
            nama = itemView.findViewById(R.id.rekomNama);
            jenis = itemView.findViewById(R.id.rekomJenis);
            energi = itemView.findViewById(R.id.rekomEnergi);
            protein = itemView.findViewById(R.id.rekomProtein);
            lemak = itemView.findViewById(R.id.rekomLemak);
            karbo = itemView.findViewById(R.id.rekomKarbo);
            bdd = itemView.findViewById(R.id.rekomPorsi);
        }
    }
}
