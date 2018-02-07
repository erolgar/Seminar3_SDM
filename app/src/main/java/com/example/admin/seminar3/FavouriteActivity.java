package com.example.admin.seminar3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
    }

    public void aboutAuthorClickListener(View view) {
        String aboutAuthor = "Albert Einstein";
        Intent aboutAuthorIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.search_uri)
                + aboutAuthor));
        if(aboutAuthorIntent.resolveActivity(getPackageManager()) != null){
            startActivity(aboutAuthorIntent);
        }
    }
}
