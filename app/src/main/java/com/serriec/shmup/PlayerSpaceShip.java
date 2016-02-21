package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PlayerSpaceShip extends MovingGoal {
    private float baseX;
    private float baseY;
    private float baseSpeed;
    private float baseRadius;
    private int lives;

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
    }

    @Override
    public void reset() {
        this.setX(baseX);
        this.setY(baseY);
        this.setSpeed(baseSpeed);
        this.setRadius(baseRadius);
        this.lives = 3;
    }

    public void upSpeed() {
        this.setSpeed(this.getSpeed() * 1.1f);
    }

    public void shot() {
        lives--;
        setRadius(getRadius() * 0.7f);
    }

    public int getLives() {
        return lives;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        super.draw(canvas, paint, screenX, screenY);

        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setTextSize(getRadius());
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(lives), this.getX(), this.getY() + getRadius() / 2, paint);

    }

}
