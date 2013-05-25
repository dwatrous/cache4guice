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
package com.hp.cache4guice.adapters.infinispan;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.hp.cache4guice.Cached;
import com.hp.cache4guice.adapters.BaseCacheModule;
import com.hp.cache4guice.aop.CacheInterceptor;
import com.hp.cache4guice.adapters.CacheAdapter;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

public class InfinispanCacheModule extends BaseCacheModule {

    @Override
    protected void configure() {
        // load external properties
        Names.bindProperties(binder(), loadProperties());

        bind(new TypeLiteral<Cache<String, Object>>() {}).toProvider(CacheProvider.class).in(Singleton.class);
        bind(CacheAdapter.class).to(InfinispanCache.class).in(Singleton.class);

        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        requestInjection(cacheInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Cached.class), cacheInterceptor);
    }

    /**
     * This Provider provides the infinispan Cache to the adapter
     */
    static class CacheProvider implements Provider<Cache<String, Object>> {
        
        private static DefaultCacheManager defaultCacheManager;
        private static final String CACHE_NAME = InfinispanCacheModule.class.getName();

        public Cache<String, Object> get() {
            if (defaultCacheManager == null) {
                defaultCacheManager = new DefaultCacheManager();
            }
            Cache<String, Object> cache = defaultCacheManager.getCache(CACHE_NAME);
            return cache;
        }
        
    }

}
