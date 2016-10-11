package com.example.gordoff2.jsongetdata;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements android.view.View.OnClickListener{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ShoppingMemoDataSource dataSource;
    private ListView mShoppingMemosListView;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // contentview setzen
        setContentView(R.layout.activity_main);

        //ShoppingMemo testMemo = new ShoppingMemo("Golf", 5, 105);
        //Log.d(LOG_TAG, "Inhalt Main" + testMemo.toString());

        //
        dataSource = new ShoppingMemoDataSource(this);

        //
        initializeShoppingMemoListView();
        Log.d(LOG_TAG, "Quelle wird geoeffnet");


        dataSource.open();

        activateAddButton();

        //
        initialzeContextualActionBar();

        //ShoppingMemo memo = dataSource.createShoppingMemo("BMW",5);

        // beim start alle vorhandenen eintraege anzeigen
        //showAllListEntries();

//        Log.d(LOG_TAG,"Quelle wird geschlossen");
        // dataSource.close();




        Button b = (Button) findViewById(R.id.button_nextSite);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });



    }

    //
    private void initializeShoppingMemoListView() {
        List<ShoppingMemo> emptyListForInitialization = new ArrayList<>();
        //
        mShoppingMemosListView = (ListView) findViewById(R.id.listview_shopping_memos);

        // ArrayAdapter deklarieren - schnittstelle - layout mitgeben
        ArrayAdapter<ShoppingMemo> arrayAdapter = new ArrayAdapter<ShoppingMemo>(this, android.R.layout
                .simple_list_item_multiple_choice, emptyListForInitialization) {
            public View getView(int position, View convertView, ViewGroup parent) {
                // implementierung aus der superklasse holen
                View view = super.getView(position, convertView, parent);
                //
                TextView textView = (TextView) view;
                // shoppingMemo Element (eintrag) holen
                ShoppingMemo memo = (ShoppingMemo) mShoppingMemosListView.getItemAtPosition(position);
                if (memo.isBought()) {
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    textView.setTextColor(Color.rgb(175, 175, 175));
                } else {
                    textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    textView.setTextColor(Color.DKGRAY);
                }
                return view;
            }

        };
        mShoppingMemosListView.setAdapter(arrayAdapter);
        mShoppingMemosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p/>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // element aus memo holen
                ShoppingMemo memo = (ShoppingMemo) parent.getItemAtPosition(position);

                ShoppingMemo updateMemo = dataSource.updateShoppingMemo(memo.getId(), memo.getProduct(),
                        memo.getQuantity(), memo.getPrice(), !memo.isBought());
                // aktuelle einträge anzeigen
                showAllListEntries();
            }
        });

    }

    // actionbar - mülleimer und stift sichtbar
    private void initialzeContextualActionBar() {
        // shoppingMemoListView holen
        final ListView shoppingMemoListView = (ListView) findViewById(R.id.listview_shopping_memos);
        // auswahlmodus auf mehrfachauswahl setzen
        shoppingMemoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        shoppingMemoListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            /**
             * Called when an item is checked or unchecked during selection mode.
             *
             * @param mode     The {@link ActionMode} providing the selection mode
             * @param position Adapter position of the item that was checked or unchecked
             * @param id       Adapter ID of the item that was checked or unchecked
             * @param checked  <code>true</code> if the item is now checked, <code>false</code>
             */

            int selCount = 0;



            /**
             * Called when an item is checked or unchecked during selection mode.
             *
             * @param mode     The {@link ActionMode} providing the selection mode
             * @param position Adapter position of the item that was checked or unchecked
             * @param id       Adapter ID of the item that was checked or unchecked
             * @param checked  <code>true</code> if the item is now checked, <code>false</code>
             */
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    // counter +1
                    selCount++;
                } else {
                    selCount--;
                }
                final int dialog_title_product = R.string.dialog_title_product;

                // Einzahl, Mehrzahl String ausgeben
                if (selCount > 1) {

                    String capTitle = selCount + " " + getString(R.string.checked_mehrzahl);
                    mode.setTitle(capTitle);
                } else {
                    String capTitle = selCount + " " + getString(R.string.checked);
                    mode.setTitle(capTitle);
                }
                //String capTitle = selCount + " " + getString(R.string.checked);
                // titel setzen
                //mode.setTitle(capTitle);
                // menue aktualisieren (neu zeichnen)

                mode.invalidate();

            }

            /**
             * Called when action mode is first created. The menu supplied will be used to
             * generate action buttons for the action mode.
             *
             * @param mode ActionMode being created
             * @param menu Menu used to populate action buttons
             * @return true if the action mode should be created, false if entering this
             * mode should be aborted.
             */
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_ontextual_action_bar, menu);
                return true;
            }

            /**
             * Called to refresh an action mode's action menu whenever it is invalidated.
             *
             * @param mode ActionMode being prepared
             * @param menu Menu used to populate action buttons
             * @return true if the menu or action mode was updated, false otherwise.
             */
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                //
                MenuItem item = menu.findItem(R.id.change);
                // wenn nur 1 eintrag ausgewählt wurde, wird editierbutton angezeigt
                if (selCount == 1) {
                    item.setVisible(true);
                } else {
                    return false;
                }
                return true;
            }

            /**
             * Called to report a user click on an action button.
             *
             * @param mode The current ActionMode
             * @param item The item that was clicked
             * @return true if this callback handled the event, false if the standard MenuItem
             * invocation should continue.
             */
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                SparseBooleanArray touchedMemoPosition = shoppingMemoListView.getCheckedItemPositions();
                // menue anlegen (Eintrag löschen)
                // ist der rückgabewert ein change oder ein delete
                switch (item.getItemId()) {
                    case R.id.delete:

                        //
                        for (int i = 0; i < touchedMemoPosition.size(); i++) {
                            boolean isChecked = touchedMemoPosition.valueAt(i);
                            if (isChecked) {
                                int posInListView = touchedMemoPosition.keyAt(i);
//                                ShoppingMemo memo = (ShoppingMemo) shoppingMemoListView.getItemIdAtPosition
//                                        (posInListView);
                                //
                                ShoppingMemo memo = (ShoppingMemo) shoppingMemoListView.getItemAtPosition(posInListView);

                                dataSource.deleteShoppingMemo(memo);
                            }

                        }
                        // änderungen anzeigen
                        showAllListEntries();

                        break;

                    case R.id.change:
                        for (int i = 0; i < touchedMemoPosition.size(); i++) {
                            boolean isChecked = touchedMemoPosition.valueAt(i);
                            if (isChecked) {
                                // wert der postition holen
                                int posInListView = touchedMemoPosition.keyAt(i);
//                                ShoppingMemo memo = (ShoppingMemo) shoppingMemoListView.getItemIdAtPosition
//                                        (posInListView);
                                ShoppingMemo memo = (ShoppingMemo) shoppingMemoListView.getItemAtPosition(posInListView);

                                // Modaldialog um einträge zu ändern
                                AlertDialog editShoppingMemoDialog = createEditShoppingMemoDialog(memo);
                                // anzeigen zum bearbeiten
                                editShoppingMemoDialog.show();
                            }
                        }

                        break;


                    default:
                        return false;
                }

                mode.finish();
                return true;

            }

            /**
             * Called when an action mode is about to be exited and destroyed.
             *
             * @param mode The current ActionMode being destroyed
             */
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                //
                selCount = 0;

            }
        });
    }











    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "DEBUG: in onStart");
        dataSource.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "DEBUG: in onStop");
        dataSource.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "DEBUG: in onDestroy");
        dataSource.close();
        //Log.i("im onDestroy", ApplicationInfo.class.getSimpleName().toString());
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "in onPause, DB close", Toast.LENGTH_SHORT);
        dataSource.close();
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "DEBUG: in onResume");
        dataSource.open();
        showAllListEntries();

    }


    /* -------------------------- neuen Datensatz erzeugen -------------*/
    private void activateAddButton() {
        // variablen initialisieren um später drauf zuzugreifen
        // button holen über die id
        Button button = (Button) findViewById(R.id.button_add_product);
        final EditText editQuantity = (EditText) findViewById(R.id.editText_quantity);
        final EditText editProduct = (EditText) findViewById(R.id.editText_product);

        final EditText editPrice = (EditText) findViewById(R.id.editText_price);

        // button einen listener hinzufügen, sonst kann er nicht reagieren
        // anonyme Klasse
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = editQuantity.getText().toString();
                String product = editProduct.getText().toString();

                String price = editPrice.getText().toString();

                // wenn quanty k
                if (TextUtils.isEmpty(quantity)) {
                    editQuantity.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if (TextUtils.isEmpty(quantity)) {
                    editProduct.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                // String aus quantiy zu int parsen
                int quant = Integer.parseInt(quantity);

                double price2 = 0.0;
                price2 = Double.parseDouble(price);

                // Texteingabefelder werte zurücksetzen auf leerstring
                editProduct.setText("");
                editQuantity.setText("");

                editPrice.setText("");






                // datenquelle
                dataSource.createShoppingMemo(product, quant, price2);

                // für die benutzung wird der InputMessageManager benötigt
                InputMethodManager inputMethodManager;
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    // eingabe unsichtbar machen
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                // alle Einträge anzeigen
                showAllListEntries();
//                dataSource.close();

            }
        });
    }

    // alle einträge aus DB anzeigen
    private void showAllListEntries() {
        // liste aus DB holen
        List<ShoppingMemo> list = dataSource.getAllShoppingMemos();
        ArrayAdapter<ShoppingMemo> adapter = (ArrayAdapter<ShoppingMemo>) mShoppingMemosListView.getAdapter();

        adapter.clear();
        // liste der Einträge aus DB zum Adapter hinzufügen
        adapter.addAll(list);
        // wenn der Wert geändert wurde
        adapter.notifyDataSetChanged();
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
            case R.id.action_settings_send:

                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);

            case R.id.action_settings_update:
                Intent intent2 = new Intent(MainActivity.this, SelectShops.class);
                startActivity(intent2);
                return super.onOptionsItemSelected(item);

            default:
                return super.onContextItemSelected(item);
        }






    }


    /* --------------- Artikel bearbeiten --------------------------------------*/
    // um ein dialog zu erzeugen braucht man einen AlertDialog.Builder
    // factory für Alert Dialoge
    private AlertDialog createEditShoppingMemoDialog(final ShoppingMemo memo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        // View wird benötigt:
        View dialogsView = inflater.inflate(R.layout.dialog_edit_shopping_memo, null);

        final EditText editTextQuantity = (EditText) dialogsView.findViewById(R.id.editText_new_quantity);
        editTextQuantity.setText(String.valueOf(memo.getQuantity()));

        final EditText editTextProduct = (EditText) dialogsView.findViewById(R.id.editText_new_product);
        editTextProduct.setText(String.valueOf(memo.getProduct()));

        final EditText editTextPrice = (EditText) dialogsView.findViewById(R.id.editText_new_price);
        editTextPrice.setText(String.valueOf(memo.getPrice()));

        //
        builder.setView(dialogsView)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(
                        R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
                            /**
                             * This method will be invoked when a button in the dialog is clicked.
                             *
                             * @param dialog The dialog that received the click.
                             * @param which  The button that was clicked (e.g.
                             *               {@link DialogInterface#BUTTON1}) or the position
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String quantityString = editTextQuantity.getText().toString();
                                String product = editTextProduct.getText().toString();

                                String priceString = editTextPrice.getText().toString();

                                if (TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(product)) {
                                    return;
                                }
                                int quantity = Integer.parseInt(quantityString);
                                double price = Double.parseDouble(priceString);

                                // geänderte werte in DB speichern
                                ShoppingMemo sMemo = dataSource.updateShoppingMemo(memo.getId(),
                                        product, quantity, price, memo.isBought());
                                showAllListEntries();
                                dialog.dismiss();
                                // keyboard nach eingabe unsichtbar machen
                                //hideKeyboard(getCurrentFocus().getRootView());
                                hideKeyboard();
                            }
                        })
                .setNegativeButton(R.string.dialog_button_negative,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }

                        });

        return builder.create();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,
                InputMethodManager.HIDE_NOT_ALWAYS);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
