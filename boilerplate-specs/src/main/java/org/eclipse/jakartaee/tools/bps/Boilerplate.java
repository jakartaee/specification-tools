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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tomitribe.util.IO;
import org.tomitribe.util.collect.ObjectMap;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Boilerplate {

    private String readmeMd;
    private String pomXml;
    private String assemblyXml;
    private String scopeAdoc;
    private String specAdoc;
    private String themeYaml;
    private String licenseEfslAdoc;
    private byte[] jakartaEeLogoPng;
    private Spec spec;

    public static Boilerplate loadFor(final File pomFile) throws IOException {
        final ExtractParentPom.Pom pom = ExtractParentPom.getPom(pomFile);

        final List<Spec> specs = Spec.loadTsv();

        final Spec spec = getSpec(pom, specs);

        final Map<String, Object> data = new HashMap<>();
        data.putAll(new ObjectMap(pom));
        data.putAll(new ObjectMap(spec));
        data.put("parentArtifactId", data.remove("artifactId"));

        normalizeSpecVersion(data);

        data.put("scopeStatement", load("scopes/" + data.get("specCode") + ".txt"));
        data.put("README.md", load("spec-template/README.md"));
        data.put("pom.xml", load("spec-template/pom.xml"));
        data.put("assembly.xml", load("spec-template/assembly.xml"));
        data.put("scope.adoc", load("spec-template/src/main/asciidoc/scope.adoc"));
        data.put("spec.adoc", load("spec-template/src/main/asciidoc/wombat-spec.adoc"));

        Templates.interpolate(data);

        return Boilerplate.builder()
                .readmeMd(data.get("README.md").toString())
                .pomXml(data.get("pom.xml").toString())
                .assemblyXml(data.get("assembly.xml").toString())
                .scopeAdoc(data.get("scope.adoc").toString())
                .specAdoc(data.get("spec.adoc").toString())
                .themeYaml(load("spec-template/src/main/theme/jakartaee-theme.yml"))
                .licenseEfslAdoc(load("spec-template/src/main/asciidoc/license-efsl.adoc"))
                .jakartaEeLogoPng(loadBytes("spec-template/src/main/asciidoc/images/jakarta_ee_logo_schooner_color_stacked_default.png"))
                .spec(spec)
                .build();
    }

    public static Spec getSpec(final ExtractParentPom.Pom pom, final List<Spec> specs) {
        {
            final Optional<Spec> spec = specs.stream()
                    .filter(s -> s != null)
                    .filter(s -> s.getProjectId().contains(pom.getShortName()))
                    .findFirst();
            if (spec.isPresent()) return spec.get();
        }
        {
            final Optional<Spec> spec = specs.stream()
                    .filter(s -> s != null)
                    .filter(s -> s.getSpecCode().toLowerCase().contains(pom.getShortName().toLowerCase()))
                    .findFirst();
            if (spec.isPresent()) return spec.get();
        }
        {
            final Optional<Spec> spec = specs.stream()
                    .filter(s -> s != null)
                    .filter(s -> s.getProjectName().toLowerCase().contains(pom.getShortName().toLowerCase()))
                    .findFirst();
            if (spec.isPresent()) return spec.get();
        }


        throw new IllegalStateException("Cannot find a spec descriptor for " + pom.getShortName());
    }


    public static String load(final String name) throws IOException {
        return IO.slurp(Boilerplate.class.getClassLoader().getResource(name));
    }

    public static byte[] loadBytes(final String name) throws IOException {
        return IO.readBytes(Boilerplate.class.getClassLoader().getResource(name));
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
