package trackingmypantry;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.trackingmypantry.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import Interfaces.LAM_Api;
import POJO.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//Permessi per scegliere una foto dalla galleria
//Permessi per scattare una foto
//Si fa l'encoding dell'immagine in base64
//Logica per la chiamata POST - create new Product

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

    private TextView barcodetext;

    private static final int CAMERA_PERM_CODE = 101;

    ActivityResultLauncher<String> getGalleryContent;
    ActivityResultLauncher<Intent> launchCamera;

    String encodedImage = null;

    //variabile che indica se l'utente abbia cliccato per la chiusura della finestra dialog
    Boolean clickedDialog = false;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createproduct);
        nametext = (EditText) findViewById(R.id.id_name);
        descriptiontext = (EditText) findViewById(R.id.id_description);
        barcodetext = (TextView) findViewById(R.id.id_barcode);
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

        barcodetext.setText(received_barcode);

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
                            Bitmap resizedImage = resizeBitmap(bitmap);
                            imageView.setImageBitmap(resizedImage);
                            encodedImage = encodeImage(resizedImage);
                        }
                    }
                });

        getGalleryContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageView.setImageURI(result);
                try {
                    final Uri imageUri = result;
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    Bitmap resizedImage = resizeBitmap(selectedImage);
                    imageView.setImageBitmap(resizedImage);
                    encodedImage = encodeImage(resizedImage);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }
            }
        });

        takephotobutton.setOnClickListener(v -> {
            askPermission();
        });

        gallerybutton.setOnClickListener(v -> {
            getGalleryContent.launch("image/*");
        });




    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
    private Bitmap resizeBitmap(Bitmap image){
        float aspectRatio = image.getWidth() /
                (float) image.getHeight();
        int width = 100;
        int height = Math.round(width / aspectRatio);
        Bitmap originalBitmap = image;
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                originalBitmap, width, height, false);
        return resizedBitmap;
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
            if (encodedImage != null){
                Log.d("diamond", "sono la  luna  " + encodedImage);
                postData.put("img", encodedImage);
            }
            Call<Product> call = lam_api.postProduct("Bearer " + received_token, postData);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Log.d("cibo", "response");
                    if (!response.isSuccessful()) {
                        Log.d("diamond", "you did but didnt work " + String.valueOf(response));
                        return;
                    }
                    Log.d("diamond", String.valueOf(response.body()));
                    Log.d("diamond", "anananananannanana " + response.code());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Product has been added!");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    clickedDialog = true;
                                    dialog.cancel();
                                }
                            });

                    builder.setNegativeButton(
                            "Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    clickedDialog = true;
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder.create();
                    alert11.show();

                    if(clickedDialog){
                        Pantry();
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

    public Context getContext() {
        return (Context)this;
    }

    private void Pantry(){
        Intent Pantry = new Intent(this, Pantry.class);
        startActivity(Pantry);
    }


}
