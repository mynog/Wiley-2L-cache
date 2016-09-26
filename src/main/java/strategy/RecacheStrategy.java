package strategy;

import java.io.Serializable;

public interface RecacheStrategy<K, V extends Serializable> {

    void get(K key);

}
