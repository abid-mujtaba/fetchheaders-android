/*
 *  Copyright 2013 Abid Hasan Mujtaba
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.abid_mujtaba.fetchheaders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.accounts_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_add_account);       // We gain access to the menu item and through it to its action view (the actionLayout definied in the menu layout)
        View itemView = MenuItemCompat.getActionView(item);

        if (itemView != null)
        {
            itemView.setOnClickListener(new View.OnClickListener() {        // To make the menu item work when it has an actionView we must explicitly attach a clicklistener to the actionview otherwise nothing happens when the menu is pressed
                @Override                                                   // Note: With this implementation we do NOT need an onOptionItemSelected method
                public void onClick(View view)
                {
                    startActivity(new Intent("com.abid_mujtaba.fetchheaders.AccountSettingActivity"));
                }
            });
        }

        return true;
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