package com.dashwood.ficby;


import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView app_logo;
    Button account, beacon, band, more;

    private String CIRCULAR_BOLD = "Circular_bold.otf";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app_logo = (TextView) findViewById(R.id.app_logo);
        account = (Button) findViewById(R.id.account_button);


    }


}
