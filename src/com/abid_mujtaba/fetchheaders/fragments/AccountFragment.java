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
import com.abid_mujtaba.fetchheaders.models.Account;


public class AccountFragment extends Fragment
{
    private Account mAccount;

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

        return v;
    }
}
