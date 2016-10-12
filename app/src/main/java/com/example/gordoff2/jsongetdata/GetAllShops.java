package com.example.gordoff2.jsongetdata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gordoff2 on 04.10.16.
 */

// get all shops (aldi, penny)
public class GetAllShops extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get products JSON

    private static String url = "http://www.microfuture.de/android/select_products.php";

    ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        productList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

/*
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

*/




        switch (item.getItemId()) {
            /*
            case R.id.action_settings_send:

                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
*/
            case R.id.action_settings_update:
                Intent intent2 = new Intent(GetAllShops.this, Main3Activity.class);
                startActivity(intent2);
                return super.onOptionsItemSelected(item);

            default:
                return super.onContextItemSelected(item);
        }






    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(GetAllShops.this);
            pDialog.setMessage("Bitte warten...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    // Getting JSON Array node
                    //JSONArray products = jsonObj.getJSONArray("products");
                    JSONArray products = new JSONArray(jsonStr);


                    // looping through All Contacts
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
                        Log.d("Item name: ", id);

                        String name = c.getString("productname");

                        //String price = c.getString("price");
/*
                        String email = c.getString("email");


*/



/*
                        JSONObject phone = c.getJSONObject("phone");



*/
                        // tmp hash map for single product
                        HashMap<String, String> product = new HashMap<>();

                        // adding each child node to HashMap key => value
                        product.put("id", id);
                        product.put("productname", name);
/*
                        contact.put("email", email);

*/
                        // adding products to product list
                        productList.add(product);

                        Log.d("Item name: ", name);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
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
                Log.e(TAG, "Couldn't get json from server.");
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
                    GetAllShops.this, productList,
                    R.layout.list_item, new String[]{"id", "productname"}, new int[]{R.id.id,
                    R.id.productname});

            lv.setAdapter(adapter);
        }

    }


}
