package com.example.e_noticeboard;

import com.example.e_noticeboard.models.dbModel;
import com.example.e_noticeboard.models.UserModel;
import com.example.e_noticeboard.models.responsemodel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface user_apiset {
    @GET("get.php")
    Call<List<UserModel>> caller();

    @FormUrlEncoded
    @POST("insertUser.php")
    Call<UserModel> call(@Field("name") String name,
                         @Field("username") String username,
                         @Field("password") String password,
                         @Field("email") String email);
    @FormUrlEncoded
    @POST("update.php")
    Call<UserModel> update(@Field("name") String name,
                         @Field("username") String username,
                         @Field("password") String password,
                         @Field("email") String email);
    @FormUrlEncoded
    @POST("delete.php")
    Call<UserModel> delete(@Field("name") String name);

    @FormUrlEncoded
    @POST("check.php")
    Call<responsemodel> verifyuser(
            @Field("username") String username,
            @Field("password") String password);
}

