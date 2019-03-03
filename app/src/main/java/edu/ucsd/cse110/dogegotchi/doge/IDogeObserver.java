package edu.ucsd.cse110.dogegotchi.doge;

public interface IDogeObserver {

    /**
     * Signalled when the doge's state changes, e.g. happy -> sad.
     *
     * @param newState New state of the doge.
     */
    void onStateChange(Doge.State newState);
}
