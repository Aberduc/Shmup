package com.serriec.shmup;

public abstract class MovingGoal extends Moving implements Item{
    private float goalX;
    private float goalY;

    public MovingGoal(int color) {
        super(color);
    }

    @Override
    public void update(long fps) {
        float d = getSpeed() / fps;
        if (d < (float) Math.sqrt((getX() - goalX) * (getX() - goalX) + (getY() - goalY) * (getY() - goalY))) {
            if (getX() != goalX) {
                float a = (getY() - goalY) / (getX() - goalX);
                float b = getY() - a * getX();
                if (getX() < goalX) {
                    setX(getX() + d / ((float) Math.sqrt(1 + a * a)));
                    setY(a * getX() + b);
                } else {
                    setX(getX() - d / ((float) Math.sqrt(1 + a * a)));
                    setY(a * getX() + b);
                }
            } else if (getY() != goalY) {
                if (getY() < goalY) {
                    setY(getY() + d);
                } else {
                    setY(getY() - d);
                }
            }
        } else {
            setX(goalX);
            setY(goalY);
        }
    }

    public void setGoal(float x, float y){
        this.goalX = x;
        this.goalY = y;
    }
}
