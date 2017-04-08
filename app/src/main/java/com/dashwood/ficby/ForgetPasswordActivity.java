package com.dashwood.ficby;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText input_mail;
    Button reset;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        input_mail = (EditText) findViewById(R.id.email_input);
        reset = (Button) findViewById(R.id.btn_reset);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = input_mail.getText().toString().trim();

                if (TextUtils.isEmpty(mail))
                {
                    input_mail.setError("E-Mail 不能為空!");
                    //Toast.makeText(getApplication(), "E-Mail 不能為空!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.sendPasswordResetEmail(mail)
                        .addOnCompleteListener(ForgetPasswordActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if ((task.isSuccessful()))
                                {
                                    Toast.makeText(ForgetPasswordActivity.this, "信件已寄出!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(ForgetPasswordActivity.this, "此信箱未註冊過!", Toast.LENGTH_LONG).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
}
