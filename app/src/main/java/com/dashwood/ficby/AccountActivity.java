package com.dashwood.ficby;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class AccountActivity extends AppCompatActivity {

    Button log_out, backto_main, account, heart_rate_history, set_id;
    TextView account_mail;
    EditText enter_id;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser user;
    Typeface typeface_regular, typeface_zh_medium;
    //String mail_address ="";
    LottieAnimationView user_fingerprint_anim, circle_anim;
    static int account_button_hit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        typeface_regular = TypefaceProvider.getTypeFace(getApplicationContext(), "Circular_book.ttf");
        typeface_zh_medium = TypefaceProvider.getTypeFace(getApplicationContext(), "Noto_Sans_Soft_Medium.ttf");

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

        account = (Button) findViewById(R.id.btn_account);
        account.setTypeface(typeface_zh_medium);
        heart_rate_history = (Button) findViewById(R.id.btn_heart_rate);
        heart_rate_history.setTypeface(typeface_zh_medium);
        set_id = (Button) findViewById(R.id.btn_set_id);
        set_id.setTypeface(typeface_zh_medium);
        enter_id = (EditText) findViewById(R.id.input_id);
        enter_id.setTypeface(typeface_regular);
        log_out = (Button) findViewById(R.id.btn_logout);
        log_out.setTypeface(typeface_zh_medium);
        account_mail = (TextView) findViewById(R.id.account_text);
        backto_main = (Button) findViewById(R.id.btn_backto_main);
        backto_main.setTypeface(typeface_zh_medium);
        user_fingerprint_anim = (LottieAnimationView) findViewById(R.id.user_animation);
        circle_anim = (LottieAnimationView) findViewById(R.id.circle_animation);

        //Intent intent = this.getIntent();
        //String mail_address = intent.getStringExtra("account_mail");

        if (auth.getCurrentUser() != null)
        {
            account_mail.setText(user.getEmail());
            account_mail.setTypeface(typeface_regular);
        }

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null)
                {
                    if (TextUtils.isEmpty(user.getDisplayName()))
                    {
                        account.setText("ID");
                        account.setTypeface(typeface_regular);
                        heart_rate_history.setVisibility(View.GONE);
                        log_out.setVisibility(View.GONE);
                        account_mail.setVisibility(View.GONE);
                        backto_main.setVisibility(View.GONE);
                        circle_anim.setVisibility(View.GONE);
                        set_id.setVisibility(View.VISIBLE);
                        enter_id.setVisibility(View.VISIBLE);
                    }
                    else if (account_button_hit == 0)
                    {
                        account.setText("ID");
                        account.setTypeface(typeface_regular);
                        account_mail.setText("ID: " + user.getDisplayName());
                        account_button_hit = 1;
                    }

                    else if (account_button_hit == 1)
                    {
                        account.setText("帳號");
                        account.setTypeface(typeface_zh_medium);
                        account_mail.setText(user.getEmail());
                        account_button_hit = 0;
                    }



                }
            }
        });

        set_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = enter_id.getText().toString().trim();
                if (id.length() < 6)
                {
                    enter_id.setError("ID要大於六個字元!");
                    return;
                }
                if (TextUtils.isEmpty(id))
                {
                    enter_id.setError("請輸入ID!");
                    return;
                }

                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(id).build();
                user.updateProfile(profileChangeRequest)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "設定成功! 可同步心率數據至資料庫!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        });
            }
        });

        heart_rate_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, HeartrateActivity.class));
            }
        });



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
                //startActivity(new Intent(AccountActivity.this, MainActivity.class));
                finish();
            }
        });

        circle_anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, MoreActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_fingerprint_anim.playAnimation();
        circle_anim.playAnimation();
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
        user_fingerprint_anim.cancelAnimation();
        circle_anim.cancelAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
        user_fingerprint_anim.cancelAnimation();
        circle_anim.cancelAnimation();
    }
}
