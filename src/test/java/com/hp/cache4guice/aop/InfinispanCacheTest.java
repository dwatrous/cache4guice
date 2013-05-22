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

import com.google.inject.Guice;
import com.hp.cache4guice.adapters.infinispan.InfinispanCacheModule;

public class InfinispanCacheTest extends TestBaseClass {

    @Override
    protected void setUp() {
        // ensure that only one injector is created
        injector = Guice.createInjector(new InfinispanCacheModule());
    }

    @Override
    protected void tearDown() {
        // pass
    }

}
