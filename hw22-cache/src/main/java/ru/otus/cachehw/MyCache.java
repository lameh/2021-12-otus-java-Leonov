package ru.otus.cachehw;


import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final List<HwListener<K, V>> listeners;

    private final Map<K, V> cache = new WeakHashMap<>();

    public MyCache() {
        listeners = new ArrayList<>();
    }

    public MyCache(List<HwListener<K, V>> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        try {
            listeners.forEach(l -> l.notify(key, value, "put in cache"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        try {
            listeners.forEach(l -> l.notify(key, value, "remove from cache"));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    @Override
    public long cacheSize() {
        return cache.size();
    }
}
