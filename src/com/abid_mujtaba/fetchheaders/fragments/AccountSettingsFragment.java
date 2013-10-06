package com.abid_mujtaba.fetchheaders.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.abid_mujtaba.fetchheaders.R;

/**
 * This fragment is used to specify the settings associated with a given account
 */

public class AccountSettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.account_setting, container, false);

        return v;
    }
}
