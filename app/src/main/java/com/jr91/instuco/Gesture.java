package com.jr91.instuco;

import android.graphics.Matrix;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * Created by Usuario on 9/12/16.
 */

class Gesture extends ScaleGestureDetector.
        SimpleOnScaleGestureListener {

    private float scale = 1f;
    private Matrix matrix = new Matrix();
    ImageView iv;

    Gesture(ImageView i) {
        this.iv = i;
    }

    public boolean onScale() {
        //scale *= detector.getScaleFactor();
        scale = Math.max(0.1f, Math.min(scale, 5.0f));
        matrix.setScale(scale, scale);
        iv.setImageMatrix(matrix);
        return true;
    }
}