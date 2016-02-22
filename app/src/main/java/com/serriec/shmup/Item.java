package com.serriec.shmup;

/**
 * Created by serriec on 20/02/2016.
 */
public abstract class Item extends Drawable {
    abstract void update(long fps);

    abstract void reset();

}
