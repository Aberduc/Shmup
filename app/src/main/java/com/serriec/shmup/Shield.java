package com.serriec.shmup;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Corentin Serrie on 24/10/2017.
 */

public class Shield extends PowerUp {
    public Shield(Resources res, int screenX, int screenY) {
        super(res.getDrawable(R.drawable.shield), screenX, screenY);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int screenX, int screenY) {
        if (isActive()) {
            super.draw(canvas, paint, screenX, screenY);
        }
    }
}
