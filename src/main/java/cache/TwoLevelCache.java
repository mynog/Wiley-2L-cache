package cache;

import cache.impl.FileCacheImpl;
import cache.impl.MemoryCacheImpl;
import exception.CacheFileDeleteException;
import exception.DeserializeException;
import exception.KeyNotFoundException;
import exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strategy.CacheStrategy;
import strategy.RecacheStrategy;

import java.io.Serializable;
import java.util.*;

public class TwoLevelCache<K, V extends Serializable> implements Cache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(TwoLevelCache.class);

    private MemoryCacheImpl<K, V> memoryCache;
    private FileCacheImpl<K, V> fileCache;
    private CacheStrategy<K, V> cacheStrategy;
    private RecacheStrategy<K, V> recacheStrategy;

    TwoLevelCache() {
    }

    public V get(K key) throws KeyNotFoundException {

        V object = null;

        if (memoryCache.contains(key)) {
            object = memoryCache.get(key);
        } else if (fileCache.contains(key)) {
            try {
                object = fileCache.get(key);
            } catch (DeserializeException e) {
                log.error("Error deserialize object, key = {0}", key);
                throw e;
            }
        }

        if (object != null) {
            recacheStrategy.get(key);
            return object;
        }

        throw new KeyNotFoundException();

    }

    public void put(K key, V value) {
        try {
            cacheStrategy.put(key, value);
        } catch (SerializeException e) {
            log.error("Error serialize object, key = {0}", key);
            throw e;
        }
    }

    public void remove(K key) throws KeyNotFoundException {
        if (memoryCache.contains(key)) {
            memoryCache.remove(key);
        } else {
            try {
                fileCache.remove(key);
            } catch (CacheFileDeleteException e) {
                log.error("Error remove file, key = {0}", key);
                throw e;
            }
        }
    }

    public void clear() {
        try {
            memoryCache.clear();
            fileCache.clear();
        } catch (CacheFileDeleteException e) {
            log.error("Error remove file");
            throw e;
        }
    }

    public boolean contains(K key) {
        return memoryCache.contains(key) || fileCache.contains(key);
    }

    public Set<K> getAll() {
        Set<K> objects = new HashSet<K>(getCapacity());
        objects.addAll(memoryCache.getAll());
        objects.addAll(fileCache.getAll());
        return objects;
    }

    public int getCapacity() {
        return memoryCache.getCapacity() + fileCache.getCapacity();
    }

    void setMemoryCache(MemoryCacheImpl<K, V> memoryCache) {
        this.memoryCache = memoryCache;
    }

    void setFileCache(FileCacheImpl<K, V> fileCache) {
        this.fileCache = fileCache;
    }

    void setCacheStrategy(CacheStrategy cacheStrategy) {
        this.cacheStrategy = cacheStrategy;
    }

    void setRecacheStrategy(RecacheStrategy recacheStrategy) {
        this.recacheStrategy = recacheStrategy;
    }
}
