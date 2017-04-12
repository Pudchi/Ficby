package com.dashwood.ficby;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.dashwood.ficby.MainActivity.SOFT_MEDIUM;

public class ScanBTActivity extends AppCompatActivity {

    TextView scanned_text;
    Typeface typeface_zh_medium;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bt);

        typeface_zh_medium = Typeface.createFromAsset(getAssets(), SOFT_MEDIUM);
        scanned_text = (TextView) findViewById(R.id.scan_text);
        scanned_text.setTypeface(typeface_zh_medium);

    }
}
