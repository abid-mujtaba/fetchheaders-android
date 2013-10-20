package com.abid_mujtaba.fetchheaders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abid_mujtaba.fetchheaders.fragments.AccountFragment;
import com.abid_mujtaba.fetchheaders.misc.Counter;
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
        TextView title = (TextView) findViewById(R.id.main_title);

//        title.setOnClickListener(listener);

        if (Account.numberOfAccounts() > 0)             // Accounts have been specified
        {
            TextView tvEmpty = (TextView) findViewById(R.id.txtNoAccounts);     // We start by removing the No Accounts view since accounts are present
            scrollList.removeView(tvEmpty);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            for (int ii = 0; ii < Account.numberOfAccounts(); ii++)
            {
                AccountFragment aF = AccountFragment.newInstance(ii);       // We create a new account fragment and specify the number of the Account associated with it
                ft.add(R.id.scrollList, aF);

                mFragments.add(aF);
            }

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
            case R.id.menu_refresh:

                refresh_fragments();
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


    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) { refresh_fragments(); }
    };


    public void refresh_fragments()
    {
        final Counter counter = new Counter(mFragments.size());         // Declare a counter to count down the fragments refreshing

        for (final AccountFragment fragment: mFragments)
        {
            Runnable refresh = new Runnable() {

                @Override
                public void run()
                {
                    fragment.remove_emails_marked_for_deletion(mHandler);       // We pass in a Handler to carry out UI actions on the background thread
                    counter.decrement();        // The counter is decremented

                    if (counter.value() == 0)           // If all fragments have been refreshed. The last fragment to refresh will cause the Activity to change
                    {
                        Intent i = getIntent();     // We restart the MainActivity with the same intent it was started with. This causes emails to be fetched again. Emails deleted while issuing fragment.remove_emails_marked_for_deletion() will not appear.
                        finish();
                        startActivity(i);
                    }
                }
            };

            ThreadPool.executeTask(refresh);
        }
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
