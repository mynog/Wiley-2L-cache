package cache.impl;

import exception.KeyNotFoundException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MemoryCacheImpl<K, V extends Serializable> extends AbstractCache<K, V> {

    Map<K, V> objects;

    public MemoryCacheImpl(int capacity) {
        super(capacity);
        this.objects = new HashMap<K, V>(capacity);
    }

    public V get(K key) throws KeyNotFoundException {
        if (contains(key)) {
            return objects.get(key);
        } else {
            throw new KeyNotFoundException();
        }
    }

    public void put(K key, V value) {
        objects.put(key, value);
    }

    public void remove(K key) throws KeyNotFoundException {
        if (contains(key)) {
            objects.remove(key);
        } else {
            throw new KeyNotFoundException();
        }
    }

    public void clear() {
        objects.clear();
    }

    public boolean contains(K key) {
        return objects.containsKey(key);
    }

    public Set<K> getAll() {
        return objects.keySet();
    }

    public boolean isFull() {
        return getCapacity() <= objects.size();
    }
}
