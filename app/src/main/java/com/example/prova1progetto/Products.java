package com.example.prova1progetto;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Products  extends AppCompatActivity {
    private LAM_Api lam_api;

    private String passed_Barcode; // barcode passato dal pantry, digitato dall'utente

    private String received_token;


    private TextView textViewResult;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofproducts);
        textViewResult = findViewById(R.id.text_view_result);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            passed_Barcode = extras.getString("barcode");
            received_token = extras.getString("token");
            textViewResult.setText(passed_Barcode);
            textViewResult.setText(received_token);
        }

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + received_token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://lam21.iot-prism-lab.cs.unibo.it/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        lam_api = retrofit.create(LAM_Api.class);


        //textViewResult.setText("ciao sono product");
        Log.d("cibo", "sono stato chiamato");
        getProducts(passed_Barcode);
        Log.d("cibo", "dopo hehehe");

    }

    private void getProducts(String barcode) {
        Log.d("cibo", "sono dentro");

        try {
            Log.d("cibo", "mah chi gli passo tryyyyyyy?" + barcode);
            Call<Product> call = lam_api.getProducts("Bearer "+received_token, barcode);

            Log.d("cibo", "dopo laaaaaaaaaaaaaaaaam");
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Log.d("cibo", "prima dell if");
                    Log.d("cibo", "sono la befana "+ response.code());
                    Log.d("cibo", "nanananananann "+ response.body());
                    if (!response.isSuccessful()) {
                        textViewResult.setText("Code: " + response.code());
                    }
                    Log.d("cibo", "scopriremo la verità conan");
                 //   List<Product> products = response.body();
               //     Log.d("cibo", products.toString());

                /*for (Product prod: products){
                    String content = "";
                    content += "ID: " + prod.getBarcode() + "\n";
                    content += "Name: " + prod.getName() + "\n";
                    content += "Description: " + prod.getDescription() + "\n\n";

                    textViewResult.append(content);
                } */
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Log.d("cibo", "oh nananna ho failato");
                    Log.d("cibo", String.valueOf(t));
                }
            });


        } catch (Exception e) {
            Log.d("cibo", "sono nel catch non ci ha provato sto scemo");
            e.printStackTrace();
        }
        Log.d("cibo", "and i aaam in the eeeend");
    }
}
