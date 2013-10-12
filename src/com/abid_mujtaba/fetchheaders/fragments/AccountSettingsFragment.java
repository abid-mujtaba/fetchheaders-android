package com.abid_mujtaba.fetchheaders.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abid_mujtaba.fetchheaders.R;
import com.abid_mujtaba.fetchheaders.models.Account;

/**
 * This fragment is used to specify the settings associated with a given account
 */

public class AccountSettingsFragment extends Fragment
{
    private EditText edtName, edtHost, edtUsername, edtPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.account_setting, container, false);

        edtName = (EditText) v.findViewById(R.id.account_name);
        edtHost = (EditText) v.findViewById(R.id.account_host);
        edtUsername = (EditText) v.findViewById(R.id.account_username);
        edtPassword = (EditText) v.findViewById(R.id.account_password);

        initiateButtons(v);

        // We read the account information from accounts.json and use it to populate the EditText fields
        Account account = Account.get(0);

        edtName.setText(account.name());
        edtHost.setText(account.host());
        edtUsername.setText(account.username());
        edtPassword.setText(account.password());

        return v;
    }


    private void initiateButtons(View v)        // Initiates the buttons in the fragment and attaches click-listeners to it
    {
        Button btnSave = (Button) v.findViewById(R.id.btnSave);
        Button btnCancel = (Button) v.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                startActivity(new Intent("com.abid_mujtaba.fetchheaders.MainActivity"));

                getActivity().finish();     // Leave the AccountsActivity and return to the Main Screen
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(), "Save pressed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
