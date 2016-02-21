package com.serriec.shmup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class SchmupActivity extends Activity {

    SchmupView schmupView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        schmupView = new SchmupView(this, size.x, size.y);
        setContentView(schmupView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        schmupView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        schmupView.pause();
    }


}
