package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Paint;


public abstract class Still extends Item {
    private Circle circle;

    public Still(int color) {
        super();
        circle = new Circle(getX(), getY(), 0, color);
    }

    @Override
    void update(long fps) {

    }

    @Override
    void reset() {

    }

    @Override
    public void setX(float x) {
        super.setX(x);
        circle.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        circle.setY(y);
    }

    public float getRadius() {
        return circle.getRadius();
    }

    public void setRadius(float radius) {
        circle.setRadius(radius);
    }

    public void setColor(int color) {
        circle.setColor(color);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        circle.draw(canvas, paint, screenX, screenY);
    }
}
