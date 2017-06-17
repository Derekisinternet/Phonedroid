package com.derekco.phonedroid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Mastermind on 6/15/17.
 */

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback {
    public JoystickView(Context context) {
        super(context);
    }

    public JoystickView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public JoystickView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
    }

    @Override
    void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    void surfaceDestroyed(SurfaceHolder holder) {

    }


}
