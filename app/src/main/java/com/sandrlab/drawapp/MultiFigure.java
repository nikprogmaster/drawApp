package com.sandrlab.drawapp;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MultiFigure extends GeomFigure implements Parcelable {

    List<PointF> points = new ArrayList<>();
    private Path path = new Path();
    private int color;

    public MultiFigure(Path path) {
        this();
        this.path = path;
    }

    public void setColor(int color){
        this.color = color;
        paint.setColor(color);
    }

    public MultiFigure() {

        figureId = 5;
        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(12f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }


    protected MultiFigure(Parcel in) {
        points = in.createTypedArrayList(PointF.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(points);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MultiFigure> CREATOR = new Creator<MultiFigure>() {
        @Override
        public MultiFigure createFromParcel(Parcel in) {
            return new MultiFigure(in);
        }

        @Override
        public MultiFigure[] newArray(int size) {
            return new MultiFigure[size];
        }
    };

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (points.isEmpty()){
            return;
        }
        if (points.size() == 1){
            drawSinglePoint(canvas);
        } else if (points.size() ==2){
            drawLine(canvas);
        } else {
            drawPolygon(canvas);
        }
    }

    private void drawPolygon(Canvas canvas) {
        path.reset();
        PointF first = points.get(0);
        path.moveTo(first.x, first.y);
        for (int i = 1; i < points.size(); i++) {
            PointF point = points.get(i);
            path.lineTo(point.x, point.y);
        }

        canvas.drawPath(path, paint);
    }

    private void drawLine(Canvas canvas) {
        PointF start = points.get(0);
        PointF stop = points.get(1);
        canvas.drawLine(start.x, start.y, stop.x, stop.y, paint);

    }

    private void drawSinglePoint(Canvas canvas) {
        canvas.drawCircle(points.get(0).x, points.get(0).y, 16, paint);
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

    public void setPoint(int index, float x, float y){
        PointF point;
        if (index<points.size()) {
            point = points.get(index);
        } else {
            point = new PointF();
            points.add(point);
        }
        point.x = x;
        point.y = y;
    }


}