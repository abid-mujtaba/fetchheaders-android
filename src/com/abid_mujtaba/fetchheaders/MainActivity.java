package com.abid_mujtaba.fetchheaders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abid_mujtaba.fetchheaders.fragments.AccountFragment;
import com.abid_mujtaba.fetchheaders.interfaces.ToggleMenu;
import com.abid_mujtaba.fetchheaders.misc.ThreadPool;
import com.abid_mujtaba.fetchheaders.models.Account;
import com.abid_mujtaba.fetchheaders.models.Email;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends ActionBarActivity implements ToggleMenu, TextToSpeech.OnInitListener
{
    private LinearLayout scrollList;
    private Menu mMenu;                 // A handle to the Menu item

    private ArrayList<AccountFragment> mFragments = new ArrayList<AccountFragment>();      // Stores all fragments added to this activity

    private Handler mHandler = new Handler();           // Handler used to carry out UI actions from background threads

    private boolean fShowSeen = false;                  // Flag which control whether seen emails should be displayed or not
    private boolean fDisableMenu = false;               // A flag that indicates whether the menu should be disabled or not. We want the menu disabled when we are performing certain tasks such as fetching emails

    private String BUNDLE_FLAG_SHOW_SEEN = "BUNDLE_FLAG_SHOW_SEEN";     // Used as a key for the showSeen flag stored in the Bundle that saves state information when the activity is restarted (possibly because of screen rotation)

    private TextToSpeech mTTS;


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

        if (mTTS == null)       // If onCreate is called multiple times we do NOT want to create multiple TextToSpeech objects
        {
            mTTS = new TextToSpeech(this, this);
        }

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
    protected void onDestroy()
    {
        if (mTTS != null)           // mTTS needs to be properly shutdown otherwise the app will complain about a leaked service
        {
            mTTS.stop();
            mTTS.shutdown();
        }

        super.onDestroy();
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

            case R.id.menu_speak:

                speak_emails();

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


    private void speak_emails()
    {
        for (int ii = 0; ii < Account.numberOfAccounts(); ii++)
        {
            Account account = Account.get(ii);
            SparseArray<Email> emails = account.emails();

            int num_emails = emails.size();

            if (account.num_unseen_emails() > 0)
            {
                speak(account);         // Speak Account name

                for (int jj = 0; jj < num_emails; jj++)
                {
                    Email email = emails.get(jj);

                    if (! email.seen())
                    {
                        speak(email);
                    }
                }
            }
        }
    }


    @Override
    public void onInit(int status)
    {
        if (status == TextToSpeech.SUCCESS)
        {
            int result = mTTS.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Resources.Loge("This Language is not supported. result: " + result, null);
                Resources.Loge("If result is -1 simply install 'Speech Synthesis Data Installer' from the Google Play Store.", null);
            }
            else { return; }
        }

        mTTS.stop();
        mTTS.shutdown();
        mTTS = null;

        Resources.Loge("Initialization failed", null);
    }


    private void speak(Email email)         // Method for applying TextToSpeech to an email
    {
        if (mTTS != null)           // mTTS is set to null if the initialization fails
        {
            // NOTE: Adding periods inside the string introduces delays in the Speech synthesized from the text
            String msg = String.format("From %s. %s.", email.from(), email.subject());

            mTTS.speak(msg, TextToSpeech.QUEUE_ADD, null);
        }
    }


    private void speak(Account account)         // Method for applying TextToSpeech to an account
    {
        if (mTTS != null)
        {
            String msg = String.format(".Account %s.", account.name());

            mTTS.speak(msg, TextToSpeech.QUEUE_ADD, null);
        }
    }
}
