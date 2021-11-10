package com.example.prova1progetto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

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

    private static final int CAMERA_PERM_CODE = 101;
    private String barcode;
    EditText barcode_input;
    Button getButton;
    Button localpantryButton;
    
    private String passed_Token = "";

    private String received_token = "";

    private String received_userId ="";

    private ImageView scanBarcode;

    private String scannedbarcode;

    ScanOptions options = new ScanOptions();
    ActivityResultLauncher<ScanOptions> barcodeLauncher;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        barcode_input = (EditText) findViewById(R.id.id_insertbarcode);

        getButton = findViewById(R.id.id_getproducts);

        localpantryButton = findViewById(R.id.id_checkPantry);

        scanBarcode = findViewById(R.id.id_imageview_scanbarcode);

        getButton.setOnClickListener(v -> {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                received_token = extras.getString("token");
                if (barcode_input.getText().toString().equals("")){
                    barcode = scannedbarcode;
                }else{
                    barcode = barcode_input.getText().toString();
                }
                Intent Products = new Intent(this, Products.class);
                Products.putExtra("barcode", barcode);
                Products.putExtra("token", received_token);//per poter inviare questi dati all'altra activity
                startActivity(Products);
            } else{
                Login();
            }


        });


        localpantryButton.setOnClickListener(v ->{
            Intent LocalDb = new Intent(this, LocalDBProducts.class);
            startActivity(LocalDb);

        });

        scanBarcode.setOnClickListener(v-> {
            askPermission();
        });
        barcodeLauncher = registerForActivityResult(new ScanContract(),
                result -> {
                    if(result.getContents() == null) {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                        scannedbarcode = result.getContents();
                    }
                });

    }

    private void Login(){
        Intent Login = new Intent(this, Login.class);
        startActivity(Login);
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            barcodeLauncher.launch(new ScanOptions());
        }
    }

}
