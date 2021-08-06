package com.polije.gizielectree.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Sharedprefs {
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public static final String app_ta = "AppTA";
    public final String onLogin = "onLogin";
    public final String first = "first";
    public final String rekom = "rekom";
    public final String user = "user";
    public final String umur = "umur";
    public final String jk = "jk";
    public final String email = "email";
    public final String aktif = "aktif";
    public final String tinggi = "tinggi";
    public final String berat = "berat";
    public final String id = "id";
    public final String is_admin = "is_admin";
    public final String gula = "gula";

    public Sharedprefs(Context context){
        sp = context.getSharedPreferences(app_ta, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }
    public void clearing(){
        spEditor.clear();
        spEditor.commit();
        saveFirst(first,true);
    }
    public void savelogin(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }
    public void saveusername(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }
    public void saveEmail(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }
    public String getEmail(){
        return sp.getString(email, "");
    }
    public String getuser(){
        return sp.getString(user, "");
    }

    public Boolean getonLogin(){
        return sp.getBoolean(onLogin, false);
    }

    public Boolean getFirst(){
        return sp.getBoolean(first, false);
    }

    public void saveFirst(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }
    public void saveRekom(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }
    public String getRekom(){
        return sp.getString(rekom, "");
    }

    public void saveJK(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }
    public void saveUmur(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }
    public void saveTinggi(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }
    public void saveBerat(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }
    public void saveAktif(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }
    public void saveGula(String keySP, String value){
        spEditor.putString(keySP,value);
        spEditor.commit();
    }
    public String getGula(){
        return sp.getString(gula,"");
    }

    public String getJK(){
        return sp.getString(jk, "");
    }
    public void saveLevel(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }
    public Boolean getLevel(){
        return sp.getBoolean(is_admin, false);
    }
    public void saveID(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }
    public String getID(){
        return sp.getString(id, "");
    }
    public String getTinggi(){
        return sp.getString(tinggi, "");
    }
    public String getUmur(){
        return sp.getString(umur, "");
    }
    public String getBerat(){
        return sp.getString(berat, "");
    }
    public String getAktif(){
        return sp.getString(aktif, "");
    }


}
