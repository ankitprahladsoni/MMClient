package com.example.mmclient.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.example.mmclient.R;

/**
 * Main Activity for the application.
 *
 * @author SWAPNIL
 */
public class MainActivity extends NavDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // use the below code instead of setContentView
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        mDrawerLayout.addView(contentView, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Redirects to enter amount screen.
     *
     * @param view
     */
    public void enterAmount(View view) {
        Log.d("Account","Entered Amount Screen");
        startActivity(new Intent(MainActivity.this, SubmitAmountActivity.class));
    }

    /**
     * Redirects to show summary screen.
     *
     * @param view
     */
    public void showSummary(View view) {
        startActivity(new Intent(MainActivity.this, SummaryActivity.class));
    }

}