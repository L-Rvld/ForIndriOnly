package com.polije.gizielectree.AdapModel;

public class ModelGula {
    String tgl;
    int gula;

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public int getGula() {
        return gula;
    }

    public void setGula(int gula) {
        this.gula = gula;
    }

    public ModelGula(String tgl, int gula) {
        this.tgl = tgl;
        this.gula = gula;
    }
}
