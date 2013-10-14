package com.abid_mujtaba.fetchheaders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abid_mujtaba.fetchheaders.models.Account;

/**
 * Activity for adding or editing the settings of a specified account
 */

public class AccountSettingActivity extends Activity
{
    private Account mAccount;

    private EditText edtName, edtHost, edtUsername, edtPassword;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);

        Bundle bdl = getIntent().getExtras();
        int account_id = bdl.getInt(Resources.BUNDLE_ACCOUNT_ID, -1);

        if (account_id >= 0)            // account_id < 0 means no account id was passed and in and so we are creating a new Account.
        {
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
                startActivity(new Intent("com.abid_mujtaba.fetchheaders.MainActivity"));

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

                // Update correspondong Account object which in turn updates the accounts.json file
                mAccount.update(name, host, username, password);

                // Return to MainActivity
                startActivity(new Intent("com.abid_mujtaba.fetchheaders.MainActivity"));
                finish();
            }
        });
    }
}