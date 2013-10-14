package com.abid_mujtaba.fetchheaders;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.abid_mujtaba.fetchheaders.models.Account;

/**
 * Activity where you can access and change the settings for the various accounts in the system.
*/


public class AccountsActivity extends Activity {

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts);

        LinearLayout accountList = (LinearLayout) findViewById(R.id.account_list);

        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int ii = 0; ii < Account.numberOfAccounts(); ii++)
        {
            TextView tv = (TextView) li.inflate(R.layout.account_setting_name, null);
            tv.setText(Account.get(ii).name());
            tv.setId(ii);

            accountList.addView(tv);
        }
    }
}