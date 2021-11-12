package trackingmypantry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.trackingmypantry.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Adapters.ProductDBView;
import Interfaces.ProductInterface;
import POJO.Product;

public class LocalDBProducts extends AppCompatActivity implements ProductInterface {

    private ListView listview;
    private static DBHelper dbh;
    ProductDBView productDBView = new ProductDBView(LocalDBProducts.this, this);
    Button btn_searchProduct;
    EditText searchproduct_text;
    List<Product> tmp = new ArrayList<>();

    Button btn_addProduct;
    private String searched_prod;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localdbproducts);

        btn_searchProduct = findViewById(R.id.btn_searchproduct);
        searchproduct_text = findViewById(R.id.id_searchproduct);
        btn_addProduct = findViewById(R.id.addproductdb);

        dbh = new DBHelper(this);
        listview = findViewById(R.id.listview);

        productDBView.setProducts_array(dbh.getAllElements());
        listview.setAdapter(productDBView);

        btn_searchProduct.setOnClickListener(v -> {
            searchProduct();
        });
        btn_addProduct.setOnClickListener(v->{
            addProduct();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        productDBView.setProducts_array(dbh.getAllElements());
        listview.setAdapter(productDBView);

    }

    @Override
    public void postRank(float rank, String idProd) {

    }

    @Override
    public boolean deleteProduct(String idProd, String userId) {
        return false;
    }


    @Override
    public void deleteLocalProduct(String idProd) {
        dbh.deleteProduct(idProd);
    }

    private void searchProduct() {
        Log.d("niente", "tanto per");
        tmp = dbh.getProds(searchproduct_text.getText().toString());
        if (tmp == null) {
            Log.d("niente", "fai schifo");
        } else {
            for (int i = 0; i < tmp.size(); i++) {
                Log.d("niente", tmp.get(i).getName());
            }
            productDBView.setProducts_array(tmp);
            listview.setAdapter(productDBView);
        }
    }

    private void addProduct(){
        Intent addProd = new Intent(this, CreateProduct.class);
        addProd.putExtra("classFrom", "LocalDBProducts");
        startActivity(addProd);
    }

    public void saveLocalProduct(String name, String description, String image, String date){
        Log.d("miao", "sono localSave");
        //dbh.insertNewProduct(name, description, image, date);
    }



}

