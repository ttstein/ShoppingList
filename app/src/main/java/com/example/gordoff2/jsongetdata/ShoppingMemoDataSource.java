package com.example.gordoff2.jsongetdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.gordoff2.jsongetdata.ShoppingMemoDbHelper.COLUMN_CHECKED;
import static com.example.gordoff2.jsongetdata.ShoppingMemoDbHelper.COLUMN_ID;
import static com.example.gordoff2.jsongetdata.ShoppingMemoDbHelper.COLUMN_PRICE;
import static com.example.gordoff2.jsongetdata.ShoppingMemoDbHelper.COLUMN_PRODUCT;
import static com.example.gordoff2.jsongetdata.ShoppingMemoDbHelper.COLUMN_QUANTITY;
import static com.example.gordoff2.jsongetdata.ShoppingMemoDbHelper.TABLE_SHOPPING_LIST;


/**
 * Created by Administrator on 06.09.2016.
 */
public class ShoppingMemoDataSource {
    private static final String LOG_TAG = ShoppingMemoDataSource.class.getSimpleName();
    private SQLiteDatabase db;
    private ShoppingMemoDbHelper helper;

    private String[] columns= {
            COLUMN_ID,
            COLUMN_PRODUCT,
            COLUMN_QUANTITY,

            COLUMN_PRICE,

            COLUMN_CHECKED
    };


    public ShoppingMemoDataSource(Context con){
        Log.d(LOG_TAG, " Der Helper wird erzeugt.");
        helper = new ShoppingMemoDbHelper(con);
    }

    public void open(){
        Log.d(LOG_TAG,"Eine Referenz wird angefragt");
        db = helper.getWritableDatabase();
        Log.d(LOG_TAG,"Pfad zur DB " + db.getPath());
    }

    public void close(){
        helper.close();
        Log.d(LOG_TAG,"Datenbank wieder geschlossen");
    }

    public ShoppingMemo createShoppingMemo(String product, int quantity, double price){

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT, product);

        values.put(COLUMN_QUANTITY, quantity);


        values.put(COLUMN_PRICE, price);

        long insertId = db.insert(TABLE_SHOPPING_LIST, null, values);

        Cursor cursor = db.query(TABLE_SHOPPING_LIST,
                columns, COLUMN_ID + "=" + insertId,
                null, null, null, null, null);
        // cursor steht nach insert, hinter dem eingefügten datensatz
        cursor.moveToFirst();
        ShoppingMemo shoppingMemo = cursorToShoppingMemo(cursor);
        cursor.close();
        return shoppingMemo;
    }


    private ShoppingMemo cursorToShoppingMemo(Cursor cursor) {

        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int idProduct = cursor.getColumnIndex(COLUMN_PRODUCT);
        int idQuantity = cursor.getColumnIndex(COLUMN_QUANTITY);

        int idPrice = cursor.getColumnIndex(COLUMN_PRICE);

        int idChecked = cursor.getColumnIndex(COLUMN_CHECKED);

        //
        String product = cursor.getString(idProduct);
        int quantity = cursor.getInt(idQuantity);
        double price = cursor.getDouble((int) idPrice);
        long id = cursor.getLong(idIndex);
        int intValueChecked = cursor.getInt(idChecked);
        // 0 = false
        boolean isChecked = (intValueChecked != 0);

        // shoppingmemo objekt erzeugen und zurückgeben
        ShoppingMemo shoppingMemo = new ShoppingMemo(product, quantity, price, id, isChecked);
        return shoppingMemo;
    }

    
    public List<ShoppingMemo> getAllShoppingMemos(){
        List<ShoppingMemo> shoppingMemoList = new ArrayList<>();
        Cursor c = db.query(TABLE_SHOPPING_LIST,columns,null,null,null,null,null);
        c.moveToFirst();
        ShoppingMemo memo;
        while(!c.isAfterLast()){
            memo = cursorToShoppingMemo(c);
            shoppingMemoList.add(memo);
            c.moveToNext();
        }
        c.close();
        return shoppingMemoList;
    }

    public void deleteShoppingMemo(ShoppingMemo shoppingMemo){
        // Datensatz löschen
        long id = shoppingMemo.getId();
        db.delete(TABLE_SHOPPING_LIST, COLUMN_ID +
                "=" + id, null);

    }

    // update methode
    public ShoppingMemo updateShoppingMemo(long id, String product, int quantity, double price, boolean newChecked){
        // schlüssel wert paare holen
        int intValueChecked = (newChecked) ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT,product);
        values.put(COLUMN_QUANTITY, quantity);

        values.put(COLUMN_PRICE, price);

        values.put(COLUMN_CHECKED, intValueChecked);

        // helper gibt uns die Tabelle
        db.update(TABLE_SHOPPING_LIST, values, COLUMN_ID
        + "=" + id, null);
        // update methode soll objetk zurück liefern, daher cursor benötigt
        Cursor cursor = db.query(TABLE_SHOPPING_LIST, columns,
                COLUMN_ID + "=" + id, null, null, null, null, null);
        cursor.moveToFirst();
        ShoppingMemo shoppingMemo = cursorToShoppingMemo(cursor);
        cursor.close();
        return shoppingMemo;
    }

}
