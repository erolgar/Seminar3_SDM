package com.example.admin.seminar3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QuotationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        String nameLess = "Nameless One";
        TextView textView = findViewById(R.id.quotation_activity_tv_refreshview);
        textView.setText(String.format(getResources().getString(R.string.text_view_refresh_info), nameLess));
    }

    public void refreshQuotation(View view) {
        TextView textViewQuotation = findViewById(R.id.quotation_activity_tv_refreshview);
        textViewQuotation.setText(getResources().getString(R.string.text_view_sample_quotation));
        TextView texViewAuthor = findViewById(R.id.quotation_activity_empty_tv);
        texViewAuthor.setText(getResources().getString(R.string.text_view_sample_author));
    }
}
