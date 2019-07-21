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

import java.util.Arrays;
import java.util.List;

public class Main {

    public static final List<String> list = Arrays.asList(
            "common-annotations-api",
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

    public static final List<String> needed = Arrays.asList(
//            "el-ri",
            "enterprise-deployment",
//            "jax-rpc-api",
            "jaxr-api"
//            "management-api",
//            "jaf"
    );


    public static void main(String[] args) throws Exception {

        final Config config = Config.builder()
//                .branch("master")
//                .branch("EE4J_8")
                .branch("master")
                .deleteLocal(true)
                .pushFork(true)
                .createFork(true)
                .createPrs(true)
                .deleteLocal(true)
                .build();

        final AddBoilerplateSpec generator = new AddBoilerplateSpec(config);

        for (final String spec : needed) {
            try {
                generator.run(spec);
            } catch (Exception e) {
                System.err.println("Spec failed " + spec);
                e.printStackTrace();
            }
        }

    }
    public static void trying(String[] args) throws Exception {
        final Config config = Config.builder()
                .branch("master")
                .deleteLocal(true)
                .build();

        final AddBoilerplateSpec generator = new AddBoilerplateSpec(config);

        for (final String spec : list) {
            try {
                generator.run(spec);
            } catch (Exception e) {
                System.err.println("Spec failed " + spec);
                e.printStackTrace();
            }
        }

    }
}
