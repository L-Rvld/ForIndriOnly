package com.polije.gizielectree.AdapModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.polije.gizielectree.R;

import java.util.ArrayList;

public class AdapterSendData extends RecyclerView.Adapter<AdapterSendData.DataViewHolder> {
    Context context;
    Dialog dialog;
    public ArrayList<ModelData> dataList;

    public AdapterSendData(Context context, ArrayList<ModelData> dataList, Dialog dialog) {
        this.context = context;
        this.dataList = dataList;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public AdapterSendData.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_send, parent, false);
        return new AdapterSendData.DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSendData.DataViewHolder holder, int position) {
        holder.id.setText(dataList.get(position).id_kode);
        holder.nama.setText(dataList.get(position).nama_bahan);
        holder.energi.setText(dataList.get(position).energi);
        holder.porsi.setText(dataList.get(position).porsi);
        holder.linedet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.lineganti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(new Intent("passData")
                                .putExtra("id", dataList.get(position).id_kode));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView id, nama, energi, porsi;
        LinearLayout linedet, lineganti;
        public DataViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.sendIdKode);
            nama = itemView.findViewById(R.id.sendAdapNama);
            energi = itemView.findViewById(R.id.sendAdapEnergi);
            porsi = itemView.findViewById(R.id.sendAdapPorsi);
            linedet = itemView.findViewById(R.id.sendDet);
            lineganti = itemView.findViewById(R.id.sendlineDetail);
        }
    }

    public void setFilter(ArrayList<ModelData> filter) {
        dataList = new ArrayList<>();
        dataList.addAll(filter);
        notifyDataSetChanged();
    }
}