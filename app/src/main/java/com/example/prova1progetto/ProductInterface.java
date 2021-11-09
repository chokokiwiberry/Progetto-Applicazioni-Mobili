package com.example.prova1progetto;

public interface ProductInterface {

    //logica per le funzioni sul webserver
    void postRank(float rank, String idProd);

    void deleteProduct(String idProd, String userId);

    void saveProduct(String prod_name, String prod_desc, String prod_img, String prod_date);
}
