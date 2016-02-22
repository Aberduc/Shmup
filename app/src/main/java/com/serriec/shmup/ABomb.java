package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class ABomb extends Still {
    private boolean isActive;
    private boolean activated;
    private int screenX;
    private int screenY;
    private float speedAction;

    public ABomb(int screenX, int screenY) {
        super(Color.argb(255, 249, 129, 0));
        this.screenX = screenX;
        this.screenY = screenY;
        speedAction = screenX/1.5f;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isActivated() {
        return activated;
    }

    public void activate() {
        isActive = false;
        activated = true;
    }

    public void setInactive(){
        isActive = false;
    }

    @Override
    public void update(long fps) {
        if(activated){
            if(getRadius()< Math.max(screenX, screenX)) {
                setRadius(getRadius()+speedAction/fps);
            }
            else {
                activated = false;
            }
        }
    }

    @Override
    public void reset() {
        isActive = true;
        Random generator = new Random();
        setRadius(screenX/23);
        setX(generator.nextFloat() * screenX);
        setY(generator.nextFloat() * screenY);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        if (isActive || activated) {
            super.draw(canvas, paint, screenX, screenY);
        }
    }
}
