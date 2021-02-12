package com.example.amialergic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private Bundle savedInstanceState;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.CAMERA},
                PackageManager.PERMISSION_GRANTED);


    }


    public void ScanButton(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)  {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) if (intentResult.getContents() == null) {
            textView.setText("Cancelled");
        } else {
            OkHttpClient client = new OkHttpClient();

            String results = intentResult.getContents();

            String url = "https://world.openfoodfacts.org/api/v0/product/" + results + ".json";

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String myResponse = response.body().toString().toLowerCase();

                                MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                String master = "Hello World Baeldung!";
//                                String target = "Baeldung";
//                                String replacement = "Java";
//                                String processed = master.replace(target, replacement);
//                                assertTrue(processed.contains(replacement));
//                                assertFalse(processed.contains(target));

                                String nonWords = "\"";
                                String processed = myResponse.replace(nonWords, "");

                                String hasAllergen;
//                                System.out.println(Str.matches("(.*)Tutorials(.*)"));
//                                if (myResponse.matches("(.*)corn(.*)")) {
//                                        hasAllergen = "True";
//                            } else{
//                                        hasAllergen = "False";
//                                    }
                                textView.setText(processed + "\n" + myResponse);

                            }
                        });
                    }
                }
            });

            super.onActivityResult(requestCode, resultCode, data);

        }
    }
}