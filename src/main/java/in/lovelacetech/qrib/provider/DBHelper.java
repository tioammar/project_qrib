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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by tioammar
 * on 9/17/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "qrib_v2.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_REMINDER_TABLE = "CREATE TABLE " + Contract.MarkColumn.TABLE_NAME + " (" +
                Contract.MarkColumn.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.MarkColumn.COLUMN_CONTENT + " TEXT NOT NULL, " +
                Contract.MarkColumn.COLUMN_TIME + " DATETIME NOT NULL, " +
                Contract.MarkColumn.COLUMN_PHOTO + " TEXT NOT NULL, " +
                Contract.MarkColumn.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                Contract.MarkColumn.COLUMN_USER + " INTEGER NOT NULL, " +
                Contract.MarkColumn.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                Contract.MarkColumn.COLUMN_LATITUDE + " REAL NOT NULL, " +
                " FOREIGN KEY (" + Contract.MarkColumn.COLUMN_USER + ") REFERENCES " +
                Contract.PersonColumn.TABLE_NAME + " (" + Contract.PersonColumn.COLUMN_ID + "));";

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + Contract.PersonColumn.TABLE_NAME + " (" +
                Contract.PersonColumn.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                Contract.PersonColumn.COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                Contract.PersonColumn.COLUMN_USER_NAME + " TEXT NOT NULL, " +
                Contract.PersonColumn.COLUMN_LONGITUDE + " REAL NOT NULL, " +
                Contract.PersonColumn.COLUMN_LATITUDE + " REAL NOT NULL, " +
                Contract.PersonColumn.COLUMN_AVATAR + " TEXT NOT NULL " + " );";

        db.execSQL(SQL_CREATE_REMINDER_TABLE);
        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (newVersion > oldVersion){
//            TODO:
//            adding desire table. for example: comment table
//        }
    }

    /**
     * helper method to clear database
     */
    public void clearDatabase(){
        getWritableDatabase().execSQL("DELETE FROM " +
                Contract.MarkColumn.TABLE_NAME + " WHERE 1");
        getWritableDatabase().execSQL("DELETE FROM " +
                Contract.PersonColumn.TABLE_NAME + " WHERE 1");
    }
}
