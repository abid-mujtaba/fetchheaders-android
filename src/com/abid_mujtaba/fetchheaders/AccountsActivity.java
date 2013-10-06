package com.abid_mujtaba.fetchheaders;

import android.app.Activity;
import android.os.Bundle;


/**
 * Activity where you can access and change the settings for the various accounts in the system.
*/

public class AccountsActivity extends Activity {

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.accounts);
    }
}