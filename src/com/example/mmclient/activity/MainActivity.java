package com.example.mmclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.mmclient.R;

/**
 * Main Activity for the application.
 * 
 * @author SWAPNIL
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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