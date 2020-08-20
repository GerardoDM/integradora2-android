package com.example.pruebanavdrawer.Interfaces;

import com.example.pruebanavdrawer.Models.AccessToken;
import com.example.pruebanavdrawer.Models.Auth;
import com.example.pruebanavdrawer.Models.Example2;
import com.example.pruebanavdrawer.Models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @POST("register")
    @FormUrlEncoded
    Call<User> register (@Field("email") String email, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<Example2> login (@Field("email") String email, @Field("password") String password);

}
