package com.example.prova1progetto;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class Pantry extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    private static final String TAG = "cibo";
    private String barcode;
    EditText barcode_input;
    Button getButton;

    Button checkPantry; //button per vedere cosa hai dentro al pantry

    private String passed_Token = "";
  //  private LamApi2021 lamapi2021;
    private String received_token ="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        
        barcode_input = (EditText) findViewById(R.id.id_insertbarcode);

        getButton = findViewById(R.id.id_getproducts);



        getButton.setOnClickListener(v->{
            Bundle extras = getIntent().getExtras();
            if (extras!=null) {
                received_token = extras.getString("token");
                Log.d("cibo", "sono stato ricevuto dal login?"+  received_token);

            }
            Log.d("cibo", "sono cliccato gettt");

            barcode = barcode_input.getText().toString();
            Log.d(TAG, "prova " +barcode);
           

            Intent Products = new Intent(this, Products.class);
            Products.putExtra("barcode", barcode);
            Products.putExtra( "token", received_token);//per poter inviare questi dati all'altra activity
            startActivity(Products);

        });




        //button per vedere che cosa hai all'interno del proprio pantry
        //quindi ci sarà un database locale

    }

}
