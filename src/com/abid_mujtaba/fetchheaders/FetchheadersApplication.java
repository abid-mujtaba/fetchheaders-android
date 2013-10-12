package com.abid_mujtaba.fetchheaders;

import android.app.Application;

/**
 * We explicitly define the application class for this app. This allows us to carry certain startup tasks.
 */

public class FetchheadersApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Resources.INTERNAL_FOLDER = getFilesDir().getAbsolutePath();        // We set the absolute path to the internal app folder at startup which we will use elsewhere
    }
}
