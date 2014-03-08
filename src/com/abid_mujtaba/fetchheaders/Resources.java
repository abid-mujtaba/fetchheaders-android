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
 * A central location for shared resources in the application. Mostly methods that are called from a number of locations
 * but don't fit in any one location.
 */


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Resources
{
    public static String INTERNAL_FOLDER = "";

    public static final String LOGTAG = "fetchheaders";

    public static final String BUNDLE_ACCOUNT_ID = "account_id";       // Used on both ends of a bundle (both for putting and extracting data)


    public static String getStringFromInputStream(InputStream is)       // Based on http://www.mkyong.com/java/how-to-convert-inputstream-to-string-in-java/
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try
        {
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch (IOException e)
        {
            Log.e(Resources.LOGTAG, "BufferedReader threw exception.", e);
        }
        finally
        {
            try { br.close(); }
            catch (IOException e) { Log.e(Resources.LOGTAG, "BufferedReader.close() failed.", e); }
        }

        return sb.toString();
    }


    public static void Loge(String message, Exception e)
    {
        Log.e(LOGTAG, message, e);
    }

    public static void Logd(String message)
    {
        Log.d(LOGTAG, message);
    }
}
