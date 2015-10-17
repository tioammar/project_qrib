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

package in.lovelacetech.qrib.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tioammar
 * on 9/17/15.
 */
public class Contract {

    public static final String CONTENT_AUTHORITY = "in.lovelacetech.qrib";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MARK = "mark";
    public static final String PATH_PERSON = "person";

    public static final class MarkColumn implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MARK).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MARK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MARK;

        public static final String TABLE_NAME = PATH_MARK;

        public static final String COLUMN_ID = "mark_id";
        public static final String COLUMN_CONTENT = "mark_content";
        public static final String COLUMN_TIME = "mark_time";
        public static final String COLUMN_PHOTO = "mark_photo";
        public static final String COLUMN_CATEGORY = "mark_category";
        public static final String COLUMN_USER = "mark_user";

        public static final String COLUMN_LONGITUDE = "mark_longitude";
        public static final String COLUMN_LATITUDE = "mark_latitude";

        public static Uri buildMarkUri(String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }

    public static final class PersonColumn implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PERSON).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSON;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSON;

        public static final String TABLE_NAME = PATH_PERSON;

        public static final String COLUMN_ID = "person_id";
        public static final String COLUMN_FULL_NAME = "person_name";
        public static final String COLUMN_USER_NAME = "person_user";
        public static final String COLUMN_AVATAR = "person_avatar";
        public static final String COLUMN_LONGITUDE = "person_longitude";
        public static final String COLUMN_LATITUDE = "person_latitude";

        public static Uri buildPersonUri(String id){
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }
}
