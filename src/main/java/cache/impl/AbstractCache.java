package cache.impl;

import cache.Cache;

import java.io.Serializable;

public abstract class AbstractCache<K, V extends Serializable> implements Cache<K, V> {

    private int capacity;

    public AbstractCache(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    abstract boolean isFull();
}
