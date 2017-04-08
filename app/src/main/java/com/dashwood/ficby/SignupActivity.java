package com.dashwood.ficby;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.dashwood.ficby.MainActivity.CIRCULAR_BOLD;
import static com.dashwood.ficby.MainActivity.CIRCULAR_BOOK;

public class SignupActivity extends AppCompatActivity {

    Button backto_login, sign_up;
    EditText input_mail, input_password;
    Typeface typeface_bold, typeface_regular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        typeface_bold = Typeface.createFromAsset(getAssets(), CIRCULAR_BOLD);
        typeface_regular = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);

        input_mail = (EditText) findViewById(R.id.email_input);
        input_mail.setTypeface(typeface_regular);
        input_password = (EditText) findViewById(R.id.password_input);
        input_password.setTypeface(typeface_regular);
        sign_up = (Button) findViewById(R.id.btn_signup);
        backto_login = (Button) findViewById(R.id.btn_backtologin);

        backto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
