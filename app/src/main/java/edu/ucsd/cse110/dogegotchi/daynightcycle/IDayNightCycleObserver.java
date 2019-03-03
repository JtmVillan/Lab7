package edu.ucsd.cse110.dogegotchi.daynightcycle;

/**
 * Observer of day and night cycle, notified upon the start of either.
 */
public interface IDayNightCycleObserver {
    /**
     * Message for the event.
     */
    enum Period {
        // Only day and night for now, feel free to add make it more granular
        DAY,
        NIGHT;

        /**
         * Helper method to return successor of a period.
         *
         * @return Next period.
         */
        public Period successor() {
            if (this.equals(DAY)) {
                return NIGHT;
            }
            return DAY;
        }
    }

    /**
     * Signalled when day/night starts.
     *
     * @param newPeriod Indicates whether day or night just started.
     */
    void onPeriodChange(Period newPeriod);
}
