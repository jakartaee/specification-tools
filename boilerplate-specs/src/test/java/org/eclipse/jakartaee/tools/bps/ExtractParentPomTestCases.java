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

import org.apache.openejb.loader.IO;
import org.junit.Test;
import org.tomitribe.util.Files;
import org.tomitribe.util.Strings;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ExtractParentPomTestCases {

    private final List<String> list = Arrays.asList("common-annotations-api",
            "concurrency-api",
            "ejb-api",
            "el-ri",
            "enterprise-deployment",
            "interceptor-api",
            "jacc",
            "jaspic",
            "jax-rpc-api",
            "jax-ws-api",
            "jaxb-api",
            "jaxr-api",
            "jaxrs-api",
            "jca-api",
            "jms-api",
            "jpa-api",
            "jsonb-api",
            "jsonp",
            "jsp-api",
            "jstl-api",
            "jta-api",
            "jws-api",
            "management-api",
            "saaj-api",
            "security-api",
            "servlet-api",
            "websocket-api",
            "jaf",
            "javamail"
    );

    public static void main(String[] args) throws Exception {
        final ExtractParentPomTestCases generator = new ExtractParentPomTestCases();
        generator.collectTestFiles();
        generator.generateTestMethod();
    }

    public void collectTestFiles() throws Exception {

        final File repos = Dirs.work().repos();
        final File resources = new File(Dirs.work().parent(), "boilerplate-specs/src/test/resources/");

        for (final String name : list) {
            final File src = new File(repos, name);
            final File dest = Files.mkdirs(new File(resources, name));

            if (new File(src, "pom.xml").exists()) {
                IO.copy(new File(src, "pom.xml"), new File(dest, "pom.xml"));
                IO.copy(IO.read(""), new File(dest, "pom.after.xml"));
            } else if (new File(src, "api/pom.xml").exists()) {
                IO.copy(new File(src, "api/pom.xml"), new File(dest, "pom.xml"));
                IO.copy(IO.read(""), new File(dest, "pom.after.xml"));
            }
        }
    }


    public void generateTestMethod() throws Exception {
        for (final String name : list) {
            System.out.printf("" +
                            "    @Test\n" +
                            "    public void test%s() throws Exception {\n" +
                            "        assertExtraction(\"%s\");\n" +
                            "    }\n",
                    Strings.ucfirst(Strings.camelCase(name)),
                    name
            );
        }
    }

}
