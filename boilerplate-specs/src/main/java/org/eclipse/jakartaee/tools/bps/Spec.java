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
import org.apache.openejb.loader.IO;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Spec {

    private String projectId;
    private String projectName;
    private String oldProjectName;
    private String projectUrl;
    private String specName;
    private String specCode;
    private String specVersion;

    private String specRepo;
    private String apiRepo;
    private String tckRepo;


    public static List<Spec> loadTsv(final InputStream in) throws IOException {
        final String content = IO.slurp(in);
        final List<String> lines = new ArrayList<>(Arrays.asList(content.split("\n")));

        // first line is metadata
        lines.remove(0);

        return lines.stream()
                .map(Spec::fromTsv)
                .collect(Collectors.toList());
    }

    public static Spec fromTsv(final String row) {
        final String[] column = row.split("\t");

        int i = 0;
        return Spec.builder()
                .projectId(column[i++])
                .projectName(column[i++])
                .oldProjectName(column[i++])
                .projectUrl(column[i++])
                .specName(column[i++])
                .specCode(column[i++])
                .specVersion(column[i++])
                .build();
    }

    public static List<Spec> loadTsv() throws IOException {
        final URL tsvUrl = Spec.class.getClassLoader().getResource("rawdata.tsv");
        return loadTsv(org.tomitribe.util.IO.read(tsvUrl));
    }
}
