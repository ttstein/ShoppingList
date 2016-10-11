package com.example.gordoff2.jsongetdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    Button fetch;
    TextView text;
    EditText et;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);





        //fetch = (Button) findViewById(R.id.action_settings_update);
        //text = (TextView) findViewById(R.id.textView);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //private MenuItem item;
    /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings_send:
                    Intent sendIntent = new Intent(Main3Activity, SendListToSms.class );
                    startActivity(sendIntent);
                    break;
                case R.id.action_settings_update:
                    Intent updateIntent = new Intent(ProductUpdate, ProductUpdate.class);
                    break;
            }
            return super.onOptionsItemSelected(item);
        }
        */


    /**
     * Programmatically opens the options menu. If the options menu is already
     * open, this method does nothing.
     */
    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        int id = item.getItemId();
        if(id == R.id.action_settings_send){
            startActivity(new Intent(Main2Activity.this, Main2Activity.class);
        }
        */

        switch (item.getItemId()) {
            case R.id.action_settings_send:

                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);

            case R.id.action_settings_update:
                Intent intent2 = new Intent(Main2Activity.this, ViewAllProducts.class);
                return super.onOptionsItemSelected(item);

            default:
                return super.onContextItemSelected(item);
        }
    }





}
