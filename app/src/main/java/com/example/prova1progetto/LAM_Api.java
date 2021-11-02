package com.example.prova1progetto;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LAM_Api {


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("products")
    Call<ListProducts> getProducts(@Header("Authorization") String auth, @Query("barcode") String barcode);

    @Headers("Content-Type: application/json")
    @POST("auth/login")
        //faccio una post del tipo user - dato che ho definito, decidere come trattare il campo email
    Call<User> Login(@Body Map<String, String> body);


    @FormUrlEncoded
    @POST("auth/login")
    Call<User> Login(
            @Field("username") String username,
            @Field("password") String password
    );


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("products")
    Call<Product> postProduct(@Header("Authorization") String auth, @Body HashMap<String, Object> body);


}
