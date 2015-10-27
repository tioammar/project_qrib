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

package in.lovelacetech.qrib.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tioammar
 * on 10/27/15.
 */
public class RegisterService extends IntentService {

    public RegisterService(){
        super(RegisterService.class.getCanonicalName());
    }

    public static void initRegister(Context context){

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
