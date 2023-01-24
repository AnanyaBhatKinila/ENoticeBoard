package com.example.e_noticeboard;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class api_controller {
    private static String url = "http://192.168.1.9/enoticeboard/";
    private static api_controller clientobject;
    private static Retrofit retrofit;



    api_controller(){
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static  void changeurl (String newurl) {
        url = newurl ;
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static synchronized api_controller getInstance(){
        if (clientobject == null){
            clientobject = new api_controller();
        }
        return clientobject;
    }

    public apiset getapi(){
        return retrofit.create(apiset.class);
    }


}
