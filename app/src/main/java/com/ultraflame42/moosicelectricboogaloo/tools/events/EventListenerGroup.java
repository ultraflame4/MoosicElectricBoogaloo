package com.ultraflame42.moosicelectricboogaloo.tools.events;

import java.util.ArrayList;

/**
 * A group of event listeners
 * Helpful in deregistering a lot of listeners at once.
 */
public class EventListenerGroup {
    private ArrayList<EventCallbackListener<Object>> listeners = new ArrayList<>();

    public <D> void subscribe(CustomEvents<D> event, EventFunctionCallback<D> cb) {
        // Add listener to list
        listeners.add(
                (EventCallbackListener<Object>) event.addListener(cb)
        );
    }

    public void unsubscribeAll() {
        // for each listener in the list, tell it to remove itself from the event
        for (EventCallbackListener<Object> listener : listeners) {
            listener.remove();
        }
    }
}
