package cache.impl;

import exception.*;

import java.io.*;
import java.util.*;

public class FileCacheImpl<K, V extends Serializable> extends AbstractCache<K, V> {

    private Map<K, String> fileNames;
    private String dir = "file-cache/";

    public FileCacheImpl(int capacity) {
        super(capacity);
        createOrCleanDir();
        fileNames = new HashMap<K, String>(capacity);
    }

    public V get(K key) throws KeyNotFoundException {
        if (contains(key)) {
            FileInputStream fis = null;
            ObjectInputStream oin = null;
            try {
                fis = new FileInputStream(fileNames.get(key));
                oin = new ObjectInputStream(fis);
                return (V) oin.readObject();
            } catch (IOException e) {
                throw new DeserializeException();
            } catch (ClassNotFoundException e) {
                throw new DeserializeException();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (oin != null) {
                        oin.close();
                    }
                } catch (IOException e) {
                    // nothing
                }
            }
        }
        throw new KeyNotFoundException();
    }


    public void put(K key, V value) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        String fileName = dir + UUID.randomUUID().toString();

        try {
            fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(value);
            oos.flush();
        } catch (IOException e) {
            throw new SerializeException();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                // nothing
            }
        }

        fileNames.put(key, fileName);
    }

    public void remove(K key) throws KeyNotFoundException {
        if (!contains(key)) {
            throw new KeyNotFoundException();
        }
        removeFile(fileNames.get(key));
        fileNames.remove(key);
    }

    public void clear() {
        for (Map.Entry<K, String> key : fileNames.entrySet()) {
            removeFile(key.getValue());
        }
        fileNames.clear();
    }

    private void removeFile(String fileName) {
        File file = new File(fileName);
        if (file.delete()) {
            return;
        }
        throw new CacheFileDeleteException();
    }

    public boolean contains(K key) {
        return fileNames.containsKey(key);
    }

    public Set<K> getAll() {
        return fileNames.keySet();
    }

    public boolean isFull() {
        return getCapacity() <= fileNames.size();
    }

    private void createOrCleanDir() throws CacheDirectoryCreateException {
        File cacheDir = new File(dir);
        if (!cacheDir.exists()) {
            if (!cacheDir.mkdir()) {
                throw new CacheDirectoryCreateException();
            }
        } else {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File file : files)
                    if (!file.isDirectory())
                        file.delete();
            }
        }
    }
}
