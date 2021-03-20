package com.polije.gizielectree.AdapModel;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.polije.gizielectree.Admin.AdminMainActivity;
import com.polije.gizielectree.Login;
import com.polije.gizielectree.R;
import com.polije.gizielectree.Utils.ToastBaseCust;
import com.polije.gizielectree.Utils.WebApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterDataMaster extends RecyclerView.Adapter<AdapterDataMaster.DataViewHolder> {
    Context context;
    public ArrayList<ModelData> dataList;
    WebApiService apiService;
    ProgressDialog progressDialog;
    Dialog dialog;
    TextInputEditText nama, jns, enrg, sumber, bdd, air, protein, lemak,    karbo,
            serat, abu, kalsium, fsfr, besi, ntrium, kalium, tmbg, seng, rtnl, bkar, ktot, thmn, rbfln, nsin, vtc;
    ImageButton closes;
    Button add;
    String[] kodes;
    String[] kates;
    final String[] koded = new String[1];

    public AdapterDataMaster(Context context, ArrayList<ModelData> dataList, ProgressDialog progressDialog, Dialog dialog) {
        this.context = context;
        this.dataList = dataList;
        apiService = new WebApiService();
        this.progressDialog = progressDialog;
        this.dialog = dialog;
        kodes = context.getResources().getStringArray(R.array.kode);
        kates = context.getResources().getStringArray(R.array.kategori);
    }

    @NonNull
    @Override
    public AdapterDataMaster.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_datamaster, parent, false);
        return new AdapterDataMaster.DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDataMaster.DataViewHolder holder, int position) {
        holder.id.setText(dataList.get(position).id_kode);
        holder.nama.setText(dataList.get(position).nama_bahan);
        holder.energi.setText(dataList.get(position).energi);
        holder.porsi.setText(dataList.get(position).porsi);
        holder.linedet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Asasas", "onClick: " + dataList.get(position).id_kode);
            }
        });
        holder.lineact.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String[] items = {"Edit", "Hapus"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                updateIt(dataList.get(position).id_kode, position);
                                break;
                            case 1:
                                AlertDialog.Builder aleBuilder = new AlertDialog.Builder(context);
                                aleBuilder.setTitle("Alert");
                                aleBuilder
                                        .setMessage("Periksa dahulu, Apakah anda yakin ingin menghapus data ini?")
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setCancelable(false)
                                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {

                                                deleteIt(dataList.get(position).id_kode, position);
                                            }
                                        })
                                        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = aleBuilder.create();
                                alertDialog.show();
                                break;
                        }
                    }
                });
                Dialog d = builder.show();
                return true;
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
        LinearLayout linedet, lineact;

        public DataViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.lidtIdKode);
            nama = itemView.findViewById(R.id.listAdapNama);
            energi = itemView.findViewById(R.id.listAdapEnergi);
            porsi = itemView.findViewById(R.id.listAdapPorsi);
            lineact = itemView.findViewById(R.id.lineAction);
            linedet = itemView.findViewById(R.id.lineDetail);
        }
    }

    public void setFilter(ArrayList<ModelData> filter) {
        dataList = new ArrayList<>();
        dataList.addAll(filter);
        notifyDataSetChanged();
    }

    public void deleteIt(String id, int pos) {
        StringRequest delete = new StringRequest(Request.Method.POST, apiService.getApi_url() + context.getString(R.string.admin_master), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        dataList.remove(pos);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Berhasil Hapus", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Gagal Hapus", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("TAG", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("kode", id);
                param.put("action", "delete");
                Log.d("TAG", "getParams: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(delete);
    }

    public void initD() {
        add = dialog.findViewById(R.id.btnAddda);
        closes = dialog.findViewById(R.id.closeDialog);

        nama = dialog.findViewById(R.id.eDMNama);
        jns = dialog.findViewById(R.id.eDMJenis);
        enrg = dialog.findViewById(R.id.eDMEnergi);
        sumber = dialog.findViewById(R.id.eDMSumber);
        bdd = dialog.findViewById(R.id.eDMBDD);
        air = dialog.findViewById(R.id.eDMAir);
        protein = dialog.findViewById(R.id.eDMProtein);
        lemak = dialog.findViewById(R.id.eDMLemak);
        karbo = dialog.findViewById(R.id.eDMKH);
        serat = dialog.findViewById(R.id.eDMSerat);
        abu = dialog.findViewById(R.id.eDMAbu);
        kalsium = dialog.findViewById(R.id.eDMKalsium);
        fsfr = dialog.findViewById(R.id.eDMFosfor);
        besi = dialog.findViewById(R.id.eDMBesi);
        ntrium = dialog.findViewById(R.id.eDMNatrium);
        kalium = dialog.findViewById(R.id.eDMKalium);
        tmbg = dialog.findViewById(R.id.eDMTembaga);
        seng = dialog.findViewById(R.id.eDMSeng);
        rtnl = dialog.findViewById(R.id.eDMRetinol);
        bkar = dialog.findViewById(R.id.eDMBKAR);
        ktot = dialog.findViewById(R.id.eDMKarTot);
        thmn = dialog.findViewById(R.id.eDMThiamin);
        rbfln = dialog.findViewById(R.id.eDMRiboflavin);
        nsin = dialog.findViewById(R.id.eDMNiasin);
        vtc = dialog.findViewById(R.id.eDMVitC);
        TextView txt = dialog.findViewById(R.id.textAtas);
        txt.setText("Ubah Data");

        jns.setFocusable(false);

        final ArrayAdapter<String> adapterjenis = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item, kates);
        jns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(dialog.getContext()).setTitle("Pilih Kategori").setAdapter(adapterjenis, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jns.setText(kates[i].toString());
                        koded[0] = kodes[i];
                        Log.d("KODED", "onClick: geting = " + kodes[i]);
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });

        add.setText("Perbarui");

        closes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                clearingText();
            }
        });
    }

    public void gotUp(String nama, String kode, String energi, String sumber, String bdd, String air, String protein, String lemak, String kh,
                      String serat, String abu, String kalsium, String fosfor, String besi, String natrium, String kalium,
                      String tmbga, String seng, String retinol, String bkar, String kartot, String thiamin, String ribo, String nsin, String vtc, String oldKode, int position) {

        StringRequest upd = new StringRequest(Request.Method.POST, apiService.getApi_url() + context.getString(R.string.admin_master), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("TAG", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("error").equals("false")) {
                        Toast.makeText(context, "Sukses Mengubah", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        clearingText();
                        dataList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Gagal Mengubah", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("TAG", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("nama", nama);
                param.put("jns", kode);
                param.put("enrg", energi);
                param.put("sumber", sumber);
                param.put("bdd", bdd);
                param.put("air", air);
                param.put("protein", protein);
                param.put("lemak", lemak);
                param.put("karbo", kh);
                param.put("serat", serat);
                param.put("abu", abu);
                param.put("kalsium", kalsium);
                param.put("fsfr", fosfor);
                param.put("besi", besi);
                param.put("ntrium", natrium);
                param.put("kalium", kalium);
                param.put("tmbg", tmbga);
                param.put("seng", seng);
                param.put("rtnl", retinol);
                param.put("bkar", bkar);
                param.put("ktot", kartot);
                param.put("thmn", thiamin);
                param.put("rbfln", ribo);
                param.put("nsin", nsin);
                param.put("vtc", vtc);
                param.put("action", "update");
                param.put("oldkode", oldKode);
                Log.d("TAG", "getParams: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(upd);
    }

    public void updateIt(String oldid, int position) {
        dialog.show();
        initD();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiService.getApi_url() + context.getString(R.string.getDetail) + "?id=" + oldid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject object = jsonArray.getJSONObject(0);

                    String kode = object.getString("id");
                    String kate = kode.substring(0, 2);
                    koded[0] = kate;

                    int pos = 0;
                    for (int i = 0; i < kodes.length; i++) {
                        if (kodes[i].contains(kate)) {
                            pos = i;
                        }
                    }
                    Log.d("KATELOG", "onResponse: " + kate + " KATE nya == " + kates[pos]);

                    nama.setText(object.getString("nama"));
                    jns.setText(kates[pos]);
                    enrg.setText(object.getString("energi"));
                    sumber.setText(object.getString("sumber"));
                    bdd.setText(object.getString("bdd"));

                    air.setText(object.getString("air"));
                    protein.setText(object.getString("protein"));
                    lemak.setText(object.getString("lemak"));
                    karbo.setText(object.getString("kh"));
                    serat.setText(object.getString("serat"));
                    abu.setText(object.getString("abu"));
                    kalsium.setText(object.getString("kalsium"));
                    fsfr.setText(object.getString("fosfor"));
                    besi.setText(object.getString("besi"));
                    ntrium.setText(object.getString("natrium"));
                    kalium.setText(object.getString("kalium"));
                    tmbg.setText(object.getString("tembaga"));
                    seng.setText(object.getString("seng"));
                    rtnl.setText(object.getString("retinol"));
                    bkar.setText(object.getString("b_kar"));
                    ktot.setText(object.getString("kar_total"));
                    thmn.setText(object.getString("thiamin"));
                    rbfln.setText(object.getString("riboflavin"));
                    nsin.setText(object.getString("niasin"));
                    vtc.setText(object.getString("vitc"));

                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(nama.getText())&&!TextUtils.isEmpty(jns.getText())&&!TextUtils.isEmpty(enrg.getText())
                                    &&!TextUtils.isEmpty(sumber.getText())&&!TextUtils.isEmpty(bdd.getText())) {
                                AlertDialog.Builder aleBuilder = new AlertDialog.Builder(context);
                                aleBuilder.setTitle("Alert");
                                aleBuilder
                                        .setMessage("Periksa dahulu, Apakah anda yakin ingin mengubah data ini?")
                                        .setIcon(R.mipmap.ic_launcher)
                                        .setCancelable(false)
                                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                String nextID = null;
                                                Log.d("TAG", "onClick: kate = " + kate + " ,, koded = " + kode);
                                                if (koded[0].equals(kate)) {
                                                    gotUp(t(nama), kode, t(enrg), t(sumber), t(bdd), t(air), t(protein), t(lemak), t(karbo),
                                                            t(serat), t(abu), t(kalsium), t(fsfr), t(besi), t(ntrium), t(kalium), t(tmbg),
                                                            t(seng), t(rtnl), t(bkar), t(ktot), t(thmn), t(rbfln), t(nsin), t(vtc), oldid,position);
                                                } else {
                                                    getSeq(koded[0], kode,position);
                                                }
                                            }
                                        })
                                        .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = aleBuilder.create();
                                alertDialog.show();
                            }else if (TextUtils.isEmpty(nama.getText())&&TextUtils.isEmpty(jns.getText())&&TextUtils.isEmpty(enrg.getText())
                                    && TextUtils.isEmpty(sumber.getText())&&TextUtils.isEmpty(bdd.getText())){
                                nama.setError("Isi Nama Makanan / Minuman");
                                jns.setError("Pilih Jenis Makanan / Minuman");
                                enrg.setError("Isi Data Energi");
                                sumber.setError("Isi Sumber Makanan / Minuman");
                                bdd.setError("Isi BDD");
                            }else if (TextUtils.isEmpty(nama.getText())){
                                nama.setError("Isi Nama Makanan / Minuman");
                            }else if (TextUtils.isEmpty(jns.getText())){
                                jns.setError("Pilih Jenis Makanan / Minuman");
                            }else if (TextUtils.isEmpty(enrg.getText())){
                                enrg.setError("Isi Data Energi");
                            }else if (TextUtils.isEmpty(sumber.getText())){
                                sumber.setError("Isi Sumber Makanan / Minuman");
                            }else if (TextUtils.isEmpty(bdd.getText())){
                                bdd.setError("Isi BDD");
                            }

                        }
                    });

                } catch (JSONException e) {
                    Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("P", "onErrorResponse: ", error);
                        Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public String t(TextInputEditText t) {
        return t.getText().toString();
    }

    public void getSeq(String id, String oldid, int position) {
        StringRequest seq = new StringRequest(Request.Method.GET, apiService.getApi_url() + "getSeq.php" + "?id=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TASsssS", "onResponse: " + response);
                String s;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String idnya = jsonObject.getString("result");
                    int idx = Integer.parseInt(idnya) + 1;
                    String nextID = id + "" + idx;

                    gotUp(t(nama), nextID, t(enrg), t(sumber), t(bdd), t(air), t(protein), t(lemak), t(karbo),
                            t(serat), t(abu), t(kalsium), t(fsfr), t(besi), t(ntrium), t(kalium), t(tmbg),
                            t(seng), t(rtnl), t(bkar), t(ktot), t(thmn), t(rbfln), t(nsin), t(vtc), oldid, position );

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: " + error);
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(seq);

    }

    public void clearingText() {
        nama.setText("");
        jns.setText("");
        enrg.setText("");
        sumber.setText("");
        bdd.setText("");

        air.setText("0");
        protein.setText("0");
        lemak.setText("0");
        karbo.setText("0");
        serat.setText("0");
        abu.setText("0");
        kalsium.setText("0");
        fsfr.setText("0");
        besi.setText("0");
        ntrium.setText("0");
        kalium.setText("0");
        tmbg.setText("0");
        seng.setText("0");
        rtnl.setText("0");
        bkar.setText("0");
        ktot.setText("0");
        thmn.setText("0");
        rbfln.setText("0");
        nsin.setText("0");
        vtc.setText("0");
    }
}
