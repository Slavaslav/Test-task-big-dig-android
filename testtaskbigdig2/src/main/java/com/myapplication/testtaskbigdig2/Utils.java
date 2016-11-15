package com.myapplication.testtaskbigdig2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Patterns;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;

public class Utils {
    static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    static Bitmap downloadImage(String uri) throws IOException {
        Bitmap bitmap = null;
        Matcher matcher = Patterns.WEB_URL.matcher(uri);
        if (matcher.matches()) {
            URLConnection connection = new URL(uri).openConnection();
            String contentType = connection.getHeaderField("Content-Type");
            if (contentType != null) {
                boolean isImage = contentType.startsWith("image/");
                if (isImage) {
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                }
            }
        }
        return bitmap;
    }
}
