/***
 * Copyright 2010 Blaine R Southam
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.hp.cache4guice;

import com.hp.cache4guice.adapters.CacheAdapter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCache implements CacheAdapter {

    private Map<String, Object> cache;

    public SimpleCache() {
        cache = new ConcurrentHashMap<String, Object>();
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, 0);
    }

    @Override
    public void put(String key, Object value, int timeToLiveInSeconds) {
        cache.put(key, value);
    }

    public boolean isKeyInCache(String key) {
        return cache.keySet().contains(key);
    }

}
