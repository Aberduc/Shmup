package com.serriec.shmup;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.Settings;
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
    private boolean startScreen;

    private Canvas canvas;
    private Paint paint;

    private long fps;
    private long timeThisFrame;

    private int screenX;
    private int screenY;

    private PlayerSpaceShip spaceShip;

    private Bullet[] playerBullets;
    private int lastPlayerBulletShot;

    private long timeBetweenTwoShots = 50;
    private long timeLastShot;

    private Enemy[] enemies;
    private int lastEnemy;

    private long timeBetweenTwoEnemies;
    private long timeLastEnemy;

    private long lastTimeLevelUp;
    private long timeBetweenLevelUp;

    private int lastScoreLife;
    private Life life;

    private int lastScoreABomb;
    private ABomb aBomb;

    private int lastScorePiercingBullet;
    private PiercingBullet piercingBullet;

    private int lastScoreShield;
    private Shield shield;

    private int score;

    private int highScore;
    SharedPreferences settings;


    public static final String PREFS_NAME = "SCHMUP_SHARED_PREFS";
    public static final String HIGHSCORE = "HIGHSCORE";

    public SchmupView(Context context, int x, int y) {
        super(context);
        this.context = context;

        settings = context.getSharedPreferences(PREFS_NAME, 0);

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        spaceShip = new PlayerSpaceShip(screenX, screenY);
        playerBullets = new Bullet[50];
        enemies = new Enemy[100];

        Resources res = getResources();
        life = new Life(res, screenX, screenY);
        aBomb = new ABomb(res, screenX, screenY);
        piercingBullet = new PiercingBullet(res, screenX, screenY);
        shield = new Shield(res, screenX, screenY);

        highScore = settings.getInt(HIGHSCORE, 0);
        for (int i = 0; i < playerBullets.length; i++) {
            playerBullets[i] = new Bullet(screenX, screenY);
        }
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy(screenX, screenY);
        }

        startScreen = true;

        prepareLevel();
    }

    private void prepareLevel() {
        for (int i = 0; i < playerBullets.length; i++) {
            playerBullets[i].setInactive();
        }
        for (int i = 0; i < enemies.length; i++) {
            enemies[i].setInactive();
        }

        spaceShip.reset();

        lastPlayerBulletShot = 0;
        timeLastShot = System.currentTimeMillis();

        lastEnemy = 0;
        timeBetweenTwoEnemies = 500;
        timeLastEnemy = System.currentTimeMillis();

        timeBetweenLevelUp = 4000;
        lastTimeLevelUp = System.currentTimeMillis();
        score = 0;

        Resources res = getResources();

        life.setInactive();
        lastScoreLife = res.getInteger(R.integer.lifeFirstPop) - res.getInteger(R.integer.lifeDelay);;

        aBomb.setInactive();
        lastScoreABomb = res.getInteger(R.integer.bombFirstPop) - res.getInteger(R.integer.bombDelay);;

        piercingBullet.setInactive();
        lastScorePiercingBullet = res.getInteger(R.integer.piercingBulletFirstPop) - res.getInteger(R.integer.piercingBulletDelay);;

        shield.setInactive();
        lastScoreShield = res.getInteger(R.integer.shieldFirstPop) - res.getInteger(R.integer.shieldDelay);;
    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();

            if (!paused) {
                update();
            } else {
                spaceShip.pause(timeThisFrame);
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

            aBomb.draw(canvas, paint, screenX, screenY);
            life.draw(canvas, paint, screenX, screenY);
            piercingBullet.draw(canvas, paint, screenX, screenY);
            shield.draw(canvas, paint, screenX, screenY);

            for (int i = 0; i < playerBullets.length; i++) {
                playerBullets[i].draw(canvas, paint, screenX, screenY);
            }

            for (int i = 0; i < enemies.length; i++) {
                enemies[i].draw(canvas, paint, screenX, screenY);
            }

            spaceShip.draw(canvas, paint, screenX, screenY);

            if(paused){
                canvas.drawColor(Color.argb(150, 0, 0, 0));

                if(startScreen){
                    paint.setColor(Color.argb(255, 249, 129, 0));
                    paint.setTextSize(screenX/13);
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText("Touch the screen to play !", screenX/2, screenY/3, paint);

                    paint.setColor(Color.argb(255, 255, 255, 255));
                    paint.setTextSize(screenX/18);
                    canvas.drawText("Direct the white circle with your finger,", screenX/2, 2*screenY/3, paint);
                    canvas.drawText("Avoid and shoot your enemies,", screenX/2, 2*screenY/3 + screenY/20, paint);
                    canvas.drawText("And try to survive as long as possible !", screenX/2, 2*screenY/3 + 2*screenY/20, paint);

                }
                else if (spaceShip.getLives() <= 0) {
                    paint.setTextAlign(Paint.Align.CENTER);

                    if (score >= highScore) {
                        paint.setColor(Color.argb(255, 249, 129, 0));
                        paint.setTextSize(screenX/9);
                        canvas.drawText("NEW HIGH SCORE !"  , screenX / 2, screenY / 2, paint);

                        paint.setColor(Color.argb(255, 255, 255, 255));
                        paint.setTextSize(screenX/10);
                        canvas.drawText("Score : " + highScore, screenX / 2, screenY / 2 + screenY/15, paint);
                    } else {
                        paint.setColor(Color.argb(255, 255, 0, 0));
                        paint.setTextSize(screenX/6);
                        canvas.drawText("YOU LOST", screenX / 2, screenY / 2, paint);

                        paint.setColor(Color.argb(255, 249, 129, 0));
                        paint.setTextSize(screenX/10);
                        canvas.drawText("High Score : " + highScore, screenX / 2, screenY / 2 + screenY/15, paint);

                        paint.setColor(Color.argb(255, 255, 255, 255));
                        paint.setTextSize(screenX/12);
                        canvas.drawText("Your Score : " + score, screenX / 2, screenY / 2 + screenY/19 + screenY/15, paint);
                    }
                    paint.setTextAlign(Paint.Align.LEFT);
                }
                else {
                    //normal pause
                    paint.setColor(Color.argb(255, 255, 255, 255));
                    paint.setTextSize(screenX/10);
                    canvas.drawText("Touch the screen to", screenX/2, screenY/2, paint);
                    canvas.drawText("resume playing !", screenX/2, screenY/2 + paint.getTextSize(), paint);



                    paint.setColor(Color.argb(255, 255, 255, 255));
                    paint.setTextSize(screenX/16);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(" Score " + score, 0, screenY/30, paint);
                }
            } else {
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(screenX/16);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(" Score " + score, 0, screenY/30, paint);

            }


            ourHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void update() {
        spaceShip.update(fps);

        Resources res = getResources();

        if (System.currentTimeMillis() - lastTimeLevelUp > timeBetweenLevelUp) {
            timeBetweenTwoEnemies = timeBetweenTwoEnemies * 19 / 20;
            spaceShip.upSpeed();
            lastTimeLevelUp = System.currentTimeMillis();
        }

        //life
        if (score >= lastScoreLife + res.getInteger(R.integer.lifeDelay) && !life.isActive()) {
            life.reset();
            lastScoreLife = score;
        }

        if (life.isActive()) {
            if (spaceShip.hardCollided(life)) {
                life.setInactive();
                spaceShip.upLives();
            }
        }

        //bomb
        if (score >= lastScoreABomb + res.getInteger(R.integer.bombDelay) && !aBomb.isActive()) {
            aBomb.reset();
            lastScoreABomb = score;
        }

        if (aBomb.isActive()) {
            if (spaceShip.hardCollided(aBomb)) {
                aBomb.activate();
            }
        }

        aBomb.update(fps);

        //piercing bullets
        if (score >= lastScorePiercingBullet + res.getInteger(R.integer.piercingBulletDelay) && !piercingBullet.isActive()) {
            piercingBullet.reset();
            lastScorePiercingBullet = score;
        }

        if (piercingBullet.isActive()) {
            if (spaceShip.hardCollided(piercingBullet)) {
                piercingBullet.setInactive();
                spaceShip.upPiercingBullets();
            }
        }

        //shield
        if (score >= lastScoreShield + res.getInteger(R.integer.shieldDelay) && !shield.isActive()) {
            shield.reset();
            lastScoreShield = score;
        }

        if (shield.isActive()) {
            if (spaceShip.hardCollided(shield)) {
                shield.setInactive();
                spaceShip.upShield();
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
                if (enemies[i].softCollided((spaceShip))) {
                    enemies[i].setInactive();
                    spaceShip.shot();
                    if (spaceShip.getLives() <= 0) {
                        paused = true;
                        if(score > highScore){
                            highScore = score;
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt(HIGHSCORE, score);
                            editor.commit();

                        }
                    }
                }

                if(aBomb.isActivated()) {
                    if (enemies[i].softCollided(aBomb)) {
                        enemies[i].setInactive();
                    }
                }

                for (int j = 0; j < playerBullets.length; j++) {
                    if (playerBullets[j].isActive()) {
                        if (enemies[i].hardCollided(playerBullets[j])) {
                            enemies[i].shot();
                            playerBullets[j].hit();
                            score = score + 10;
                        }
                    }
                }
                for (int j = 0; j < enemies.length; j++) {
                    if (j != i && enemies[i].softCollided(enemies[j])) {
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
                    playerBullets[lastPlayerBulletShot].reset(spaceShip.getX(), spaceShip.getY(), motionEvent.getX(), motionEvent.getY(), spaceShip.getBulletsPiercing());
                    timeLastShot = System.currentTimeMillis();
                    if (lastPlayerBulletShot < playerBullets.length - 1) {
                        lastPlayerBulletShot++;
                    } else {
                        lastPlayerBulletShot = 0;
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                startScreen = false;
                if (paused) {
                    if (spaceShip.getLives() <= 0) {
                        this.prepareLevel();
                    }
                    paused = false;
                }
                spaceShip.setGoal(motionEvent.getX(), motionEvent.getY());
                if (System.currentTimeMillis() - timeLastShot > timeBetweenTwoShots) {
                    playerBullets[lastPlayerBulletShot].reset(spaceShip.getX(), spaceShip.getY(), motionEvent.getX(), motionEvent.getY(), spaceShip.getBulletsPiercing());
                    timeLastShot = System.currentTimeMillis();
                    if (lastPlayerBulletShot < playerBullets.length - 1) {
                        lastPlayerBulletShot++;
                    } else {
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
}
