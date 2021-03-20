package com.polije.gizielectree.AdapModel;

public class ModelUser {
    String email, nama, jk, umur, tinggi, berat, aktif;

    public ModelUser(String email, String nama, String jk, String umur, String tinggi, String berat, String aktif) {
        this.email = email;
        this.nama = nama;
        this.jk = jk;
        this.umur = umur;
        this.tinggi = tinggi;
        this.berat = berat;
        this.aktif = aktif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getTinggi() {
        return tinggi;
    }

    public void setTinggi(String tinggi) {
        this.tinggi = tinggi;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getAktif() {
        return aktif;
    }

    public void setAktif(String aktif) {
        this.aktif = aktif;
    }
}
