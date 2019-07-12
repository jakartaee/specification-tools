/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.eclipse.jakartaee.tools.bps;

import org.tomitribe.util.collect.ObjectMap;

/**
 * The javamail pom.xml data is not totally accurate as it uses
 * an incorrect repo name of `git@github.com/eclipse-ee4j/mail.git`
 * while it is actually `git@github.com/eclipse-ee4j/javamail.git`
 */
public class Javamail {

    public static void correct(final ExtractParentPom.Pom pom) {
        pom.setRepoName("javamail");
        new ObjectMap(pom)
                .entrySet().stream()
                .filter(entry -> !entry.getKey().equals("class"))
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> entry.getValue() instanceof String)
                .forEach(entry -> entry.setValue(((String) entry.getValue()).replace("/mail.git", "/javamail.git")));
    }
}
