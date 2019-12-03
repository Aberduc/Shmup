package com.serriec.shmup;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;


public class Life extends PowerUp {

    public Life(Resources res, int screenX, int screenY) {
        super(res.getDrawable(R.drawable.life), screenX, screenY);
    }

    @Override
    public void update(long fps) {

    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        if (isActive()) {
            super.draw(canvas, paint, screenX, screenY);
        }
    }
}
