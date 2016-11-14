package com.myapplication.testtaskbigdig1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class BigDigProvider extends ContentProvider {

    static final String IMAGE_COLUMN_ID = "_id";
    static final String IMAGE_COLUMN_URI = "uri";
    static final String IMAGE_COLUMN_STATUS = "status";
    static final String IMAGE_COLUMN_DATE = "date";
    private static final int IMAGES = 1;
    private static final int IMAGE_ID = 2;
    private static final UriMatcher uriMatcher;
    private static final String DB_NAME = "bigdig";
    private static final int DB_VERSION = 1;
    private static final String IMAGE_TABLE_NAME = "images";
    private static final String DB_CREATE_TABLE = "create table " + IMAGE_TABLE_NAME + "("
            + IMAGE_COLUMN_ID + " integer primary key autoincrement, "
            + IMAGE_COLUMN_URI + " text, "
            + IMAGE_COLUMN_STATUS + " integer, "
            + IMAGE_COLUMN_DATE + " text" + ");";
    private static final String PROVIDER_NAME = "com.myapplication.testtaskbigdig1.provider.BigGig";
    private static final String IMAGE_PATH = "images";
    static final Uri IMAGE_CONTENT_URI = Uri.parse("content://"
            + PROVIDER_NAME + "/" + IMAGE_PATH);

    private static final String CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + PROVIDER_NAME + "." + IMAGE_PATH;
    private static final String CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + PROVIDER_NAME + "." + IMAGE_PATH;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "images", IMAGES);
        uriMatcher.addURI(PROVIDER_NAME, "images/#", IMAGE_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        switch (uriMatcher.match(uri)) {
            case IMAGES:
                if (TextUtils.isEmpty(s1)) {
                    s1 = IMAGE_COLUMN_ID + " ASC";
                }
                break;
            case IMAGE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = IMAGE_COLUMN_ID + " = " + id;
                } else {
                    s = s + " AND " + IMAGE_COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        Cursor cursor = db.query(IMAGE_TABLE_NAME, strings, s,
                strings1, null, null, s1);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case IMAGES:
                return CONTACT_CONTENT_TYPE;
            case IMAGE_ID:
                return CONTACT_CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (uriMatcher.match(uri) != IMAGES)
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        long rowID = db.insert(IMAGE_TABLE_NAME, "", contentValues);
        Uri resultUri = ContentUris.withAppendedId(IMAGE_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        switch (uriMatcher.match(uri)) {
            case IMAGE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = IMAGE_COLUMN_ID + " = " + id;
                } else {
                    s = s + " AND " + IMAGE_COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int countRows = db.delete(IMAGE_TABLE_NAME, s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return countRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        switch (uriMatcher.match(uri)) {
            case IMAGE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = IMAGE_COLUMN_ID + " = " + id;
                } else {
                    s = s + " AND " + IMAGE_COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int countRows = db.update(IMAGE_TABLE_NAME, contentValues, s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return countRows;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DB_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            // empty
        }
    }
}
