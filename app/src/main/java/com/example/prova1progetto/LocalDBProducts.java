package com.example.prova1progetto;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LocalDBProducts extends AppCompatActivity {

    private ListView listview;
    private static DBHelper dbh;
    ProductDBView productDBView = new ProductDBView(LocalDBProducts.this);

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localdbproducts);
        dbh = new DBHelper(this);
        listview = findViewById(R.id.listview);
        productDBView.setProducts_array(dbh.getAllElements());
        listview.setAdapter(productDBView);
    }
}
