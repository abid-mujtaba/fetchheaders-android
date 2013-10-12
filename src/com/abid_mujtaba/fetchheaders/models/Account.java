package com.abid_mujtaba.fetchheaders.models;

import android.util.Log;

import com.abid_mujtaba.fetchheaders.Resources;
import com.abid_mujtaba.fetchheaders.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Model representing an email account. It encapsulates the various methods which connect to an email account and interact with it
 */

public class Account
{
    private String mName;
    private String mHost;
    private String mUsername;
    private String mPassword;

    private int mId;

    private static ArrayList<Account> sInstances = new ArrayList<Account>();
    private static int sNumOfInstances = 0;


    private Account()        // Default empty constructor used to track objects
    {
        mId = sNumOfInstances;

        sInstances.add(mId, this);      // Add instance to the list and increment number of instances

        sNumOfInstances++;
    }

    public Account(String _name, String _host, String _username, String _password)
    {
        this();

        mName = _name;
        mHost = _host;
        mUsername = _username;
        mPassword = _password;
    }

    public String name() { return mName; }
    public String host() { return mHost; }
    public String username() { return mUsername; }
    public String password() { return mPassword; }

    public Account get(int id)
    {
        return sInstances.get(id);
    }

    @Override
    public String toString()
    {
        return String.format("<Account - name: %s - host: %s - username: %s - password: %s>", mName, mHost, mUsername, mPassword);
    }


    public static void createAccountsFromJson()     // Reads the specified json file and uses it to construct Account objects
    {
        try
        {
            FileInputStream fis = new FileInputStream(new File(Resources.INTERNAL_FOLDER, Settings.ACCOUNTS_JSON_FILE));

            String content = Resources.getStringFromInputStream(fis);
            fis.close();

            JSONObject root = new JSONObject(content);
            JSONArray accounts = root.getJSONArray("accounts");

            JSONObject account = accounts.getJSONObject(0);         // Pull the first item in the array

            new Account(account.getString("name"), account.getString("host"), account.getString("username"), account.getString("password"));
        }
        catch (FileNotFoundException e) { Log.e(Resources.LOGTAG, "Unable to open " + Settings.ACCOUNTS_JSON_FILE, e); }
        catch (IOException e) { Log.e(Resources.LOGTAG, "Unable to close FileInputStream for " + Settings.ACCOUNTS_JSON_FILE, e); }
        catch (JSONException e) { Log.e(Resources.LOGTAG, "Exception thrown while working with JSON", e); }
    }
}
