package com.ultraflame42.moosicelectricboogaloo.tools.events;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A utility class for easy events and callbacks.
 * @param <D> The type of data to be passed to the event listener.
 */
public class CustomEvents<D> {
    private List<EventFunctionCallback<D>> listeners = new ArrayList<>();
    private List<EventFunctionCallback<D>> onceListeners = new ArrayList<>();


    public CustomEvents() {

    }

    /**
     * Adds a listener to the event.
     * @param listener The listener to add.
     */
    public EventCallbackListener<D> addListener(EventFunctionCallback<D> listener) {
        // add callback to list
        listeners.add(listener);
        // return a new instance of EventCallbackListener to wrap around the listener
        return new EventCallbackListener<D>(listener, this);
    }

    /**
     * Adds a listener to the event. This listener will only be called once.
     * @param listener The listener to add.
     */
    public EventCallbackListener<D> addListenerOnce(EventFunctionCallback<D> listener) {
        // add callback to list
        listeners.add(listener);
        // add callback to once list;
        onceListeners.add(listener);
        // return a new instance of EventCallbackListener to wrap around the listener
        return new EventCallbackListener<D>(listener, this);
    }

    /**
     * Removes the listner from the event
     * @param listener The listener to remove.
     */
    public void removeListener(EventFunctionCallback<D> listener) {
        listeners.remove(listener);
        if (onceListeners.contains(listener)) {
            onceListeners.remove(listener);
        }
    }

    /**
     * Calls all callback/listeners registered to the event.
     * @param data The data to pass to the listeners.
     */
    public void pushEvent(@Nullable D data) {
        // use a copy while iterating to make it threadsafe
        new ArrayList<>(listeners).forEach(t -> {
            t.call(data);
        });

        onceListeners.forEach(t -> {
            listeners.remove(t);
        });
        onceListeners.clear();
    }

    /**
     * Returns the number of listeners registered
     * @return
     */
    public int getListenerCount() {
        return listeners.size();
    }

    /**
     * Returns all the listeners registered to the event.
     * @return
     */
    public List<EventFunctionCallback<D>> getAllListener() {
        return listeners;
    }

    /**
     * Removes all the listeners registered to the event.
     */
    public void clearListeners() {
        listeners.clear();
        onceListeners.clear();
    }
}
