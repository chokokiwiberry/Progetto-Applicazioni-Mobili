package com.example.prova1progetto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.proto.ProtoOutputStream;

import androidx.annotation.Nullable;

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

    public ArrayList<Product> getAllElements() {

        ArrayList<Product> list = new ArrayList<Product>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Product obj = new Product();
                        //only one column
                        obj.setId(cursor.getString(0));

                        //you could add additional columns here..

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
