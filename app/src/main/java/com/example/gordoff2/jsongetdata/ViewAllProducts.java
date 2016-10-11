package com.example.gordoff2.jsongetdata;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gordoff2 on 01.10.16.
 */

public class ViewAllProducts extends AppCompatActivity {
    /**
     * Returns a string containing a concise, human-readable description of this
     * object. Subclasses are encouraged to override this method and provide an
     * implementation that takes into account the object's type and data. The
     * default implementation is equivalent to the following expression:
     * <pre>
     *   getClass().getName() + '@' + Integer.toHexString(hashCode())</pre>
     * <p>See <a href="{@docRoot}reference/java/lang/Object.html#writing_toString">Writing a useful
     * {@code toString} method</a>
     * if you intend implementing your own {@code toString} method.
     *
     * @return a printable representation of this object.
     */

    private final static String LOG = ViewAllProducts.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;




    // entsprechenden sql-string aufrufen
    private int sql_para;


    //Toast.makeText(ViewAllProducts.this, "Shopzähler: " + text2, Toast.LENGTH_SHORT).show();


// URL to get products JSON

    private static String url = "http://www.microfuture.de/android/select_products_aldi.php";
    private static String url2 = "http://www.microfuture.de/android/select_products_edeka.php";
    private static String url3 = "http://www.microfuture.de/android/select_products_all.php";

    TextView t;
    private String sText;

    //private static String url3 = "http://www.microfuture.de/android/select_products.php";

    ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_all_products);

        //editText_hint_shop = (EditText) findViewById(R.id.listview_products);

        t=new TextView(this);
        t=(TextView)findViewById(com.example.gordoff2.jsongetdata.R.id.editText_show_shops);

        sText = "Step One: unpack egg";



        /*
         *********************** datenübergabe (integer) von SelectShops
         * je nachdem welcher Shop in SelectShops ausgewählt wurde, wird die entsprechende SQL-Abfrage
         * ausgeführt
        */




        productList = new ArrayList<>();

        //lv = (ListView) findViewById(R.id.list);
        lv = (ListView) findViewById(R.id.listview_products);

        new getProducts().execute();
    }



    /**
     * Async task class to get json by making HTTP call
     */
    private class getProducts extends AsyncTask<Void, Void, Void> {

        String jsonStr = new String();
        //HttpHandler sh = new HttpHandler();




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progress dialog erstellen, anzeigen
            pDialog = new ProgressDialog(ViewAllProducts.this);
            pDialog.setMessage("Bitte warten...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Bundle sqlAuswahlString = getIntent().getExtras();
            int text2 = sqlAuswahlString.getInt("sql_para");

            HttpHandler sh = new HttpHandler();
            String jsonStr = new String();

            Log.d("sql_para: ", String.valueOf(text2));
            switch(text2) {
                case 1:
                    jsonStr = sh.makeServiceCall(url);
                    sText = "Aldi";
                    break;
                case 2:
                    sText = "Edeka";
                    jsonStr = sh.makeServiceCall(url2);
                    break;
                case 3:
                    sText = "Aldi / Edeka";
                    jsonStr = sh.makeServiceCall(url3);
                    break;
            }


            //jsonStr = sh.makeServiceCall(url);

            Log.e(LOG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    // Getting JSON Array node
                    //JSONArray products = jsonObj.getJSONArray("products");
                    JSONArray products = new JSONArray(jsonStr);


                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        //JSONObject c = products.getJSONObject(i);

                        JSONObject c = jsonArray.getJSONObject(i);

                        //JSONArray c = products.getJSONArray(i);
                        //JSONArray data = jsonObj.getJSONArray("jsonArray");


                        //JSONArray c1 = getJSONArray();

                        // get DB-Fields
                        //String id = data.getString("id");
                        String id = c.getString("id");

                        //String value = c.getString("Fieldname");


                        String name = c.getString("productname");
                        //Log.d("Productname: ", name);

                        String price = c.getString("price");

/*
                        String email = c.getString("email");


*/



/*
                        JSONObject phone = c.getJSONObject("phone");



*/
                        // tmp hash map for single product
                        HashMap<String, String> product = new HashMap<>();

                        // adding each child node to HashMap key => value
                        //product.put("id", id);
                        product.put("productname", name);
                        product.put("price", price + "€");
/*
                        contact.put("email", email);

*/
                        // adding products to product list
                        productList.add(product);
                        Log.d("Item name: ", id);
                        Log.d("Item name: ", name);
                        Log.d("Price: ", price);

                    }
                } catch (final JSONException e) {
                    Log.e(LOG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(LOG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    ViewAllProducts.this, productList,
                    R.layout.list_item, new String[]{"id", "productname", "price"}, new int[]{R.id.id,
                    R.id.productname, R.id.price});
            //R.layout.list_item, new String[]{"id", "productname"}, new int[]{R.id.id,
                 //   R.id.productname});

            lv.setAdapter(adapter);

            // ausgewählte(n) Supermarkt anzeigen
            t.setText("Auswahl: " + sText + " Produkte");
        }

    }


    // checken ob internet verbunden
    public boolean internetAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

}
