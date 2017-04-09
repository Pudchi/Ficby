package com.dashwood.ficby;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.dashwood.ficby.MainActivity.CIRCULAR_BOOK;
import static com.dashwood.ficby.MainActivity.SOFT_MEDIUM;

public class AccountActivity extends AppCompatActivity {

    Button log_out, backto_main;
    TextView account_mail;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser user;
    Typeface typeface_regular, typeface_zh_medium;
    String mail_address ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        typeface_regular = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);
        typeface_zh_medium = Typeface.createFromAsset(getAssets(), SOFT_MEDIUM);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user_tmp = firebaseAuth.getCurrentUser();
                if (user_tmp == null)
                {
                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        log_out = (Button) findViewById(R.id.btn_logout);
        log_out.setTypeface(typeface_zh_medium);
        account_mail = (TextView) findViewById(R.id.account_text);
        backto_main = (Button) findViewById(R.id.btn_backto_main);
        backto_main.setTypeface(typeface_zh_medium);

        //Intent intent = this.getIntent();
        //tring mail_address = intent.getStringExtra("account_mail");

        if (auth.getCurrentUser() != null)
        {
            account_mail.setText(user.getEmail());
            account_mail.setTypeface(typeface_regular);
        }



        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finish();
            }
        });

        backto_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
