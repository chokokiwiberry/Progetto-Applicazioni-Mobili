package com.example.prova1progetto;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("username")
    private String username;

    @SerializedName("accessToken")
    private String token;

    public User(String email, String password, String token){
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    //si fa un altro costruttore - questo dovrebbe essere utilizzato per il login
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }


    public String getToken() {
        return token;
    }
}
