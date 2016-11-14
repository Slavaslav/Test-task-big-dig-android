package com.myapplication.testtaskbigdig2;

import android.content.Context;
import android.os.StrictMode;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Utils {
    static boolean isURIImage(String uri) throws IOException {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        boolean isImage = false;
        if (URLUtil.isValidUrl(uri)) {
            URLConnection connection = new URL(uri).openConnection();
            String contentType = connection.getHeaderField("Content-Type");
            isImage = contentType.startsWith("image/");
        }
        return isImage;
    }

    static void showToast(Context context, String text, int length) {
        Toast toast = Toast.makeText(context, text, length);
        toast.show();
    }

}
