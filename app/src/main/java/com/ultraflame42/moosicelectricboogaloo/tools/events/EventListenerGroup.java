package com.ultraflame42.moosicelectricboogaloo.tools.events;

import java.util.ArrayList;

/**
 * A group of event listeners
 * Helpful in deregister alot of listeners at once.
 */
public class EventListenerGroup {
    private ArrayList<EventCallbackListener<Object>> listeners = new ArrayList<>();

    public <D> void subscribe(CustomEvents<D> event, EventFunctionCallback<D> cb) {
        listeners.add(
                (EventCallbackListener<Object>) event.addListener(cb)
        );
    }

    public void unsubscribeAll() {
        for (EventCallbackListener<Object> listener : listeners) {
            listener.remove();
        }
    }
}
