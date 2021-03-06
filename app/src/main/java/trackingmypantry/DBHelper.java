package trackingmypantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.constraintlayout.solver.PriorityGoalRow;

import java.util.ArrayList;
import java.util.List;

import POJO.Product;

public class DBHelper extends SQLiteOpenHelper {

public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DATE = "date";

    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;


    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PRODUCTS + "( " + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME	+ " text not null, "
            + COLUMN_DESCRIPTION + " text not null, "
            + COLUMN_IMAGE + " text not null, "
            + COLUMN_DATE + " text not null);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }
    public long insertNewProduct(String name, String description, String image, String date) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_IMAGE, image);
        cv.put(COLUMN_DATE, date);

        long code = getWritableDatabase().insert(TABLE_PRODUCTS, null, cv);
        return code;
    }

    public List<Product> getAllElements() {

        List<Product> list = new ArrayList<Product>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                if (cursor.moveToFirst()) {
                    do {
                        Product obj = new Product();
                        //only one column
                        obj.setId(cursor.getString(0));
                        obj.setName(cursor.getString(1));
                        obj.setDescription(cursor.getString(2));
                        obj.setImage(cursor.getString(3));
                        obj.setCreatedAtString(cursor.getString(4));

                        list.add(obj);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }

        return list;
    }

    public void deleteProduct(String id){
        getWritableDatabase().delete(TABLE_PRODUCTS, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) });
    }

    public Cursor getProductsName(String prodname) {
        return getWritableDatabase().query(TABLE_PRODUCTS, null, COLUMN_NAME + "=?",
                new String[] { prodname }, null, null, null);
    }
    public List<Product> getProds(String prodname) {

        List<Product> list = new ArrayList<Product>();


        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = getProductsName(prodname);
            try {

                if (cursor.moveToFirst()) {
                    do {
                        Product obj = new Product();
                
                        obj.setId(cursor.getString(0));
                        obj.setName(cursor.getString(1));
                        obj.setDescription(cursor.getString(2));
                        obj.setImage(cursor.getString(3));
                        obj.setCreatedAtString(cursor.getString(4));

                        list.add(obj);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }

        return list;
    }



}
