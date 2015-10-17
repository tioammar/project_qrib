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
import android.location.Location;
import android.preference.PreferenceManager;

import in.lovelacetech.qrib.model.LocationHelper;

/**
 * Created by tioammar
 * on 10/17/15.
 */
public class LocationUtility  {

    private static final String PERSON_LONGITUDE_KEY = "person_longitude";
    private static final String PERSON_LATITUDE_KEY = "person_latitude";

    /**
     * helper method to save user location
     */
    public static boolean saveUserLocation(Context context, Location location) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spe = sp.edit();

        final double longitude = location.getLongitude();
        final double latitude = location.getLatitude();

        // saving user location on default preferences
        spe.putLong(PERSON_LONGITUDE_KEY, Double.doubleToRawLongBits(longitude));
        spe.putLong(PERSON_LATITUDE_KEY, Double.doubleToRawLongBits(latitude));
        return spe.commit();
    }

    public static LocationHelper getLastLocation(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final double longitude = Double.longBitsToDouble(sp
                .getLong(PERSON_LONGITUDE_KEY, 0));
        final double latitude = Double.longBitsToDouble(sp
                .getLong(PERSON_LATITUDE_KEY, 0));

        if (longitude == 0 && latitude == 0) return null;

        // return custom object with location
        return LocationHelper.getInstance(longitude, latitude);
    }
}
