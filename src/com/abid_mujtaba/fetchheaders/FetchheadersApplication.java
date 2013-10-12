package com.abid_mujtaba.fetchheaders;

/**
 * We explicitly define the application class for this app. This allows us to carry certain startup tasks.
 */


import android.app.Application;
import com.abid_mujtaba.fetchheaders.models.Account;


public class FetchheadersApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Resources.INTERNAL_FOLDER = getFilesDir().getAbsolutePath();        // We set the absolute path to the internal app folder at startup which we will use elsewhere

        // Set up Account objects based on the accounts.json file
        Account.createAccountsFromJson();
    }
}
