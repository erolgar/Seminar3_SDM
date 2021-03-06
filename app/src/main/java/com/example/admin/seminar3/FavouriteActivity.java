package com.example.admin.seminar3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Adapters.FavouriteQuotationsArrayAdapter;
import databases.QuotationRoomDatabase;
import databases.SQLiteOpenHelperSeminarDatabase;
import pojoObjects.Quotation;

public class FavouriteActivity extends AppCompatActivity {

    private static final boolean CLEAR_ALL_QUOTATIONS_OPTION_VISIBLE = false;
    private static final String LIST_PREFERENCE_DATABASE = "list_preference_database";
    private static final int SQLITE_HELPER_DATABASE = 0;
    private static final int ROOM_DATABASE = 1;

    private static FavouriteQuotationsArrayAdapter quotationsArrayAdapter;
    private static ListView listViewQuotations;
    private static int selected_database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        selectDatabase();
        quotationsArrayAdapter = new FavouriteQuotationsArrayAdapter(FavouriteActivity.this, R.layout.quotation_list_row, getQuotations());
        listViewQuotations = findViewById(R.id.list_view_quotes);
        listViewQuotations.setAdapter(quotationsArrayAdapter);

        listViewQuotations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aboutAuthorClickListener((TextView) view.findViewById(R.id.text_view_author));
            }
        });
        listViewQuotations.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertRemove = new AlertDialog.Builder(FavouriteActivity.this);
                alertRemove.setTitle(R.string.remove_alert_title);
                alertRemove.setMessage(R.string.remove_alert_question);
                alertRemove.setPositiveButton(R.string.remove_alert_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteQuotation(position);
                        removeItemOfList(position);
                    }
                });
                alertRemove.setNegativeButton(R.string.remove_alert_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertRemove.create().show();
                return true;
            }
        });
    }

    private void selectDatabase() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("DATABASE PREFERENCE: ", sharedPreferences.getString(LIST_PREFERENCE_DATABASE, "1"));
        if (sharedPreferences.getString(LIST_PREFERENCE_DATABASE, "1").equals("1"))
            selected_database = ROOM_DATABASE;
        else selected_database = SQLITE_HELPER_DATABASE;
    }

    private void removeItemOfList(int pos) {
        quotationsArrayAdapter.remove(quotationsArrayAdapter.getItem(pos));
        // No hace falta notificar que los datos cambian porque
        // el padre del adapter (ArrayAdapter) ya lo hace al hacer remove()
    }

    private void aboutAuthorClickListener(TextView textViewAuthor) {
        Intent aboutAuthorIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.search_uri) + textViewAuthor.getText()));
        if (aboutAuthorIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(aboutAuthorIntent);
        }
    }

    private ArrayList<Quotation> getQuotations() {

        return selected_database == SQLITE_HELPER_DATABASE ?
                SQLiteOpenHelperSeminarDatabase.getInstance(this).getQuotations(this)
                : (ArrayList<Quotation>) QuotationRoomDatabase.getInstance(this).quotationDao().getQuotations();
      /*  ArrayList<Quotation> exampleData = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) exampleData.add(new Quotation("quoteExample" + i, "Albert Einstein"));
            else exampleData.add(new Quotation("quoteExample" + i, "Nikola Tesla"));
        }
        return exampleData;*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favourite_activity, menu);
        if (this.quotationsArrayAdapter.isEmpty()) {
            MenuItem itemClearAll = menu.findItem(R.id.menu_item_clearall);
            itemClearAll.setVisible(CLEAR_ALL_QUOTATIONS_OPTION_VISIBLE);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_clearall:
                AlertDialog.Builder alertClearAll = new AlertDialog.Builder(FavouriteActivity.this);
                alertClearAll.setTitle(R.string.remove_alert_title);
                alertClearAll.setMessage(R.string.clear_all_confirmation);
                alertClearAll.setPositiveButton(R.string.remove_alert_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        quotationsArrayAdapter.clear();

                        // No hace falta notificar que los datos cambian porque
                        // el padre del adapter (ArrayAdapter) ya lo hace al hacer clear()
                        deleteAllQuotations();
                        menuItem.setVisible(CLEAR_ALL_QUOTATIONS_OPTION_VISIBLE);
                    }
                });
                alertClearAll.setNegativeButton(R.string.remove_alert_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertClearAll.create().show();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void deleteAllQuotations() {
        if (selected_database == SQLITE_HELPER_DATABASE) {
            Log.d("SQLiteHelper: ", "delete all quotations using SQL HELPER");
            SQLiteOpenHelperSeminarDatabase.getInstance(this).deleteAllQuotations(this);
        } else {
            Log.d("Room: ", "delete all quotations using ROOM");
            QuotationRoomDatabase.getInstance(this).quotationDao().deleteAllQuotations();
        }
    }

    private void deleteQuotation(int position) {
        Quotation quotation = quotationsArrayAdapter.getItem(position);
        if (selected_database == SQLITE_HELPER_DATABASE) {
            SQLiteOpenHelperSeminarDatabase.getInstance(this).deleteQuotation(this, quotation);
        } else {
            QuotationRoomDatabase.getInstance(this).quotationDao().deleteQuotation(quotation);
        }
    }
}
