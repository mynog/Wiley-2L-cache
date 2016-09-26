package strategy.impl;

import cache.impl.FileCacheImpl;
import cache.impl.MemoryCacheImpl;
import counter.CacheCounter;
import strategy.RecacheStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SimpleRecacheStrategy<K, V extends Serializable> implements RecacheStrategy<K, V> {

    private final MemoryCacheImpl<K, V> memoryCache;
    private final FileCacheImpl<K, V> fileCache;
    private final CacheCounter<K, V> counter;

    private final int RECACHE_FREQUENCY;
    private int getCount;

    public SimpleRecacheStrategy(MemoryCacheImpl<K, V> memoryCache, FileCacheImpl<K, V> fileCache, CacheCounter<K, V> counter) {
        this.memoryCache = memoryCache;
        this.fileCache = fileCache;
        this.counter = counter;
        getCount = 0;
        RECACHE_FREQUENCY = 10;
    }


    public void get(K key) {
        getCount++;
        if (getCount > RECACHE_FREQUENCY) {
            recache();
        }
    }

    private void recache() {
        LinkedList<K> counts = counter.getCounts();

        if (counts == null)
            return;

        Set<K> inMemory = memoryCache.getAll();
        Set<K> inFile = fileCache.getAll();

        Set<K> toMemory = new HashSet<K>(counts.subList(0, inMemory.size() - 1));
        Set<K> toFile = new HashSet<K>(counts.subList(inMemory.size(), counts.size() - 1));
        toMemory.removeAll(inMemory);
        toFile.removeAll(inFile);

        for (K key : toFile) {
            V object = memoryCache.get(key);
            fileCache.put(key, object);
            memoryCache.remove(key);
        }

        for (K key : toMemory) {
            V object = fileCache.get(key);
            memoryCache.put(key, object);
            fileCache.remove(key);
        }

        getCount = 0;
    }
}
