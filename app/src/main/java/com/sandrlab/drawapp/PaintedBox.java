package com.sandrlab.drawapp;

import android.graphics.Paint;
import android.graphics.PointF;

public class PaintedBox extends Box {

    public PaintedBox(PointF origin) {
        super(origin);
        figureId = 4;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

}
