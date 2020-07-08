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
package org.eclipse.jakartaee.tools.reviews;

import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Option;
import org.tomitribe.crest.api.Out;
import org.tomitribe.util.Files;
import org.tomitribe.util.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AssignCommand {

    @Command
    public void assign(@Out final PrintStream out,
                       @Option("specifications") final File specificationsTxt,
                       @Option("reviewers") final File reviewersTxt) throws IOException {
        final List<String> specifications = load(specificationsTxt, "specifications.txt");
        final List<String> reviewers = load(reviewersTxt, "members.txt");

        // Shuffle the reviewers list so people aren't always matched
        // to the same specifications.
        Collections.shuffle(reviewers);

        final Iterator<String> reviewer = new InfiniteIterable<>(reviewers).iterator();

        for (final String specification : specifications) {
            out.println(specification);
            out.printf(" - %s%n%n", reviewer.next());
        }
    }

    private List<String> load(final File file, final String defaultSource) throws IOException {
        final InputStream source;

        if (file != null) {
            Files.exists(file);
            Files.readable(file);
            source = IO.read(file);
        } else {
            final URL resource = this.getClass().getClassLoader().getResource(defaultSource);
            if (resource == null) {
                throw new IllegalStateException("Missing classpath resource: " + defaultSource);
            }
            source = IO.read(resource);
        }

        return new BufferedReader(new InputStreamReader(source))
                .lines()
                .collect(Collectors.toList());
    }
}
