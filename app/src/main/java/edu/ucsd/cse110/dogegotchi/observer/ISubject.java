package edu.ucsd.cse110.dogegotchi.observer;

/**
 * Generic interface describing a subject (aka observable).
 *
 * @param <ObserverT> Type of observers for this subject.
 */
public interface ISubject<ObserverT> {
    /**
     * Register a new listener.
     */
    void register(ObserverT observer);

    /**
     * Unregister a listener.
     */
    void unregister(ObserverT observer);

    // note that using a generic interface like this is DRY, but precludes
    // adding type specific methods as we talked in discussion.
}
