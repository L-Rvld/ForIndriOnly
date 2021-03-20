package com.polije.gizielectree.AdapModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.polije.gizielectree.R;

import java.util.ArrayList;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.DataViewHolder> {
    Context context;
    public ArrayList<ModelUser> dataList;

    public AdapterUser(Context context, ArrayList<ModelUser> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AdapterUser.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user,parent,false);
        return new AdapterUser.DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.DataViewHolder holder, int position) {
        holder.nama.setText(dataList.get(position).getNama());
        holder.email.setText(dataList.get(position).getEmail());
        holder.jk.setText(dataList.get(position).getJk());
        holder.umur.setText(dataList.get(position).getUmur());
        holder.tinggi.setText(dataList.get(position).getTinggi());
        holder.berat.setText(dataList.get(position).getBerat());
        holder.aktif.setText(dataList.get(position).getAktif());
    }

    @Override
    public int getItemCount() {
        if (dataList != null){
            return dataList.size();
        }
        return 0;
    }
    public String senstext(String text){
        String seperate = text.substring(0,3);
        String separater = text.substring(3);

        return separater+" : "+seperate;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder{
        TextView nama, email, jk, umur, tinggi, berat, aktif;
        public DataViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.usNama);
            email = itemView.findViewById(R.id.usEmail);
            jk = itemView.findViewById(R.id.usJK);
            umur = itemView.findViewById(R.id.usUmur);
            tinggi = itemView.findViewById(R.id.usTinggi);
            berat = itemView.findViewById(R.id.usBerat);
            aktif = itemView.findViewById(R.id.usAktif);
        }
    }
}
