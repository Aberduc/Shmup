package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.content.res.Resources;

import java.util.Random;

public class ABomb extends PowerUp {
    private boolean activated;
    private float speedAction;
    private int explosionRadius;

    public ABomb(Resources res, int screenX, int screenY) {
        super(res.getDrawable(R.drawable.explosion), screenX, screenY);
        speedAction = screenX/1.5f;
        explosionRadius = Math.max(screenX, screenY) / 2;
    }

    public boolean isActivated() {
        return activated;
    }

    public void activate() {
        activated = true;
    }

    @Override
    public void update(long fps) {
        if(activated){
            if(getRadius()< explosionRadius) {
                setRadius(getRadius()+speedAction/fps);
            }
            else {
                activated = false;
                setInactive();
            }
        }
    }

    @Override
    public void reset() {
        activated = false;
        super.reset();
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        if (isActive()) {
            if(activated){
                paint.setColor(Color.argb(255, 249, 129, 0));
                canvas.drawCircle(getX(), getY(), getRadius(), paint);
            }else {
                super.draw(canvas, paint, screenX, screenY);
            }
        }
    }
}
