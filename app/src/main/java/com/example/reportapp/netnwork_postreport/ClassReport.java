package com.example.reportapp.netnwork_postreport;

import com.google.gson.annotations.SerializedName;

public class ClassReport {


    @SerializedName("r_report")
    private String Report;

    @SerializedName("em_id")
    private String id ;

    @SerializedName("response")
    private String Response ;

    public String getId() {
        return id;
    }

    public String getReport() {
        return Report;
    }


    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
