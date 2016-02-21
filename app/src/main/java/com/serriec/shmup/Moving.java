package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by serriec on 20/02/2016.
 */
public abstract class Moving implements Item {
    private  float x;
    private float y;
    private float speed;
    private Circle circle;

    public Moving(int color){
        circle = new Circle(0,0,0, color);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        circle.setX(x);
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        circle.setY(y);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getRadius(){
        return circle.getRadius();
    }

    public void setRadius (float radius){
        circle.setRadius(radius);
    }

    public void draw (Canvas canvas, Paint paint, int screenX, int screenY){
        circle.draw(canvas,paint,screenX,screenY);
    }
}
