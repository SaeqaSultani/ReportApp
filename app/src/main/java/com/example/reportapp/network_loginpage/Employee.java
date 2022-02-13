package com.example.reportapp.network_loginpage;

import com.google.gson.annotations.SerializedName;

public class Employee {

    @SerializedName("em_id")
    private Integer id ;

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

    //-------
    @SerializedName("SignIn")
    private String signIn;


    public Integer getId() {
        return id;
    }

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

    public String getSignIn() {
        return signIn;
    }

    public String getResponse() {
        return Response;
    }
}
