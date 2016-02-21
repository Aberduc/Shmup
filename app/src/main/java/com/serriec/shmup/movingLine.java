package com.serriec.shmup;

/**
 * Created by serriec on 20/02/2016.
 */
public abstract class MovingLine extends Moving implements Item {
    private float a;
    private float b;
    private boolean vertical;
    private int heading;

    public final int RIGHT = 1;//top
    public final int LEFT = -1;//bot

    public MovingLine(int color) {
        super(color);
    }

    public void reset(float startX, float startY, float goalX, float goalY) {
        setX(startX);
        setY(startY);
        if (getX() != goalX) {
            vertical = false;
            a = (getY() - goalY) / (getX() - goalX);
            b = getY() - a * getX();
            if (getX() < goalX) {
                heading = RIGHT;
            } else {
                heading = LEFT;
            }
        } else if (getY() != goalY) {
            vertical = true;
            if (getY() < goalY) {
                heading = RIGHT;
            } else {
                heading = LEFT;
            }
        } else {
            vertical = true;
            heading = RIGHT;
        }
        heading = - heading;
    }

    @Override
    public void update(long fps) {
        float d = getSpeed() / fps;
        if (vertical) {
            setY(getY() + heading * d);
        } else {
            setX(getX() + heading * d / ((float) Math.sqrt(1 + a * a)));
            setY(a * getX() + b);
        }

    }
}
