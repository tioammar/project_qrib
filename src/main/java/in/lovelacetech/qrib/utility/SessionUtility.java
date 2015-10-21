/*
 * Copyright 2015 Aditya Amirullah. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.lovelacetech.qrib.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import in.lovelacetech.qrib.model.Person;

/**
 * Created by tioammar
 * on 10/17/15.
 */
public class SessionUtility {

    private static final String PERSON_ID_KEY = "person_id";
    private static final String PERSON_NAME_KEY = "person_name";
    private static final String PERSON_USER_KEY = "person_user";
    private static final String PERSON_AVATAR_KEY = "person_avatar";
    private static final String PERSON_LONGITUDE_KEY = "person_longitude";
    private static final String PERSON_LATITUDE_KEY = "person_latitude";
    private static final String PERSON_STATUS_KEY = "person_status";

    /**
     * helper method to save user data in shared preference
     */
    public static boolean saveUserData(Context context, Person person){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sp.edit();

        // saving user data including location etc
        spe.putLong(PERSON_ID_KEY, person.id);
        spe.putString(PERSON_NAME_KEY, person.name);
        spe.putString(PERSON_USER_KEY, person.user);
        spe.putString(PERSON_AVATAR_KEY, person.avatar);

        // this only access on init login so location is set to zero
        spe.putLong(PERSON_LONGITUDE_KEY, 0);
        spe.putLong(PERSON_LATITUDE_KEY, 0);
        spe.putBoolean(PERSON_STATUS_KEY, true);

        return spe.commit();
    }

    /**
     * helper method to get login status
     */
    public static boolean getLoginStatus(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PERSON_STATUS_KEY, false);
    }
}
