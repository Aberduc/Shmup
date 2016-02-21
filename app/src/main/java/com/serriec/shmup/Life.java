package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by serriec on 21/02/2016.
 */
public class Life implements Item {
    private Circle circle;
    private boolean isActive;
    private int screenX;
    private int screenY;

    public Life(int screenX, int screenY) {
        circle = new Circle(0, 0, screenX / 23, Color.argb(255, 0, 255, 0));
        this.screenX = screenX;
        this.screenY = screenY;
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setInactive() {
        isActive = false;
    }

    @Override
    public void update(long fps) {

    }

    @Override
    public void reset() {
        isActive = true;
        Random generator = new Random();
        circle.setX(generator.nextFloat() * screenX);
        circle.setY(generator.nextFloat() * screenY);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        if (isActive) {
            circle.draw(canvas, paint, screenX, screenY);
            paint.setColor(Color.argb(255, 0, 0, 0));
            paint.setTextSize(circle.getRadius());
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("+1", this.getX(), this.getY() + circle.getRadius() * 3 / 8, paint);
        }
    }

    public float getX() {
        return circle.getX();
    }

    public float getY() {
        return circle.getY();
    }
}
