package com.dashwood.ficby;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import im.dacer.androidcharts.LineView;

public class BandActivity extends AppCompatActivity {

    TextView band_text, warn;
    Typeface typeface_zh_medium;
    Button draw, connect_band;
    int data_random = 8;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    static int bind_band_result = 0;

    LineView lineView;
    ArrayList <String> x_value = new ArrayList<String>();
    ArrayList <String> y_value = new ArrayList<String>();
    String[] heart_rate = new String[] {"75", "80", "85", "90", "95", "100", "105", "110"};
    int[] hr_stable = new int[] {86, 88, 84, 81, 84, 106, 93, 108};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band);

        typeface_zh_medium = TypefaceProvider.getTypeFace(getApplicationContext(), "Noto_Sans_Soft_Medium.ttf");
        //BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        band_text = (TextView) findViewById(R.id.band_text);
        band_text.setTypeface(typeface_zh_medium);

        lineView = (LineView) findViewById(R.id.line_view);
        connect_band = (Button) findViewById(R.id.btn_connect_band);
        connect_band.setTypeface(typeface_zh_medium);
        draw = (Button) findViewById(R.id.btn_draw_line_chart);
        draw.setTypeface(typeface_zh_medium);
        warn = (TextView)findViewById(R.id.warn_text);
        warn.setTypeface(typeface_zh_medium);

        initLineView(lineView);

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bind_band_result == 0)
                {
                    warn.setVisibility(View.VISIBLE);
                }
                else
                {
                    drawLineChart(lineView);
                }

            }
        });

        connect_band.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BandActivity.this, ScanBTActivity.class));
            }
        });

        /*lineChart = (LineChart) findViewById(R.id.chart);
        lineChart.setContentDescription("心率折線圖");*/


    }

    private void initLineView(LineView lineView) {
        ArrayList<String> x_val = new ArrayList<String>();
        ArrayList<String> y_val = new ArrayList<String>();
        for (int x=0; x<data_random;x++)
        {
            y_val.add(heart_rate[x]);
        }

        for (int i=0; i<data_random; i++)
        {
            x_val.add(String.valueOf(i+1));
        }
        lineView.setBottomTextList(x_val);

        lineView.setColorArray(new int[]{Color.parseColor("#F44336"),Color.parseColor("#9C27B0"),
                Color.parseColor("#2196F3"),Color.parseColor("#009688")});
        lineView.setDrawDotLine(true);

        lineView.setShowPopup(LineView.SHOW_POPUPS_All);
    }

    private void drawLineChart(LineView lineView) {
        ArrayList<Integer> dataList = new ArrayList<>();
        //float random = (float)(Math.random()*41+70);
        for (int i=0; i<hr_stable.length; i++){
            dataList.add(hr_stable[i]);
        }
        ArrayList<ArrayList<Integer>> linedataList = new ArrayList<>();
        linedataList.add(dataList);

        lineView.setDataList(linedataList);

    }

    @Override
    protected void onResume() {
        super.onResume();
        warn.setVisibility(View.INVISIBLE);
    }
}
