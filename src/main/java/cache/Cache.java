package cache;

import exception.KeyNotFoundException;

import java.io.Serializable;
import java.util.Set;

public interface Cache<K, V extends Serializable> {

    V get(K key) throws KeyNotFoundException;

    void put(K key, V value);

    void remove(K key) throws KeyNotFoundException;

    void clear();

    boolean contains(K key);

    Set<K> getAll();
}
