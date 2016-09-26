package strategy;

import java.io.Serializable;

public interface CacheStrategy<K, V extends Serializable> {

    void put(K key, V value);

}
