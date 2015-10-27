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

package in.lovelacetech.qrib.model;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

import java.io.Serializable;

/**
 * Created by tioammar
 * on 10/17/15.
 */
public class Person implements Serializable {

    @SerializedName("person_id")
    public long id;

    @SerializedName("person_full_name")
    public String name;

    @SerializedName("person_user_name")
    public String user;

    @SerializedName("person_longitude")
    public double longitude;

    @SerializedName("person_latitude")
    public double latitude;

    @SerializedName("person_avatar")
    public String avatar;

    public static Person getInstance(User user){
        Person person = new Person();
        person.id = user.id;
        person.name = user.name;
        person.user = user.screenName;

        // getting full size avatar
        String avatarNormal = user.profileImageUrl;
        person.avatar = avatarNormal.replace("_normal", "");
        return person;
    }
}
