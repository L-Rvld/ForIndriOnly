package com.polije.gizielectree.AdapModel;

public class ModelData {
    String id_kode,nama_bahan,energi,porsi;

    public ModelData(String id_kode, String nama_bahan, String energi, String porsi) {
        this.id_kode = id_kode;
        this.nama_bahan = nama_bahan;
        this.energi = energi;
        this.porsi = porsi;
    }

    public String getId_kode() {
        return id_kode;
    }

    public void setId_kode(String id_kode) {
        this.id_kode = id_kode;
    }

    public String getNama_bahan() {
        return nama_bahan;
    }

    public void setNama_bahan(String nama_bahan) {
        this.nama_bahan = nama_bahan;
    }

    public String getEnergi() {
        return energi;
    }

    public String getPorsi() {
        return porsi;
    }

    public void setPorsi(String porsi) {
        this.porsi = porsi;
    }

    public void setEnergi(String energi) {
        this.energi = energi;
    }
}
