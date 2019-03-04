package edu.ucsd.cse110.dogegotchi.ticker;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Uses an {@link AsyncTask} to implement a {@link ITicker}.
 *
 * @see ITicker
 */
public class AsyncTaskTicker
        extends AsyncTask<Void, Void, Void>
        implements ITicker
{
    /**
     * Observers of this ticker.
     */
    private final Collection<ITickerObserver> observers;

    /**
     * Duration, in seconds, of the wait between ticks.
     */
    private final int tickInterval;

    /**
     * Constructor.
     *
     * @param observers List of observers.
     * @param tickInterval Duration in seconds of wait between ticks.
     */
    public AsyncTaskTicker(final Collection<ITickerObserver> observers,
                           final int tickInterval)
    {
        this.observers = observers;
        this.tickInterval = tickInterval;
    }

    /**
     * Constructor.
     *
     * @param tickInterval Duration in seconds of wait between ticks.
     */
    public AsyncTaskTicker(final int tickInterval) {
        this(new ArrayList<>(), tickInterval);
    }

    /**
     * Switch for turning flag on/off.
     */
    private volatile boolean running = false;

    public void register(final ITickerObserver observer) {
        observers.add(observer);
    }

    public void unregister(final ITickerObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void start() {
        this.running = true;
        super.execute();
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    protected Void doInBackground(Void... none) {
        int counter = 0;
        while (running) {
            try {
                // do tick
                if ((++counter % 2) == 0) {
                    publishProgress();
                    counter = 0;
                }

                /**
                 * Refresh canvas every {@link tickInterval} seconds.
                 */
                Log.i(this.getClass().getSimpleName(), "Sleeping for " + tickInterval + " seconds.");
                Thread.sleep(tickInterval * 1000);
                Log.i(this.getClass().getSimpleName(), "Slept for " + tickInterval + " seconds.");
            } catch (InterruptedException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            }
        }

        Log.i(this.getClass().getSimpleName(), "[DONE] Exiting game loop.");

        // quirk to get around Void return type
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... none) {
        doTick();
    }

    private void doTick() {
        Log.i(this.getClass().getSimpleName(), "🕰 Tick...");
        for (ITickerObserver observer : observers) {
            observer.onTick();
        }
    }
}
