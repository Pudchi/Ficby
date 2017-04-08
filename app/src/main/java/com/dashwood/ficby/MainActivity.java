package com.dashwood.ficby;


import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView app_logo;
    Button account, beacon, band, more;

    String CIRCULAR_BOLD = "fonts/Circular_bold.ttf";
    String CIRCULAR_BOOK = "fonts/Circular_book.ttf";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), CIRCULAR_BOLD);
        Typeface typeface_book = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);


        app_logo = (TextView) findViewById(R.id.app_logo);
        app_logo.setTypeface(typeface_bold);

        account = (Button) findViewById(R.id.account_button);
        account.setTypeface(typeface_book);
        beacon = (Button) findViewById(R.id.beacon_button);
        beacon.setTypeface(typeface_book);
        band = (Button) findViewById(R.id.band_button);
        band.setTypeface(typeface_book);
        more = (Button) findViewById(R.id.more_button);
        more.setTypeface(typeface_book);

    }


}
