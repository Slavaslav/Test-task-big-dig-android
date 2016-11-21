package com.myapplication.testtaskbigdig2;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

class ContentProviderHelper {
    static final String IMAGE_COLUMN_ID = "_id";
    static final String IMAGE_COLUMN_URI = "uri";
    static final String IMAGE_COLUMN_STATUS = "status";
    private static final Uri IMAGE_CONTENT_URI = Uri.parse("content://com.myapplication.testtaskbigdig1.provider.BigGig/images");
    private static final String IMAGE_COLUMN_DATE = "date";

    static void insert(Context context, String uri, byte status, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_COLUMN_URI, uri);
        contentValues.put(IMAGE_COLUMN_STATUS, status);
        contentValues.put(IMAGE_COLUMN_DATE, date);
        context.getContentResolver().insert(IMAGE_CONTENT_URI, contentValues);
    }

    static void update(Context context, int id, byte status, String date) {
        Uri uriWithId = Uri.parse(IMAGE_CONTENT_URI.toString() + "/" + id);
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_COLUMN_STATUS, status);
        contentValues.put(IMAGE_COLUMN_DATE, date);
        context.getContentResolver().update(uriWithId, contentValues, null, null);
    }

    static void delete(Context context, int id) {
        Uri uriWithId = Uri.parse(IMAGE_CONTENT_URI.toString() + "/" + id);
        context.getContentResolver().delete(uriWithId, null, null);
    }
}
