package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by serriec on 20/02/2016.
 */
public interface Drawable {
    void draw(Canvas canvas, Paint paint, int screenX, int screenY);
}
