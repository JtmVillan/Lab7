package edu.ucsd.cse110.dogegotchi.ticker;

import edu.ucsd.cse110.dogegotchi.observer.ISubject;

/**
 * Implementors translate passage of a fixed period of time
 * in the real-world into a tick. A tick is a unit of time
 * in the context of the game domain, a count of which can
 * be used to define in-game events (e.g. 30 ticks being a
 * night).
 */
public interface ITicker extends ISubject<ITickerObserver> {
    /**
     * Start the ticker.
     */
    void start();

    /**
     * Stop the ticker.
     */
    void stop();
}
