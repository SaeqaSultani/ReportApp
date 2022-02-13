package com.example.reportapp.netnwork_postreport;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterfaceReport {

    @FormUrlEncoded
    @POST("Reports_api/{em_id}")
    Call<ClassReport> uploadReport(@Field("r_report") String report, @Path("em_id") int id);
}
