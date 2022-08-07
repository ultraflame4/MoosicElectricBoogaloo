package com.ultraflame42.moosicelectricboogaloo.tools.registry;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.ultraflame42.moosicelectricboogaloo.tools.events.CustomEvents;
import com.ultraflame42.moosicelectricboogaloo.tools.events.DefaultEvent;

import java.util.HashMap;

public class Registry<D> {
    protected HashMap<Integer,RegistryItem<D>> items = new HashMap<>();
    protected int idCounter = 0;

    /**
     * This event is fired when an item is added to the registry
     */

    public DefaultEvent OnItemsUpdate = new DefaultEvent();

    public Registry() {
    }

    public void add(D item) {
        // create registry item for the item
        RegistryItem<D> r = new RegistryItem<>(item, idCounter);
        // add it to the items HashMap with th idCounter
        items.put(idCounter, r);
        // increment the idCounter
        idCounter++;
        // push event
        OnItemsUpdate.pushEvent(null);
    }

    public void remove(int itemId) {
        // Check if item exists
        if (!items.containsKey(itemId)) {
            Log.w("Registry:REMOVE", "Tried to remove item that doesn't exist. Item id: " + itemId);
            return;
        }
        // get the item
        RegistryItem<D> r = items.get(itemId);
        // remove it from the Hashmap
        items.remove(itemId);
        // push out event
        OnItemsUpdate.pushEvent(null);
    }

    public RegistryItem<D> get(int itemId) {
        // Verify item exists
        if (!items.containsKey(itemId)) {
            // if not log warning
            Log.w("Registry:GET", "Tried to get item that doesn't exist. Item id: " + itemId);
        }
        // Return the item
        return items.get(itemId);
    }

    /**
     * Shorthand for get(id).item
     * @param itemId
     * @return
     */
    public D getItem(int itemId) {
        return get(itemId).item;
    }

    public RegistryItem<D>[] getAllItems() {
        // return all the items in the registry
        return items.values().toArray(new RegistryItem[0]);
    }

    /**
     *  Number of item in the registry
     * @return
     */
    public int count() {
        return items.size();
    }

    /**
     * Whether the registry contains an item with the given id
     * @param itemId
     * @return
     */
    public boolean contains(int itemId) {
        return items.containsKey(itemId);
    }

    /**
     * Whether the registry contains the given registry item
     * @param registryItem
     * @return
     */
    public boolean containsItem(RegistryItem<D> registryItem) {
        return items.containsValue(registryItem);
    }

    /**
     * Returns the next id that will be used for a new item
     * @return
     */
    public int getNextId() {
        return idCounter;
    }

}
