package com.serriec.shmup;

import android.graphics.RectF;

import java.util.Random;

/**
 * Created by serriec on 19/02/2016.
 */
public class Enemy {
    private float radius;
    private float speed;
    private float goalX;
    private float goalY;
    private float x;
    private float y;
    private int screenX;
    private int screenY;
    private boolean isActive;
    private int lives;


    public Enemy(int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public float getRadius() {
        return radius;
    }

    public boolean restartEnemy(){
        if(!isActive) {
            Random generator = new Random();
            int topBottomLeftRight = generator.nextInt(4);
            if (topBottomLeftRight == 0) {
                this.x = generator.nextFloat() * screenX;
                this.y = 0;
            } else if (topBottomLeftRight == 1) {
                this.x = generator.nextFloat() * screenX;
                this.y = screenY;
            } else if (topBottomLeftRight == 2) {
                this.x = 0;
                this.y = generator.nextFloat() * screenY;
            } else if (topBottomLeftRight == 3) {
                this.x = screenX;
                this.y = generator.nextFloat() * screenY;
            }
            lives = 1;
            isActive = true;
            radius = screenX/30;
            speed = screenY/6;
            return true;
        }
        return false;
    }

    public void update(long fps, float goalX, float goalY) {
        float d = speed / fps;
        this.goalX = goalX;
        this.goalY = goalY;
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

    public  boolean isActive() {
        return isActive;
    }

    public void setInactive() {
        isActive = false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void shot() {
        lives--;
        if (lives <= 0) {
            isActive = false;
        }
        else {
            radius = screenX/30 * ((float)Math.sqrt(lives));
            speed = screenY/6 * ((float) Math.pow(1.1, lives -1));
        }
    }

    public int getLives() {
        return lives;
    }

    public static void mergeEnemies (Enemy e1, Enemy e2) {
        if(e1.isActive && e2.isActive) {
            e1.lives = e1.lives + e2.lives;
            e2.isActive = false;
            e1.radius = e1.screenX/30 * ((float)Math.sqrt(e1.lives));
            e1.speed = e1.screenY/6 * ((float) Math.pow(1.1, e1.lives -1));
        }
    }

}
