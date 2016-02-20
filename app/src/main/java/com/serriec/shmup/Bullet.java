package com.serriec.shmup;

import android.graphics.RectF;

/**
 * Created by serriec on 19/02/2016.
 */
public class Bullet {
    private float x;
    private float y;
    //y = ax+b
    private float a;
    private float b;
    private final int LEFT = -1;//down
    private final int RIGHT = 1;//up
    private int heading = 0;
    private boolean vertical;
    private int radius;
    private float speed;

    private boolean isActive;

    public Bullet(int screenX, int screenY) {
        speed = screenY/2;
        radius = 6;
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getRadius(){
        return radius;
    }

    public void setInactive() {
        isActive = false;
    }

    public boolean shoot(float startX, float startY, float goalX, float goalY) {
        //if (!isActive) {
            x = startX;
            y = startY;
            if (x != goalX) {
                vertical = false;
                a = (y - goalY) / (x - goalX);
                b = y - a * x;
                if (x < goalX) {
                    heading = RIGHT;
                } else {
                    heading = LEFT;
                }
            } else if (y != goalY) {
                vertical = true;
                if (y < goalY) {
                    heading = RIGHT;
                } else {
                    heading = LEFT;
                }
            } else {
                vertical = true;
                heading = RIGHT;
            }
            heading = - heading;
            isActive = true;
            return true;
        //}
        //return false;
    }

    public void update(long fps) {
        float d = speed / fps;
        if (vertical) {
            y = y + heading * d;
        } else {
            x = x + heading * d / ((float) Math.sqrt(1 + a * a));
            y = a * x + b;
        }

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
