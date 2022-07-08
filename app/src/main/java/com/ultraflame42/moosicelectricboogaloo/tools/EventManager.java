package com.ultraflame42.moosicelectricboogaloo.tools;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class EventManager<D> {
    private List<EventFunctionCallback<D>> listeners = new ArrayList<>();
    private List<EventFunctionCallback<D>> onceListeners = new ArrayList<>();

    public void addListener(EventFunctionCallback<D> listener) {
        listeners.add(listener);
    }

    public void addListenerOnce(EventFunctionCallback<D> listener) {
        listeners.add(listener);
        onceListeners.add(listener);
    }

    public void pushEvent(@Nullable D data) {
        listeners.forEach(t -> {
            t.call(data);
        });

        onceListeners.forEach(t -> {
            listeners.remove(t);
        });
        onceListeners.clear();
    }
}
