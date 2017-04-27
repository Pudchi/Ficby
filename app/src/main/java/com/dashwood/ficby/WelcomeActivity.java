package com.dashwood.ficby;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;

public class WelcomeActivity extends AppCompatActivity {
    //ProgressBar welcome_load;
    LottieAnimationView welcome_animation, welcome_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //welcome_load = (ProgressBar) findViewById(R.id.welcome_load);
        welcome_animation = (LottieAnimationView) findViewById(R.id.welcome_animation);
        welcome_loader = (LottieAnimationView) findViewById(R.id.welcome_loader);

        //welcome_load.setVisibility(View.VISIBLE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //welcome_load.setVisibility(View.INVISIBLE);
                welcome_animation.cancelAnimation();
                welcome_loader.cancelAnimation();
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }, 6000);
    }
}
