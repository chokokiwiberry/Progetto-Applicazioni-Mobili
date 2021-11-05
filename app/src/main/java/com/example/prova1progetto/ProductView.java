package com.example.prova1progetto;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductView extends BaseAdapter {
    private List<Product> products_array;
    Context activity;

    public List<Product> getProducts_array() {
        return products_array;
    }

    public void setProducts_array(List<Product> products_array) {
        this.products_array = products_array;
    }
    ProductView(Context activity){
        this.activity = activity;
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
        Log.d("cibo", "ma ci arrivo fino a view?");
        convertView = inflater.inflate(R.layout.product_layout, null);

        ImageView imageProduct = convertView.findViewById(R.id.product_image);
        TextView barcodeProduct = convertView.findViewById(R.id.product_barcode);
        TextView nameProduct = convertView.findViewById(R.id.product_name);
        TextView descriptionProduct = convertView.findViewById(R.id.product_description);

        //have to decode the image received from the server and then set it

        barcodeProduct.setText(products_array.get(position).getBarcode());
        nameProduct.setText(products_array.get(position).getName());
        descriptionProduct.setText(products_array.get(position).getDescription());

        return convertView;
    }
}
