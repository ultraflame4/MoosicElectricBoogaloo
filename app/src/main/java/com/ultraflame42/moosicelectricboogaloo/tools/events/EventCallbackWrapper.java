package com.ultraflame42.moosicelectricboogaloo.tools.events;

/**
 * A utility class for wrapping around event listeners.
 * To:
 * 1. Allow easy removal of listeners from the event.
 * @param <D>
 */
public class EventCallbackWrapper<D> {
    private EventFunctionCallback<D> cb;
    private CustomEvents<D> manager;

    public EventCallbackWrapper(EventFunctionCallback<D> cb, CustomEvents<D> manager) {
        this.cb = cb;
        this.manager = manager;
    }

    public EventFunctionCallback<D> getCallback() {
        return cb;
    }

    public void remove() {
        manager.removeListener(cb);
    }


}
