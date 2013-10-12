package com.abid_mujtaba.fetchheaders.models;

/**
 * Model representing an email account. It encapsulates the various methods which connect to an email account and interact with it
 */

public class Account
{
    private String mName;
    private String mHost;
    private String mUsername;
    private String mPassword;

    public Account(String _name, String _host, String _username, String _password)
    {
        mName = _name;
        mHost = _host;
        mUsername = _username;
        mPassword = _password;
    }


    public void createAccountsFromJson()     // Reads the specified json file and uses it to construct Account objects
    {

    }
}
