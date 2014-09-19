package com.example.mmclient.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mmclient.R;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * Created by Ankit on 14-Sep-14.
 */
public class LoginActivity extends Activity {
    Button select;
    String[] avail_accounts;
    ListView list;
    ArrayAdapter<String> adapter;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_page);
        select = (Button) findViewById(R.id.select_button);

        avail_accounts = getAccountNames();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, avail_accounts);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        String tkn=pref.getString("Access Token", null);
        if (tkn != null) {
            startMainActivity();
        }
        select.setOnClickListener(new View.OnClickListener() {
            Dialog accountDialog;

            @Override
            public void onClick(View arg0) {

                if (avail_accounts.length != 0) {
                    accountDialog = new Dialog(LoginActivity.this);
                    accountDialog.setContentView(R.layout.accounts_dialog);
                    accountDialog.setTitle("Select Google Account");
                    list = (ListView) accountDialog.findViewById(R.id.list);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            SharedPreferences.Editor edit = pref.edit();
                            //Storing Data using SharedPreferences
                            edit.putString("Email", avail_accounts[position]);
                            edit.apply();
                            new Authenticate().execute();
                            accountDialog.cancel();
                        }
                    });
                    accountDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "No accounts found, Add a Account and Continue.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(mainIntent);
    }

    private String[] getAccountNames() {
        AccountManager mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

    private class Authenticate extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        String mEmail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Authenticating....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            mEmail = pref.getString("Email", "");
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String token) {
            pDialog.dismiss();
            if (token != null) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Access Token using SharedPreferences
                edit.putString("Access Token", token).apply();
                Toast.makeText(getApplicationContext(), "Authenticated", Toast.LENGTH_SHORT).show();
                startMainActivity();
            }
        }

        @Override
        protected String doInBackground(String... arg0) {

            String token = null;
            try {
                token = GoogleAuthUtil.getToken(
                        LoginActivity.this,
                        mEmail,
                        "oauth2:https://docs.google.com/feeds https://spreadsheets.google.com/feeds");
            } catch (IOException transientEx) { // Network or server error, try later
                Log.e("IOException", transientEx.toString());
            } catch (UserRecoverableAuthException e) { // Recover (with e.getIntent())
                startActivityForResult(e.getIntent(), 1001);
                Log.e("AuthException", e.toString());
            } catch (GoogleAuthException authEx) {
                Log.e("GoogleAuthException", authEx.toString());
            }
            return token;
        }
    }
}