package com.myapplication.testtaskbigdig2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleStartup();
    }

    private void handleStartup() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action)) {
            String uri = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (uri != null) {
                try {
                    boolean isImage = Utils.isURIImage(uri);
                    if (!isImage) {
                        Utils.showToast(this, getString(R.string.no_image), Toast.LENGTH_LONG);
                    }
                } catch (java.io.IOException e) {
                    Utils.showToast(this, getString(R.string.error_handling_uri), Toast.LENGTH_LONG);
                }
                /*ContentProviderHelper provider = new ContentProviderHelper(this);
                provider.insert(uri, (byte) 1, "12.12.12");
                provider.getAllImagesData();*/
            }
        } else {
            // autoclose
        }
    }
}
