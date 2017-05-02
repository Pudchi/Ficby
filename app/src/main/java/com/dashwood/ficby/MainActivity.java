package com.dashwood.ficby;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;


public class MainActivity extends AppCompatActivity {

    TextView app_logo, version;
    Button account, beacon, band, more;

    static final String CIRCULAR_BOLD = "fonts/Circular_bold.ttf";
    static final String CIRCULAR_BOOK = "fonts/Circular_book.ttf";
    static final String SOFT_MEDIUM = "fonts/Noto_Sans_Soft_Medium.ttf";

    Typeface typeface_bold, typeface_regular
            ,typeface_zh_medium;

    LottieAnimationView main_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typeface_bold = TypefaceProvider.getTypeFace(getApplicationContext(), "Circular_bold.ttf");
        typeface_regular = TypefaceProvider.getTypeFace(getApplicationContext(), "Circular_book.ttf");
        typeface_zh_medium = TypefaceProvider.getTypeFace(getApplicationContext(), "Noto_Sans_Soft_Medium.ttf");

        main_animation = (LottieAnimationView) findViewById(R.id.main_animation);

        app_logo = (TextView) findViewById(R.id.app_logo);
        app_logo.setTypeface(typeface_bold);
        version = (TextView) findViewById(R.id.version);
        version.setTypeface(typeface_bold);

        account = (Button) findViewById(R.id.account_button);
        account.setTypeface(typeface_zh_medium);
        beacon = (Button) findViewById(R.id.beacon_button);
        beacon.setTypeface(typeface_zh_medium);
        band = (Button) findViewById(R.id.band_button);
        band.setTypeface(typeface_zh_medium);
        more = (Button) findViewById(R.id.more_button);
        more.setTypeface(typeface_zh_medium);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, AccountActivity.class));
            }
        });

        band.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BandActivity.class));
            }
        });

        beacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BeaconActivity.class));
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MoreActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        main_animation.playAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        main_animation.cancelAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        main_animation.cancelAnimation();
    }
}
