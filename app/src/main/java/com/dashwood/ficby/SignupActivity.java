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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.dashwood.ficby.MainActivity.CIRCULAR_BOLD;
import static com.dashwood.ficby.MainActivity.CIRCULAR_BOOK;
import static com.dashwood.ficby.MainActivity.SOFT_MEDIUM;

public class SignupActivity extends AppCompatActivity {

    Button backto_login, sign_up;
    EditText input_mail, input_password;
    TextView signup_text;
    Typeface typeface_bold, typeface_regular, typeface_zh_medium;
    //String mail = "";

    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        typeface_bold = Typeface.createFromAsset(getAssets(), CIRCULAR_BOLD);
        typeface_regular = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);
        typeface_zh_medium = Typeface.createFromAsset(getAssets(), SOFT_MEDIUM);

        signup_text = (TextView) findViewById(R.id.sign_up_text);
        signup_text.setTypeface(typeface_zh_medium);
        input_mail = (EditText) findViewById(R.id.email_input);
        input_mail.setTypeface(typeface_regular);
        input_password = (EditText) findViewById(R.id.password_input);
        input_password.setTypeface(typeface_regular);
        sign_up = (Button) findViewById(R.id.btn_signup);
        sign_up.setTypeface(typeface_zh_medium);
        backto_login = (Button) findViewById(R.id.btn_backtologin);
        backto_login.setTypeface(typeface_zh_medium);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        backto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = input_mail.getText().toString().trim();
                String passwd = input_password.getText().toString().trim();

                if (TextUtils.isEmpty(mail))
                {
                    input_mail.setError("請輸入帳號(Mail)!");
                    //Toast.makeText(getApplicationContext(), "請輸入帳號(E-Mail)!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwd))
                {
                    input_password.setError("請輸入密碼!");
                    //Toast.makeText(getApplicationContext(), "請輸入密碼!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.length() < 6)
                {
                    input_password.setError("密碼太短!! 要大於6個字元");
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(mail, passwd)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "註冊成功!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                if (!task.isSuccessful())
                                {
                                    Toast.makeText(SignupActivity.this, "註冊失敗! 請檢查帳號密碼可用性!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Intent intent = new Intent(SignupActivity.this, AccountActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
