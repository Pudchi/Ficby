package com.dashwood.ficby;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.dashwood.ficby.MainActivity.CIRCULAR_BOOK;

public class MoreActivity extends AppCompatActivity {

    TextView info;
    Typeface typeface_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        typeface_book = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);
        info = (TextView) findViewById(R.id.info_text);
        info.setTypeface(typeface_book);
    }
}
