0package trackingmypantry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.trackingmypantry.R;

import java.util.HashMap;
import java.util.Map;

import Adapters.ProductView;
import Interfaces.LAM_Api;
import Interfaces.ProductInterface;
import POJO.ListProducts;
import POJO.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Logica per chiamate GET
//Logica per chiamata POSTPREFERENCE
//Logica per SAVE DEL 0000000000000000000SINGOLO PRODOTTO
public class Products e00xtends AppCompatActivity implements ProductInterface {
    private LAM_Api lam_api;

    private String passed_Barcode; // barcode passato dal pantry, digitato dall'utente

    private String received_token;

    private TextView textViewResult;

    private Button addproduct;

    private String sessionToken;

    private String userId;

    String PREF_NAME1 = "session_token";
    String MY_KEY1 = "";
    SharedPreferences sharedPreferences1;


    private RelativeLayout layout;

    private ListView listview;
    private ListProducts tmp;


    ProductView productview = new ProductView(Products.this, this);
    private static DBHelper dbh;

    private Boolean deletedone = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofproducts);

        addproduct = findViewById(R.id.id_addproduct);
        listview = findViewById(R.id.listview);


        dbh = new DBHelper(this);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString("userid", "");


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


        addproduct.setOnClickListener(v -> {
            Intent createProduct = new Intent(this, CreateProduct.class);
            createProduct.putExtra("sessionToken", sessionToken);
            createProduct.putExtra("accessToken", received_token); //bearer Token
            createProduct.putExtra("barcode", passed_Barcode);
            createProduct.putExtra("classFrom",  "Products");
            startActivity(createProduct);

        });

    }



    private void getProducts(String barcode) {
        try {
            Call<ListProducts> call = lam_api.getProducts("Bearer " + received_token, barcode);

            call.enqueue(new Callback<ListProducts>() {
                @Override
                public void onResponse(Call<ListProducts> call, Response<ListProducts> response) {
                    if (!response.isSuccessful()) {
                       AlertDialogFun("The call didn't work as expcted...");
                    } else{
                        tmp = response.body();

                        productview.setProducts_array(tmp.getProducts());
                        listview.setAdapter(productview);



                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("session_token",response.body().getToken());
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call<ListProducts> call, Throwable t) {
                  AlertDialogFun("The call has failed..."+t.toString());

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String myData = preferences.getString("session_token", "");

        sessionToken = myData;

    }


    @Override
    public void postRank(float rank, String idProd) {
        try {
            Map<String, Object> postData = new HashMap<>();
            postData.put("token", sessionToken);
            postData.put("rating", rank);
            postData.put("productId", idProd);

            Call<Product> call = lam_api.postPreference("Bearer " + received_token, postData);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (!response.isSuccessful()){
                        AlertDialogFun("The call didn't work as expected..."+response.toString());
                        return;
                    }

                    AlertDialogFun("The product has been rated!");

                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    AlertDialogFun("The call has failed..."+ t.toString());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteProduct(String idProd, String idUser) {
        if (idUser.equals(userId)){
            try{
                Call<Product> call = lam_api.deleteProduct("Bearer " + received_token, idProd);
                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (!response.isSuccessful()){
                            AlertDialogFun("The call didn't work as expected...");
                            return;
                        }
                        deletedone = true;

                        AlertDialogFun("Product has been deleted!");


                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        AlertDialogFun("The call failed! "+ t.toString());
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        } else{
            Toast.makeText(this, "This product is not yours", Toast.LENGTH_SHORT).show();
        }

    return deletedone;
    }


    @Override
    public void saveLocalProduct(String prod_name, String prod_desc, String prod_img, String prod_date) {
        DBHelper dbh = new DBHelper(getApplicationContext());
        long code = dbh.insertNewProduct(prod_name, prod_desc, prod_img, prod_date);
        if (code != -1)
            Toast.makeText(getApplicationContext(), "Product saved successfully!",
                    Toast.LENGTH_LONG).show();
        finish();

    }

    @Override
    public void deleteLocalProduct(String idProd) {

    }
    public Context getContext() {
        return (Context)this;
    }

    private void AlertDialogFun(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }

}