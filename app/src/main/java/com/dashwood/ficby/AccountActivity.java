package com.dashwood.ficby;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.dashwood.ficby.MainActivity.CIRCULAR_BOOK;

public class AccountActivity extends AppCompatActivity {

    Button log_out;
    ProgressBar progressBar;
    TextView account_mail;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    Typeface typeface_regular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        typeface_regular = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        account_mail = (TextView) findViewById(R.id.account_text);
        Intent intent = this.getIntent();
        String mail_address = intent.getStringExtra("account_mail");

        account_mail.setText(mail_address);
        account_mail.setTypeface(typeface_regular);


        if (progressBar != null)
        {
            progressBar.setVisibility(View.GONE);
        }

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
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

}
