package com.example.admin.seminar3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void dashboardProxy(View view){
        Intent dashboardIntent;
        if(view.equals(findViewById(R.id.button_get_quotations))){
            dashboardIntent = new Intent(DashboardActivity.this,QuotationActivity.class);
            startActivity(dashboardIntent);
        }else if (view.equals(findViewById(R.id.button_favourite_quotations))){
            dashboardIntent = new Intent(DashboardActivity.this, FavouriteActivity.class);
            startActivity(dashboardIntent);
        }else if(view.equals(findViewById(R.id.button_settings))){
            dashboardIntent = new Intent(DashboardActivity.this, SettingsActivity.class);
            startActivity(dashboardIntent);
        }else{
            dashboardIntent = new Intent(DashboardActivity.this, AboutActivity.class);
            startActivity(dashboardIntent);
        }
    }
}
