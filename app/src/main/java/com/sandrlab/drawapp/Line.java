package com.sandrlab.drawapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Line extends GeomFigure{
    private static final float STROKE_WIDTH = 8f;
    private Path path;

    private int color = Color.BLACK;

    Line(){
        paint = new Paint();
        setUpPaint(paint);
        figureId = 2;
        path = new Path();
    }

    public Line(Parcel in) {
        this();
        color = in.readInt();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(this.getPath(), paint);
    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }


    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private void setUpPaint(Paint paint) {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public Path getPath() {
        return path;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(color);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(color);
    }

    public static final Creator<Line> CREATOR = new Creator<Line>() {
        @Override
        public Line createFromParcel(Parcel in) {
            return new Line(in);
        }

        @Override
        public Line[] newArray(int size) {
            return new Line[size];
        }
    };
}
