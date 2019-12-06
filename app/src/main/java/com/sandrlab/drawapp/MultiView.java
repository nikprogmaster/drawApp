package com.sandrlab.drawapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class MultiView extends View {

    private List<GeomFigure> figuresList = new ArrayList<>();
    private static int SELECTED_MODE;
    private static int COLOR = Color.BLACK;
    private static final int MODE_DOT = 0;
    private static final int MODE_STRAIGHT_LINE = 1;
    private static final int MODE_LINE = 2;
    private static final int MODE_RECT = 3;
    private static final int MODE_PAINTED_RECT = 4;
    private static final int MODE_MULTITOUCH = 5;
    private ScaleGestureDetector scaleGestureDetector;
    private boolean mIsZooming = false;
    private float scale = 1f;
    private Paint mBackgroundPaint;
    private Box mCurrentBox;
    private Line currentLine;
    private Dot currentDot;
    private StraightLine currentStrLine;
    private PaintedBox paintedBox;
    private PointF pivotPoint = new PointF();

    private ScaleGestureDetector.OnScaleGestureListener scaleListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            scale = scale*scaleGestureDetector.getScaleFactor();
            pivotPoint.x = scaleGestureDetector.getFocusX();
            pivotPoint.y = scaleGestureDetector.getFocusY();
            invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {}
    };


    public MultiView(Context context) {
        this(context, null);
    }

    public MultiView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MultiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.WHITE);
        scaleGestureDetector = new ScaleGestureDetector(getContext(), scaleListener);
    }

    public boolean ismIsZooming() {
        return mIsZooming;
    }

    public void setmIsZooming(boolean mIsZooming) {
        this.mIsZooming = mIsZooming;
    }


    public static int getSelectedMode() {
        return SELECTED_MODE;
    }

    public static void setSelectedMode(int selectedMode) {
        SELECTED_MODE = selectedMode;
    }

    public static int getCOLOR() {
        return COLOR;
    }

    public static void setCOLOR(int COLOR) {
        MultiView.COLOR = COLOR;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.scale(scale, scale, pivotPoint.x, pivotPoint.y);
        canvas.save();
        canvas.restore();
        canvas.drawPaint(mBackgroundPaint);
        for (GeomFigure geomFigure: figuresList) {
            switch (geomFigure.getFigureId()){
                case MODE_DOT: Dot dot = (Dot) geomFigure;
                dot.draw(canvas);
                break;

                case MODE_STRAIGHT_LINE: StraightLine straightline = (StraightLine) geomFigure;
                straightline.draw(canvas);
                break;

                case MODE_LINE: Line line = (Line) geomFigure;
                line.draw(canvas);
                break;

                case MODE_RECT: Box box = (Box) geomFigure;
                box.draw(canvas);
                break;

                case MODE_PAINTED_RECT: PaintedBox paintedBox = (PaintedBox) geomFigure;
                paintedBox.draw(canvas);
                break;

                case MODE_MULTITOUCH: MultiFigure multiFigure = (MultiFigure) geomFigure;
                multiFigure.draw(canvas);
                break;
            }
        }
        canvas.save();
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsZooming){
            scaleGestureDetector.onTouchEvent(event);
            return true;
        }
        int action = event.getActionMasked();
        switch (SELECTED_MODE) {
            case MODE_MULTITOUCH: {
                MultiFigure multiFigure;
                if (figuresList.isEmpty()) {
                    multiFigure = null;
                } else if (figuresList.get(figuresList.size() - 1).figureId != 5) {
                    multiFigure = null;
                } else {
                    multiFigure = (MultiFigure) figuresList.get(figuresList.size() - 1);
                }

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        multiFigure = new MultiFigure();
                        multiFigure.setColor(COLOR);
                        figuresList.add(multiFigure);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        break;
                }
                for (int pointerInex = 0; pointerInex < event.getPointerCount(); pointerInex++) {
                    int pointerId = event.getPointerId(pointerInex);
                    multiFigure.setPoint(pointerId, event.getX(pointerInex)*scale, event.getY(pointerInex)*scale);
                }
                break;
            }

            case MODE_DOT: {
                float eventX = event.getX();
                float eventY = event.getY();
                switch (action) {
                    case ACTION_DOWN:
                        currentDot = new Dot();
                        currentDot.setColor(COLOR);
                        currentDot.setPoint(eventX, eventY);
                        figuresList.add(currentDot);
                        return true;
                    case ACTION_MOVE:
                        break;
                    case ACTION_UP:
                        break;
                    default:
                        return false;
                }
                break;
            }

            case MODE_STRAIGHT_LINE: {
                float eventX = event.getX();
                float eventY = event.getY();
                switch (action) {
                    case ACTION_DOWN:
                        currentStrLine = new StraightLine();
                        currentStrLine.setColor(COLOR);
                        figuresList.add(currentStrLine);
                        currentStrLine.getPath().moveTo(eventX, eventY);
                        return true;
                    case ACTION_MOVE:
                        break;
                    case ACTION_UP:
                        currentStrLine.getPath().lineTo(eventX, eventY);
                        break;
                    default:
                        return false;
                }
                break;
            }

            case MODE_LINE: {
                float eventX = event.getX();
                float eventY = event.getY();
                switch (action) {
                    case ACTION_DOWN:
                        currentLine = new Line();
                        currentLine.setColor(COLOR);
                        figuresList.add(currentLine);
                        currentLine.getPath().moveTo(eventX, eventY);
                        return true;
                    case ACTION_MOVE:
                        currentLine.getPath().lineTo(eventX, eventY);
                        break;
                    case ACTION_UP:
                        break;
                    default:
                        return false;
                }
                break;
            }
            case MODE_RECT: {
                PointF current = new PointF(event.getX(), event.getY());

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mCurrentBox = new Box(current);
                        mCurrentBox.setColor(COLOR);
                        figuresList.add(mCurrentBox);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mCurrentBox != null) {
                            mCurrentBox.setCurrentPoint(current);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mCurrentBox = null;
                        break;
                }
                break;
            }

            case MODE_PAINTED_RECT: {
                PointF current = new PointF(event.getX(), event.getY());

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        paintedBox = new PaintedBox(current);
                        paintedBox.setColor(COLOR);
                        figuresList.add(paintedBox);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (paintedBox != null) {
                            paintedBox.setCurrentPoint(current);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        paintedBox = null;
                        break;
                }
                break;
            }
        }

        invalidate();
        return true;
    }

    public void clear() {
        figuresList.clear();
        invalidate();
    }


    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState =  super.onSaveInstanceState();
        SavedState state = new SavedState(superState);
        state.figures = this.figuresList;

        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(state);

        figuresList = savedState.figures;
    }

    private void init() {
        scaleGestureDetector = new ScaleGestureDetector(getContext(), scaleListener);
    }

    private static class SavedState extends BaseSavedState{

        private List<GeomFigure> figures;

        public SavedState(Parcel source) {
            super(source);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }
}
