package com.polije.gizielectree.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.polije.gizielectree.AdapModel.AdapTemp;
import com.polije.gizielectree.AdapModel.ModelGula;
import com.polije.gizielectree.AdapModel.ModelSave;

import java.util.ArrayList;
import java.util.List;

public class SqliteHelpers extends SQLiteOpenHelper {
    public static final String DB_NAME = "dblite";
    public static final String TABLE_NAME = "temporary_save";

    private static final int DB_VERSION = 1;

    public SqliteHelpers(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(id_temp INTEGER PRIMARY KEY AUTOINCREMENT, status VARCHAR, idR VARCHAR);";
        db.execSQL(sql);
        String sql2 = "CREATE TABLE data_rekom (hari VARCHAR, id VARCHAR,nama VARCHAR,sumber VARCHAR, air VARCHAR,energi VARCHAR, protein VARCHAR,lemak VARCHAR,kh VARCHAR,serat VARCHAR,abu VARCHAR,kalsium VARCHAR,fosfor VARCHAR,besi VARCHAR,natrium VARCHAR,kalium VARCHAR,tembaga VARCHAR,seng VARCHAR,retinol VARCHAR,b_kar VARCHAR,kar_total VARCHAR,thiamin VARCHAR,riboflavin VARCHAR,niasin VARCHAR,vitc VARCHAR,bdd VARCHAR);";
        db.execSQL(sql2);
        String sql3 = "CREATE TABLE data_gula (tgl VARCHAR, gula INTEGER)";
        db.execSQL(sql3);
    }

    public void clear(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
    }
    public void addTemp(String hari, String idR){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", hari);
        contentValues.put("idR",idR);
        db.insert("temporary_save",null,contentValues);
        db.close();
    }
    public void removeTemp(String idOld){
        SQLiteDatabase db = this.getWritableDatabase();
        String arg[] = {idOld.toString()};
        db.delete("temporary_save", "idR=?",arg);
    }

    public List<AdapTemp> getTemp(){
        List<AdapTemp> lists = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                AdapTemp jadwaladap = new AdapTemp(cursor.getString(1),cursor.getString(2));
                lists.add(jadwaladap);
            } while (cursor.moveToNext());
        }
        return lists;
    }
    public List<ModelSave> getRekomSave(){
        List<ModelSave> lis = new ArrayList<>();
        String selectQuery = "SELECT * FROM data_rekom";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                ModelSave save = new ModelSave(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),cursor.getString(15),cursor.getString(16),cursor.getString(17),cursor.getString(18),cursor.getString(19),cursor.getString(20),cursor.getString(21),cursor.getString(22),cursor.getString(23),cursor.getString(24),cursor.getString(25));
                lis.add(save);
            } while (cursor.moveToNext());
        }
        return lis;
    }
    public List<ModelGula> getGula(){
        List<ModelGula> lis = new ArrayList<>();
        String selectQuery = "SELECT * FROM data_gula";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                ModelGula gula = new ModelGula(cursor.getString(0),cursor.getInt(1));
                lis.add(gula);
            } while (cursor.moveToNext());
        }
        return lis;
    }

    public void addGula(String tgl, int gula){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tgl", tgl);
        contentValues.put("gula", gula);
        db.insert("data_gula",null,contentValues);
        db.close();
    }

    public void addDataM(String hari, String id, String nama, String sumber, String air, String energi,
                         String protein, String lemak, String kh, String serat, String abu, String kalsium,
                         String fosfor, String besi, String natrium, String kalium, String tembaga, String seng,
                         String retinol, String b_kar, String kar_total, String thiamin, String riboflavin,
                         String niasin, String vitc, String bdd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hari", hari);
        contentValues.put("id", id);
        contentValues.put("nama", nama);
        contentValues.put("sumber", sumber);
        contentValues.put("air", air);
        contentValues.put("energi", energi);
        contentValues.put("protein", protein);
        contentValues.put("lemak", lemak);
        contentValues.put("kh", kh);
        contentValues.put("serat", serat);
        contentValues.put("abu", abu);
        contentValues.put("kalsium", kalsium);
        contentValues.put("fosfor", fosfor);
        contentValues.put("besi", besi);
        contentValues.put("natrium", natrium);
        contentValues.put("besi", besi);
        contentValues.put("fosfor", fosfor);
        contentValues.put("kalium", kalium);
        contentValues.put("tembaga", tembaga);
        contentValues.put("seng", seng);
        contentValues.put("retinol", retinol);
        contentValues.put("b_kar", b_kar);
        contentValues.put("kar_total", kar_total);
        contentValues.put("thiamin", thiamin);
        contentValues.put("riboflavin", riboflavin);
        contentValues.put("niasin", niasin);
        contentValues.put("vitc", vitc);
        contentValues.put("bdd", bdd);
        db.insert("data_rekom",null,contentValues);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String sql2 = "DROP TABLE IF EXISTS data_rekom";
        db.execSQL(sql);
        db.execSQL(sql2);
        onCreate(db);
    }

}
