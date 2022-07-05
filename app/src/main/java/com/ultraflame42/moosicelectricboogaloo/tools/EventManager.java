package com.ultraflame42.moosicelectricboogaloo.tools;

import java.util.ArrayList;
import java.util.List;

public class EventManager<T extends EventFunctionCallback> {
    private List<T> listeners = new ArrayList<T>();

    public void addListener(T listener) {
        listeners.add(listener);
    }

    public void pushEvent() {
        listeners.forEach(t -> {
            t.call();
        });
    }
}
