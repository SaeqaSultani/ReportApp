package com.example.reportapp.network_signuppage;

import com.google.gson.annotations.SerializedName;

public class ClassSignup {

    @SerializedName("em_name")
    private String Name ;

    @SerializedName("em_last_name")
    private String Last_Name;

    @SerializedName("em_username")
    private String Username ;

    @SerializedName("em_pass")
    private String Password;

    @SerializedName("em_sec")
    private String Section ;

    @SerializedName("response")
    private String Response ;

    public String getName() {
        return Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getSection() {
        return Section;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
