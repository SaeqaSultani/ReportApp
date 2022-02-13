package com.example.reportapp.network_getreport;

import com.google.gson.annotations.SerializedName;

public class ClassGetReport {

    @SerializedName("r_report")
    private String Report;

    @SerializedName("r_date")
    private String Date;

    @SerializedName("em_id")
    private String id;

    public String getId() {
        return id;
    }

    public String getReport() {
        return Report;
    }

    public String getDate() {
        return Date;
    }


}
