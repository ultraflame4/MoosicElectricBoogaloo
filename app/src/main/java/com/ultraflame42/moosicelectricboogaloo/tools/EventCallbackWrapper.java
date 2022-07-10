package com.ultraflame42.moosicelectricboogaloo.tools;

import android.util.Log;

/**
 * A utility class for wrapping around event listeners.
 * To:
 * 1. Allow easy removal of listeners from the event.
 * 2. Auto remove listeners before garbage collection.
 * @param <D>
 */
public class EventCallbackWrapper<D> {
    private EventFunctionCallback<D> cb;
    private EventManager<D> manager;

    public EventCallbackWrapper(EventFunctionCallback<D> cb, EventManager<D> manager) {
        this.cb = cb;
        this.manager = manager;
    }

    public EventFunctionCallback<D> getCallback() {
        return cb;
    }

    public void remove() {
        manager.removeListener(cb);
    }

    @Override
    protected void finalize() throws Throwable {
        //remove listener when object shld be destroyed
        remove();
        super.finalize();
    }
}
