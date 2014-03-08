/*
 *  Copyright 2014 Abid Hasan Mujtaba
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.abid_mujtaba.fetchheaders;

/**
 * We explicitly define the application class for this app. This allows us to carry certain startup tasks.
 */


import android.app.Application;

import com.abid_mujtaba.fetchheaders.models.Account;


public class FetchheadersApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Resources.INTERNAL_FOLDER = getFilesDir().getAbsolutePath();        // We set the absolute path to the internal app folder at startup which we will use elsewhere

        // Set up Account objects based on the accounts.json file
        Account.createAccountsFromJson();
    }
}
