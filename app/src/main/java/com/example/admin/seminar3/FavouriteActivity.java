package com.example.admin.seminar3;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Adapters.FavouriteQuotationsArrayAdapter;
import pojoObjects.Quotation;

public class FavouriteActivity extends AppCompatActivity {

    private static FavouriteQuotationsArrayAdapter quotationsArrayAdapter;
    private static ListView listViewQuotations;

    private static final boolean CLEAR_ALL_QUOTATIONS_OPTION_VISIBLE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        quotationsArrayAdapter = new FavouriteQuotationsArrayAdapter(FavouriteActivity.this, R.layout.quotation_list_row, getMockQuotations());
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

    public ArrayList<Quotation> getMockQuotations() {

        ArrayList<Quotation> exampleData = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) exampleData.add(new Quotation("quoteExample" + i, "Albert Einstein"));
            else exampleData.add(new Quotation("quoteExample" + i, "Nikola Tesla"));
        }
        return exampleData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favourite_activity, menu);
        if (this.quotationsArrayAdapter.isEmpty()) {
            MenuItem itemClearAll = (MenuItem) findViewById(R.id.menu_item_clearall);
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
}
