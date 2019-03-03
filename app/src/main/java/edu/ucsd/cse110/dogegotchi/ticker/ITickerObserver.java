package edu.ucsd.cse110.dogegotchi.ticker;

public interface ITickerObserver {
    /**
     * Notifies observer of a tick.
     */
    void onTick();
}
