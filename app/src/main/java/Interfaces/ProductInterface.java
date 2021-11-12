package Interfaces;

public interface ProductInterface {

    //logica per le funzioni sul webserver
    void postRank(float rank, String idProd);

    boolean deleteProduct(String idProd, String userId);

    //logica per le funzioni in locale
    void saveLocalProduct(String prod_name, String prod_desc, String prod_img, String prod_date);

    void deleteLocalProduct(String idProd);
}
