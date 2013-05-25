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
package com.hp.cache4guice.adapters.ehcache;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hp.cache4guice.adapters.CacheAdapter;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class EhCache implements CacheAdapter {

    private final Cache cache;
    private int timeToLiveSeconds;

    @Inject
    public EhCache(Cache cache, @Named("timeToLiveSeconds") String timeToLiveSeconds) {
        this.cache = cache;
        this.timeToLiveSeconds = Integer.parseInt(timeToLiveSeconds);
    }

    @Override
    public Object get(String key) {
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, timeToLiveSeconds);
    }

    @Override
    public void put(String key, Object value, int timeToLiveInSeconds) {
        Element element = new Element(key, value, Boolean.FALSE, 0, timeToLiveInSeconds);
        cache.put(element);
    }

    public boolean isKeyInCache(String key) {
        return cache.isKeyInCache(key);
    }
}
