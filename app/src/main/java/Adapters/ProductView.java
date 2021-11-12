package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import Interfaces.ProductInterface;
import com.trackingmypantry.R;

import java.util.List;

import POJO.Product;

//Adapter per vedere i prodotti ricevuti dal server
public class ProductView extends BaseAdapter {
    private List<Product> products_array;



    Context activity;
    ProductInterface productInterface;


    public List<Product> getProducts_array() {
        return products_array;
    }

    public void setProducts_array(List<Product> products_array) {
        this.products_array = products_array;
    }
    public ProductView(Context activity, ProductInterface productInterface){

        this.activity = activity;
        this.productInterface = productInterface;
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
        convertView = inflater.inflate(R.layout.product_layout, null);

        //prendo il valore di userid da register


        ImageView imageProduct = convertView.findViewById(R.id.product_image);
        TextView barcodeProduct = convertView.findViewById(R.id.product_barcode);
        TextView nameProduct = convertView.findViewById(R.id.product_name);
        TextView descriptionProduct = convertView.findViewById(R.id.product_description);
        RatingBar ratingproduct = convertView.findViewById(R.id.product_ranking);

        Button saveproduct = convertView.findViewById(R.id.product_save);
        Button deleteproduct = convertView.findViewById(R.id.product_delete);

        //have to decode the image received from the server and then set it

        barcodeProduct.setText(products_array.get(position).getBarcode());
        nameProduct.setText(products_array.get(position).getName());
        descriptionProduct.setText(products_array.get(position).getDescription());
        Object tmp = products_array.get(position).getImage();

        if (tmp!=null){
          Bitmap image = decodeImage(tmp.toString());
          imageProduct.setImageBitmap(image);
        } else{
            imageProduct.setImageBitmap(null);
        }


        //questo è il modo per poter ascoltare alla rating bar
       ratingproduct.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
           @Override
           public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               Log.d("palla", "ma perch");
               productInterface.postRank(rating, products_array.get(position).getId());

           }
       });
        saveproduct.setOnClickListener(v->{
            String data;
            Log.d("ciboo", "ma is spero che funzioni?");
            if (products_array.get(position).getImage()==null){
                data = "null";
            } else{
                data = products_array.get(position).getImage().toString();
            }
            productInterface.saveLocalProduct(
                    products_array.get(position).getName(),
                    products_array.get(position).getDescription(),
                    data,
                    products_array.get(position).getCreatedAt().toString()
            );
            products_array.add(products_array.get(position));
            this.setProducts_array(products_array);
            this.notifyDataSetChanged();
        });

        deleteproduct.setOnClickListener(v->{
            if (productInterface.deleteProduct(products_array.get(position).getId(), products_array.get(position).getUserId())) {
                products_array.remove(products_array.get(position));
                this.setProducts_array(products_array);
                this.notifyDataSetChanged();
            }
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

