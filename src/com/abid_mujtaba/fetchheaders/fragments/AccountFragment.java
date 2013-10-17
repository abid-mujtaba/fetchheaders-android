package com.abid_mujtaba.fetchheaders.fragments;

/**
 * This fragment implements showing the email information for a single account.
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import com.abid_mujtaba.fetchheaders.R;
import com.abid_mujtaba.fetchheaders.misc.ThreadPool;
import com.abid_mujtaba.fetchheaders.models.Account;
import com.abid_mujtaba.fetchheaders.models.Email;
import com.abid_mujtaba.fetchheaders.views.EmailView;

import java.util.ArrayList;


public class AccountFragment extends Fragment
{
    private Account mAccount;
    private ArrayList<Email> mEmails;

    public static AccountFragment newInstance(int account_id)
    {
        AccountFragment af = new AccountFragment();

        af.mAccount = Account.get(account_id);          // Associate account with Fragment using account_id

        return af;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.account_fragment, container, false);     // The false specifies that this view is NOT to be attached to root since we will attach it explicitly

        final TextView tvAccountName = (TextView) v.findViewById(R.id.tvAccountName);
        final LinearLayout emailList = (LinearLayout) v.findViewById(R.id.emailList);      // The root layout of the fragment. We shall add views to this.

        tvAccountName.setText(mAccount.name());

        LayoutInflater li = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View pb = li.inflate(R.layout.progress_view, null);
        emailList.addView(pb);

        final Handler handler = new Handler();      // Create a handler to give access to the UI Thread in this fragment

        Runnable fetchEmails = new Runnable() {

            @Override
            public void run()
            {
                mEmails = mAccount.fetchEmails(true);       // Passing "true" here means only unseen emails will be returned

                if (mEmails != null)
                {
                    if (mEmails.size() > 0)
                    {
                        handler.post(new Runnable() {           // We define tasks that need to be carried out on the frontend UI thread. Most UI tasks.

                            @Override
                            public void run()
                            {
                                emailList.removeView(pb);

                                for (int ii = 0; ii < mEmails.size(); ii++)
                                {
                                    Email email = mEmails.get(ii);

                                    EmailView ev = new EmailView(AccountFragment.this.getActivity(), null);
                                    ev.setInfo(email.date(), email.from(), email.subject());
                                    ev.setId(ii);
                                    ev.setOnClickListener(listener);

                                    emailList.addView(ev);
                                }
                            }
                        });
                    }
                    else        // No emails extracted so we remove the account from the list displayed
                    {
                        handler.post(new Runnable() {

                            @Override
                            public void run()
                            {
                                ((LinearLayout) v).removeView(tvAccountName);       // Remove account title

                                ViewGroup.LayoutParams params = v.getLayoutParams();     // Collapse height of fragment layout to 0
                                params.height = 0;
                                v.setLayoutParams(params);
                            }
                        });
                    }
                }
                else
                {
                    handler.post(new Runnable() {

                        @Override
                        public void run()
                        {
                            emailList.removeView(pb);

                            // TODO: Create a custom layout for Error TextViews in general to be used in such cases. Possibly add a triangular icon indicating an error.

                            TextView tv = new TextView(getActivity());
                            tv.setText("Authentication Failure. Verify credentials.");

                            emailList.addView(tv);
                        }
                    });
                }
            }
        };

        ThreadPool.executeTask(fetchEmails);        // Execute this runnable on a background thread, part of a pool

        return v;
    }


    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view)
        {
            Email email = mEmails.get(view.getId());
            EmailView ev = (EmailView) view;

            email.toggleDeletion();

            if (email.isToBeDeleted())
            {
                ev.strikethrough();
            }
            else
            {
                ev.removeStrikethrough();
            }
        }
    };


    public void refresh()           // Called by parent activity to force the fragment to refresh its contents. This will cause emails set for deletions to be deleted.
    {
        // We iterate over the emails weeding out the emails marked for deletion
        ArrayList<Email> emails = new ArrayList<Email>();

        for (Email email: mEmails)
        {
            if (email.isToBeDeleted()) { emails.add(email); }
        }

        mAccount.delete(emails);
    }
}
