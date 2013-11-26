package com.abid_mujtaba.fetchheaders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abid_mujtaba.fetchheaders.models.Account;

/**
 * Activity where you can access and change the settings for the various accounts in the system.
*/


public class AccountsActivity extends ActionBarActivity {

    private View mLastViewTouched;


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
            tv.setOnCreateContextMenuListener(onCreateContextMenuListener);
            tv.setOnTouchListener(onTouchListener);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.accounts_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_add_account:

                startActivity(new Intent("com.abid_mujtaba.fetchheaders.AccountSettingActivity"));
                finish();

                return true;

            default:    return super.onOptionsItemSelected(item);
        }
    }


    private View.OnCreateContextMenuListener onCreateContextMenuListener = new View.OnCreateContextMenuListener() {

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
        {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.account_context_menu, contextMenu);

            contextMenu.setHeaderTitle("Options");
        }
    };


    @Override
    public boolean onContextItemSelected(MenuItem item)         // ALL context menu item select events are reported here (for all Views and the Activity with a onCreateContextMenuListener
    {
        switch (item.getItemId())
        {
            case R.id.menu_delete_account:

                AlertDialog dialog = createAlertDialog();
                dialog.show();

                return true;

            default:    return super.onContextItemSelected(item);
        }
    }


    private AlertDialog createAlertDialog()         // Creates the Alert Dialog used to confirm Account deletion
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete Account?");
        builder.setMessage("Account details will be removed from the phone.");
        builder.setPositiveButton("Yes", onConfirmedDeletion);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}      // Empty method indicates cancellation with no other tasks
        });

        return builder.create();
    }


    private DialogInterface.OnClickListener onConfirmedDeletion = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i)     // Deletion has been confirmed. Carry out the necessary actions.
        {
            Account.deleteAccount( mLastViewTouched.getId() );      // Get the Account id from the View that launched the ContextMenu. Then delete it.

            Intent intent = getIntent();         // Now that the account has been deleted we restart this activity to update its contents
            startActivity(intent);
            finish();
        }
    };


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent)
        {
            mLastViewTouched = view;        // Store a handle on the last view touched. This will be used to identify the view on which the Context Menu was launched

            return false;       // We return false since this indicates that the touch was not handled and so it is passed down the stack to be handled appropriately
        }
    };


    @Override
    public void onBackPressed()         // Override the behaviour of the back button
    {
        startActivity(new Intent("com.abid_mujtaba.fetchheaders.MainActivity"));
        finish();
    }
}