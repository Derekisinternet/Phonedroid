package com.derekco.phonedroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Mastermind on 6/15/17.
 */

public class JoystickView extends SurfaceView implements Runnable {

    /**
     CONSTANTS
     */
    private final double RAD = 57.2957795;
    public final static long DEFAULT_LOOP_INTERVAL = 100; // 100 ms

    /**
    VARIABLES
    */
    private final String TAG = "JOYSTICKVIEW";
    private float centerX = 0;
    private float centerY = 0;
    private int xPosish = 0; // touch x position
    private int yPosish = 0; // touch y position
    private float xDiff = 0;
    private float yDiff = 0;
    private float baseRadius = 0;
    private float hatRadius = 0;
    private long loopInterval = DEFAULT_LOOP_INTERVAL;
    private Thread thread = new Thread(this);
    private int lastAngle = 0; // angle of touch event
    private int lastPower = 0; // distance from event to center
    private OnJoystickMoveListener onJoystickMoveListener; //self-explanatory?

    /**
    CONSTRUCTORS
     */
    public JoystickView(Context context) {
        super(context);
    }

    public JoystickView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public JoystickView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
    }

//    @Override
//    protected void onFinishInflate() {
//
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // setting the measured values to resize the view to a certain width and
        // height
        int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));

        setMeasuredDimension(d, d);

    }

    private int measure(int measureSpec) {
        int result = 0;

        // Decode the measurement specifications.
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            // Return a default size of 200 if no bounds are specified.
            result = 200;
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int newX, int newY, int oldX, int oldY) {
        super.onSizeChanged(newX, newY, oldX, oldY);
        setDimensions();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawJoystick(canvas);
    }

    public void drawJoystick(Canvas myCanvas) {
        setDimensions();
        if (getHolder().getSurface().isValid()) {
            Paint colors = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //clears background
            colors.setARGB(255, 50, 50, 50); //Joystick base color
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setARGB(255, 0, 0, 255); // Joystick top color
            myCanvas.drawCircle(centerX, centerY, hatRadius, colors);
            getHolder().unlockCanvasAndPost(myCanvas);
            Log.d(TAG, "successfully rendered joystick");
        } else {
            Log.e(TAG, "Failed to render joystick");
        }
    }

    private void setDimensions() {
        Log.d(TAG, "setting dimensions");
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 3;
        hatRadius = Math.min(getWidth(), getHeight()) / 5;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xPosish = (int) event.getX();
        yPosish = (int) event.getY();
        xDiff = xPosish - centerX;
        yDiff = yPosish - centerY;
        double abs = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        //TODO: what's happening here?
        if (abs > baseRadius) {
            xPosish = (int) (xDiff * baseRadius / abs + centerX);
            yPosish = (int) (yDiff * baseRadius / abs + centerY);
        }
        invalidate();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            xPosish = (int) centerX;
            yPosish = (int) centerY;
            thread.interrupt();
            if (onJoystickMoveListener != null) {
                onJoystickMoveListener.onValueChanged(getAngle(), getPower(),
                        getDirection());
            }
        }
        if (onJoystickMoveListener != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
            thread = new Thread(this);
            thread.start();
            if (onJoystickMoveListener != null) {
                onJoystickMoveListener.onValueChanged(getAngle(), getPower(),
                        getDirection());
            }
        }
        return true;
    }

    private int getAngle() {
        if (xPosish > centerX) {
            if (yPosish < centerY) {
                return lastAngle = (int) (Math.atan(yDiff / xDiff) * RAD + 90);
            } else if (yPosish > centerY) {
                return lastAngle = (int) (Math.atan(yDiff / xDiff) * RAD) + 90;
            } else {
                return lastAngle = 90;
            }
        } else if (xPosish < centerX) {
            if (yPosish < centerY) {
                return lastAngle = (int) (Math.atan(yDiff / xDiff) * RAD - 90);
            } else if (yPosish > centerY) {
                return lastAngle = (int) (Math.atan(yDiff / xDiff) * RAD) - 90;
            } else {
                return lastAngle = -90;
            }
        } else {
            if (yPosish <= centerY) {
                return lastAngle = 0;
            } else {
                if (lastAngle < 0) {
                    return lastAngle = -180;
                } else {
                    return lastAngle = 180;
                }
            }
        }
    }

    private int getPower() {
        return (int) (100 * Math.sqrt(xDiff * xDiff + yDiff * yDiff) / baseRadius);
    }


    private int getDirection() {
        if (lastPower == 0 && lastAngle == 0) {
            return 0;
        }
        int a = 0;
        if (lastAngle <= 0) {
            a = (lastAngle * -1) + 90;
        } else if (lastAngle > 0) {
            if (lastAngle <= 90) {
                a = 90 - lastAngle;
            } else {
                a = 360 - (lastAngle - 90);
            }
        }

        int direction = (int) (((a + 22) / 45) + 1);

        if (direction > 8) {
            direction = 1;
        }
        return direction;
    }

    public void setOnJoystickMoveListener(OnJoystickMoveListener listener,
                                          long repeatInterval) {
        this.onJoystickMoveListener = listener;
        this.loopInterval = repeatInterval;
    }

    public interface OnJoystickMoveListener {
        public void onValueChanged(int angle, int power, int direction);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            post(new Runnable() {
                public void run() {
                    if (onJoystickMoveListener != null)
                        onJoystickMoveListener.onValueChanged(getAngle(),
                                getPower(), getDirection());
                }
            });
            try {
                Thread.sleep(loopInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}