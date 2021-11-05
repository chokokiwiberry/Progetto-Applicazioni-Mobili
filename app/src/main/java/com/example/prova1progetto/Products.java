package com.example.prova1progetto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Products extends AppCompatActivity {
    private LAM_Api lam_api;

    private String passed_Barcode; // barcode passato dal pantry, digitato dall'utente

    private String received_token;

    private TextView textViewResult;

    private Button addproduct;

    private String sessionToken;

    String PREF_NAME1 = "session_token";
    String MY_KEY1 = "";
    SharedPreferences sharedPreferences1;


    private RelativeLayout layout;

    private ListView listview;
    private ListProducts tmp;



    ProductView productview = new ProductView(Products.this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofproducts);
        textViewResult = findViewById(R.id.text_view_result);
        textViewResult.setMovementMethod(new ScrollingMovementMethod());
        addproduct = findViewById(R.id.id_addproduct);
        listview = findViewById(R.id.listview);

        layout = findViewById(R.id.layoutlist);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passed_Barcode = extras.getString("barcode");
            received_token = extras.getString("token");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lam21.iot-prism-lab.cs.unibo.it/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        lam_api = retrofit.create(LAM_Api.class);
        getProducts(passed_Barcode);

        //Log.d("cibo", "sono sessionToken " +  sessionToken);

        addproduct.setOnClickListener(v -> {
            Log.d("cibo", "sono click e voglio vedere session Token " + sessionToken);
            Intent createProduct = new Intent(this, CreateProduct.class);
            createProduct.putExtra("sessionToken", sessionToken);
            createProduct.putExtra("accessToken", received_token); //bearer Token
            createProduct.putExtra("barcode", passed_Barcode);
            startActivity(createProduct);

        });

    }

    private void getProducts(String barcode) {
        try {
            Call<ListProducts> call = lam_api.getProducts("Bearer " + received_token, barcode);

            call.enqueue(new Callback<ListProducts>() {
                @Override
                public void onResponse(Call<ListProducts> call, Response<ListProducts> response) {
                    if (!response.isSuccessful()) {
                        textViewResult.setText("Code: " + response.code());
                    }

                    tmp = response.body();
                    productview.setProducts_array(tmp.getProducts());
                    listview.setAdapter(productview);

                    Log.d("cibo", "voglio vedere eheeeeeeeeeeeeeeeeeeeeeeeeeeee "+response.body().getToken());
                    SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME1, MODE_PRIVATE).edit();
                    editor.putString(MY_KEY1, response.body().getToken());
                    editor.apply();
                    ///da capire perché non funziona, ma funziona con il login
                }

                @Override
                public void onFailure(Call<ListProducts> call, Throwable t) {
                    textViewResult.setText(t.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME1, Context.MODE_PRIVATE);
        String myData = sharedPreferences.getString(MY_KEY1, "");
        Log.d("cibo","sono shared preference muahahha "+ myData);
        sessionToken = myData;

    }



}
