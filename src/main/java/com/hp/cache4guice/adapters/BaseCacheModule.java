/*
 * Copyright 2013 watrous.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hp.cache4guice.adapters;

import com.google.inject.AbstractModule;
import com.hp.cache4guice.adapters.ehcache.EhCacheModule;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author watrous
 */
public abstract class BaseCacheModule extends AbstractModule {

    private Properties properties = new Properties();
    private Logger log = Logger.getLogger(EhCacheModule.class.getName());

    protected Properties loadProperties() {
        try {
            // first we try to load a configuration file on the class path
            InputStream configurationProperties = EhCacheModule.class.getClassLoader().getResourceAsStream("c4g.properties");
            // if there is no configuration file, we'll get a null here
            if (configurationProperties != null) {
                properties.load(configurationProperties);
                log.info("Loaded c4g.properties");
            } else {
                // we should always be safe loading c4g-default.properties because it's included in the release jar
                properties.load(EhCacheModule.class.getClassLoader().getResourceAsStream("c4g-default.properties"));
                log.info("Loaded c4g-default.properties");
            }
        } catch (IOException ex) {
            // ah crap
            System.out.println("FAILED TO LOAD c4g.properties. See details below.");
            System.out.println(ex.getMessage());
        }
        return properties;
    }
}
