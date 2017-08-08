package com.example.coustomtoolbar.ImageCache;

import android.util.LruCache;

/**
 * Created by yaojian on 2017/8/8.
 */

public class  MyLruCache< K,V> extends LruCache<K,V> {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public MyLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    public void resize(int maxSize) {
        super.resize(maxSize);
    }

    @Override
    public void trimToSize(int maxSize) {
        super.trimToSize(maxSize);
    }

    @Override
    protected V create(K key) {
        return super.create(key);
    }

    @Override
    protected int sizeOf(K key, V value) {
        return super.sizeOf(key, value);
    }
}
