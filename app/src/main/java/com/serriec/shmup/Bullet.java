package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bullet extends MovingLine {
    private boolean isActive;

    public Bullet(int screenX, int screenY) {
        super(Color.argb(255, 255, 255, 255));
        this.setSpeed(screenY / 2);
        setX(0);
        setY(0);
        setRadius(6);
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setInactive() {
        isActive = false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void reset(float startX, float startY, float goalX, float goalY) {
        super.reset(startX, startY, goalX, goalY);
        isActive = true;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        if (isActive) {
            super.draw(canvas, paint, screenX, screenY);
        }
    }

    @Override
    public void update(long fps) {
        if (isActive) {
            super.update(fps);
        }
    }
}
