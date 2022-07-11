package com.ultraflame42.moosicelectricboogaloo.tools.events;

@FunctionalInterface
public interface EventFunctionCallback<D> {
    void call(D data);
}
