package com.polije.gizielectree.AdapModel;

public class ModelRekomendasi {

    String id, nama, jenis, energi, protein, lemak, karbo, bdd;

    public ModelRekomendasi(String id, String nama, String jenis, String energi, String protein, String lemak, String karbo, String bdd) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.energi = energi;
        this.protein = protein;
        this.lemak = lemak;
        this.karbo = karbo;
        this.bdd = bdd;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getEnergi() {
        return energi;
    }

    public void setEnergi(String energi) {
        this.energi = energi;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getLemak() {
        return lemak;
    }

    public void setLemak(String lemak) {
        this.lemak = lemak;
    }

    public String getKarbo() {
        return karbo;
    }

    public void setKarbo(String karbo) {
        this.karbo = karbo;
    }

    public String getBdd() {
        return bdd;
    }

    public void setBdd(String bdd) {
        this.bdd = bdd;
    }
}
