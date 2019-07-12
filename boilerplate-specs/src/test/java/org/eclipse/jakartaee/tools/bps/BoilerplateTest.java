package org.eclipse.jakartaee.tools.bps;

import org.junit.Test;
import org.tomitribe.util.IO;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class BoilerplateTest {

    @Test
    public void testReadme() throws Exception {
        final File file = getFile("jms-api/pom.xml");

        final Boilerplate boilerplate = Boilerplate.loadFor(file);

        assertEquals("Jakarta Messaging Specification\n" +
                "============================\n" +
                "\n" +
                "This project generates the Jakarta Messaging Specification.\n" +
                "\n" +
                "Building\n" +
                "--------\n" +
                "\n" +
                "Prerequisites:\n" +
                "\n" +
                "* JDK8+\n" +
                "* Maven 3.0.3+\n" +
                "\n" +
                "Run the full build:\n" +
                "\n" +
                "`mvn install`\n" +
                "\n" +
                "Locate the html files:\n" +
                "- `target/generated-docs/messaging-spec-<version>.html`\n" +
                "\n" +
                "Locate the PDF files:\n" +
                "- `target/generated-docs/messaging-spec-<version>.pdf`\n", boilerplate.getReadmeMd());
    }

    @Test
    public void testPom() throws Exception {
        final File file = getFile("jms-api/pom.xml");

        final Boilerplate boilerplate = Boilerplate.loadFor(file);

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!--\n" +
                "\n" +
                "    Copyright (c) 2019 Contributors to the Eclipse Foundation.\n" +
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
                "\n" +
                "-->\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                "    <parent>\n" +
                "        <groupId>org.eclipse.ee4j.jms</groupId>\n" +
                "        <artifactId>jms-parent</artifactId>\n" +
                "        <version>2.1-SNAPSHOT</version>\n" +
                "        <relativePath/>\n" +
                "    </parent>\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <artifactId>messaging-spec</artifactId>\n" +
                "    <packaging>pom</packaging>\n" +
                "    <name>Jakarta Messaging Specification</name>\n" +
                "\n" +
                "    <properties>\n" +
                "        <site.output.dir>${project.build.directory}/staging</site.output.dir>\n" +
                "        <maven.site.skip>true</maven.site.skip>\n" +
                "        <asciidoctor.maven.plugin.version>1.5.7.1</asciidoctor.maven.plugin.version>\n" +
                "        <asciidoctorj.version>1.6.2</asciidoctorj.version>\n" +
                "        <asciidoctorj.pdf.version>1.5.0-alpha.16</asciidoctorj.pdf.version>\n" +
                "        <jruby.version>9.2.6.0</jruby.version>\n" +
                "        <!-- status: DRAFT, BETA, etc., or blank for final -->\n" +
                "        <status>DRAFT</status>\n" +
                "        <maven.build.timestamp.format>MMMM dd, yyyy</maven.build.timestamp.format>\n" +
                "        <revisiondate>${maven.build.timestamp}</revisiondate>\n" +
                "    </properties>\n" +
                "\n" +
                "    <build>\n" +
                "        <defaultGoal>package</defaultGoal>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-enforcer-plugin</artifactId>\n" +
                "                <version>1.4.1</version>\n" +
                "                <executions>\n" +
                "                    <execution>\n" +
                "                        <id>enforce-versions</id>\n" +
                "                        <goals>\n" +
                "                            <goal>enforce</goal>\n" +
                "                        </goals>\n" +
                "                        <configuration>\n" +
                "                            <rules>\n" +
                "                                <requireJavaVersion>\n" +
                "                                    <version>[1.8.0,1.9.0)</version>\n" +
                "                                    <message>You need JDK8 or lower</message>\n" +
                "                                </requireJavaVersion>\n" +
                "                            </rules>\n" +
                "                        </configuration>\n" +
                "                    </execution>\n" +
                "                </executions>\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <groupId>org.asciidoctor</groupId>\n" +
                "                <artifactId>asciidoctor-maven-plugin</artifactId>\n" +
                "                <version>${asciidoctor.maven.plugin.version}</version>\n" +
                "                <dependencies>\n" +
                "                    <dependency>\n" +
                "                        <groupId>org.jruby</groupId>\n" +
                "                        <artifactId>jruby-complete</artifactId>\n" +
                "                        <version>${jruby.version}</version>\n" +
                "                    </dependency>\n" +
                "                    <dependency>\n" +
                "                        <groupId>org.asciidoctor</groupId>\n" +
                "                        <artifactId>asciidoctorj</artifactId>\n" +
                "                        <version>${asciidoctorj.version}</version>\n" +
                "                    </dependency>\n" +
                "                    <dependency>\n" +
                "                        <groupId>org.asciidoctor</groupId>\n" +
                "                        <artifactId>asciidoctorj-pdf</artifactId>\n" +
                "                        <version>${asciidoctorj.pdf.version}</version>\n" +
                "                    </dependency>\n" +
                "                </dependencies>\n" +
                "                <executions>\n" +
                "                    <execution>\n" +
                "                        <id>asciidoc-to-html</id>\n" +
                "                        <phase>generate-resources</phase>\n" +
                "                        <goals>\n" +
                "                            <goal>process-asciidoc</goal>\n" +
                "                        </goals>\n" +
                "                        <configuration>\n" +
                "                            <backend>html5</backend>\n" +
                "                            <outputFile>${project.build.directory}/generated-docs/messaging-spec-${project.version}.html</outputFile>\n" +
                "                            <attributes>\n" +
                "                                <doctype>book</doctype>\n" +
                "                                <status>${status}</status>\n" +
                "                                <data-uri />\n" +
                "                                <icons>font</icons>\n" +
                "                                <toc>left</toc>\n" +
                "                                <icons>font</icons>\n" +
                "                                <sectanchors>true</sectanchors>\n" +
                "                                <idprefix />\n" +
                "                                <idseparator>-</idseparator>\n" +
                "                                <docinfo1>true</docinfo1>\n" +
                "                            </attributes>\n" +
                "                        </configuration>\n" +
                "                    </execution>\n" +
                "                    <execution>\n" +
                "                        <id>asciidoc-to-pdf</id>\n" +
                "                        <phase>generate-resources</phase>\n" +
                "                        <goals>\n" +
                "                            <goal>process-asciidoc</goal>\n" +
                "                        </goals>\n" +
                "                        <configuration>\n" +
                "                            <backend>pdf</backend>\n" +
                "                            <outputFile>${project.build.directory}/generated-docs/messaging-spec-${project.version}.pdf</outputFile>\n" +
                "                            <attributes>\n" +
                "                                <pdf-stylesdir>${project.basedir}/src/main/theme</pdf-stylesdir>\n" +
                "                                <pdf-style>jakartaee</pdf-style>\n" +
                "                                <doctype>book</doctype>\n" +
                "                                <status>${status}</status>\n" +
                "                                <data-uri />\n" +
                "                                <icons>font</icons>\n" +
                "                                <pagenums />\n" +
                "                                <toc />\n" +
                "                                <icons>font</icons>\n" +
                "                                <sectanchors>true</sectanchors>\n" +
                "                                <idprefix />\n" +
                "                                <idseparator>-</idseparator>\n" +
                "                                <docinfo1>true</docinfo1>\n" +
                "                                <embedAssets>true</embedAssets>\n" +
                "                            </attributes>\n" +
                "                        </configuration>\n" +
                "                    </execution>\n" +
                "                </executions>\n" +
                "                <configuration>\n" +
                "                    <sourceDocumentName>messaging-spec.adoc</sourceDocumentName>\n" +
                "                    <sourceHighlighter>coderay</sourceHighlighter>\n" +
                "                    <attributes>\n" +
                "                        <revnumber>${project.version}</revnumber>\n" +
                "                        <revremark>${status}</revremark>\n" +
                "                        <revdate>${revisiondate}</revdate>\n" +
                "                    </attributes>\n" +
                "                </configuration>\n" +
                "\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-release-plugin</artifactId>\n" +
                "                <version>2.5.2</version>\n" +
                "                <configuration>\n" +
                "                    <mavenExecutorId>forked-path</mavenExecutorId>\n" +
                "                    <useReleaseProfile>false</useReleaseProfile>\n" +
                "                    <arguments>${release.arguments}</arguments>\n" +
                "                </configuration>\n" +
                "                <dependencies>\n" +
                "                    <dependency>\n" +
                "                        <groupId>org.apache.maven.scm</groupId>\n" +
                "                        <artifactId>maven-scm-provider-gitexe</artifactId>\n" +
                "                        <version>1.9.4</version>\n" +
                "                    </dependency>\n" +
                "                </dependencies>\n" +
                "            </plugin>\n" +
                "\n" +
                "            <!--\n" +
                "                This is the rule that builds the zip file for download.\n" +
                "            -->\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-assembly-plugin</artifactId>\n" +
                "                <version>3.1.1</version>\n" +
                "                <inherited>false</inherited>\n" +
                "                <executions>\n" +
                "                    <execution>\n" +
                "                        <phase>package</phase>\n" +
                "                        <goals>\n" +
                "                            <goal>single</goal>\n" +
                "                        </goals>\n" +
                "                        <configuration>\n" +
                "                            <appendAssemblyId>false</appendAssemblyId>\n" +
                "                            <descriptors>\n" +
                "                                <descriptor>assembly.xml</descriptor>\n" +
                "                            </descriptors>\n" +
                "                        </configuration>\n" +
                "                    </execution>\n" +
                "                </executions>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>\n", boilerplate.getPomXml());
    }

    @Test
    public void testAssembly() throws Exception {
        final File file = getFile("jms-api/pom.xml");

        final Boilerplate boilerplate = Boilerplate.loadFor(file);

        assertEquals("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
                "<!--\n" +
                " \n" +
                "    Copyright (c) 2019 Contributors to the Eclipse Foundation.\n" +
                " \n" +
                "    This program and the accompanying materials are made available under the\n" +
                "    terms of the Eclipse Public License v. 2.0, which is available at\n" +
                "    http://www.eclipse.org/legal/epl-2.0.\n" +
                " \n" +
                "    This Source Code may also be made available under the following Secondary\n" +
                "    Licenses when the conditions for such availability set forth in the\n" +
                "    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,\n" +
                "    version 2 with the GNU Classpath Exception, which is available at\n" +
                "    https://www.gnu.org/software/classpath/license.html.\n" +
                " \n" +
                "    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0\n" +
                " \n" +
                "-->\n" +
                "\n" +
                "<assembly>\n" +
                "    <id>spec</id>\n" +
                "    <formats>\n" +
                "    <format>zip</format>\n" +
                "    </formats>\n" +
                "    <baseDirectory>messaging-spec</baseDirectory>\n" +
                "    <fileSets>\n" +
                "        <fileSet>\n" +
                "            <directory>target/generated-docs</directory>\n" +
                "            <outputDirectory></outputDirectory>\n" +
                "        </fileSet>\n" +
                "    </fileSets>\n" +
                "</assembly>\n", boilerplate.getAssemblyXml());
    }

    @Test
    public void testScope() throws Exception {
        final File file = getFile("jms-api/pom.xml");

        final Boilerplate boilerplate = Boilerplate.loadFor(file);

        assertEquals("== Specification Scope\n" +
                "\n" +
                "Jakarta Messaging describes a means for Java applications to create, send, and receive messages via loosely coupled, reliable asynchronous communication services.\n", boilerplate.getScopeAdoc());
    }

    @Test
    public void testSpec() throws Exception {
        final File file = getFile("jms-api/pom.xml");

        final Boilerplate boilerplate = Boilerplate.loadFor(file);

        assertEquals("//\n" +
                "// Copyright (c) 2017, 2019 Contributors to the Eclipse Foundation\n" +
                "//\n" +
                "\n" +
                "= Jakarta Messaging 2.0\n" +
                ":authors: Jakarta Messaging Team, https://projects.eclipse.org/projects/ee4j.jms\n" +
                ":email: https://dev.eclipse.org/mailman/listinfo/jms-dev\n" +
                ":version-label!:\n" +
                ":doctype: book\n" +
                ":license: Eclipse Foundation Specification License v1.0\n" +
                ":source-highlighter: coderay\n" +
                ":toc: left\n" +
                ":toclevels: 4\n" +
                ":sectnumlevels: 4\n" +
                ":sectanchors:\n" +
                "ifdef::backend-pdf[]\n" +
                ":pagenums:\n" +
                ":numbered:\n" +
                ":title-logo-image: image:jakarta_ee_logo_schooner_color_stacked_default.png[pdfwidth=4.25in,align=right]\n" +
                "endif::[]\n" +
                "\n" +
                "// == License\n" +
                ":sectnums!:\n" +
                "include::license-efsl.adoc[]\n" +
                "\n" +
                "// == Scope\n" +
                ":sectnums:\n" +
                "include::scope.adoc[]\n", boilerplate.getSpecAdoc());
    }

    public File getFile(final String name) throws IOException {
        final URL url = Resources.find(name);
        final File file = File.createTempFile("pom-before", ".xml");
        file.deleteOnExit();
        IO.copy(url, file);
        return file;
    }

}