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
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectory;

public class AddBoilerplateSpec {

    private static final GitHub github = Env.github();

    public static void main(String[] args) throws Exception {
        new AddBoilerplateSpec().main("jms-api");
    }

    public void main(final String name) throws Exception {
        main(name, "master");
        main(name, "EE4J_8");
    }
    public void main(final String name, final String branch) throws Exception {
        final String master = branch;

        final GHOrganization ee4j = github.getOrganization("eclipse-ee4j");
        final GHRepository apiRepo = ee4j.getRepository(name);

        final GHRepository fork = apiRepo.fork();
        final File forks = Files.mkdirs(Dirs.work().forks());

        final Git git;

        final File clone = new File(forks, fork.getName() + "-" + master);

        if (!clone.exists()) {
            git = Git.cloneRepository()
                    .setURI(fork.getSshUrl())
                    .setBranch(master)
                    .setDirectory(clone)
                    .call();
            git.remoteAdd().setName("eclipse").setUri(new URIish(apiRepo.getSshUrl())).call();
        } else {
            git = Git.open(clone);
            git.remoteAdd().setName("eclipse").setUri(new URIish(apiRepo.getSshUrl())).call();
            git.pull().setRemote("eclipse").setRemoteBranchName(master).call();
        }

        final Path project = clone.toPath();

        /**
         * if it already has a `spec` dir, skip it
         */
        if (project.resolve("spec").toFile().exists()) {
            return;
        }

        /**
         * if it is a single module project, make it multi-module
         */
        if (project.resolve("src").toFile().exists()) {
            createApiSubmodule(git, project);
        }

        createSpecSubmodule(git, project);

        createPullRequest(git, project, apiRepo, master);

    }

    /**
     * Move the `src` directory into an `api` submodule, add a parent pom and commit it
     */
    public void createApiSubmodule(final Git git, final Path project) throws IOException, GitAPIException {
        final Path api = createDirectory(project.resolve("api"));

        java.nio.file.Files.move(project.resolve("src"), api.resolve("src"));
        java.nio.file.Files.move(project.resolve("pom.xml"), api.resolve("pom.xml"));
        git.add().addFilepattern("api").call();
        git.commit()
                .setMessage("Move src/ to an new api/src/ submodule")
                .setAll(true).call();

        final String parent = ExtractParentPom.from(api.resolve("pom.xml").toFile());
        IO.copy(IO.read(parent), project.resolve("pom.xml").toFile());

        git.add().addFilepattern("pom.xml").call();
        git.commit()
                .setMessage("New parent pom.xml")
                .setAll(true).call();
    }


    private void createSpecSubmodule(final Git git, final Path project) throws IOException, GitAPIException {

        final Path spec = createDirectory(project.resolve("spec"));
        final Path src = createDirectory(project.resolve("spec/src"));
        final Path main = createDirectory(project.resolve("spec/src/main"));
        final Path asciidoc = createDirectory(project.resolve("spec/src/main/asciidoc"));
        final Path images = createDirectory(project.resolve("spec/src/main/asciidoc/images"));
        final Path theme = createDirectory(project.resolve("spec/src/main/theme"));

        final File parentPomXml = project.resolve("pom.xml").toFile();

        final Boilerplate boilerplate = Boilerplate.loadFor(parentPomXml);

        IO.copy(IO.read(boilerplate.getAssemblyXml()), spec.resolve("assembly.xml").toFile());
        IO.copy(IO.read(boilerplate.getPomXml()), spec.resolve("pom.xml").toFile());
        IO.copy(IO.read(boilerplate.getReadmeMd()), spec.resolve("README.md").toFile());
        IO.copy(IO.read(boilerplate.getScopeAdoc()), asciidoc.resolve("scope.adoc").toFile());
        IO.copy(IO.read(boilerplate.getSpecAdoc()), asciidoc.resolve(boilerplate.getSpec().getSpecCode() + "-spec.adoc").toFile());
        IO.copy(IO.read(boilerplate.getThemeYaml()), theme.resolve("jakartaee-theme.yml").toFile());
        IO.copy(IO.read(boilerplate.getLicenseEfslAdoc()), asciidoc.resolve("license-efsl.adoc").toFile());
        IO.copy(IO.read(boilerplate.getJakartaEeLogoPng()), images.resolve("jakarta_ee_logo_schooner_color_stacked_default.png").toFile());


        final String updatedPom = IO.slurp(parentPomXml)
                .replaceAll("(        <module>api</module>\n)", "$1        <module>spec</module>\n");
        IO.copy(IO.read(updatedPom), parentPomXml);

        git.add().addFilepattern("spec").call();
        git.commit()
                .setMessage("Add spec/ submodule with boilerplate asciidoc")
                .setAll(true).call();

    }

    private void createPullRequest(final Git git, final Path project, GHRepository repo, final String branch) throws GitAPIException, IOException {
        final String s = "boilerplate-spec-" + branch;
        git.branchCreate().setName(s).call();
        git.push().setRemote("origin").setRefSpecs(new RefSpec(s + ":" + s)).call();

        repo.createPullRequest("Boilerplate Spec for " + branch + " Branch", "dblevins:" + s, branch, "Generated boilerplate spec.  Feel free to merge, then refine.\n\n See https://wiki.eclipse.org/How_to_Prepare_API_Projects_to_Jakarta_EE_8_Release");
    }
}
