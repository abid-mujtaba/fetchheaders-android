package com.abid_mujtaba.fetchheaders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abid_mujtaba.fetchheaders.fragments.AccountFragment;
import com.abid_mujtaba.fetchheaders.models.Account;

public class MainActivity extends FragmentActivity
{
    private LinearLayout scrollList;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        scrollList = (LinearLayout) findViewById(R.id.scrollList);

        if (Account.numberOfAccounts() > 0)             // Accounts have been specified
        {
            TextView tvEmpty = (TextView) findViewById(R.id.txtNoAccounts);     // We start by removing the No Accounts view since accounts are present
            scrollList.removeView(tvEmpty);

            AccountFragment aF1 = AccountFragment.newInstance("Hello");
            AccountFragment aF2 = AccountFragment.newInstance("World");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.scrollList, aF1);
            ft.add(R.id.scrollList, aF2);
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_accounts:

                startActivity(new Intent("com.abid_mujtaba.fetchheaders.AccountsActivity"));
                finish();

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }
}
