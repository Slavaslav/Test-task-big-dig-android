package com.myapplication.testtaskbigdig2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ContentProviderHelper {
    private static final Uri IMAGE_CONTENT_URI = Uri.parse("content://com.myapplication.testtaskbigdig1.provider.BigGig/images");
    private static final String IMAGE_COLUMN_ID = "_id";
    private static final String IMAGE_COLUMN_URI = "uri";
    private static final String IMAGE_COLUMN_STATUS = "status";
    private static final String IMAGE_COLUMN_DATE = "date";
    private Context context;

    public ContentProviderHelper(Context context) {
        this.context = context;
    }

    public void getAllImagesData() {
        Cursor c = context.getContentResolver().query(IMAGE_CONTENT_URI, null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("_id");
            int uriColIndex = c.getColumnIndex("uri");
            int statusColIndex = c.getColumnIndex("status");
            int dateColIndex = c.getColumnIndex("date");
            do {
                System.out.println(String.format("id = %d, uri = %s, status = %d, date = %s", c.getInt(idColIndex), c.getString(uriColIndex), c.getInt(statusColIndex), c.getString(dateColIndex)));
            } while (c.moveToNext());
        }
    }

    public void insert(String uri, byte status, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_COLUMN_URI, uri);
        contentValues.put(IMAGE_COLUMN_STATUS, status);
        contentValues.put(IMAGE_COLUMN_DATE, date);
        context.getContentResolver().insert(IMAGE_CONTENT_URI, contentValues);
    }
}
