package com.example.prova1progetto;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CreateProduct extends AppCompatActivity {

    private Button savebutton, gallerybutton;

    private EditText nametext;
    private EditText descriptiontext;
    private Switch testswitch;
    private String received_token;
    private String received_sessiontoken;
    private String received_barcode;
    private Button takephotobutton;
    private LAM_Api lam_api;
    private ImageView imageView;


    private static final int CAMERA_PERM_CODE = 101;

    ActivityResultLauncher<String> getGalleryContent;
    ActivityResultLauncher<Intent> launchCamera;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createproduct);
        nametext = (EditText) findViewById(R.id.id_name);
        descriptiontext = (EditText) findViewById(R.id.id_description);
        savebutton = (Button) findViewById(R.id.id_saveproduct);
        testswitch = (Switch) findViewById(R.id.id_test);
        takephotobutton = (Button) findViewById(R.id.id_takephoto);
        gallerybutton = (Button) findViewById(R.id.id_gallery);
        imageView = findViewById(R.id.id_imageview);


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

        launchCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Log.d("diamond", "credo che non arrivi qui");
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                });

        getGalleryContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageView.setImageURI(result);
            }
        });

        takephotobutton.setOnClickListener(v -> {
            askPermission();
        });

        gallerybutton.setOnClickListener(v -> {
            getGalleryContent.launch("image/*");
        });


    }


    private void saveProduct() {
        try {
            Map<String, Object> postData = new HashMap<>();
            postData.put("token", received_sessiontoken);
            postData.put("name", this.nametext.getText().toString());
            postData.put("description", this.descriptiontext.getText().toString());
            postData.put("barcode", received_barcode); //credo che non farò inserire il barcode dall'utente - che venga letto da camera o meno
            postData.put("test", testswitch.isChecked()); //quando ci sarà la versione finale, questo sarà sempre di default falso
            Log.d("diamond", "sto per essere impachettato: " + postData);
            Call<Product> call = lam_api.postProduct("Bearer " + received_token, postData);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Log.d("cibo", "response");
                    if (!response.isSuccessful()) {
                        Log.d("diamond", "you did but didnt work " + String.valueOf(response));
                    }
                    Log.d("diamond", String.valueOf(response.body()));
                    Log.d("diamond", "anananananannanana " + response.code());
                    if (response.code() == 201) {
                        //faccio partire un messaggio con l'anteprima oppure messaggio ok
                        //reinderizzamento
                    }
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

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            launchCamera.launch(camera);
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }
}
