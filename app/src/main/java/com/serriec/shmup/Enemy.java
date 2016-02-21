package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Enemy extends MovingGoal {
    private boolean isActive;
    private int lives;
    private int screenX;
    private int screenY;


    public Enemy(int screenX, int screenY) {
        super(Color.argb(255, 255, 0, 0));
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setInactive() {
        isActive = false;
    }

    public void shot() {
        lives--;
        if (lives <= 0) {
            isActive = false;
        } else {
            setRadius(screenX / 30 * ((float) Math.sqrt(lives)));
            this.setSpeed(screenY / 6 * ((float) Math.pow(1.1, lives - 1)));
        }
    }

    public static void mergeEnemies(Enemy e1, Enemy e2) {
        if (e1.isActive && e2.isActive) {
            e1.lives = e1.lives + e2.lives;
            e2.isActive = false;
            e1.setRadius(e1.screenX / 30 * ((float) Math.sqrt(e1.lives)));
            e1.setSpeed(e1.screenY / 6 * ((float) Math.pow(1.1, e1.lives - 1)));
        }
    }

    @Override
    public void reset() {
        if (!isActive) {
            Random generator = new Random();
            int topBottomLeftRight = generator.nextInt(4);
            if (topBottomLeftRight == 0) {
                this.setX(generator.nextFloat() * screenX);
                this.setY(0);
            } else if (topBottomLeftRight == 1) {
                this.setX(generator.nextFloat() * screenX);
                this.setY(screenY);
            } else if (topBottomLeftRight == 2) {
                this.setX(0);
                this.setY(generator.nextFloat() * screenY);
            } else if (topBottomLeftRight == 3) {
                this.setX(screenX);
                this.setY(generator.nextFloat() * screenY);
            }
            lives = 1;
            isActive = true;
            setRadius(screenX / 30);
            this.setSpeed(screenY / 6);
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        if (isActive) {
            super.draw(canvas, paint, screenX, screenY);
            if (lives > 1) {
                paint.setColor(Color.argb(255, 0, 0, 0));
                paint.setTextSize(getRadius());
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(String.valueOf(lives), this.getX(), this.getY() + getRadius() * 3 / 8, paint);
            }
        }
    }
}
