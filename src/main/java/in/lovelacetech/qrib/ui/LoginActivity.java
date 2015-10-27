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

package in.lovelacetech.qrib.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import in.lovelacetech.qrib.R;
import in.lovelacetech.qrib.model.Person;
import in.lovelacetech.qrib.services.RegisterService;
import in.lovelacetech.qrib.utility.SessionUtility;
import io.fabric.sdk.android.Fabric;

/**
 * Created by tioammar
 * on 9/19/15.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private TwitterLoginButton mLoginButton;
    private TwitterSession mTwitterSession;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "7TFG6drIb3Y2av41hatNYHNTZ";
    private static final String TWITTER_SECRET = "LLKfTX61bwk3m1MVDN2yTCDZiEiojHTUeAgXzSnv5z6JP0Qga7";

    private static final String PERSON_LONGITUDE_KEY = "person_longitude";
    private static final String PERSON_LATITUDE_KEY = "person_latitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY,
                TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);

        mTwitterSession = null;

        mLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(LOG_TAG, "Finish auth");
                mTwitterSession = result.data;
                getUserProfile(mTwitterSession);
                mLoginButton.setVisibility(View.GONE);
            }

            @Override
            public void failure(TwitterException e) {
                Log.d(LOG_TAG, "Ups! Auth failed");
            }
        });
    }

    private void getUserProfile(TwitterSession mTwitterSession) {
        Callback<User> callback = new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                Log.d(LOG_TAG, "Authorize..");
                User user = result.data;

                // getting person instance from user data
                Person person = Person.getInstance(user);
                createSession(person);
            }

            @Override
            public void failure(TwitterException e) {
                Log.d(LOG_TAG, e.getMessage());
            }
        };
        Twitter.getApiClient(mTwitterSession).
                getAccountService().verifyCredentials(true, false, callback);
    }

    private void createSession(Person profile) {
        Log.d(LOG_TAG, "Creating session");
        if (profile != null) {
            if (SessionUtility.saveUserData(this, profile)) {
                Log.d(LOG_TAG, "Session created");

                // registering profile on server
                RegisterService.initRegister(this);

                // preparing the preference
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor spe = sp.edit();

                // saving user location on default preferences
                spe.putLong(PERSON_LONGITUDE_KEY, 0);
                spe.putLong(PERSON_LATITUDE_KEY, 0);
                if (spe.commit()) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            }
        } else throw new RuntimeException();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
