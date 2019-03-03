package edu.ucsd.cse110.dogegotchi.daynightcycle;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

import edu.ucsd.cse110.dogegotchi.observer.ISubject;
import edu.ucsd.cse110.dogegotchi.ticker.ITickerObserver;

/**
 * Keeps track of a configurable number of in-game ticks to
 * cycle between day and night.
 *
 * This class observes a ticker objecter via the
 * {@link ITickerObserver} interface.
 *
 * It publishes its own events day/night start events via the
 * {@link IDayNightCycleObserver} interface.
 */
public class DayNightCycleMediator implements ISubject<IDayNightCycleObserver>, ITickerObserver {
    Collection<IDayNightCycleObserver> observers;

    /**
     * Number of ticks per period ({@link IDayNightCycleObserver.Period}).
     */
    final int ticksPerPeriod;

    /**
     * Number of ticks so far.
     */
    int currentPeriodTickCount;

    /**
     * Current period of the day.
     */
    IDayNightCycleObserver.Period currentPeriod;


    /**
     * Constructor.
     *
     * @param observers List of observers.
     * @param ticksPerPeriod Duration of a day/night in ticks.
     */
    public DayNightCycleMediator(final Collection<IDayNightCycleObserver> observers,
                                 final int ticksPerPeriod)
    {
        this.observers = observers;

        this.ticksPerPeriod = ticksPerPeriod;

        // by default we always start during the day
        this.currentPeriodTickCount = 0;
        this.currentPeriod = IDayNightCycleObserver.Period.DAY;
        Log.i(this.getClass().getSimpleName(), String.format(
                "Creating day/night cycler with %d ticks per day/night.", this.ticksPerPeriod));
    }

    /**
     * Constructor.
     *
     * @param ticksPerPeriod Duration of a day/night in ticks.
     */
    public DayNightCycleMediator(final int ticksPerPeriod) {
        this(new ArrayList<>(), ticksPerPeriod);
    }

    /**
     * Count tick and advance period every {@link #ticksPerPeriod} ticks.
     */
    @Override
    public void onTick() {
        currentPeriodTickCount++;

        Log.d(this.getClass().getSimpleName(),
                currentPeriod + " :: Current tick count:" + currentPeriodTickCount);

        // Notify on first tick per period
        // (This allows notification on very first run!)
        if (this.currentPeriodTickCount == 1) {
            this.observers.forEach(observer -> observer.onPeriodChange(this.currentPeriod));
        }
        else if ((currentPeriodTickCount % ticksPerPeriod) == 0) {
            this.currentPeriod = this.currentPeriod.successor();
            this.currentPeriodTickCount = 0; // reset
        }
    }

    @Override
    public void register(IDayNightCycleObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(IDayNightCycleObserver observer) {
        observers.remove(observer);
    }
}
