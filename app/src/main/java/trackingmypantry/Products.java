package trackingmypantry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.trackingmypantry.R;

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
//Logica per DELETE DEL SINGOLO PRODOTTO
public class Products extends AppCompatActivity implements ProductInterface {
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofproducts);

        addproduct = findViewById(R.id.id_addproduct);
        listview = findViewById(R.id.listview);


        dbh = new DBHelper(this);

        //prendo il valore di userid da register
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("USERID", Context.MODE_PRIVATE);
        userId = prefs.getString("userid", "");//"No name defined" is the default value.
        Log.d("diamond2", prefs.getString("userid", ""));

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
            Log.d("cibo", "sono click e voglio vedere session Token " + sessionToken);
            Intent createProduct = new Intent(this, CreateProduct.class);
            createProduct.putExtra("sessionToken", sessionToken);
            createProduct.putExtra("accessToken", received_token); //bearer Token
            createProduct.putExtra("barcode", passed_Barcode);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("The call didn't work as expected...");
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

                    tmp = response.body();
                    for (int i=0; i<tmp.getProducts().size(); i++){
                        Log.d("tronk", String.valueOf(tmp.getProducts().get(i).getImage()));
                    }
                    productview.setProducts_array(tmp.getProducts());
                    listview.setAdapter(productview);

                    Log.d("cibo", "voglio vedere eheeeeeeeeeeeeeeeeeeeeeeeeeeee " + response.body().getToken());
                    SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME1, MODE_PRIVATE).edit();
                    editor.putString(MY_KEY1, response.body().getToken());
                    editor.apply();
                    ///da capire perché non funziona, ma funziona con il login
                }

                @Override
                public void onFailure(Call<ListProducts> call, Throwable t) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("The call failed! "+t.toString());
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
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME1, Context.MODE_PRIVATE);
        String myData = sharedPreferences.getString(MY_KEY1, "");
        Log.d("cibo", "sono shared preference muahahha " + myData);
        sessionToken = myData;

    }

    @Override
    public void postRank(float rank, String idProd) {
        Log.d("palla", "sono in products e sono interfaccia overridato " + rank + "sono prodotto id " + idProd);
        try {
            Map<String, Object> postData = new HashMap<>();
            postData.put("token", sessionToken);
            postData.put("rating", rank);
            postData.put("productId", idProd);

            Log.d("palla", "sto per essere impachettato: " + postData);

            Call<Product> call = lam_api.postPreference("Bearer " + received_token, postData);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (!response.isSuccessful()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("The call didn't work as expected...");
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

                        Log.d("palla", "nope rating nn ha funzionato");
                        Log.d("palla", response.toString());
                        return;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Product has been rated!");
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

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("The call has failed");
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(String idProd, String idUser) {
        Log.d("diamond2", "sono di products " +userId);
        Log.d("diamond2", "sono stato  passato " + idUser);
        if (idUser.equals(userId)){
            try{
                Call<Product> call = lam_api.deleteProduct("Bearer " + received_token, idProd);
                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (!response.isSuccessful()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("The call didn't work as expected...");
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

                            return;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Product has been deleted!");
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

                        Product res = response.body();
                        Log.d("palla", "ecco hai cancellato " + res);
                        Log.d("palla", response.message());
                        Log.d("palla", "il codice di cancellazione " + response.code());
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("The call failed! "+ t.toString());
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
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        } else{

            Toast.makeText(this, "This product is not yours", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void saveProduct(String prod_name, String prod_desc, String prod_img, String prod_date) {
        Log.d("ciboo", "per caso sono in products?????!");
        Log.d("ciboo", "dovrebbe essere in local");
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

}