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
package com.hp.cache4guice.adapters.jbosscache;

import com.google.inject.Inject;
import com.hp.cache4guice.adapters.CacheAdapter;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jboss.cache.eviction.ExpirationAlgorithmConfig;

public class JBossCache implements CacheAdapter {

    private final org.jboss.cache.Cache<String, Object> cache;

    @Inject
    public JBossCache(org.jboss.cache.Cache<String, Object> cache) {
        this.cache = cache;
    }

    @Override
    public Object get(String key) {
        Fqn<String> fqn = Fqn.fromString(key);
        return cache.get(fqn, key);
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, 0);
    }

    @Override
    public void put(String key, Object value, int timeToLiveInSeconds) {
        Fqn<String> fqn = Fqn.fromString(key);
        Node<String, Object> node = cache.getNode(fqn);
        if (node == null) {
            node = cache.getRoot().addChild(fqn);
        }
        node.put(key, value);
        node.put(ExpirationAlgorithmConfig.EXPIRATION_KEY, expireTime(timeToLiveInSeconds));
    }

    private Long expireTime(int timeToLiveInSeconds) {
        return new Long(System.currentTimeMillis() + timeToLiveInSeconds * 1000);
    }

    public boolean isKeyInCache(String key) {
        Fqn<String> fqn = Fqn.fromString(key);
        if (cache.get(fqn, key) != null) {
            return true;
        }
        return false;
    }

}
