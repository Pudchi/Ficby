package com.dashwood.ficby;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class DrawView extends View{

    DeviceAdapter mDeviceAdapter;

    public DrawView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint r = new Paint();
        r.setColor(Color.WHITE);
        r.setAlpha(255);

        Rect rect = new Rect(0, 0, 10000, 1500);
        canvas.drawRect(rect, r);
        Paint p = new Paint();

        p.setColor(Color.parseColor("#2894FF"));//blue
        p.setAntiAlias(true);// 設置畫筆的鋸齒效果。 true是去除，大家一看效果就明白了
        p.setAlpha(255);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(350, 90, (float) mDeviceAdapter.blue_dis * 200, p);

        p.setColor(Color.parseColor("#02C874"));//green
        p.setAntiAlias(true);// 設置畫筆的鋸齒效果。 true是去除，大家一看效果就明白了
        p.setAlpha(255);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(50, 610, (float) mDeviceAdapter.green_dis * 200, p);

        p.setColor(Color.parseColor("#B15BFF"));//purple
        p.setAntiAlias(true);// 設置畫筆的鋸齒效果。 true是去除，大家一看效果就明白了
        p.setAlpha(255);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(650, 610, (float) mDeviceAdapter.purple_dis * 200, p);
    }
}
