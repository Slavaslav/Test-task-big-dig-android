package com.myapplication.testtaskbigdig2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.imageView);
        handleIntent();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action)) {
            Bundle bundle = intent.getExtras();
            String id = (String) bundle.get(ContentProviderHelper.IMAGE_COLUMN_ID);
            String uri = (String) bundle.get(ContentProviderHelper.IMAGE_COLUMN_URI);
            String status = (String) bundle.get(ContentProviderHelper.IMAGE_COLUMN_STATUS);
            if (id != null) {
                new HandleURIImage().execute(uri, id, status);
            } else {
                new HandleURIImage().execute(uri);
            }
        } else if (Intent.ACTION_MAIN.equals(action)) {
            // autoclose
        }
    }

    private void showImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    private void deleteImageFromDbAndSave(final int id, Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ContentProviderHelper.delete(MainActivity.this, id);
                        Toast.makeText(MainActivity.this, R.string.link_was_removed, Toast.LENGTH_LONG).show();
                        // save bitmap
                    }
                }, 15000);
            }
        });
    }

    private class HandleURIImage extends AsyncTask<String, String, String> {
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Context ctx = MainActivity.this;
            int successful = 1;
            int error = 2;
            int unknown = 3;

            String uri = strings[0];
            int status;
            String toastMessage = null;
            try {
                bitmap = Utils.downloadImage(uri);
                if (bitmap == null) {
                    toastMessage = getString(R.string.no_image);
                    status = unknown;
                } else {
                    status = successful;
                }
            } catch (Exception e) {
                toastMessage = getString(R.string.error_handling_uri);
                status = error;
            }

            if (strings.length == 1) {
                ContentProviderHelper.insert(ctx, uri, (byte) status, Utils.getDateTime());
            } else if (strings.length > 1) {
                int oldStatus = Integer.parseInt(strings[2]);
                int id = Integer.parseInt(strings[1]);
                if (oldStatus == successful && oldStatus == status) {
                    deleteImageFromDbAndSave(id, bitmap);
                } else if (oldStatus != status) {
                    ContentProviderHelper.update(ctx, id, (byte) status, Utils.getDateTime());
                }
            }
            return toastMessage;
        }

        @Override
        protected void onPostExecute(String toastMessage) {
            super.onPostExecute(toastMessage);
            progressBar.setVisibility(View.GONE);
            if (toastMessage != null) {
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
            if (bitmap != null) {
                showImage(bitmap);
            }
        }
    }
}
