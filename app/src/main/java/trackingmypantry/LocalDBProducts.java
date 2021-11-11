package trackingmypantry;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.trackingmypantry.R;

import Adapters.ProductDBView;
import Interfaces.ProductInterface;

public class LocalDBProducts extends AppCompatActivity implements ProductInterface {

    private ListView listview;
    private static DBHelper dbh;
    ProductDBView productDBView = new ProductDBView(LocalDBProducts.this, this);

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localdbproducts);
        dbh = new DBHelper(this);
        listview = findViewById(R.id.listview);

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
        dbh.deleteProduct(idProd);
    }
}
