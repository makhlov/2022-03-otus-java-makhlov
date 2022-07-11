package ru.otus.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> cacheMap;
    private final List<CacheListener<K,V>> listeners;

    public MyCache() {
        cacheMap = new WeakHashMap<>();
        listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        cacheMap.put(key, value);
        sendNotify(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        cacheMap.remove(key);
        sendNotify(key, null, "REMOVE");
    }

    @Override
    public V get(K key) {
        V result = cacheMap.get(key);
        sendNotify(key, result, "GET");
        return result;
    }

    @Override
    public void addListener(CacheListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(CacheListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void sendNotify(K key, V value, String action) {
        for (var listener: listeners) {
            listener.notify(key, value, action);
        }
    }
}
