package strategy.impl;

import cache.impl.FileCacheImpl;
import cache.impl.MemoryCacheImpl;
import counter.CacheCounter;
import strategy.CacheStrategy;

import java.io.Serializable;

public class MruStrategy<K, V extends Serializable> implements CacheStrategy<K, V> {

    private final MemoryCacheImpl<K, V> memoryCache;
    private final FileCacheImpl<K, V> fileCache;
    private final CacheCounter<K, V> counter;

    public MruStrategy(MemoryCacheImpl<K, V> memoryCache, FileCacheImpl<K, V> fileCache, CacheCounter<K, V> counter) {
        this.memoryCache = memoryCache;
        this.fileCache = fileCache;
        this.counter = counter;
    }

    public void put(K key, V value) {
        if (!memoryCache.isFull()) {
            memoryCache.put(key, value);
        } else if (!fileCache.isFull()) {
            fileCache.put(key, value);
        } else {
            K first = counter.getMostPopular();
            counter.remove(first);
            put(key, value);
        }
    }
}
