package com.example.reportapp.network_getreport;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterfaceGetReport {

    @GET("Reports_api/{id}")
    Call<List<ClassGetReport>> GetReport(@Path("id") int id);
}
