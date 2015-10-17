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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by tioammar
 * on 9/17/15.
 */
public class Provider extends ContentProvider {

    private static final UriMatcher mUriMatcher = getUriMatcher();

    private static final String mReportWithIdSelection =
            Contract.MarkColumn.TABLE_NAME + "." + Contract.MarkColumn.COLUMN_ID + " = ? ";
    private static final String mUserWithIdSelection = Contract.PersonColumn.COLUMN_ID + " = ? ";

    private static UriMatcher getUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        matcher.addURI(authority, Contract.PATH_MARK, MARK);
        matcher.addURI(authority, Contract.PATH_MARK + "/#", MARK_ID);

        matcher.addURI(authority, Contract.PATH_PERSON, PERSON);
        matcher.addURI(authority, Contract.PATH_PERSON + "/#", PERSON_ID);

        return matcher;
    }

    private DBHelper mDbHelper;

    static final int MARK = 100;
    static final int MARK_ID = 101;

    static final int PERSON = 200;
    static final int PERSON_ID = 201;

    private static final SQLiteQueryBuilder mQueryBuilder;

    static {
        mQueryBuilder = new SQLiteQueryBuilder();
        mQueryBuilder.setTables(
                Contract.MarkColumn.TABLE_NAME + " INNER JOIN " + Contract.PersonColumn.TABLE_NAME +
                        " ON " + Contract.MarkColumn.TABLE_NAME + "." + Contract.MarkColumn.COLUMN_USER +
                        " = " + Contract.PersonColumn.TABLE_NAME + "." + Contract.PersonColumn.COLUMN_ID
        );
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = mUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case MARK: {
                cursor = getReport(projection, selection, selectionArgs);
                break;
            }
            case MARK_ID: {
                cursor = getReportWithId(uri, projection);
                break;
            }
            case PERSON: {
                cursor = mDbHelper.getReadableDatabase().query(
                        Contract.PersonColumn.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            }
            case PERSON_ID: {
                String id = uri.getLastPathSegment();
                cursor = mDbHelper.getReadableDatabase().query(
                        Contract.PersonColumn.TABLE_NAME,
                        projection,
                        mUserWithIdSelection,
                        new String[]{id},
                        null,
                        null,
                        null
                );
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor getReportWithId(Uri uri, String[] projection) {
        String selection = mReportWithIdSelection;
        String id = uri.getLastPathSegment();
        String[] selectionArgs = new String[]{id};

        return mQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    private Cursor getReport(String[] projection, String selection, String[] selectionArgs) {
        String sortOrder = Contract.MarkColumn.COLUMN_TIME + " DESC";
        return mQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match){
            case MARK:
                return Contract.MarkColumn.CONTENT_TYPE;
            case MARK_ID:
                return Contract.MarkColumn.CONTENT_ITEM_TYPE;
            case PERSON:
                return Contract.PersonColumn.CONTENT_TYPE;
            case PERSON_ID:
                return Contract.PersonColumn.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        Uri retUri;
        switch (match){
            case MARK: {
                db.insertWithOnConflict(Contract.MarkColumn.TABLE_NAME, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                retUri = Contract.MarkColumn.buildMarkUri(values.getAsString(Contract.MarkColumn.COLUMN_ID));
                break;
            }
            case PERSON: {
                db.insertWithOnConflict(Contract.PersonColumn.TABLE_NAME, null, values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                retUri = Contract.PersonColumn.buildPersonUri(values.getAsString(Contract.PersonColumn.COLUMN_ID));
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsDeleted;
        if (selection == null) selection = "1";
        switch (match){
            case MARK: {
                rowsDeleted = db.delete(Contract.MarkColumn.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case PERSON: {
                rowsDeleted = db.delete(Contract.PersonColumn.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsUpdated;
        switch (match){
            case MARK: {
                rowsUpdated = db.update(Contract.MarkColumn.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            }
            case PERSON: {
                rowsUpdated = db.update(Contract.PersonColumn.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match =  mUriMatcher.match(uri);
        int retCount = 0;
        switch (match){
            case MARK: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values){
                        long id = db.insertWithOnConflict(Contract.MarkColumn.TABLE_NAME,
                                null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1){
                            retCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }
            case PERSON: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values){
                        long id = db.insertWithOnConflict(Contract.PersonColumn.TABLE_NAME,
                                null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1){
                            retCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }
            default:
                return super.bulkInsert(uri, values);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retCount;
    }
}
