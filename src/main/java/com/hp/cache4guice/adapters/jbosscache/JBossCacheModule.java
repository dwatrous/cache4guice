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

import org.jboss.cache.Cache;
import org.jboss.cache.DefaultCacheFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.hp.cache4guice.Cached;
import com.hp.cache4guice.adapters.BaseCacheModule;
import com.hp.cache4guice.adapters.CacheAdapter;
import com.hp.cache4guice.aop.CacheInterceptor;

public class JBossCacheModule extends BaseCacheModule {

    @Override
    protected void configure() {
        // load external properties
        Names.bindProperties(binder(), loadProperties());

        bind(new TypeLiteral<Cache<String, Object>>() {}).toProvider(CacheProvider.class).in(Singleton.class);
        bind(CacheAdapter.class).to(JBossCache.class).in(Singleton.class);

        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        requestInjection(cacheInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Cached.class), cacheInterceptor);
    }

    /**
     * This Provider provides the JBoss Cache to the adapter
     */
    static class CacheProvider implements Provider<Cache<String, Object>> {

        public Cache<String, Object> get() {
            Cache<String, Object> cache = new DefaultCacheFactory<String, Object>().createCache();
            return cache;
        }
    }

}
