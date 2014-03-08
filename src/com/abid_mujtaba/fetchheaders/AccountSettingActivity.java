/*
 *  Copyright 2014 Abid Hasan Mujtaba
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.abid_mujtaba.fetchheaders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abid_mujtaba.fetchheaders.models.Account;

/**
 * Activity for adding or editing the settings of a specified account
 */

public class AccountSettingActivity extends ActionBarActivity
{
    private Account mAccount;

    private EditText edtName, edtHost, edtUsername, edtPassword;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Bundle bdl = getIntent().getExtras();

        if (bdl != null)
        {
            int account_id = bdl.getInt(Resources.BUNDLE_ACCOUNT_ID);

            mAccount = Account.get(account_id);
        }

        edtName = (EditText) findViewById(R.id.account_name);
        edtHost = (EditText) findViewById(R.id.account_host);
        edtUsername = (EditText) findViewById(R.id.account_username);
        edtPassword = (EditText) findViewById(R.id.account_password);

        initiateButtons();

        if (mAccount != null)           // If mAccount == null it means we are creating a new account
        {
            edtName.setText(mAccount.name());
            edtHost.setText(mAccount.host());
            edtUsername.setText(mAccount.username());
            edtPassword.setText(mAccount.password());

            setTitle( String.format("%s - Settings", mAccount.name()) );        // Change ActionBar title to include Account name
        }
    }


    private void initiateButtons()        // Initiates the buttons in the fragment and attaches click-listeners to it
    {
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                startActivity(new Intent("com.abid_mujtaba.fetchheaders.AccountsActivity"));

                finish();     // Leave the AccountsActivity and return to the Main Screen
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                // Extract information from UI

                String name = edtName.getText().toString();
                String host = edtHost.getText().toString();
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (mAccount == null) { mAccount = Account.newInstance(); }     // This means a new account is being created. The following update command will populate it.

                // Update correspondong Account object which in turn updates the accounts.json file
                mAccount.update(name, host, username, password);

                // Return to MainActivity
                startActivity(new Intent("com.abid_mujtaba.fetchheaders.AccountsActivity"));
                finish();
            }
        });
    }
}