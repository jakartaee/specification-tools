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

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.IOException;

public class CloneRepos {

    private static final GitHub github = Env.github();

    public static void main(String[] args) throws IOException {
        main();
    }

    public static void main() throws IOException {

        final File repos = Dirs.work().repos();
        repos.mkdirs();

        for (final GHRepository repo : github.getOrganization("eclipse-ee4j").listRepositories()) {
            System.out.printf("Cloning repo %s%n", repo.getName());
            try {
                clone(repos, repo);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    public static File clone(final File repos, final GHRepository repo) throws Exception {
        final File dest = new File(repos, repo.getName());
        jgit("clone", repo.getSshUrl(), dest.getAbsolutePath());
        return dest;
    }

    public static void jgit(final String... args) throws Exception {
        org.eclipse.jgit.pgm.Main.main(args);
    }
}
