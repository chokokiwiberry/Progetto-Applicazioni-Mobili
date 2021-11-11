package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import Interfaces.ProductInterface;
import com.trackingmypantry.R;

import java.util.List;

import POJO.Product;

//adapter per vedere i prodotti in locale
public class ProductDBView  extends BaseAdapter {
    private List<Product> products_array;
    Context activity;
    ProductInterface productInterface;

    public ProductDBView(Context activity, ProductInterface productInterface){
        this.activity = activity;
        this.productInterface = productInterface;
    }
    public List<Product> getProducts_array() {
        return products_array;
    }

    public void setProducts_array(List<Product> products_array) {
        this.products_array = products_array;
    }

    @Override
    public int getCount() {
        return products_array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.product_db_layout, null);

        TextView nameprod = convertView.findViewById(R.id.nameprod);
        TextView descriptionprod = convertView.findViewById(R.id.descriptionprod);
        TextView dateprod = convertView.findViewById(R.id.dateprod);
        ImageView imageprod = convertView.findViewById(R.id.id_imageview);
        Button deleteProd = convertView.findViewById(R.id.button_delete);

        nameprod.setText(products_array.get(position).getName());
        dateprod.setText(products_array.get(position).getCreatedAtString());
        Object tmpImage = products_array.get(position).getImage();

        if (tmpImage!=null){
            Bitmap image = decodeImage(tmpImage.toString());
            imageprod.setImageBitmap(image);
        }

        descriptionprod.setText(products_array.get(position).getDescription());



        deleteProd.setOnClickListener(v->{
            productInterface.deleteLocalProduct(products_array.get(position).getId());
            products_array.remove(products_array.get(position));
            this.setProducts_array(products_array);
            this.notifyDataSetChanged();
        });


        return convertView;
    }
    private Bitmap decodeImage(String encoded){
        final String encodedString = encoded;
        final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return decodedBitmap;
    }
}
