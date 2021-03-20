package com.polije.gizielectree.AdapModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.polije.gizielectree.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.DataViewHolder> {
    Context context;
    public ArrayList<ModelData> dataList;

    public AdapterData(Context context, ArrayList<ModelData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public AdapterData.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_data,parent,false);
        return new DataViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterData.DataViewHolder holder, int position) {
        holder.id.setText(dataList.get(position).id_kode);
        holder.nama.setText(dataList.get(position).nama_bahan);
        holder.energi.setText(dataList.get(position).energi);
        holder.porsi.setText(dataList.get(position).porsi);
        holder.linedet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("asasas", "onClick: "+dataList.get(position).id_kode);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataList != null){
            return dataList.size();
        }
        return 0;
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder{
        TextView id, nama, energi, porsi;
        LinearLayout linedet;
        public DataViewHolder(View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.lidtIdKode);
            nama=itemView.findViewById(R.id.listAdapNama);
            energi=itemView.findViewById(R.id.listAdapEnergi);
            porsi=itemView.findViewById(R.id.listAdapPorsi);
            linedet = itemView.findViewById(R.id.lineDetail);
        }
    }
    public void setFilter(ArrayList<ModelData> filter){
        dataList = new ArrayList<>();
        dataList.addAll(filter);
        notifyDataSetChanged();
    }
}
