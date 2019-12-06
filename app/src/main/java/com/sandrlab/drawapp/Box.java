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

public class Box extends GeomFigure{

    private PointF mOrigin = new PointF();
    private PointF mCurrent = new PointF();
    private static final float STROKE_WIDTH = 8f;
    private int color = Color.BLACK;

    public Box(Parcel in) {
        mCurrent.readFromParcel(in);
        mOrigin.readFromParcel(in);
        paint = new Paint();
        color = in.readInt();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        this.figureId = 3;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        this.figureId = 3;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float left = Math.min(this.getOrigin().x, this.getCurrentPoint().x);
        float right = Math.max(this.getOrigin().x, this.getCurrentPoint().x);
        float top = Math.min(this.getOrigin().y, this.getCurrentPoint().y);
        float bottom = Math.max(this.getOrigin().y, this.getCurrentPoint().y);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    public PointF getCurrentPoint() {
        return mCurrent;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setCurrentPoint(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        mOrigin.writeToParcel(parcel, i);
        mCurrent.writeToParcel(parcel, i);
        parcel.writeInt(color);
        parcel.writeInt(figureId);
    }

    public static final Creator<Box> CREATOR = new Creator<Box>() {
        @Override
        public Box createFromParcel(Parcel in) {
            return new Box(in);
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };
}
