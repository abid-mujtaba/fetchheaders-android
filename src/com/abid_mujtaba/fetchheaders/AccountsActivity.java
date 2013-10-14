package com.abid_mujtaba.fetchheaders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
            tv.setId(ii);                               // Store the account_id as the view id

            tv.setOnClickListener(listener);

            accountList.addView(tv);
        }
    }


    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view)
        {
            Bundle bdl = new Bundle();
            bdl.putInt(Resources.BUNDLE_ACCOUNT_ID, view.getId());

            Intent i = new Intent("com.abid_mujtaba.fetchheaders.AccountSettingActivity");
            i.putExtras(bdl);

            startActivity(i);
            finish();
        }
    };
}