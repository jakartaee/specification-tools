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

import org.tomitribe.util.IO;
import org.tomitribe.util.collect.ObjectMap;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Boilerplate {
    public static Map<String, Object> loadTemplatesFor(final File file) throws IOException {
        final ExtractParentPom.Pom pom = ExtractParentPom.getPom(file);

        final List<Spec> specs = Spec.loadTsv();

        final Spec spec = specs.stream()
                .filter(s -> s.getProjectId().contains(pom.getShortName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find a spec descriptor for "+pom.getShortName()));

        final Map<String, Object> data = new HashMap<>();
        data.putAll(new ObjectMap(pom));
        data.putAll(new ObjectMap(spec));
        data.put("parentArtifactId", data.remove("artifactId"));

        normalizeSpecVersion(data);

        data.put("README.md", load("spec-template/README.md"));
        data.put("pom.xml", load("spec-template/pom.xml"));
        data.put("assembly.xml", load("spec-template/assembly.xml"));
        data.put("scope.adoc", load("spec-template/src/main/asciidoc/scope.adoc"));
        data.put("spec.adoc", load("spec-template/src/main/asciidoc/wombat-spec.adoc"));

        Templates.interpolate(data);
        return data;
    }

    public static String load(final String name) throws IOException {
        return IO.slurp(Boilerplate.class.getClassLoader().getResource(name));
    }

    public static void normalizeSpecVersion(final Map<String, Object> data) {
        final String key = "specVersion";
        final Object specVersion = data.get(key);
        if (specVersion != null) {
            final String s = (String) specVersion;
            final String version = s.replaceAll("([0-9]+\\.[0-9]+)\\.[0-9]+", "$1");
            data.put(key, version);
        }
    }
}
