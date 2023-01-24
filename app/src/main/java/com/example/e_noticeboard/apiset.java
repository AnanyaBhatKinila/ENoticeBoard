package com.example.e_noticeboard;

import com.example.e_noticeboard.models.UserModel;
import com.example.e_noticeboard.models.dbModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface apiset {
    @GET("retreive.php")
    Call<List<dbModel>> caller();

    @FormUrlEncoded
    @POST("insert.php")
    Call<dbModel> call(@Field("title") String title,
                       @Field("description") String description,
                       @Field("imageFileName") String imageFileName);


}
