package com.ultraflame42.moosicelectricboogaloo.tools.registry;

import android.util.Log;

import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;

import java.util.HashMap;

public class Registry<D> {
    private HashMap<Integer,RegistryItem<D>> items = new HashMap<>();
    private int idCounter = 0; // todo load from storage

    /**
     * This event is fired when an item is added to the registry
     */

    public CustomEvents<RegistryUpdateData<D>> OnItemsUpdate = new CustomEvents<>();

    public Registry() {
    }

    public void add(D item) {
        RegistryItem<D> r = new RegistryItem<>(item, idCounter);
        items.put(idCounter, r);
        idCounter++;
        OnItemsUpdate.pushEvent(new RegistryUpdateData<>(r, false));
    }

    public void remove(int itemId) {
        if (!items.containsKey(itemId)) {
            Log.w("Registry:REMOVE", "Tried to remove item that doesn't exist. Item id: " + itemId);
            return;
        }
        RegistryItem<D> r = items.get(itemId);
        items.remove(itemId);
        OnItemsUpdate.pushEvent(new RegistryUpdateData<>(r, true));
    }

    public RegistryItem<D> get(int itemId) {
        if (!items.containsKey(itemId)) {
            Log.w("Registry:GET", "Tried to get item that doesn't exist. Item id: " + itemId);
        }
        return items.get(itemId);
    }
    public D getItem(int itemId) {

        return get(itemId).item;
    }

    public RegistryItem<D>[] getAllItems() {
        return items.values().toArray(new RegistryItem[0]);
    }
    public int count() {
        return items.size();
    }

    public boolean contains(int itemId) {
        return items.containsKey(itemId);
    }
    public boolean containsItem(RegistryItem<D> registryItem) {
        return items.containsValue(registryItem);
    }

}
