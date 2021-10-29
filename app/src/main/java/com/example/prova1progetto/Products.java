package com.example.prova1progetto;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Products<gson> extends AppCompatActivity {
    private LAM_Api lam_api;

    private String passed_Barcode; // barcode passato dal pantry, digitato dall'utente

    private String received_token;


    private TextView textViewResult;


    private Gson gson;



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
        ArrayList<Object> dataArrayList = new ArrayList<>();

        try {
            Log.d("cibo", "mah chi gli passo tryyyyyyy?" + barcode);
            Call<ListProducts> call = lam_api.getProducts("Bearer "+received_token, barcode);

            Log.d("cibo", "dopo laaaaaaaaaaaaaaaaam");
            call.enqueue(new Callback<ListProducts>() {
                @Override
                public void onResponse(Call<ListProducts> call, Response<ListProducts> response) {
                    Log.d("cibo", "prima dell if");
                    Log.d("cibo", "sono la befana "+ response.code());
                    Log.d("cibo", "nanananananann "+ response.body().getProducts().get(0));
                    Product tmp = response.body();
                    Log.d("cibo", "sono tmp var assegnata "+ tmp);
                    if (!response.isSuccessful()) {
                        textViewResult.setText("Code: " + response.code());
                    }



                  /*  try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        Log.d("cibo", "sono jsonobj " +jsonObject );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } */
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
                public void onFailure(Call<ListProducts> call, Throwable t) {
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
