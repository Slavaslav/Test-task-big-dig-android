package com.myapplication.testtaskbigdig2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
        handleStartup();
    }

    private void handleStartup() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action)) {
            String uri = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (uri != null) {
                new DownloadImage().execute(uri);
                /*ContentProviderHelper provider = new ContentProviderHelper(this);
                provider.insert(uri, (byte) 1, "12.12.12");
                provider.getAllImagesData();*/
            }
        } else {
            // autoclose
        }
    }

    private void showImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    private class DownloadImage extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String uri = strings[0];
            int status;
            String toastMessage = null;
            ContentProviderHelper provider = new ContentProviderHelper(MainActivity.this);
            Bitmap bitmap = null;
            try {
                bitmap = Utils.downloadImage(uri);
                if (bitmap == null) {
                    toastMessage = getString(R.string.no_image);
                    status = 3;
                } else {
                    //toastMessage = getString(R.string.image_successfully_downloaded);
                    status = 1;
                }
            } catch (java.io.IOException e) {
                toastMessage = getString(R.string.error_handling_uri);
                status = 2;
            }
            provider.insert(uri, (byte) status, Utils.getDateTime());
            publishProgress(toastMessage);
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null && values[0] != null) {
                Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressBar.setVisibility(View.GONE);
            if (bitmap != null) {
                showImage(bitmap);
            }
        }
    }
}
