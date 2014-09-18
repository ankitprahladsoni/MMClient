package com.example.mmclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mmclient.activity.LoginActivity;

/**
 * Activity to manage Splash effect.
 *
 * @author SWAPNIL
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Adding a delay of 1000 milliseconds after which calling MainActivity.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 1000);
    }
}
