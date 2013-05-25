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
package com.hp.cache4guice.aop;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hp.cache4guice.Cached;
import com.hp.cache4guice.adapters.CacheAdapter;
import com.hp.cache4guice.key.KeyGenerator;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheInterceptor implements MethodInterceptor {

    @Inject private final CacheAdapter cache = null;
    @Inject @Named("timeToLiveSeconds") private String timeToLiveSeconds;
    private static final Logger LOG = LoggerFactory.getLogger(CacheInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String cacheKey = getCacheKey(methodInvocation);
        if (cache.isKeyInCache(cacheKey)) {
            Object cacheElement = cache.get(cacheKey);
            LOG.debug("Returning element in cache: {}", cacheElement);
            return cacheElement;
        }
        return getResultAndCache(methodInvocation, cacheKey);
    }

    Object getResultAndCache(MethodInvocation methodInvocation, String cacheKey) throws Throwable {
        LOG.debug("Unable to find element in cache.  Calling method to calculate value...");
        Object result = methodInvocation.proceed();
        int ttl = getTTL(methodInvocation);
        LOG.debug("Putting result in cache: {}", result);
        cache.put(cacheKey, result, ttl);
        return result;
    }

    int getTTL(MethodInvocation methodInvocation) {
        int ttl = getCachedAnnotation(methodInvocation).timeToLiveSeconds();
        if (ttl > 0) {
            return ttl;
        } else {
            return Integer.parseInt(timeToLiveSeconds);
        }
    }

    String getCacheKey(MethodInvocation methodInvocation) throws Throwable {
        Cached cacheAnnotation = getCachedAnnotation(methodInvocation);
        if (cacheAnnotation != null) {
            return getCacheKey(methodInvocation, cacheAnnotation);
        }
        throw new IllegalArgumentException("Method " + methodInvocation.getMethod().getName() + " does not have a "
                + Cached.class.getSimpleName() + " annotation.");
    }

    Cached getCachedAnnotation(MethodInvocation methodInvocation) {
        return methodInvocation.getMethod().getAnnotation(Cached.class);
    }

    String getCacheKey(MethodInvocation methodInvocation, Cached cacheAnnotation) throws Throwable {
        String cacheKey = getKeyGeneatorInstance(cacheAnnotation).getCacheKey(methodInvocation);
        LOG.debug("Using key {} for cache lookup", cacheKey);
        return cacheKey;
    }

    KeyGenerator getKeyGeneatorInstance(Cached cacheAnnotation) throws Throwable {
        Class<? extends KeyGenerator> keyGenerator = cacheAnnotation.keyGeneratorClass();
        return keyGenerator.newInstance();
    }

}
