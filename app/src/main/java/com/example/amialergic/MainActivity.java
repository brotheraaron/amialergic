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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) if (intentResult.getContents() == null) {
            textView.setText("Cancelled");
        } else {
            textView.setText(intentResult.getContents());
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    /*
    URL url = new URL("https://world.openfoodfacts.org/api/v0/product/" + intentResult.getContents() + ".json");
try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
    for (String line; (line = reader.readLine()) != null;) {
        System.out.println(line);
    }
}
     */


       /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if (intentResult != null){
    if (intentResult.getContents() == null){
    textView.setText(“Cancelled”);
    }else {
    textView.setText(intentResult.getContents());
    }
    }
    super.onActivityResult(requestCode, resultCode, data);
    }
    }

     */
}