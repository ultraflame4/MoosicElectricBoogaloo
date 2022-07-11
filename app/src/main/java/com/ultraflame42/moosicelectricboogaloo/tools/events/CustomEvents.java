package com.ultraflame42.moosicelectricboogaloo.tools.events;

import android.util.Log;

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
    public final String name;

    public CustomEvents(String name_) {
        name = name_;
    }

    public EventCallbackWrapper<D> addListener(EventFunctionCallback<D> listener) {
        listeners.add(listener);
        Log.d("EventManager", "Listener list" + listeners.toString());
        return new EventCallbackWrapper<D>(listener, this);
    }

    public EventCallbackWrapper<D> addListenerOnce(EventFunctionCallback<D> listener) {
        listeners.add(listener);
        onceListeners.add(listener);
        return new EventCallbackWrapper<D>(listener, this);
    }

    public void removeListener(EventFunctionCallback<D> listener) {
        listeners.remove(listener);
        if (onceListeners.contains(listener)) {
            onceListeners.remove(listener);
        }
    }

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

    public int getListenerCount() {
        return listeners.size();
    }
    public List<EventFunctionCallback<D>> getAllListener() {
        return listeners;
    }
}