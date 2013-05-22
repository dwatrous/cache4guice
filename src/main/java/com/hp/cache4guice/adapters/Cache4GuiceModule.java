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
package com.hp.cache4guice.adapters;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.hp.cache4guice.Cached;
import com.hp.cache4guice.SimpleCache;
import com.hp.cache4guice.aop.CacheInterceptor;

public class Cache4GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CacheAdapter.class).to(SimpleCache.class).in(Singleton.class);
        
        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        requestInjection(cacheInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Cached.class), cacheInterceptor);
    }

}
