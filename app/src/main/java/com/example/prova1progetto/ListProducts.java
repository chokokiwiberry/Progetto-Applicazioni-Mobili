package com.example.prova1progetto;

import android.widget.ArrayAdapter;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
//POJO
public class ListProducts extends Product {

    @SerializedName("token")
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @SerializedName("products")
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


}
