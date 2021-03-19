package com.example.test1.service;

import com.example.test1.model.LoginUser;
import com.example.test1.model.User;
import com.example.test1.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
    @POST("user")
    Call<User> createAccount(@Body User user);

    @POST("user/login")
    Call<LoginUser> login(@Body LoginUser user);

    @GET("user")
    Call<UserInfo> getInfo(@Header("Authorization") String auth);
}
