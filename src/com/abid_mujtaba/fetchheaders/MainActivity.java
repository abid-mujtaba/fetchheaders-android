package com.abid_mujtaba.fetchheaders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abid_mujtaba.fetchheaders.fragments.AccountFragment;
import com.abid_mujtaba.fetchheaders.interfaces.ToggleMenu;
import com.abid_mujtaba.fetchheaders.misc.ThreadPool;
import com.abid_mujtaba.fetchheaders.models.Account;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements ToggleMenu
{
    private LinearLayout scrollList;
    private Menu mMenu;                 // A handle to the Menu item

    private ArrayList<AccountFragment> mFragments = new ArrayList<AccountFragment>();      // Stores all fragments added to this activity

    private Handler mHandler = new Handler();           // Handler used to carry out UI actions from background threads

    private boolean fShowSeen = false;                  // Flag which control whether seen emails should be displayed or not
    private boolean fDisableMenu = false;               // A flag that indicates whether the menu should be disabled or not. We want the menu disabled when we are performing certain tasks such as fetching emails

    private String BUNDLE_FLAG_SHOW_SEEN = "BUNDLE_FLAG_SHOW_SEEN";     // Used as a key for the showSeen flag stored in the Bundle that saves state information when the activity is restarted (possibly because of screen rotation)


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        if (savedInstanceState != null)     // If the passed in state information bundle is non-empty we expect it to contain the saved value of fShowSeen. We also pass in a default value.
        {
            fShowSeen = savedInstanceState.getBoolean(BUNDLE_FLAG_SHOW_SEEN, false);
        }

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

        mMenu = menu;       // Store a handle to the Menu item.

        if (fShowSeen) { mMenu.findItem(R.id.menu_show_seen).setTitle("Hide Seen"); }       // Set Menu Item Title based on fShowSeen.
        else { mMenu.findItem(R.id.menu_show_seen).setTitle("Show Seen"); }                 // Since this method is called every time the activity is recreated (including when the screen is rotated we check fShowSeen and then set the menu item title

        return true;
    }


//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu)
//    {
//        super.onPrepareOptionsMenu(menu);           // We make sure to carry out the actions we are overriding to carry out the requisite functionality
//
//        if (fDisableMenu) { Toast.makeText(this, "Waiting ...", Toast.LENGTH_SHORT).show(); }       // The menu is disabled and we apprise the user of this.
//
//        return ! fDisableMenu;              // Basically decide whether pressing the menu actually shows any items based on the fDisableMenu flag.
//    }


    private int toggleMenuCount = 0;        // This integer is used to count the number of fragments who have asked to disable the menu. When this count is zero the menu is enabled.

    public void enableMenu()
    {
        if (--toggleMenuCount == 0)         // We predecrement the count of requests to disable the menu. If the count falls to zero we change the flag.
        {
            fDisableMenu = false;
        }
    }


    public void disableMenu()
    {
        fDisableMenu = true;        // Set the Disable Menu flag

        toggleMenuCount++;          // Increment the count to show that one more entity has asked for the menu to be disabled
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

                return true;

            case R.id.menu_show_seen:

                fShowSeen = ! fShowSeen;

                if (fShowSeen) { mMenu.findItem(R.id.menu_show_seen).setTitle("Hide Seen"); }       // Toggle Menu Item Title
                else { mMenu.findItem(R.id.menu_show_seen).setTitle("Show Seen"); }

                for (AccountFragment fragment: mFragments)
                {
                    fragment.showSeen(fShowSeen);
                }

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putBoolean(BUNDLE_FLAG_SHOW_SEEN, fShowSeen);
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
