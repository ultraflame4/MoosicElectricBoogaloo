package com.ultraflame42.moosicelectricboogaloo.tools.events;

/**
 * A utility class for wrapping around event listeners.
 * To:
 * 1. Allow easy removal of listeners from the event.
 * @param <D>
 */
public class EventCallbackListener<D> {
    private EventFunctionCallback<D> cb;
    private CustomEvents<D> manager;

    public EventCallbackListener(EventFunctionCallback<D> cb, CustomEvents<D> manager) {
        this.cb = cb;
        this.manager = manager;
    }

    /**
     * Returns the callback that this object is wrapping around.
     * @return
     */
    public EventFunctionCallback<D> getCallback() {
        return cb;
    }

    /**
     * When called, removes itself from the event.
     */
    public void remove() {
        manager.removeListener(cb);
    }


}
