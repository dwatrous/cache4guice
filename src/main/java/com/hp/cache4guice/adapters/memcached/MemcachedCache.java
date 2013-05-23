/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.cache4guice.adapters.memcached;

import com.hp.cache4guice.adapters.CacheAdapter;
import net.spy.memcached.MemcachedClient;

/**
 *
 * @author watrous
 */
public class MemcachedCache implements CacheAdapter {
    
    MemcachedClient c;

    public Object get(String key) {
        return c.get(key);
    }

    public void put(String key, Object value) {
        put(key, value, 0);
    }

    public void put(String key, Object value, int timeToLiveInSeconds) {
        // magic number 1000 is to convert seconds to milliseconds
        c.set(key, timeToLiveInSeconds*1000, value);
    }

    public boolean isKeyInCache(String key) {
        // not ideal, but it's the best we've got with memcached
        if (c.get(key) == null) {
            return false;
        } else {
            return true;
        }
    }
    
}
