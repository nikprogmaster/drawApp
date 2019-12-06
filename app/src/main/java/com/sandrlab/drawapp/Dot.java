package com.sandrlab.drawapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Dot extends GeomFigure {

    private static final int radius = 16;
    private int color = Color.BLACK;
    private PointF point;

    Dot(){
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        point = new PointF();
        figureId = 0;
    }

    public Dot(Parcel in) {
        this();
        color = in.readInt();
        paint.setColor(color);
        point.readFromParcel(in);

    }

    public PointF getPoint() {
        return point;
    }

    public void setPoint(float x, float y) {
        point.x = x;
        point.y = y;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawCircle(point.x, point.y, radius, paint);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        point.writeToParcel(parcel, i);
        parcel.writeInt(color);
    }

    public static final Creator<Dot> CREATOR = new Creator<Dot>() {
        @Override
        public Dot createFromParcel(Parcel in) {
            return new Dot(in);
        }

        @Override
        public Dot[] newArray(int size) {
            return new Dot[size];
        }
    };
}
