package com.example.prova1progetto;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofproducts);
        textViewResult = findViewById(R.id.text_view_result);

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
                    for (int i = 0; i < response.body().getProducts().size(); i++) {
                        Log.d("cibo", "nuvola " + response.body().getProducts().get(i).getName());

                        String content = "";
                        content += response.body().getProducts().get(i).getBarcode() + "\n";
                        content += response.body().getProducts().get(i).getName() + "\n";
                        content += response.body().getProducts().get(i).getDescription() + "\n";
                        content += response.body().getProducts().get(i).getUserId() + "\n\n";

                        textViewResult.append(content);

                    }
                }

                @Override
                public void onFailure(Call<ListProducts> call, Throwable t) {
                    textViewResult.setText(t.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
