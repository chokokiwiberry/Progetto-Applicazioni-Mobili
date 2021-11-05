package com.example.prova1progetto;


import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

//POJO
public class Product {

    @SerializedName("id")
    private String id; // non so se tenerla o meno

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("img")
    private String image; //da definire meglio, se img o string

    @SerializedName("userId")
    private String userId; //user? da guardare meglio
    @SerializedName("test")
    private Boolean test; //se il prodotto è in test è true, altrimenti false
    @SerializedName("createdAt")
    private Date createdAt;
    @SerializedName("updatedAt")
    private Date updatedAt;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getImage() {
        return image;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getTest() {
        return test;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt; }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
