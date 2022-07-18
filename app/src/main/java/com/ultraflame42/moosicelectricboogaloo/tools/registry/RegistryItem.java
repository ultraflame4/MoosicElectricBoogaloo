package com.ultraflame42.moosicelectricboogaloo.tools.registry;

public class RegistryItem<D> {
    public final D item;
    public final int id;

    public RegistryItem(D item, int id) {
        this.item = item;
        this.id = id;
    }
}
