package com.dashwood.ficby;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

import im.dacer.androidcharts.LineView;

import static com.dashwood.ficby.MainActivity.SOFT_MEDIUM;

public class BandActivity extends AppCompatActivity {

    TextView band_text;
    Typeface typeface_zh_medium;
    Button draw;
    int data_random = 10;

    LineView lineView;
    ArrayList <String> x_value = new ArrayList<String>();
    ArrayList <String> y_value = new ArrayList<String>();
    String[] mouth = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band);

        typeface_zh_medium = Typeface.createFromAsset(getAssets(), SOFT_MEDIUM);

        band_text = (TextView) findViewById(R.id.band_text);
        band_text.setTypeface(typeface_zh_medium);

        lineView = (LineView) findViewById(R.id.line_view);
        draw = (Button) findViewById(R.id.btn_draw_line_chart);
        draw.setTypeface(typeface_zh_medium);

        initLineView(lineView);

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawLineChart(lineView);
            }
        });

        /*lineChart = (LineChart) findViewById(R.id.chart);
        lineChart.setContentDescription("心率折線圖");*/


    }

    private void initLineView(LineView lineView) {
        ArrayList<String> test = new ArrayList<String>();
        for (int i=0; i<data_random; i++){
            test.add(String.valueOf(i+1));
        }
        lineView.setBottomTextList(test);
        lineView.setColorArray(new int[]{Color.parseColor("#F44336"),Color.parseColor("#9C27B0"),Color.parseColor("#2196F3"),Color.parseColor("#009688")});
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);
    }

    private void drawLineChart(LineView lineView) {
        ArrayList<Integer> dataList = new ArrayList<>();
        float random = (float)(Math.random()*9+1);
        for (int i=0; i<data_random; i++){
            dataList.add((int)(Math.random()*random));
        }
        ArrayList<ArrayList<Integer>> linedataList = new ArrayList<>();
        linedataList.add(dataList);

        lineView.setDataList(linedataList);

    }


}
