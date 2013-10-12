package com.abid_mujtaba.fetchheaders;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.abid_mujtaba.fetchheaders.fragments.AccountSettingsFragment;
import com.abid_mujtaba.fetchheaders.models.Account;


/**
 * Activity where you can access and change the settings for the various accounts in the system.
*/

public class AccountsActivity extends FragmentActivity {

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.accounts);

        // Set up Account objects based on the accounts.json file
        Account.createAccountsFromJson();

        // Create and add AccountSettingsFragment to this activity.
        AccountSettingsFragment fragment = new AccountSettingsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.account_settings_frame, fragment);
        transaction.commit();
    }
}