package cache;

import cache.impl.FileCacheImpl;
import cache.impl.MemoryCacheImpl;
import counter.CacheCounter;
import strategy.Strategy;
import strategy.impl.LruStrategy;
import strategy.impl.MruStrategy;
import strategy.impl.SimpleRecacheStrategy;

import java.io.Serializable;

public class CacheFactory<K, V extends Serializable> {

    public Cache<K, V> build(int memoryCacheCapacity, int fileCacheCapacity, Strategy strategy) {
        TwoLevelCache<K, V> cache = new TwoLevelCache<K, V>();
        MemoryCacheImpl<K, V> memoryCache = new MemoryCacheImpl<K, V>(memoryCacheCapacity);
        FileCacheImpl<K, V> fileCache = new FileCacheImpl<K, V>(fileCacheCapacity);
        cache.setMemoryCache(memoryCache);
        cache.setFileCache(fileCache);
        CacheCounter<K, V> counter = new CacheCounter<K, V>(cache);
        cache.setRecacheStrategy(new SimpleRecacheStrategy<K, V>(memoryCache, fileCache, counter));
        switch (strategy) {
            case LRU:
                cache.setCacheStrategy(new LruStrategy<K, V>(memoryCache, fileCache, counter));
                break;
            case MRU:
                cache.setCacheStrategy(new MruStrategy<K, V>(memoryCache, fileCache, counter));
                break;
        }
        return counter;
    }

}
