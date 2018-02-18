package com.example.admin.seminar3;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;

import databases.QuotationRoomDatabase;
import databases.SQLiteOpenHelperSeminarDatabase;
import pojoObjects.Quotation;

public class QuotationActivity extends AppCompatActivity {

    private static final String LIST_PREFERENCE_DATABASE = "list_preference_database";
    private static final String EMPTY_FIELD = "";
    private static final String USERNAME = "username";
    private static final String QUOTATION_KEY_SAVED = "quotation_key";
    private static final String AUTHOR_KEY_SAVED = "author_key";
    private static final String QUOTATION_NUMBER_KEY_SAVED = "quotation_number_key";
    private static final String ADD_OPTION_KEY_SAVED = "add_option_key";
    private static final int SQLITE_HELPER_DATABASE = 0;
    private static final int ROOM_DATABASE = 1;

    private static int recivedQuotations = 0;
    private static Menu optionsMenu;
    private static TextView textViewQuotation;
    private static TextView textViewAuthor;
    private static boolean add_option_visible = false;
    private static int selected_database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        //Log.d("keySet emptyness", savedInstanceState.keySet().isEmpty() ? "Empty":"No empy");
        selectDatabase();
        if (savedInstanceState == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            TextView textView = findViewById(R.id.quotation_activity_tv_refreshview);
            if (preferences.contains(USERNAME) && !preferences.getString(USERNAME, EMPTY_FIELD).isEmpty()) {
                textView.setText(String.format(getResources().getString(R.string.text_view_refresh_info), preferences.getString(USERNAME, EMPTY_FIELD)));
            } else {
                textView.setText(String.format(getResources().getString(R.string.text_view_refresh_info), getResources().getString(R.string.nameless)));
            }
        } else {
            //retrieve last access status
            textViewQuotation = findViewById(R.id.quotation_activity_tv_refreshview);
            textViewAuthor = findViewById(R.id.quotation_activity_empty_tv);
            onCreateOptionsMenu(optionsMenu);
            textViewQuotation.setText(savedInstanceState.getString(QUOTATION_KEY_SAVED));
            textViewAuthor.setText(savedInstanceState.getString(AUTHOR_KEY_SAVED));
            recivedQuotations = savedInstanceState.getInt(QUOTATION_NUMBER_KEY_SAVED);
            add_option_visible = savedInstanceState.getBoolean(ADD_OPTION_KEY_SAVED);
        }
    }

    private void selectDatabase() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString(LIST_PREFERENCE_DATABASE, "1").equals("1"))
            selected_database = ROOM_DATABASE;
        else selected_database = SQLITE_HELPER_DATABASE;
    }

    private void refreshQuotation() {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder, Locale.ENGLISH);
        textViewQuotation = findViewById(R.id.quotation_activity_tv_refreshview);
        textViewQuotation.setText(formatter.format(getResources().getString(R.string.text_view_sample_quotation), recivedQuotations).toString());
        textViewAuthor = findViewById(R.id.quotation_activity_empty_tv);
        textViewAuthor.setText(formatter.format(getResources().getString(R.string.text_view_sample_author), recivedQuotations).toString());
    }

    private Boolean existsQuotation(String quote, String author) {
        return selected_database == SQLITE_HELPER_DATABASE ?
                SQLiteOpenHelperSeminarDatabase.getInstance(this).isQuotationFavourite(this, new Quotation(quote, author))
                : !(QuotationRoomDatabase.getInstance(this).quotationDao().getQuotation(quote) == null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quotation_activity, menu);
        optionsMenu = menu;
        optionsMenu.findItem(R.id.item_add).setVisible(add_option_visible);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_add:
                menuItem.setVisible(false);
                add_option_visible = false;
                addQuotation(new Quotation(textViewQuotation.getText().toString(), textViewAuthor.getText().toString()));
                return true;
            case R.id.item_refresh:
                refreshQuotation();
                recivedQuotations++;
                if (!existsQuotation(textViewQuotation.getText().toString(), textViewAuthor.getText().toString())) {
                    optionsMenu.findItem(R.id.item_add).setVisible(true);
                    add_option_visible = true;
                }
                //((MenuItem) findViewById(R.id.item_add)).setVisible(true);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (textViewQuotation != null && textViewQuotation != null) {
            savedInstanceState.putString(QUOTATION_KEY_SAVED, textViewQuotation.getText().toString());
            savedInstanceState.putString(AUTHOR_KEY_SAVED, textViewAuthor.getText().toString());
            savedInstanceState.putInt(QUOTATION_NUMBER_KEY_SAVED, recivedQuotations);
            savedInstanceState.putBoolean(ADD_OPTION_KEY_SAVED, add_option_visible);
        }
    }

    public void addQuotation(Quotation quotation) {
        if (selected_database == SQLITE_HELPER_DATABASE) {
            SQLiteOpenHelperSeminarDatabase.getInstance(this).insertQuotation(this, quotation.getQuoteText(), quotation.getQuoteAuthor());
        } else {
            QuotationRoomDatabase.getInstance(this).quotationDao().addQuotation(quotation);
        }
    }
}
