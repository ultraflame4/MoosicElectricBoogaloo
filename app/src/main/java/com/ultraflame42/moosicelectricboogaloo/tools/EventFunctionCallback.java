package com.ultraflame42.moosicelectricboogaloo.tools;

@FunctionalInterface
public interface EventFunctionCallback<D> {
    void call(D data);
}
