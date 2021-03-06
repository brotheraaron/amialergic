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
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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
        Collection<String> BARCODE_TYPES =
        Collections.unmodifiableCollection(Arrays.asList("UPC_A", "UPC-E", "EAN_8", "EAN_13"));
        intentIntegrator.initiateScan(BARCODE_TYPES);
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty() || str.isEmpty();
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
                        final String myResponse = response.body().string();

                        Object document = Configuration.defaultConfiguration().jsonProvider().parse(myResponse);

                        String ingredientsRaw = JsonPath.read(document, "$.product.ingredients_text");
                        String ingredients0 = ingredientsRaw.toLowerCase();

                        String allergen = "(.*)corn(.*)";
                        String hasAllergen;

                        //hasAllergen = ingredients0;
                        if(isNullOrEmpty(ingredients0)) hasAllergen ="Is null.\n";
                        else hasAllergen = "Is not null.\n";

//                        if(isNullOrEmpty(ingredients0)) hasAllergen = "No results found.";
//                        else if (!ingredients0.matches(allergen)) hasAllergen = "False";
//                        else hasAllergen = "True";


                        String finalHasAllergen = hasAllergen;
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(finalHasAllergen);
                            }
                        });
                    }
                }
            });

            super.onActivityResult(requestCode, resultCode, data);

        }
    }
}