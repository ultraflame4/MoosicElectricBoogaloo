package com.ultraflame42.moosicelectricboogaloo.tools.registry;

// Represents an item in the registry.
public class RegistryItem<D> {

    public final D item; // item it holds
    public final int id; // the id of the item in the registry

    public RegistryItem(D item, int id) {
        this.item = item;
        this.id = id;
    }
}
