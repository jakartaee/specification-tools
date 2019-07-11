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

import org.apache.openejb.loader.Files;
import org.apache.openejb.loader.IO;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.tomitribe.swizzle.stream.StreamBuilder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.function.Consumer;

import static java.nio.file.Files.createDirectory;

public class AddBoilerplateSpec {

    private static final GitHub github = Env.github();

    public static void main(String[] args) throws Exception {
        new AddBoilerplateSpec().main();
    }

    public void main() throws Exception {
        final File file = new File(Dirs.work().repos(), "jms-api");


        final GHOrganization ee4j = github.getOrganization("eclipse-ee4j");
        final GHRepository jmsApi = ee4j.getRepository("jms-api");

        final GHRepository fork = jmsApi.fork();
        final File forks = Files.mkdirs(Dirs.work().forks());

        final Git git;

        final File clone = new File(forks, fork.getName());
        if (!clone.exists()) {
            git = Git.cloneRepository()
                    .setURI(jmsApi.getSshUrl())
                    .setDirectory(clone)
                    .call();
            git.remoteAdd().setName("eclipse").setUri(new URIish(jmsApi.getSshUrl())).call();
        } else {
            git = Git.open(clone);
            git.remoteAdd().setName("eclipse").setUri(new URIish(jmsApi.getSshUrl())).call();
            git.pull().setRemote("eclipse").setRemoteBranchName("master").call();
        }

        final Path project = clone.toPath();

        if (project.resolve("src").toFile().exists()) {
            createApiSubmodule(git, project);
        }
    }

    /**
     * Move the `src` directory into an `api` submodule, add a parent pom and commit it
     */
    public void createApiSubmodule(final Git git, final Path project) throws IOException, GitAPIException {
        final Path api = createDirectory(project.resolve("api"));

        java.nio.file.Files.move(project.resolve("src"), api.resolve("src"));
        java.nio.file.Files.move(project.resolve("pom.xml"), api.resolve("pom.xml"));
        git.add().addFilepattern("api").call();
        git.add().addFilepattern("spec").call();
        git.commit()
                .setMessage("Converting to an api submodule")
                .setAll(true).call();

        final String parent = ExtractParentPom.from(api.resolve("pom.xml").toFile());
        IO.copy(IO.read(parent), project.resolve("pom.xml").toFile());
    }
}
