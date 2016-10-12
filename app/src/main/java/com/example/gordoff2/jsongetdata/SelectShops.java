package com.example.gordoff2.jsongetdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Created by gordoff2 on 05.10.16.
 */

public class SelectShops  extends AppCompatActivity {

    // addiert alle Shop zaehlen im case für entsprechende sqlabfragen
    int shopZaehler = 0;
    final int aldi = 1;
    int penny = 2;
    int edeka = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_all_shops);

        final CheckBox checkAldi = (CheckBox) findViewById(R.id.checkBox5);
        final CheckBox checkPenny = (CheckBox) findViewById(R.id.checkBox2);

        Button displayBtn = (Button) findViewById(R.id.button);
        displayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopZaehler = 0;

                // --------- ausgewählte checkbox Shops checken ---------
                if (checkAldi.isChecked()) {
                    shopZaehler = shopZaehler + aldi;
                }

                if (checkPenny.isChecked()) {
                    shopZaehler = shopZaehler + penny;
                }


                Toast.makeText(SelectShops.this, "Shopzähler: " + shopZaehler, Toast.LENGTH_SHORT).show();
/*
                String newLine = System.getProperty("line.separator");
                Toast.makeText(SelectShops.this,
                        "Checked checked " + checkAldi.isChecked() + newLine
                                +  "CheckBox2 checked " + checkPenny.isChecked(),
                        Toast.LENGTH_SHORT).show();
*/

                // *************** ausgewählte shops an ViewAllProducts übergeben

                Bundle sql_para = new Bundle();
                sql_para.putInt("sql_para", shopZaehler);


                Intent intent = new Intent(SelectShops.this, ViewAllProducts.class);
                intent.putExtras(sql_para);
                startActivity(intent);
                //return super.onOptionsItemSelected(item);



            }
        });


    }






}
