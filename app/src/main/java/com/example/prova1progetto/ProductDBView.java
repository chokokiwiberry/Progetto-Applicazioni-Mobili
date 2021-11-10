package com.example.prova1progetto;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
//adapter per vedere i prodotti in locale
public class ProductDBView  extends BaseAdapter {
    private List<Product> products_array;
    Context activity;
    ProductInterface productInterface;

    ProductDBView(Context activity, ProductInterface productInterface){
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

        TextView nameprod = convertView.findViewById(R.id.namprod);
        TextView descriptionprod = convertView.findViewById(R.id.descriptionprod);

        nameprod.setText(products_array.get(position).getName());

        descriptionprod.setText(products_array.get(position).getDescription());

        Button deleteProd = convertView.findViewById(R.id.button_delete);

        deleteProd.setOnClickListener(v->{
            productInterface.deleteLocalProduct(products_array.get(position).getId());
            products_array.remove(products_array.get(position));
            this.setProducts_array(products_array);
            this.notifyDataSetChanged();
        });


        return convertView;
    }
}
