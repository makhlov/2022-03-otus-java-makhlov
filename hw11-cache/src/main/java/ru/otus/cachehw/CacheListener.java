package ru.otus.cachehw;


public interface CacheListener<K, V> {
    void notify(K key, V value, String action);
}
