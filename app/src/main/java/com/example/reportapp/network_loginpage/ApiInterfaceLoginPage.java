package com.example.reportapp.network_loginpage;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterfaceLoginPage {


    @GET("SignIn_SignUp_api/{em_username}/{em_pass}")
    Call<Employee> login(@Path("em_username") String username, @Path("em_pass") String password);


}
