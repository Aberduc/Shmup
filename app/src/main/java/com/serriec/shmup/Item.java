package com.serriec.shmup;

/**
 * Created by serriec on 20/02/2016.
 */
public interface Item extends Drawable {
    void update(long fps);

    void reset();

}
