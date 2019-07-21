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

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.tomitribe.util.Files;
import org.tomitribe.util.IO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectory;

public class AddBoilerplateSpec {

    private static final GitHub github = Env.github();

    private final Config config;

    public AddBoilerplateSpec(final Config config) {
        this.config = config;
    }

    public void run(final String name) throws Exception {
        for (final String branch : config.getBranches()) {
            run(name, branch);
        }
    }

    private void run(final String name, final String branch) throws Exception {

        final GHOrganization ee4j = github.getOrganization("eclipse-ee4j");
        final GHRepository apiRepo = ee4j.getRepository(name);

        /**
         * Fork the repo or optionally clone the main repo
         */
        final GHRepository fork = (config.isCreateFork()) ? apiRepo.fork() : apiRepo;

        final File forks = Files.mkdirs(Dirs.work().forks());

        final Git git;

        final File clone = new File(forks, fork.getName() + "-" + branch);

        // Clean up any prior attempts
        if (config.isDeleteLocal()) Files.remove(clone);

        if (!clone.exists()) {
            git = Git.cloneRepository()
                    .setURI(fork.getSshUrl())
                    .setBranch(branch)
                    .setDirectory(clone)
                    .call();
            git.remoteAdd().setName("eclipse").setUri(new URIish(apiRepo.getSshUrl())).call();
        } else {
            git = Git.open(clone);
            git.remoteAdd().setName("eclipse").setUri(new URIish(apiRepo.getSshUrl())).call();
            git.pull().setRemote("eclipse").setRemoteBranchName(branch).call();
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

        /**
         * if it is a single module project, make it multi-module
         */
        if (!project.resolve("pom.xml").toFile().exists()) {
            createParentPom(git, project);
        }

        createSpecSubmodule(git, project);

        final String s = "boilerplate-spec-" + branch;

        /**
         * We're allowed to push our changes if we've forked
         */
        if (config.isPushFork() && config.isCreateFork()) {
            pushChanges(git, s);
        }

        /**
         * Create a PR for our fork (again, only if we forked)
         */
        if (config.isCreatePrs() && config.isCreateFork()) {
            createPullRequest(apiRepo, branch, s);
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
        git.commit()
                .setMessage("" +
                        "Move src/ to an new api/src/ submodule" +
                        "\n" +
                        "\n" +
                        "Signed-off-by: David Blevins <david.blevins@gmail.com>\n")
                .setAll(true).call();

        final String parent = ExtractParentPom.from(api.resolve("pom.xml").toFile());
        IO.copy(IO.read(parent), project.resolve("pom.xml").toFile());

        git.add().addFilepattern("pom.xml").call();
        git.commit()
                .setMessage("New parent pom.xml" +
                        "\n" +
                        "\n" +
                        "Signed-off-by: David Blevins <david.blevins@gmail.com>\n")
                .setAll(true).call();
    }

    /**
     * Move the `src` directory into an `api` submodule, add a parent pom and commit it
     */
    public void createParentPom(final Git git, final Path project) throws IOException, GitAPIException {
        final Path api = project.resolve("api");
        final File apiPom = api.resolve("pom.xml").toFile();

        if (!apiPom.exists()) throw new IllegalStateException("No api/pom.xml file to copy");

        final String parent = ExtractParentPom.from(apiPom);
        IO.copy(IO.read(parent), project.resolve("pom.xml").toFile());

        git.add().addFilepattern("pom.xml").call();
        git.commit()
                .setMessage("New parent pom.xml" +
                        "\n" +
                        "\n" +
                        "Signed-off-by: David Blevins <david.blevins@gmail.com>\n")
                .setAll(true).call();
    }


    private void createSpecSubmodule(final Git git, final Path project) throws IOException, GitAPIException {

        createSpecFiles(project);

        commitSpecFiles(git);

    }

    private static void commitSpecFiles(final Git git) throws GitAPIException {
        git.add().addFilepattern("spec").call();
        git.commit()
                .setMessage("Add spec/ submodule with boilerplate asciidoc" +
                        "\n" +
                        "\n" +
                        "Signed-off-by: David Blevins <david.blevins@gmail.com>\n")
                .setAll(true).call();
    }

    public static void createSpecFiles(final Path project) throws IOException {
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
    }

    public void pushChanges(final Git git, final String s) throws GitAPIException {
        git.branchCreate().setName(s).call();
        git.push().setRemote("origin").setRefSpecs(new RefSpec(s + ":" + s)).call();
    }

    public void createPullRequest(final GHRepository repo, final String branch, final String s) throws IOException {
        final GHPullRequest pullRequest = repo.createPullRequest("Boilerplate Spec for " + branch + " Branch", "dblevins:" + s, branch, "Generated boilerplate spec.  Feel free to merge, then refine.\n\n See https://wiki.eclipse.org/How_to_Prepare_API_Projects_to_Jakarta_EE_8_Release");
        try {
            System.out.println("HtmlUrl: " + pullRequest.getHtmlUrl());
        } catch (Exception e) {
        }
        try {
            System.out.println("URL: " + pullRequest.getUrl());
        } catch (Exception e) {
        }
    }
}
