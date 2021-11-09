package com.example.prova1progetto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductDBView  extends BaseAdapter {
    private List<Product> products_array;
    Context activity;

    ProductDBView(Context activity){
        this.activity = activity;
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

        TextView nameprod = convertView.findViewById(R.id.namprod);
        TextView descriptionprod = convertView.findViewById(R.id.descriptionprod);

        nameprod.setText(products_array.get(position).getName());
        descriptionprod.setText(products_array.get(position).getDescription());

        return convertView;
    }
}
