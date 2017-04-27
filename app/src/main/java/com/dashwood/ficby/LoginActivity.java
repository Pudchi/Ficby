package com.dashwood.ficby;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.dashwood.ficby.MainActivity.CIRCULAR_BOOK;
import static com.dashwood.ficby.MainActivity.SOFT_MEDIUM;

public class LoginActivity extends AppCompatActivity {

    //TextView app_logo;
    Button login, forget, sign_up;
    EditText input_mail, input_password;
    TextView login_text;
    Typeface typeface_regular, typeface_zh_medium;
    ProgressBar progressBar;
    private FirebaseAuth auth;
    LottieAnimationView check_pop_anim;
    //String mail = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null)
        {
            startActivity(new Intent(LoginActivity.this, AccountActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);

        typeface_regular = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);
        typeface_zh_medium = Typeface.createFromAsset(getAssets(), SOFT_MEDIUM);

        login_text = (TextView) findViewById(R.id.login_text);
        login_text.setTypeface(typeface_zh_medium);
        input_mail = (EditText) findViewById(R.id.email_input);
        input_mail.setTypeface(typeface_regular);
        input_password = (EditText) findViewById(R.id.password_input);
        input_password.setTypeface(typeface_regular);

        login = (Button) findViewById(R.id.btn_login);
        login.setTypeface(typeface_zh_medium);
        forget = (Button) findViewById(R.id.btn_forget);
        forget.setTypeface(typeface_zh_medium);
        sign_up = (Button) findViewById(R.id.btn_signup);
        sign_up.setTypeface(typeface_zh_medium);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        check_pop_anim = (LottieAnimationView) findViewById(R.id.check_animation);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = input_mail.getText().toString().trim();
                final String passwd = input_password.getText().toString().trim();

                if (TextUtils.isEmpty(mail))
                {
                    input_mail.setError("請輸入帳號(Mail)!");
                    //Toast.makeText(getApplicationContext(), "請輸入帳號(Mail)!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwd))
                {
                    input_password.setError("請輸入密碼!");
                    //Toast.makeText(getApplicationContext(), "請輸入密碼!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(mail, passwd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful())
                                {
                                    if (passwd.length() < 6)
                                    {
                                        input_password.setError("密碼太短!! 至少6個字元");
                                        //Toast.makeText(getApplicationContext(), "密碼太短!!需大於6個字元", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(LoginActivity.this, "登入失敗! 檢查帳號密碼或前往註冊!", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    check_pop_anim.setVisibility(View.VISIBLE);
                                    check_pop_anim.playAnimation();
                                    new CountDownTimer(1000, 1000)
                                    {
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            check_pop_anim.cancelAnimation();
                                            check_pop_anim.setVisibility(View.INVISIBLE);
                                            Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }.start();
                                }
                            }
                        });
            }
        });

    }
}
