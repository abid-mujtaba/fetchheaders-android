package com.abid_mujtaba.fetchheaders.fragments;

/**
 * This fragment implements showing the email information for a single account.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import com.abid_mujtaba.fetchheaders.R;
import com.abid_mujtaba.fetchheaders.Resources;
import com.abid_mujtaba.fetchheaders.models.Account;
import com.abid_mujtaba.fetchheaders.models.Email;
import com.abid_mujtaba.fetchheaders.views.EmailView;


public class AccountFragment extends Fragment
{
    private Account mAccount;
    private Email[] mEmails;

    public static AccountFragment newInstance(int account_id)
    {
        AccountFragment af = new AccountFragment();

        af.mAccount = Account.get(account_id);          // Associate account with Fragment using account_id

        return af;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.account_fragment, container, false);     // The false specifies that this view is NOT to be attached to root since we will attach it explicitly

        TextView tvAccountName = (TextView) v.findViewById(R.id.tvAccountName);
        LinearLayout emailList = (LinearLayout) v.findViewById(R.id.emailList);      // The root layout of the fragment. We shall add views to this.

        tvAccountName.setText(mAccount.name());

        mEmails = mAccount.fetchEmails();

        for (int ii = 0; ii < mEmails.length; ii++)
        {
            Email email = mEmails[ii];

            EmailView ev = new EmailView(this.getActivity(), null);
            ev.setInfo(email.date(), email.from(), email.subject());
            ev.setId(ii);
            ev.setOnClickListener(listener);

            emailList.addView(ev);
        }

        return v;
    }


    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view)
        {
            Email email = mEmails[view.getId()];
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
        Resources.Logd("Fragment refreshed.");
    }
}
