package trackingmypantry;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.trackingmypantry.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Pantry extends AppCompatActivity {

    private static final int CAMERA_PERM_CODE = 101;
    private String barcode;
    EditText barcode_input;
    Button getButton;
    Button localpantryButton;

    private String passed_Token = "";

    private String received_token = "";
    private String prefToken;


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
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            prefToken = preferences.getString("token", "");

            if (extras != null) {
                received_token = extras.getString("token");
                passed_Token = received_token;
            }
            else if (!prefToken.isEmpty()) {
               passed_Token = prefToken;
            }
            if (barcode_input.getText().toString().equals("")){
                    barcode = scannedbarcode;
            }else{
                    barcode = barcode_input.getText().toString();
                }
                Intent Products = new Intent(this, Products.class);
                Products.putExtra("barcode", barcode);
                Products.putExtra("token", passed_Token);
                startActivity(Products);
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
    public Context getContext() {
        return (Context)this;
    }
}
