package com.example.reportapp.network_signuppage;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterfaceSignup {

    @FormUrlEncoded
    @POST("SignIn_SignUp_api")
    Call<ClassSignup> uploadData(
            @Field("em_name") String name,
            @Field("em_last_name") String last_name,
            @Field("em_username") String username,
            @Field("em_pass") String password ,
            @Field("em_sec") String section);
}
