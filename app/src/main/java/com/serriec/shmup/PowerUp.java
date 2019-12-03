package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;


public abstract class PowerUp extends Item {
    private boolean isActive;
    private int screenX;
    private int screenY;
    private android.graphics.drawable.Drawable icon;

    public PowerUp(android.graphics.drawable.Drawable icon, int screenX, int screenY) {
        super();
        hitbox = new Circle(getX(), getY(), 0, Color.argb(255, 0, 255, 0));
        this.screenX = screenX;
        this.screenY = screenY;
        this.icon = icon;
        isActive = false;
    }

    @Override
    void update(long fps) {

    }

    @Override
    void reset() {
        isActive = true;
        Random generator = new Random();
        setRadius(screenX / 23);

        float newX = getX();
        float newY = getY();

        while((getX() - newX) * (getX() - newX) + (getY() - newY) * (getY() - newY) < (4 * getRadius()) * (4 * getRadius())){
            newX = 2 * getRadius() + generator.nextFloat() * (screenX - 4 * getRadius());
            newY = 2 * getRadius() + generator.nextFloat() * (screenY - 4 * getRadius());
        }

        setX(newX);
        setY(newY);

    }

    public boolean isActive() {
        return isActive;
    }

    public void setInactive() {
        isActive = false;
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        hitbox.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        hitbox.setY(y);
    }

    public float getRadius() {
        return hitbox.getRadius();
    }

    public void setRadius(float radius) {
        hitbox.setRadius(radius);
    }

    public void setColor(int color) {
        hitbox.setColor(color);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        hitbox.draw(canvas, paint, screenX, screenY);
        icon.setBounds((int) (getX() - getRadius()), (int) (getY() - getRadius()), (int) (getX() + getRadius()), (int) (getY() + getRadius()));
        icon.draw(canvas);
    }


}
