package com.abid_mujtaba.fetchheaders.fragments;

/**
 * This fragment implements showing the email information for a single account.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.abid_mujtaba.fetchheaders.R;


public class AccountFragment extends Fragment
{
    private String text;

    public static AccountFragment newInstance(String _text)
    {
        AccountFragment af = new AccountFragment();
        af.text = _text;

        return af;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.account_fragment, container, false);     // The false specifies that this view is NOT to be attached to root since we will attach it explicitly

        TextView tvPrototype = (TextView) v.findViewById(R.id.tvPrototype);
        tvPrototype.setText("Account Fragment: " + this.text);

        return v;
    }
}
