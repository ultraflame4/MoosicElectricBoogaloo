package com.ultraflame42.moosicelectricboogaloo.tools;

import java.util.ArrayList;
import java.util.List;

// todo find better way of doing this. Observers?
public class EventManager<T extends EventFunctionCallback> {
    private List<T> listeners = new ArrayList<T>();
    private List<T> onceListeners = new ArrayList<T>();

    public void addListener(T listener) {
        listeners.add(listener);
    }

    public void addListenerOnce(T listener) {
        listeners.add(listener);
        onceListeners.add(listener);
    }

    public void pushEvent() {
        listeners.forEach(t -> {
            t.call();
        });

        onceListeners.forEach(t -> {
            listeners.remove(t);
        });
        onceListeners.clear();
    }
}
