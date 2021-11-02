package com.example.prova1progetto;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateProduct  extends AppCompatActivity {
    private Button savebutton;
    private EditText nametext;
    private EditText descriptiontext;
    private Switch testswitch;
    private String received_token;
    private String received_sessiontoken;
    private String received_barcode;
    private LAM_Api lam_api;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createproduct);
        nametext = (EditText) findViewById(R.id.id_name);
        descriptiontext = (EditText) findViewById(R.id.id_description);
        savebutton = (Button) findViewById(R.id.id_saveproduct);
        testswitch = (Switch) findViewById(R.id.id_test);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            received_barcode = extras.getString("barcode");
            received_token = extras.getString("accessToken");
            received_sessiontoken = extras.getString("sessionToken");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lam21.iot-prism-lab.cs.unibo.it/") //l'url è stato cambiato
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        lam_api = retrofit.create(LAM_Api.class);

        savebutton.setOnClickListener(v -> {
            saveProduct();
        });
    }

    private void saveProduct() {

        try {
            HashMap<String, Object> postData = new HashMap<>();
            postData.put("token", received_sessiontoken);
            postData.put("name", this.nametext.getText().toString());
            postData.put("description", this.descriptiontext.getText().toString());
            postData.put("barcode", received_barcode);
            postData.put("test", String.valueOf(testswitch.isChecked()));
            Call<Product> call = lam_api.postProduct("Bearer " + received_token, postData);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Log.d("cibo", "response");
                    if (!response.isSuccessful()) {
                        Log.d("diamond","you did but didnt work "+ String.valueOf(response));
                    }
                    Log.d("diamond", String.valueOf(response.body()));
                    Log.d("diamond", "anananananannanana "+ response.code());

                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Log.d("diamond", "ho failato male");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
