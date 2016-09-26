package counter;

import cache.Cache;
import cache.TwoLevelCache;
import exception.KeyNotFoundException;

import java.io.Serializable;
import java.util.*;

public class CacheCounter<K, V extends Serializable> implements Cache<K, V> {

    private TwoLevelCache<K, V> cache;
    private LinkedList<K> counter;

    public CacheCounter(TwoLevelCache<K, V> cache) {
        counter = new LinkedList<K>();
        this.cache = cache;
    }

    public V get(K key) throws KeyNotFoundException {
        V object = cache.get(key);
        counter.remove(key);
        counter.addFirst(key);
        return object;
    }

    public void put(K key, V value) {
        if (cache.contains(key)) {
            remove(key);
        }
        cache.put(key, value);
        counter.addFirst(key);
    }

    public void remove(K key) throws KeyNotFoundException {
        cache.remove(key);
        counter.remove(key);
    }

    public void clear() {
        cache.clear();
        counter.clear();
    }

    public boolean contains(K key) {
        return cache.contains(key);
    }

    public Set<K> getAll() {
        return cache.getAll();
    }

    public K getLastFrequency() {
        if (!counter.isEmpty())
            return counter.getLast();
        return null;
    }

    public K getMostPopular() {
        if (!counter.isEmpty())
            return counter.getFirst();
        return null;
    }

    public LinkedList<K> getCounts() {
        return counter;
    }
}
