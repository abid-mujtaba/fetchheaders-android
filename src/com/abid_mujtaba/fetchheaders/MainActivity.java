package com.abid_mujtaba.fetchheaders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abid_mujtaba.fetchheaders.fragments.AccountFragment;
import com.abid_mujtaba.fetchheaders.misc.ThreadPool;
import com.abid_mujtaba.fetchheaders.models.Account;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity
{
    private LinearLayout scrollList;

    private ArrayList<AccountFragment> mFragments = new ArrayList<AccountFragment>();      // Stores all fragments added to this activity

    private Handler mHandler = new Handler();           // Handler used to carry out UI actions from background threads


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

            FragmentManager fM = getSupportFragmentManager();
            FragmentTransaction fT = fM.beginTransaction();

            for (int ii = 0; ii < Account.numberOfAccounts(); ii++)
            {
                String tag = "TAG_" + ii;           // This is the tag we will use to get a handle on the fragment in the FragmentManager

                AccountFragment aF = (AccountFragment) fM.findFragmentByTag(tag);           // We attempt to access the fragment via the specified tag

                if (aF == null)         // This indicates that the Fragment does not exist yet so we create it. It has setRetainInstance(true) so it persists across configuration changes.
                {
                    aF = AccountFragment.newInstance(ii);

                    fT.add(R.id.scrollList, aF, tag);       // Note: The addition to the scrollList only happens when aF == null, which happens when the persistent fragment has not been created yet
                }                                           //       Since Views retain state across config changes the scrollList remembers that it has fragments added to it

                mFragments.add(aF);
            }

            fT.commit();
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
            case R.id.menu_refresh:

                refresh();
                return true;

            case R.id.menu_delete:

                remove_deleted_emails();
                return true;

            case R.id.menu_accounts:

                startActivity(new Intent("com.abid_mujtaba.fetchheaders.AccountsActivity"));
                finish();

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }


    public void refresh()           // Reloads the activity. Emails marked for deletion are NOT deleted.
    {
        Intent i = getIntent();     // We restart the MainActivity with the same intent it was started with. This causes emails to be fetched again. Emails deleted while issuing fragment.remove_emails_marked_for_deletion() will not appear.
        finish();
        startActivity(i);
    }


    public void remove_deleted_emails()     // Method for removing emails marked for deletion from the UI
    {
        for (final AccountFragment fragment: mFragments)
        {
            Runnable delete = new Runnable() {

                @Override
                public void run()
                {
                    fragment.remove_emails_marked_for_deletion(mHandler);
                }
            };

            ThreadPool.executeTask(delete);
        }
    }
}
