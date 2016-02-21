package com.serriec.shmup;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class SchmupView extends SurfaceView implements Runnable {

    Context context;

    private Thread gameThread;

    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = true;

    private Canvas canvas;
    private Paint paint;

    private long fps;
    private long timeThisFrame;

    private int screenX;
    private int screenY;

    private PlayerSpaceShip spaceShip;

    private Bullet[] playerBullets;
    private int lastPlayerBulletShot;

    private long timeBetweenTwoShots;
    private long timeLastShot;

    private Enemy[] enemies;
    private int lastEnemy;

    private long timeBetweenTwoEnemies;
    private long timeLastEnemy;

    private int lastScoreLevelUp;

    private int lastScoreLife;
    private Life life;

    private int score;

    private int highScore;
    SharedPreferences settings;


    public static final String PREFS_NAME = "HighScore";

    public SchmupView(Context context, int x, int y) {
        super(context);
        this.context = context;

        settings = context.getSharedPreferences(PREFS_NAME, 0);

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        this.spaceShip = new PlayerSpaceShip(screenX, screenY);

        playerBullets = new Bullet[50];

        enemies = new Enemy[50];
        life = new Life(screenX, screenY);

        prepareLevel();
    }

    private void prepareLevel() {
        highScore = settings.getInt("highScore", 0);
        for (int i = 0; i < playerBullets.length; i++) {
            playerBullets[i] = new Bullet(screenX, screenY);
        }
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy(screenX, screenY);
        }

        spaceShip.reset();

        lastPlayerBulletShot = 0;
        timeBetweenTwoShots = 50;
        timeLastShot = System.currentTimeMillis();

        lastEnemy = 0;
        timeBetweenTwoEnemies = 500;
        timeLastEnemy = System.currentTimeMillis();

        lastScoreLevelUp = 0;
        score = 0;

        life.setInactive();
        lastScoreLife = 0;
    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();

            if (!paused) {
                update();
            }

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame > 0) {
                fps = 1000 / timeThisFrame;
            }

        }
    }

    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            life.draw(canvas, paint, screenX, screenY);

            for (int i = 0; i < playerBullets.length; i++) {
                playerBullets[i].draw(canvas, paint, screenX, screenY);
            }

            spaceShip.draw(canvas, paint, screenX, screenY);

            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(40);
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Lives " + spaceShip.getLives() + "   Score " + score, 10, 50, paint);


            for (int i = 0; i < enemies.length; i++) {
                enemies[i].draw(canvas, paint, screenX, screenY);
            }

            if (spaceShip.getLives() <= 0) {
                paint.setTextAlign(Paint.Align.CENTER);
                String text;

                if (score > highScore) {
                    paint.setColor(Color.argb(255, 249, 129, 0));
                    text = "NEW HIGH SCORE : " + score;
                    setTextSizeForWidth(paint, screenX, text, 90);
                    canvas.drawText(text, screenX / 2, screenY / 2, paint);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("highScore", score);
                    editor.commit();
                } else {
                    paint.setColor(Color.argb(255, 255, 0, 0));
                    text = "YOU LOST";
                    setTextSizeForWidth(paint, screenX, text, 90);
                    canvas.drawText(text, screenX / 2, screenY / 2, paint);
                    paint.setColor(Color.argb(255, 249, 129, 0));
                    text = "High Score : " + highScore;
                    setTextSizeForWidth(paint, screenX, text, 70);
                    canvas.drawText(text, screenX / 2, screenY / 2 + 110, paint);
                }
                paint.setTextAlign(Paint.Align.LEFT);
            }

            ourHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void update() {
        spaceShip.update(fps);

        if (score >= lastScoreLife + 500 && !life.isActive()) {
            life.reset();
            lastScoreLife = score;
        }
        if (life.isActive()) {
            if ((life.getX() - spaceShip.getX()) * (life.getX() - spaceShip.getX()) + (life.getY() - spaceShip.getY()) * (life.getY() - spaceShip.getY()) < (spaceShip.getRadius()) * (spaceShip.getRadius())) {
                life.setInactive();
                spaceShip.upLives();
            }
        }

        for (int i = 0; i < playerBullets.length; i++) {
            playerBullets[i].update(fps);
        }

        if (System.currentTimeMillis() - timeLastEnemy > timeBetweenTwoEnemies) {
            enemies[lastEnemy].reset();
            timeLastEnemy = System.currentTimeMillis();
            if (lastEnemy < enemies.length - 1) {
                lastEnemy++;
            } else {
                lastEnemy = 0;
            }
        }

        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].isActive()) {
                enemies[i].setGoal(spaceShip.getX(), spaceShip.getY());
                enemies[i].update(fps);
                if ((enemies[i].getX() - spaceShip.getX()) * (enemies[i].getX() - spaceShip.getX()) + (enemies[i].getY() - spaceShip.getY()) * (enemies[i].getY() - spaceShip.getY()) < (spaceShip.getRadius()) * (spaceShip.getRadius())) {
                    enemies[i].setInactive();
                    spaceShip.shot();
                    if (spaceShip.getLives() <= 0) {
                        paused = true;
                    }
                }
                for (int j = 0; j < playerBullets.length; j++) {
                    if (playerBullets[j].isActive()) {
                        if ((enemies[i].getX() - playerBullets[j].getX()) * (enemies[i].getX() - playerBullets[j].getX()) + (enemies[i].getY() - playerBullets[j].getY()) * (enemies[i].getY() - playerBullets[j].getY()) < (enemies[i].getRadius()) * (enemies[i].getRadius())) {
                            enemies[i].shot();
                            playerBullets[j].setInactive();
                            score = score + 10;
                            if (score >= lastScoreLevelUp + 100) {
                                timeBetweenTwoEnemies = timeBetweenTwoEnemies * 9 / 10;
                                spaceShip.upSpeed();
                                lastScoreLevelUp = score;
                            }

                        }
                    }
                }
                for (int j = 0; j < enemies.length; j++) {
                    if (j != i && ((enemies[i].getX() - enemies[j].getX()) * (enemies[i].getX() - enemies[j].getX()) + (enemies[i].getY() - enemies[j].getY()) * (enemies[i].getY() - enemies[j].getY())) < Math.max(enemies[i].getRadius(), enemies[j].getRadius()) * Math.max(enemies[i].getRadius(), enemies[j].getRadius())) {
                        Enemy.mergeEnemies(enemies[i], enemies[j]);
                    }
                }
            }
        }


    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();

    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("error", "joining thread");
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                spaceShip.setGoal(motionEvent.getX(), motionEvent.getY());
                if (System.currentTimeMillis() - timeLastShot > timeBetweenTwoShots) {
                    if (lastPlayerBulletShot < playerBullets.length - 1) {
                        playerBullets[lastPlayerBulletShot].reset(spaceShip.getX(), spaceShip.getY(), motionEvent.getX(), motionEvent.getY());
                        timeLastShot = System.currentTimeMillis();
                        lastPlayerBulletShot++;
                    } else {
                        playerBullets[lastPlayerBulletShot].reset(spaceShip.getX(), spaceShip.getY(), motionEvent.getX(), motionEvent.getY());
                        timeLastShot = System.currentTimeMillis();
                        lastPlayerBulletShot = 0;
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (paused) {
                    if (spaceShip.getLives() <= 0) {
                        this.prepareLevel();
                    }
                    paused = false;
                }
                spaceShip.setGoal(motionEvent.getX(), motionEvent.getY());
                if (System.currentTimeMillis() - timeLastShot > timeBetweenTwoShots) {
                    if (lastPlayerBulletShot < playerBullets.length - 1) {
                        playerBullets[lastPlayerBulletShot].reset(spaceShip.getX(), spaceShip.getY(), motionEvent.getX(), motionEvent.getY());
                        timeLastShot = System.currentTimeMillis();
                        lastPlayerBulletShot++;
                    } else {
                        playerBullets[lastPlayerBulletShot].reset(spaceShip.getX(), spaceShip.getY(), motionEvent.getX(), motionEvent.getY());
                        timeLastShot = System.currentTimeMillis();
                        lastPlayerBulletShot = 0;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                paused = true;
                break;
        }
        return true;
    }

    private static void setTextSizeForWidth(Paint paint, float maxWidth, String text, float maxSize) {
        final float testTextSize = 48;
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        float desiredTextSize = testTextSize * maxWidth / bounds.width();
        if (desiredTextSize > maxSize) {
            paint.setTextSize(maxSize);
        } else {
            paint.setTextSize(desiredTextSize);
        }

    }

}
