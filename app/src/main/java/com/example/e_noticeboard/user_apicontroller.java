package com.example.e_noticeboard;

import com.example.e_noticeboard.models.UserModel;

import java.lang.ref.Reference;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class user_apicontroller {
    private static String url = "http://192.168.1.9/userinfo/";
    private static user_apicontroller clientobject;
    private static Retrofit retrofit;

    user_apicontroller(){
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


    public static synchronized user_apicontroller getinstance(){
        if (clientobject == null){
            clientobject = new user_apicontroller();
        }
        return clientobject;
    }

    public user_apiset getApi(){
        return retrofit.create(user_apiset.class);
    }

}
