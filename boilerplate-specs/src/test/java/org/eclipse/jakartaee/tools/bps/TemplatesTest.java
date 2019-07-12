package org.eclipse.jakartaee.tools.bps;

import org.apache.openejb.util.Join;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TemplatesTest {

    @Test
    public void simple() throws Exception {
        final HashMap<String, Object> map = new HashMap<>();

        map.put("year", "2019");
        map.put("header", "Copyright (c) {year}");


        final String expected = "header: Copyright (c) 2019\n" +
                "-------\n" +
                "year: 2019";

        assertInterpolation(map, expected);
    }

    @Test
    public void ordered() throws Exception {
        final HashMap<String, Object> map = new HashMap<>();

        map.put("year", "2019");
        map.put("shortName", "jms");
        map.put("repoName", "{shortName}-api");
        map.put("scmUrl", "git://github.com/eclipse-ee4j/{repoName}.git");


        final String expected = "repoName: jms-api\n" +
                "-------\n" +
                "scmUrl: git://github.com/eclipse-ee4j/jms-api.git\n" +
                "-------\n" +
                "shortName: jms\n" +
                "-------\n" +
                "year: 2019";

        assertInterpolation(map, expected);
    }


    @Test
    public void ignoresDollarBrace() throws Exception {
        final HashMap<String, Object> map = new HashMap<>();

        map.put("year", "2019");
        map.put("shortName", "jms");
        map.put("repoName", "{shortName}-api");
        map.put("scmUrl", "git://${github}.com/eclipse-ee4j/{repoName}.git");


        final String expected = "repoName: jms-api\n" +
                "-------\n" +
                "scmUrl: git://${github}.com/eclipse-ee4j/jms-api.git\n" +
                "-------\n" +
                "shortName: jms\n" +
                "-------\n" +
                "year: 2019";

        assertInterpolation(map, expected);
    }

    @Test
    public void unresolvableItem() throws Exception {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("first", "Joe");
        map.put("last", "Cool");
        map.put("fullName", "{first} {mi} {last}");

        assertInterpolation(map, "first: Joe\n" +
                "-------\n" +
                "fullName: Joe {mi} Cool\n" +
                "-------\n" +
                "last: Cool");
    }

    @Test
    public void ignoreDollarBrace2() throws Exception {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("first", "Joe ${nickname}");
        map.put("last", "Cool");
        map.put("fullName", "{first} {mi} {last}");

        assertInterpolation(map, "first: Joe ${nickname}\n" +
                "-------\n" +
                "fullName: Joe ${nickname} {mi} Cool\n" +
                "-------\n" +
                "last: Cool");
    }

    @Test
    public void unresolvableItemComplex() throws Exception {
        final HashMap<String, Object> map = new HashMap<>();

        map.put("repoName", "common-annotations-api/issues");
        map.put("name", "${extension.name} API");
        map.put("pom", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "        <!--\n" +
                "        {header}-->\n" +
                "        \n" +
                "        <project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "            <modelVersion>4.0.0</modelVersion>\n" +
                "        \n" +
                "            <parent>{parent}</parent>\n" +
                "        \n" +
                "            <groupId>{groupId}</groupId>\n" +
                "            <artifactId>{artifactId}</artifactId>\n" +
                "            <packaging>pom</packaging>\n" +
                "            <version>{version}</version>\n" +
                "            <name>{name}</name>\n" +
                "            <description>{description}</description>\n" +
                "            <url>{url}</url>\n" +
                "        \n" +
                "            <scm>{scm}</scm>\n" +
                "        \n" +
                "            <modules>\n" +
                "                <module>api</module>\n" +
                "            </modules>\n" +
                "        \n" +
                "            <licenses>{licenses}</licenses>\n" +
                "        \n" +
                "        </project>\n" +
                "        ");
        map.put("header", "\n" +
                "            Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.\n" +
                "        \n" +
                "            This program and the accompanying materials are made available under the\n" +
                "            terms of the Eclipse Public License v. 2.0, which is available at\n" +
                "            http://www.eclipse.org/legal/epl-2.0.\n" +
                "        \n" +
                "            This Source Code may also be made available under the following Secondary\n" +
                "            Licenses when the conditions for such availability set forth in the\n" +
                "            Eclipse Public License v. 2.0 are satisfied: GNU General Public License,\n" +
                "            version 2 with the GNU Classpath Exception, which is available at\n" +
                "            https://www.gnu.org/software/classpath/license.html.\n" +
                "        \n" +
                "            SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0\n" +
                "        \n" +
                "        ");
        map.put("description", "Common Annotations for the JavaTM Platform API");
        map.put("groupId", "org.eclipse.ee4j.{shortName}");
        map.put("url", "https://projects.eclipse.org/projects/ee4j.ca");
        map.put("shortName", "ca");
        map.put("year", "2019");
        map.put("scm", "\n" +
                "                <connection>scm:git:https://github.com/eclipse-ee4j/common-annotations-api.git</connection>\n" +
                "                <developerConnection>scm:git:git@github.com:eclipse-ee4j/common-annotations-api.git</developerConnection>\n" +
                "                <url>https://github.com/eclipse-ee4j/common-annotations-api</url>\n" +
                "                <tag>HEAD</tag>\n" +
                "            ");
        map.put("licenses", "\n" +
                "                <license>\n" +
                "                    <name>EPL 2.0</name>\n" +
                "                    <url>http://www.eclipse.org/legal/epl-2.0</url>\n" +
                "                    <distribution>repo</distribution>\n" +
                "                </license>\n" +
                "                <license>\n" +
                "                    <name>GPL2 w/ CPE</name>\n" +
                "                    <url>https://www.gnu.org/software/classpath/license.html</url>\n" +
                "                    <distribution>repo</distribution>\n" +
                "                </license>\n" +
                "            ");
        map.put("artifactId", "{shortName}-parent");
        map.put("parent", "\n" +
                "                <groupId>org.eclipse.ee4j</groupId>\n" +
                "                <artifactId>project</artifactId>\n" +
                "                <version>1.0.4</version>\n" +
                "            ");
        map.put("version", "1.3.3-SNAPSHOT");


        final String expected = "artifactId: ca-parent\n" +
                "-------\n" +
                "description: Common Annotations for the JavaTM Platform API\n" +
                "-------\n" +
                "groupId: org.eclipse.ee4j.ca\n" +
                "-------\n" +
                "header: \n" +
                "            Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.\n" +
                "        \n" +
                "            This program and the accompanying materials are made available under the\n" +
                "            terms of the Eclipse Public License v. 2.0, which is available at\n" +
                "            http://www.eclipse.org/legal/epl-2.0.\n" +
                "        \n" +
                "            This Source Code may also be made available under the following Secondary\n" +
                "            Licenses when the conditions for such availability set forth in the\n" +
                "            Eclipse Public License v. 2.0 are satisfied: GNU General Public License,\n" +
                "            version 2 with the GNU Classpath Exception, which is available at\n" +
                "            https://www.gnu.org/software/classpath/license.html.\n" +
                "        \n" +
                "            SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0\n" +
                "        \n" +
                "        \n" +
                "-------\n" +
                "licenses: \n" +
                "                <license>\n" +
                "                    <name>EPL 2.0</name>\n" +
                "                    <url>http://www.eclipse.org/legal/epl-2.0</url>\n" +
                "                    <distribution>repo</distribution>\n" +
                "                </license>\n" +
                "                <license>\n" +
                "                    <name>GPL2 w/ CPE</name>\n" +
                "                    <url>https://www.gnu.org/software/classpath/license.html</url>\n" +
                "                    <distribution>repo</distribution>\n" +
                "                </license>\n" +
                "            \n" +
                "-------\n" +
                "name: ${extension.name} API\n" +
                "-------\n" +
                "parent: \n" +
                "                <groupId>org.eclipse.ee4j</groupId>\n" +
                "                <artifactId>project</artifactId>\n" +
                "                <version>1.0.4</version>\n" +
                "            \n" +
                "-------\n" +
                "pom: <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "        <!--\n" +
                "        \n" +
                "            Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.\n" +
                "        \n" +
                "            This program and the accompanying materials are made available under the\n" +
                "            terms of the Eclipse Public License v. 2.0, which is available at\n" +
                "            http://www.eclipse.org/legal/epl-2.0.\n" +
                "        \n" +
                "            This Source Code may also be made available under the following Secondary\n" +
                "            Licenses when the conditions for such availability set forth in the\n" +
                "            Eclipse Public License v. 2.0 are satisfied: GNU General Public License,\n" +
                "            version 2 with the GNU Classpath Exception, which is available at\n" +
                "            https://www.gnu.org/software/classpath/license.html.\n" +
                "        \n" +
                "            SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0\n" +
                "        \n" +
                "        -->\n" +
                "        \n" +
                "        <project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "            <modelVersion>4.0.0</modelVersion>\n" +
                "        \n" +
                "            <parent>\n" +
                "                <groupId>org.eclipse.ee4j</groupId>\n" +
                "                <artifactId>project</artifactId>\n" +
                "                <version>1.0.4</version>\n" +
                "            </parent>\n" +
                "        \n" +
                "            <groupId>org.eclipse.ee4j.ca</groupId>\n" +
                "            <artifactId>ca-parent</artifactId>\n" +
                "            <packaging>pom</packaging>\n" +
                "            <version>1.3.3-SNAPSHOT</version>\n" +
                "            <name>${extension.name} API</name>\n" +
                "            <description>Common Annotations for the JavaTM Platform API</description>\n" +
                "            <url>https://projects.eclipse.org/projects/ee4j.ca</url>\n" +
                "        \n" +
                "            <scm>\n" +
                "                <connection>scm:git:https://github.com/eclipse-ee4j/common-annotations-api.git</connection>\n" +
                "                <developerConnection>scm:git:git@github.com:eclipse-ee4j/common-annotations-api.git</developerConnection>\n" +
                "                <url>https://github.com/eclipse-ee4j/common-annotations-api</url>\n" +
                "                <tag>HEAD</tag>\n" +
                "            </scm>\n" +
                "        \n" +
                "            <modules>\n" +
                "                <module>api</module>\n" +
                "            </modules>\n" +
                "        \n" +
                "            <licenses>\n" +
                "                <license>\n" +
                "                    <name>EPL 2.0</name>\n" +
                "                    <url>http://www.eclipse.org/legal/epl-2.0</url>\n" +
                "                    <distribution>repo</distribution>\n" +
                "                </license>\n" +
                "                <license>\n" +
                "                    <name>GPL2 w/ CPE</name>\n" +
                "                    <url>https://www.gnu.org/software/classpath/license.html</url>\n" +
                "                    <distribution>repo</distribution>\n" +
                "                </license>\n" +
                "            </licenses>\n" +
                "        \n" +
                "        </project>\n" +
                "        \n" +
                "-------\n" +
                "repoName: common-annotations-api/issues\n" +
                "-------\n" +
                "scm: \n" +
                "                <connection>scm:git:https://github.com/eclipse-ee4j/common-annotations-api.git</connection>\n" +
                "                <developerConnection>scm:git:git@github.com:eclipse-ee4j/common-annotations-api.git</developerConnection>\n" +
                "                <url>https://github.com/eclipse-ee4j/common-annotations-api</url>\n" +
                "                <tag>HEAD</tag>\n" +
                "            \n" +
                "-------\n" +
                "shortName: ca\n" +
                "-------\n" +
                "url: https://projects.eclipse.org/projects/ee4j.ca\n" +
                "-------\n" +
                "version: 1.3.3-SNAPSHOT\n" +
                "-------\n" +
                "year: 2019";

        assertInterpolation(map, expected);
    }


    public static void assertInterpolation(final HashMap<String, Object> map, final String expected) {
        Templates.interpolate(map);

        final List<String> list = map.entrySet().stream()
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        assertEquals(expected, Join.join("\n-------\n", list));
    }
}