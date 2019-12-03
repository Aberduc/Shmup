package com.serriec.shmup;

import android.animation.TimeAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.provider.Settings;

public class PlayerSpaceShip extends MovingGoal {
    private float baseX;
    private float baseY;
    private float baseSpeed;
    private float baseRadius;
    private int lives;
    private int bulletsPiercing;
    private int shield;

    private long lastTimeShieldProcced;
    private boolean shieldUp;

    private RectF shieldOval;

    public PlayerSpaceShip(int screenX, int screenY) {
        super(Color.argb(255, 255, 255, 255));
        baseX = screenX / 2;
        baseY = screenY / 2;
        baseSpeed = screenX / 3.5f;
        baseRadius = screenX / 15;
        setX(baseX);
        setY(baseY);
        setRadius(baseRadius);
        this.setSpeed(baseSpeed);

        shieldOval = new RectF();
    }

    @Override
    public void reset() {
        this.setX(baseX);
        this.setY(baseY);
        this.setSpeed(baseSpeed);
        this.setRadius(baseRadius);
        this.lives = 3;
        this.bulletsPiercing = 1;
        this.shield = 0;
        shieldUp = false;
        lastTimeShieldProcced = System.currentTimeMillis();
    }

    public void upSpeed() {
        setSpeed(this.getSpeed() * 1.1f);
    }

    public void shot() {
        if (shieldUp) {
            shieldUp = false;
            lastTimeShieldProcced = System.currentTimeMillis();
        } else {
            lives--;
            setRadius(getRadius() * 0.7f);
        }
    }

    public int getLives() {
        return lives;
    }

    public int getBulletsPiercing() {
        return bulletsPiercing;
    }

    public void pause(long timeThisFrame){
        lastTimeShieldProcced += timeThisFrame;
    }

    public int getShield() {
        return shield;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        if (!shieldUp && shield > 0) {
            float sweepAngle = (System.currentTimeMillis() - lastTimeShieldProcced) * shield * 360 / 30000;
            shieldOval.set(getX() - getRadius() - 10, getY() - getRadius() - 10, getX() + getRadius() + 10, getY() + getRadius() + 10);
            paint.setColor(Color.argb(255, 198, 234, 242));
            canvas.drawArc(shieldOval, 270, sweepAngle, true, paint);
        }
        super.draw(canvas, paint, screenX, screenY);

        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setTextSize(getRadius());
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(lives), this.getX(), this.getY() + getRadius() * 3 / 8, paint);

        if (shieldUp) {
            paint.setColor(Color.argb(150, 198, 234, 242));
            canvas.drawCircle(getX(), getY(), getRadius() + 10, paint);
        }

    }

    @Override
    public void update(long fps) {
        super.update(fps);

        if (!shieldUp && shield > 0) {
            if (System.currentTimeMillis() - lastTimeShieldProcced > (30000 / shield)) {
                shieldUp = true;
            }
        }

    }

    public void upLives() {
        lives++;
        setRadius(getRadius() * 10f / 7f);
    }

    public void upShield() {
        shield++;
        shieldUp = true;
    }

    public void upPiercingBullets() {
        bulletsPiercing++;
    }

}
