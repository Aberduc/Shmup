package com.serriec.shmup;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Debug;
import android.util.Log;

/**
 * Created by serriec on 20/02/2016.
 */
public abstract class Moving extends Item {
    private float speed;

    private float lastX;
    private float lastY;

    public Moving(int color) {
        super();
        hitbox = new Circle(0, 0, 0, color);
    }

    @Override
    public void setX(float x) {
        lastX = super.getX();

        super.setX(x);
        hitbox.setX(x);
    }

    @Override
    public void setY(float y) {
        lastY = super.getY();

        super.setY(y);
        hitbox.setY(y);
    }

    //the center of this item entered in the other item's hitbox or vice versa
    public boolean softCollided(Item item){
        float deltaX = lastX - getX();
        float deltaY = lastY - getY();

        float distSquared;

        if (deltaX == 0 && deltaY == 0){
            distSquared = (getX() - item.getX()) * (getX() - item.getX()) + (getY() - item.getY()) * (getY() - item.getY());
        }
        else {
            float ux=item.getX()-getX();
            float uy=item.getY()-getY();

            float dp=deltaX*ux+deltaY*uy;
            if (dp<0){
                distSquared = (getX() - item.getX()) * (getX() - item.getX()) + (getY() - item.getY()) * (getY() - item.getY());
            }
            else {

                float sn2 = deltaX*deltaX+deltaY*deltaY;
                if (dp>sn2){
                    distSquared = (lastX - item.getX()) * (lastX - item.getX()) + (lastY - item.getY()) * (lastY - item.getY());
                }
                else{
                    float ah2 = dp*dp / sn2;
                    float un2=ux*ux+uy*uy;

                    distSquared = un2-ah2;
                }
            }
        }

        return (distSquared < Math.max(item.hitbox.getRadius(), hitbox.getRadius()) * Math.max(item.hitbox.getRadius(), hitbox.getRadius()));
    }

    //both item's hitboxes collided
    public boolean hardCollided(Item item){
        float deltaX = lastX - getX();
        float deltaY = lastY - getY();

        float distSquared;

        if (deltaX == 0 && deltaY == 0){
            distSquared = (getX() - item.getX()) * (getX() - item.getX()) + (getY() - item.getY()) * (getY() - item.getY());
        }
        else {
            float ux=item.getX()-getX();
            float uy=item.getY()-getY();

            float dp=deltaX*ux+deltaY*uy;
            if (dp<0){
                distSquared = (getX() - item.getX()) * (getX() - item.getX()) + (getY() - item.getY()) * (getY() - item.getY());
            }
            else {

                float sn2 = deltaX*deltaX+deltaY*deltaY;
                if (dp>sn2){
                    distSquared = (lastX - item.getX()) * (lastX - item.getX()) + (lastY - item.getY()) * (lastY - item.getY());
                }
                else{
                    float ah2 = dp*dp / sn2;
                    float un2=ux*ux+uy*uy;

                    distSquared = un2-ah2;
                }
            }
        }

        return (distSquared < (item.hitbox.getRadius() + hitbox.getRadius()) * (item.hitbox.getRadius() + hitbox.getRadius()));
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getRadius() {
        return hitbox.getRadius();
    }

    public void setRadius(float radius) {
        hitbox.setRadius(radius);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        hitbox.draw(canvas, paint, screenX, screenY);
    }
}
