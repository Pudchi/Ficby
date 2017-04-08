package com.dashwood.ficby;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView app_logo, version;
    Button account, beacon, band, more;

    static final String CIRCULAR_BOLD = "fonts/Circular_bold.ttf";
    static final String CIRCULAR_BOOK = "fonts/Circular_book.ttf";

    Typeface typeface_bold;
    Typeface typeface_regular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typeface_bold = Typeface.createFromAsset(getAssets(), CIRCULAR_BOLD);
        typeface_regular = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);


        app_logo = (TextView) findViewById(R.id.app_logo);
        app_logo.setTypeface(typeface_bold);
        version = (TextView) findViewById(R.id.version);
        version.setTypeface(typeface_bold);

        account = (Button) findViewById(R.id.account_button);
        account.setTypeface(typeface_regular);
        beacon = (Button) findViewById(R.id.beacon_button);
        beacon.setTypeface(typeface_regular);
        band = (Button) findViewById(R.id.band_button);
        band.setTypeface(typeface_regular);
        more = (Button) findViewById(R.id.more_button);
        more.setTypeface(typeface_regular);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

    }


}
