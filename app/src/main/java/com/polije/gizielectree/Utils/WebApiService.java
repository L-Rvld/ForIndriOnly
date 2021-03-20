package com.polije.gizielectree.Utils;

public class WebApiService {
//    private String Api_url = "http://192.168.1.16/api_gizi/";       //soluta
    private String Api_url = "http://192.168.1.6/api_gizi/";      //omah
//    private String Api_url = "http://192.168.43.197/api_gizi/";      //hotspot
//    private String Api_url = "http://192.168.0.153/api_gizi/"; //punya indri

    public String getApi_url(){
        return Api_url;
    }
}
