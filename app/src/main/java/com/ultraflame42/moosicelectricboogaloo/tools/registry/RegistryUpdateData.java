package com.ultraflame42.moosicelectricboogaloo.tools.registry;

public class RegistryUpdateData<D>  {
    public final boolean isRemoved;
    public final RegistryItem<D> registryItem;

    /**
     *
     * @param registryItem The registry item that caused the update.
     * @param isRemoved True if item is being removed from registry, false if item is being added to registry.
     */
    public RegistryUpdateData(RegistryItem<D> registryItem, boolean isRemoved) {
        this.registryItem = registryItem;
        this.isRemoved = isRemoved;
    }

}
