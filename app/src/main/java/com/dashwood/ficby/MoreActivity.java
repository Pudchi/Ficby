package com.dashwood.ficby;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import static com.dashwood.ficby.MainActivity.CIRCULAR_BOOK;
import static com.dashwood.ficby.MainActivity.SOFT_MEDIUM;

public class MoreActivity extends AppCompatActivity {

    TextView info;
    Typeface typeface_book, typeface_zh_medium;
    final static String intro = "近年老年化人口攀升\n導致健康與安全問題越趨重要\n" +
            "人們不僅要負擔工作壓力\n還要照顧家中長者\n" +
            "更可能因此累垮了自己\n" +
            "我們以此為出發點開發出了 Ficby\n" +
            "以健康與安全為主軸\n結合穿戴式裝置和Beacon技術\n希望提供一個兼具設計感及美觀\n" +
            "又能獲取所需健康數據\n及定位資訊的服務型應用";

    final static String intro_en = "F-I-C means\nFriendly iHealth Cloud\n\ncombined above concepts\nFICBY stands for\nHealth Assistant";

    static int intro_flag = 0;
    LottieAnimationView core_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        typeface_book = Typeface.createFromAsset(getAssets(), CIRCULAR_BOOK);
        typeface_zh_medium = Typeface.createFromAsset(getAssets(), SOFT_MEDIUM);
        info = (TextView) findViewById(R.id.info_text);
        info.setTypeface(typeface_book);
        core_animation = (LottieAnimationView) findViewById(R.id.core_animation);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intro_flag == 0) {
                    info.setText(intro);
                    info.setTypeface(typeface_zh_medium);
                    intro_flag = 1;
                }
                else if (intro_flag == 1)
                {
                    info.setText(intro_en);
                    info.setTypeface(typeface_book);
                    intro_flag = 0;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        core_animation.cancelAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();

        core_animation.cancelAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        core_animation.playAnimation();
    }
}