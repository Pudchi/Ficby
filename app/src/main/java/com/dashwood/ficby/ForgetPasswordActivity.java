package com.dashwood.ficby;

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
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText input_mail;
    TextView forget_text;
    Button reset;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    Typeface typeface_zh_medium, typeface_regular;
    LottieAnimationView mail_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        typeface_zh_medium = TypefaceProvider.getTypeFace(getApplicationContext(), "Noto_Sans_Soft_Medium.ttf");
        typeface_regular = TypefaceProvider.getTypeFace(getApplicationContext(), "Circular_book.ttf");

        forget_text = (TextView) findViewById(R.id.forget_text);
        forget_text.setTypeface(typeface_zh_medium);
        input_mail = (EditText) findViewById(R.id.email_input);
        input_mail.setTypeface(typeface_regular);
        reset = (Button) findViewById(R.id.btn_reset);
        reset.setTypeface(typeface_zh_medium);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mail_animation = (LottieAnimationView) findViewById(R.id.mail_sent_animation);

        auth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = input_mail.getText().toString().trim();

                if (TextUtils.isEmpty(mail))
                {
                    input_mail.setError("E-Mail 不能為空!");
                    //Toast.makeText(getApplication(), "Mail不能為空!", Toast.LENGTH_SHORT).show();
                    return;
                }



                progressBar.setVisibility(View.VISIBLE);

                auth.sendPasswordResetEmail(mail)
                        .addOnCompleteListener(ForgetPasswordActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if ((task.isSuccessful()))
                                {
                                    //Toast.makeText(ForgetPasswordActivity.this, "信件已寄出!", Toast.LENGTH_SHORT).show();
                                    mail_animation.setVisibility(View.VISIBLE);
                                    mail_animation.playAnimation();
                                    new CountDownTimer(mail_animation.getDuration(), 1000)
                                    {
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            mail_animation.setVisibility(View.INVISIBLE);
                                            finish();
                                        }
                                    }.start();
                                    //finish();
                                }
                                else
                                {
                                    Toast.makeText(ForgetPasswordActivity.this, "此信箱未註冊過! 返回註冊!", Toast.LENGTH_LONG).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
}
