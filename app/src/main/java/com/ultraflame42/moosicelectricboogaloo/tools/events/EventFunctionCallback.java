package com.ultraflame42.moosicelectricboogaloo.tools.events;

/**
 * Functional Interface for Event Callbacks.
 * @param <D>
 */
@FunctionalInterface
public interface EventFunctionCallback<D> {
    void call(D data);
}
