package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by serriec on 20/02/2016.
 */
public abstract class Drawable {
    private float x;
    private float y;

    public Drawable(){
        this.x = 0;
        this.y = 0;
    }

    abstract void draw(Canvas canvas, Paint paint, int screenX, int screenY);

    public float getX(){
        return x;
    }

    float getY(){
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
