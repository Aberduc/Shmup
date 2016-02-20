package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by serriec on 20/02/2016.
 */
public class Circle implements Drawable{
    private float x;
    private float y;
    private float radius;
    private int color;

    public Circle(float x, float y, float radius, int color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
    }
}
