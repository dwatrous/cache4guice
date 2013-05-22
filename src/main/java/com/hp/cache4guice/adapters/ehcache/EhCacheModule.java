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

import java.util.concurrent.locks.ReentrantLock;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.hp.cache4guice.Cached;
import com.hp.cache4guice.adapters.CacheAdapter;
import com.hp.cache4guice.aop.CacheInterceptor;

public class EhCacheModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Cache.class).toProvider(CacheProvider.class).in(Singleton.class);
        bind(CacheAdapter.class).to(EhCache.class).in(Singleton.class);

        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        requestInjection(cacheInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Cached.class), cacheInterceptor);
    }

    /**
     * This Provider provides the ehcache Cache to the adapter
     */
    static class CacheProvider implements Provider<Cache> {
        
        private static final ReentrantLock CACHE_CREATE_LOCK = new ReentrantLock();
        private static final String CACHE_NAME = EhCacheModule.class.getName();
        
        public Cache get() {
            CACHE_CREATE_LOCK.lock();
            try {
                Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
                if (cache == null) {
                    CacheManager.create().addCache(CACHE_NAME);
                    cache = CacheManager.getInstance().getCache(CACHE_NAME);
                }
                return cache;
            } finally {
                CACHE_CREATE_LOCK.unlock();
            }
        }
    }
    
}
