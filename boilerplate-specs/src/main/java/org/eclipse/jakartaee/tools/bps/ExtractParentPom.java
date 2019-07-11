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

import lombok.Data;
import org.apache.openejb.loader.IO;
import org.tomitribe.swizzle.stream.StreamBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExtractParentPom {


    public static String from(final File file) throws IOException {
        final Pom pom = new Pom();

        final InputStream inputStream = IO.read(file);
        new StreamBuilder(inputStream)
                .watch("<!--\n", "-->\n", pom::setHeader)
                .watch("\n    <parent>", "</parent>", pom::setParent)
                .watch("\n    <name>", "</name>", pom::setName)
                .watch("\n    <groupId>jakarta.", "</groupId>", pom::setShortName)
                .watch("\n    <artifactId>jakarta.", "</artifactId>", pom::setShortName)
                .watch("\n    <url>https://github.com/eclipse-ee4j/", "</url>", pom::setShortName)
                .watch("\n    <url>https://projects.eclipse.org/projects/ee4j.", "</url>", pom::setShortName)
                .watch("\n    <version>", "</version>", pom::setVersion)
                .watch("\n    <url>", "</url>", pom::setUrl)
                .watch("\n    <description>", "</description>", pom::setDescription)
                .watch("\n    <licenses>", "</licenses>", pom::setLicenses)
                .watch("\n    <scm>", "</scm>", pom::setScm)
                .watch(">https://github.com/eclipse-ee4j/", "</", pom::setRepoName)
                .to(new OutputStream() {
                    @Override
                    public void write(final int b) throws IOException {

                    }
                });

        Templates.interpolate(pom);

        return pom.getPom();
    }

    @Data
    public static class Pom {

        private String shortName;
        private String year = "2019";

        private String header =
                "\n" +
                        "    Copyright (c) {year} Contributors to the Eclipse Foundation \n" +
                        "\n" +
                        "    This program and the accompanying materials are made available under the\n" +
                        "    terms of the Eclipse Public License v. 2.0, which is available at\n" +
                        "    http://www.eclipse.org/legal/epl-2.0.\n" +
                        "\n" +
                        "    This Source Code may also be made available under the following Secondary\n" +
                        "    Licenses when the conditions for such availability set forth in the\n" +
                        "    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,\n" +
                        "    version 2 with the GNU Classpath Exception, which is available at\n" +
                        "    https://www.gnu.org/software/classpath/license.html.\n" +
                        "\n" +
                        "    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0\n" +
                        "\n";

        private String licenses = "\n" +
                "        <license>\n" +
                "            <name>Eclipse Public License 2.0</name>\n" +
                "            <url>https://projects.eclipse.org/license/epl-2.0</url>\n" +
                "            <distribution>repo</distribution>\n" +
                "        </license>\n" +
                "        <license>\n" +
                "            <name>GNU General Public License, version 2 with the GNU Classpath Exception</name>\n" +
                "            <url>https://projects.eclipse.org/license/secondary-gpl-2.0-cp</url>\n" +
                "            <distribution>repo</distribution>\n" +
                "        </license>\n" +
                "    ";

        private String parent = "\n" +
                "        <groupId>org.eclipse.ee4j</groupId>\n" +
                "        <artifactId>project</artifactId>\n" +
                "        <version>1.0.5</version>\n" +
                "    ";

        private String repoName = "{shortName}-api";

        private String scm = "\n" +
                "        <connection>scm:git:git://github.com/eclipse-ee4j/{repoName}.git</connection>\n" +
                "        <developerConnection>scm:git:git@github.com:eclipse-ee4j/{repoName}.git</developerConnection>\n" +
                "        <url>https://github.com/eclipse-ee4j/{repoName}</url>\n" +
                "        <tag>HEAD</tag>\n" +
                "    ";

        private String description = "{shortName}";
        private String groupId = "org.eclipse.ee4j.{shortName}";
        private String artifactId = "{shortName}-parent";
        private String version;
        private String name = "{shortName}";
        private String url = "https://github.com/eclipse-ee4j/{shortName}";

        private String pom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!--\n" +
                "{header}" +
                "-->\n" +
                "\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "\n" +
                "    <parent>{parent}</parent>\n" +
                "\n" +
                "    <groupId>{groupId}</groupId>\n" +
                "    <artifactId>{artifactId}</artifactId>\n" +
                "    <packaging>pom</packaging>\n" +
                "    <version>{version}</version>\n" +
                "    <name>{name}</name>\n" +
                "    <description>{description}</description>\n" +
                "    <url>{url}</url>\n" +
                "\n" +
                "    <scm>{scm}</scm>\n" +
                "\n" +
                "    <modules>\n" +
                "        <module>api</module>\n" +
                "    </modules>\n" +
                "\n" +
                "    <licenses>{licenses}</licenses>\n" +
                "\n" +
                "</project>\n";
    }
}
