package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle extends Drawable {
    private float radius;
    private int color;

    public Circle(float x, float y, float radius, int color) {
        super();
        this.setX(x);
        this.setY(y);
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        paint.setColor(color);
        canvas.drawCircle(getX(), getY(), radius, paint);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
