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
package com.hp.cache4guice.key;

import org.aopalliance.intercept.MethodInvocation;

public abstract class AbstractKeyGenerator implements KeyGenerator {

    public String getCacheKey(MethodInvocation methodInvocation) {
        String key = getDefaultKeyName(methodInvocation);
        if (isNotEmpty(methodInvocation.getArguments())) {
            key += getKeyFromArguments(methodInvocation.getArguments());
        }
        return key;
    }

    protected String getKeyFromArguments(Object[] arguments) {
        StringBuilder key = new StringBuilder();
        key.append("(").append(arguments[0]);
        for (int i = 1; i < arguments.length; i++) {
            key.append(", ").append(getKey(arguments[i]));
        }
        key.append(")");
        return key.toString();
    }

    protected String getDefaultKeyName(MethodInvocation methodInvocation) {
        return methodInvocation.getThis().getClass().getName() + "#" + methodInvocation.getMethod().getName();
    }

    protected boolean isNotEmpty(Object[] arguments) {
        return arguments != null && arguments.length != 0;
    }

    public abstract String getKey(Object keyObject);

}
