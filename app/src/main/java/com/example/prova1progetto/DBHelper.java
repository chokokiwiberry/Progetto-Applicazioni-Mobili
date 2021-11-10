package com.example.prova1progetto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.proto.ProtoOutputStream;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

public static final String TABLE_PRODUCTS = "grades";
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
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
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
                        String tmpDate = cursor.getString(4);
                        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");
                        Date tmpDateParsed = null;
                        try {
                            tmpDateParsed = formatter1.parse(tmpDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        obj.setCreatedAt(tmpDateParsed);

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

}
