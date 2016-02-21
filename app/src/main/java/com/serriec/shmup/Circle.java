package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Paint;

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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
