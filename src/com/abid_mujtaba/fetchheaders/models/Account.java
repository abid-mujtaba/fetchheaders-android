package com.abid_mujtaba.fetchheaders.models;

import android.util.Log;

import com.abid_mujtaba.fetchheaders.Resources;
import com.abid_mujtaba.fetchheaders.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
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

    public static Account get(int id)
    {
        return sInstances.get(id);
    }

    @Override
    public String toString()
    {
        return String.format("<Account - name: %s - host: %s - username: %s - password: %s>", mName, mHost, mUsername, mPassword);
    }


    public void update(String _name, String _host, String _username, String _password)      // Used to update account information
    {
        mName = _name;
        mHost = _host;
        mUsername = _username;
        mPassword = _password;

        writeAccountsToJson();
    }


    public static int numberOfAccounts()
    {
        return sNumOfInstances;
    }


    public static void createAccountsFromJson()     // Reads the specified json file and uses it to construct Account objects
    {
        try
        {
            File file = new File(Resources.INTERNAL_FOLDER, Settings.ACCOUNTS_JSON_FILE);

            if (!file.exists())
            {
                createEmptyAccountsJsonFile(file);
            }

            FileInputStream fis = new FileInputStream(file);

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


    private static void writeAccountsToJson()       // Reads all Account objects (some of them updated) and uses them to write this data to accounts.json
    {
        try
        {
            JSONObject jRoot = new JSONObject();
            JSONArray jAccounts = new JSONArray();

            for (int ii = 0; ii < sNumOfInstances; ii++)
            {
                JSONObject jAccount = new JSONObject();
                Account account = sInstances.get(ii);

                jAccount.put("name", account.name());
                jAccount.put("host", account.host());
                jAccount.put("username", account.username());
                jAccount.put("password", account.password());

                jAccounts.put(jAccount);
            }

            jRoot.put("accounts", jAccounts);

            // Save JSON to accounts.json
            FileWriter fw = new FileWriter(new File(Resources.INTERNAL_FOLDER, Settings.ACCOUNTS_JSON_FILE));    // Write root JSON object to file info.json
            fw.write(jRoot.toString());
            fw.flush();
            fw.close();
        }
        catch (JSONException e) { Log.e(Resources.LOGTAG, "Exception raised while manipulate JSON objects.", e); }
        catch (IOException e) { Log.e(Resources.LOGTAG, "Exception raised while saving content to json file.", e); }
    }


    private static void createEmptyAccountsJsonFile(File file)
    {
        try
        {
            // We start by creating the JSON tree for an empty accounts.json file.
            JSONObject root = new JSONObject();
            JSONArray accounts = new JSONArray();

            root.put("accounts", accounts);

            // Save JSON to accounts.json
            FileWriter fw = new FileWriter(file);    // Write root JSON object to file info.json
            fw.write(root.toString());
            fw.flush();
            fw.close();
        }
        catch (JSONException e) { Log.e(Resources.LOGTAG, "Exception raised while manipulate JSON objects.", e); }
        catch (IOException e) { Log.e(Resources.LOGTAG, "Exception raised while saving content to json file.", e); }
    }
}
