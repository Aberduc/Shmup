package com.serriec.shmup;

import android.graphics.RectF;

/**
 * Created by serriec on 18/02/2016.
 */
public class PlayerSpaceShip {
    private float radius;
    private float speed;
    private float goalX;
    private float goalY;
    private float x;
    private float y;


    public PlayerSpaceShip(int screenX, int screenY) {
        radius = screenX/15;
        speed = screenY/3.5f;
        x = screenX / 2;
        y = screenY / 2;
    }

    public float getRadius() {
        return radius;
    }

    public void setGoal(float x, float y) {
        goalX = x;
        goalY = y;
    }

    public void update(long fps) {
        float d = speed / fps;
        if (d < (float) Math.sqrt((x - goalX) * (x - goalX) + (y - goalY) * (y - goalY))) {
            if (x != goalX) {
                float a = (y - goalY) / (x - goalX);
                float b = y - a * x;
                if (x < goalX) {
                    x = x + d / ((float) Math.sqrt(1 + a * a));
                    y = a * x + b;
                } else {
                    x = x - d / ((float) Math.sqrt(1 + a * a));
                    y = a * x + b;
                }
            } else if (y != goalY) {
                if (y < goalY) {
                    y = y + d;
                } else {
                    y = y - d;
                }
            }
        } else {
            x = goalX;
            y = goalY;
        }


    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void reset(int screenX, int screenY) {
        this.x = screenX/2;
        this.y = screenY/2;
        this.speed = screenY/3.5f;
    }

    public void upSpeed() {
        speed = speed *1.1f;
    }
}
