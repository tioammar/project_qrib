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

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import in.lovelacetech.qrib.provider.Contract;

/**
 * Created by tioammar
 * on 10/17/15.
 */
public class MarkItem implements Serializable {

    @SerializedName("mark_id")
    public int id;

    @SerializedName("mark_content")
    public String content;

    @SerializedName("mark_time")
    public String time;

    @SerializedName("mark_photo")
    public String photo;

    @SerializedName("mark_category")
    public String category;

    @SerializedName("mark_longitude")
    public double longitude;

    @SerializedName("mark_latitude")
    public double latitude;

    @SerializedName("person_profile")
    public Person profile;

    @SerializedName("mark_user")
    public long user;

    /**
     * getting mark instance from database
     */
    public static MarkItem getInstance(Cursor c){
        MarkItem mark = new MarkItem();

        mark.id = c.getInt(COLUMN_MARK_ID);
        mark.content = c.getString(COLUMN_MARK_CONTENT);
        mark.category = c.getString(COLUMN_MARK_CATEGORY);
        mark.longitude = c.getDouble(COLUMN_LONGITUDE);
        mark.latitude = c.getDouble(COLUMN_LATITUDE);

        return mark;
    }

    /**
     * database column projection
     * must corresponds with column index
     */
    public static final String[] COLUMNS = {
            Contract.MarkColumn.COLUMN_ID,
            Contract.MarkColumn.COLUMN_CONTENT,
            Contract.MarkColumn.COLUMN_CATEGORY,
            Contract.MarkColumn.COLUMN_PHOTO,
            Contract.MarkColumn.COLUMN_TIME,
            Contract.PersonColumn.COLUMN_FULL_NAME,
            Contract.PersonColumn.COLUMN_AVATAR,
            Contract.PersonColumn.COLUMN_ID,
            Contract.MarkColumn.COLUMN_LONGITUDE,
            Contract.MarkColumn.COLUMN_LATITUDE
    };

    public static final int COLUMN_MARK_ID = 0;
    public static final int COLUMN_MARK_CONTENT = 1;
    public static final int COLUMN_MARK_CATEGORY = 2;
    public static final int COLUMN_MARK_PHOTO = 3;
    public static final int COLUMN_MARK_TIME = 4;
    public static final int COLUMN_PERSON_FULL_NAME = 5;
    public static final int COLUMN_PERSON_AVATAR = 6;
    public static final int COLUMN_PERSON_ID = 7;
    public static final int COLUMN_LONGITUDE = 8;
    public static final int COLUMN_LATITUDE = 9;
}
