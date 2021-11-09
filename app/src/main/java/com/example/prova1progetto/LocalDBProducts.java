package com.example.prova1progetto;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LocalDBProducts extends AppCompatActivity implements ProductInterface {

    private ListView listview;
    private static DBHelper dbh;
    ProductDBView productDBView = new ProductDBView(LocalDBProducts.this, this);

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localdbproducts);
        dbh = new DBHelper(this);
        listview = findViewById(R.id.listview);
        //dbh.getAllElements è null

        Log.d("cibo", "nananananan " + dbh.getAllElements().size());
        for (int i=0; i<dbh.getAllElements().size(); i++){
            Log.d("cibo", "nanananana batman "+ dbh.getAllElements().get(i).getId());
        }

        productDBView.setProducts_array(dbh.getAllElements());
        listview.setAdapter(productDBView);
    }

    @Override
    public void postRank(float rank, String idProd) {

    }

    @Override
    public void deleteProduct(String idProd, String userId) {

    }

    @Override
    public void saveProduct(String prod_name, String prod_desc, String prod_img, String prod_date) {
    }

    @Override
    public void deleteLocalProduct(String idProd) {
        Log.d("cibo", "sono in localDBLOCALDB sono delete local");
        dbh.deleteProduct(idProd);
    }
}
