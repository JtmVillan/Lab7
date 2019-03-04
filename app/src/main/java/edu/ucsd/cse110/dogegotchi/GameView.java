package edu.ucsd.cse110.dogegotchi;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.common.collect.ImmutableList;

import java.util.Collection;

import edu.ucsd.cse110.dogegotchi.daynightcycle.IDayNightCycleObserver;
import edu.ucsd.cse110.dogegotchi.sprite.ISprite;
import edu.ucsd.cse110.dogegotchi.ticker.ITickerObserver;

/**
 * Controller of the canvas on which all sprites are drawn.
 */
public class GameView
        extends SurfaceView
        implements SurfaceHolder.Callback, ITickerObserver, IDayNightCycleObserver {
    // background images
    Bitmap bg_day   = BitmapFactory.decodeResource(getResources(), R.drawable.doge_house_day_2x),
           bg_night = BitmapFactory.decodeResource(getResources(), R.drawable.doge_house_night_2x_test),
           bg = bg_day;

    boolean canvasCreated = false;

    MediaPlayer dayPlayer, nightPlayer;

    private Collection<ISprite> sprites;

    /**
     * @see SurfaceView#SurfaceView(Context, AttributeSet)
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        setFocusable(true);
    }

    public GameView(final Context context) {
        this(context, null);
    }

    /**
     * Set the background tunes that play during the day/night.
     */
    public void setMedia(final MediaPlayer dayPlayer, final MediaPlayer nightPlayer) {
        this.dayPlayer   = dayPlayer;
        this.nightPlayer = nightPlayer;
    }

    public void setSprites(final Collection<ISprite> sprites) {
        this.sprites = ImmutableList.copyOf(sprites);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) { canvasCreated = true; onTick(); }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { canvasCreated = false; }

    /**
     * Re-draws the game view with up-to-date states of different game entities.
     */
    public void onTick() {
        if (canvasCreated) {
            Canvas canvas = null;

            try {
                Log.i(this.getClass().getSimpleName(), ">\tAttempting to draw on canvas...");

                // 1. Acquire lock on the canvas (as multiple views will be writing to it)
                canvas = this.getHolder().lockCanvas();

                // 2. Update the canvas
                synchronized (this.getHolder()) {
                    Log.i(this.getClass().getSimpleName(), ">\tDrawing on canvas...");
                    this.draw(canvas);
                }
            } catch (Exception exception) {
                Log.e(this.getClass().getSimpleName(),
                        "🛑 Failed to draw on canvas: " + exception.getMessage(), exception);
            } finally {
                if (canvas != null) {
                    try {
                        // 3. Release the lock and update the canvas
                        this.getHolder().unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.i(this.getClass().getSimpleName(), "[DONE] Drew on canvas.");
        }
    }

    /**
     * Draws the background and all sprites in {@link #sprites} on the {@code canvas}.
     *
     * @param canvas Canvas on which to draw.
     */
    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            canvas.drawColor(Color.parseColor("#107594"));
            canvas.drawBitmap(bg, 0, 0, null);

            for (ISprite sprite : sprites) {
                sprite.draw(canvas);
            }
        }
    }

    /**
     * Updates the background to that of the current period, and
     * plays the corresponding background music. Music is set to
     * loop so that if it's shorter than the period it keeps playing.
     *
     * @param newPeriod Indicates whether day or night just started.
     */
    @Override
    public void onPeriodChange(Period newPeriod) {
        Log.i(this.getClass().getSimpleName(), "It is now: " + newPeriod);

        if (newPeriod == Period.DAY) {
            if (nightPlayer != null && nightPlayer.isPlaying()) {
                nightPlayer.pause();
                nightPlayer.seekTo(0); // rewind
            }
            bg = bg_day;
            if (dayPlayer != null) {
                dayPlayer.start();
                dayPlayer.setLooping(true);
            }
        } else if (newPeriod == Period.NIGHT) {
            if (dayPlayer != null && dayPlayer.isPlaying()) {
                dayPlayer.pause();
                dayPlayer.seekTo(0); // rewind
            }
            bg = bg_night;
            if (nightPlayer != null) {
                nightPlayer.start();
                nightPlayer.setLooping(true);
            }
        }

        onTick(); // to force a draw
    }
}
