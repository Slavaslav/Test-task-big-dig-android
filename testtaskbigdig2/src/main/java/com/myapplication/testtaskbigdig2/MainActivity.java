package com.myapplication.testtaskbigdig2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        handleIntent();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action)) {
            Bundle bundle = intent.getExtras();
            String operationType = (String) bundle.get(ContentProviderHelper.OPERATION_TYPE);
            if (operationType != null) {
                String uri = (String) bundle.get(ContentProviderHelper.IMAGE_COLUMN_URI);
                if (operationType.equals(ContentProviderHelper.OPERATION_CREATE)) {
                    new HandleURIImage().execute(uri);
                } else if (operationType.equals(ContentProviderHelper.OPERATION_UPDATE)) {
                    String id = (String) bundle.get(ContentProviderHelper.IMAGE_COLUMN_ID);
                    String status = (String) bundle.get(ContentProviderHelper.IMAGE_COLUMN_STATUS);
                    new HandleURIImage().execute(uri, id, status);
                }
            }
        } else if (Intent.ACTION_MAIN.equals(action)) {
            closeApplicationByTimer();
        }
    }

    private void closeApplicationByTimer() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                textView.setText(getString(R.string.is_not_standalone_application, msg.what));
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 10; i >= 1; i--) {
                    handler.sendEmptyMessage(i);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
                finish();
            }
        }).start();
    }

    private void showImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    private void deleteImageFromDbAndSave(final int id, final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // delete
                        ContentProviderHelper.delete(MainActivity.this, id);
                        Toast.makeText(MainActivity.this, R.string.link_was_removed, Toast.LENGTH_LONG).show();
                        // save bitmap
                        try {
                            Utils.saveImage(bitmap, id);
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, R.string.error_saving_image, Toast.LENGTH_LONG).show();
                        }
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
