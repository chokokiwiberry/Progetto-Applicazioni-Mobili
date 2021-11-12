package trackingmypantry;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
                Log.d("diamond4", "sono token: "+received_token);
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
