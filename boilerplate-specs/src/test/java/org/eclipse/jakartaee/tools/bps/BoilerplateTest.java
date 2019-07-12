package org.eclipse.jakartaee.tools.bps;

import org.junit.Ignore;
import org.junit.Test;
import org.tomitribe.util.IO;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class BoilerplateTest {

    @Test
    public void testReadme() throws Exception {
        final File file = getFile("extractpom/jms-api/pom.xml");

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
        final File file = getFile("extractpom/jms-api/pom.xml");

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
        final File file = getFile("extractpom/jms-api/pom.xml");

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
        final File file = getFile("extractpom/jms-api/pom.xml");

        final Boilerplate boilerplate = Boilerplate.loadFor(file);

        assertEquals("== Specification Scope\n" +
                "\n" +
                "Jakarta Messaging describes a means for Java applications to create, send, and receive messages via loosely coupled, reliable asynchronous communication services.\n", boilerplate.getScopeAdoc());
    }

    @Test
    public void testSpec() throws Exception {
        final File file = getFile("extractpom/jms-api/pom.xml");

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

    public static File getFile(final String name) throws IOException {
        final URL url = Resources.find(name);
        final File file = File.createTempFile("pom-before", ".xml");
        file.deleteOnExit();
        IO.copy(url, file);
        return file;
    }


    @Test
    public void testCommonAnnotationsApi() throws Exception {
        Results.of("common-annotations-api")
                .assertProjectId("ee4j.ca")
                .assertSpecName("Jakarta Annotations")
                .assertOldProjectName("Eclipse Project for Common Annotations")
                .assertSpecVersion("1.3.4")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.ca")
                .assertProjectName("Jakarta Annotations")
                .assertSpecCode("annotations")
                .assertSpecRepo("common-annotations-api")
                .assertScopeStatement("Jakarta Annotations defines a collection of annotations" +
                        " representing common semantic concepts that enable a declarative style" +
                        " of programming that applies across a variety of Java technologies.");
    }

    @Test
    public void testConcurrencyApi() throws Exception {
        Results.of("concurrency-api")
                .assertProjectId("ee4j.cu")
                .assertSpecName("Jakarta Concurrency")
                .assertOldProjectName("Eclipse Project for Concurrency Utilities")
                .assertSpecVersion("1.1.1")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.cu")
                .assertProjectName("Jakarta Concurrency")
                .assertSpecCode("concurrency")
                .assertSpecRepo("concurrency-api")
                .assertScopeStatement("Jakarta Concurrency defines a framework for leveraging " +
                        "concurrency in application components without compromising container integrity " +
                        "while still preserving the Jakarta EE Platform's fundamental benefits.");

    }

    @Test
    public void testEjbApi() throws Exception {
        Results.of("ejb-api")
                .assertProjectId("ee4j.ejb")
                .assertSpecName("Jakarta Enterprise Beans")
                .assertOldProjectName("Eclipse Project for EJB")
                .assertSpecVersion("3.2.3")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.ejb")
                .assertProjectName("Jakarta Enterprise Beans")
                .assertSpecCode("enterprise-beans")
                .assertSpecRepo("ejb-api")
                .assertScopeStatement("Jakarta Enterprise Beans defines an architecture" +
                        " for the development and deployment of component-based business applications.");
    }

    @Test
    public void testElRi() throws Exception {
        Results.of("el-ri")
                .assertProjectId("ee4j.el")
                .assertSpecName("Jakarta Expression Language")
                .assertOldProjectName("Eclipse Project for Expression Language")
                .assertSpecVersion("3.0.2")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.el")
                .assertProjectName("Jakarta Expression Language")
                .assertSpecCode("expression-language")
                .assertSpecRepo("el-ri")
                .assertScopeStatement("Jakarta Expression Language defines an expression language for Java applications.");
    }

    @Test
    public void testEnterpriseDeployment() throws Exception {
        Results.of("enterprise-deployment")
                .assertProjectId("ee4j.jakartaee-stable")
                .assertSpecName("Jakarta Deployment")
                .assertOldProjectName("Eclipse Project for Stable Jakarta EE APIs")
                .assertSpecVersion("1.7.1")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jakartaee-stable")
                .assertProjectName("Jakarta Deployment")
                .assertSpecCode("deployment")
                .assertSpecRepo("enterprise-deployment")
                .assertScopeStatement("Jakarta Deployment defines standard APIs that will " +
                        "enable any deployment tool that uses the deployment APIs to deploy any " +
                        "assembled application onto a Jakarta EE compatible platform.");
    }

    @Test
    public void testInterceptorApi() throws Exception {
        Results.of("interceptor-api")
                .assertProjectId("ee4j.interceptors")
                .assertSpecName("Jakarta Interceptors")
                .assertOldProjectName("Eclipse Project for Interceptors")
                .assertSpecVersion("1.2.3")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.interceptors")
                .assertProjectName("Jakarta Interceptors")
                .assertSpecCode("interceptors")
                .assertSpecRepo("interceptor-api")
                .assertScopeStatement("Jakarta Interceptors defines a means of interposing on " +
                        "business method invocations and specific events—such as lifecycle events " +
                        "and timeout events—that occur on instances of Jakarta EE components and other " +
                        "managed classes.");
    }

    @Test
    public void testJacc() throws Exception {
        Results.of("jacc")
                .assertProjectId("ee4j.jacc")
                .assertSpecName("Jakarta Authorization")
                .assertOldProjectName("Eclipse Project for JACC")
                .assertSpecVersion("1.6.1")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jacc")
                .assertProjectName("Jakarta Authorization")
                .assertSpecCode("authorization")
                .assertSpecRepo("jacc")
                .assertScopeStatement("Jakarta Authorization defines the installation" +
                        " and configuration of authorization providers for use by containers." +
                        " The specification defines the interfaces that a provider must make available" +
                        " to allow container deployment tools to create and manage permission collections" +
                        " corresponding to roles.");
    }

    @Test
    public void testJaspic() throws Exception {
        Results.of("jaspic")
                .assertProjectId("ee4j.jaspic")
                .assertSpecName("Jakarta Authentication")
                .assertOldProjectName("Eclipse Project for JASPIC")
                .assertSpecVersion("1.1.2")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jaspic")
                .assertProjectName("Jakarta Authentication")
                .assertSpecCode("authentication")
                .assertSpecRepo("jaspic")
                .assertScopeStatement("") // TODO what is the scope statement
        ;
    }

    @Test
    public void testJaxRpcApi() throws Exception {
        Results.of("jax-rpc-api")
                .assertProjectId("ee4j.jakartaee-stable")
                .assertSpecName("Jakarta XML RPC")
                .assertOldProjectName("Eclipse Project for Stable Jakarta EE APIs")
                .assertSpecVersion("1.1.3")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jakartaee-stable")
                .assertProjectName("Jakarta Stable APIs")
                .assertSpecCode("xml-rpc")
                .assertSpecRepo("jax-rpc-api")
                .assertScopeStatement("Jakarta XML-based RPC defines consistent Java APIs for using XML based RPC standards.");
    }

    @Test
    public void testJaxWsApi() throws Exception {
        Results.of("jax-ws-api")
                .assertProjectId("ee4j.jaxws")
                .assertSpecName("Jakarta XML Web Services")
                .assertOldProjectName("Eclipse Project for JAX-WS")
                .assertSpecVersion("2.3.2")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jaxws")
                .assertProjectName("Jakarta XML Web Services")
                .assertSpecCode("xml-ws")
                .assertSpecRepo("jax-ws-api")
                .assertScopeStatement("Jakarta XML Web Services defines a means for implementing XML-Based Web " +
                        "Services, Web Services Metadata, and SOAP with Attachments.");
    }

    @Test
    public void testJaxbApi() throws Exception {
        Results.of("jaxb-api")
                .assertProjectId("ee4j.jaxb")
                .assertSpecName("Jakarta XML Binding")
                .assertOldProjectName("Eclipse Project for JAXB")
                .assertSpecVersion("2.3.2")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jaxb")
                .assertProjectName("Jakarta XML Binding")
                .assertSpecCode("xml-binding")
                .assertSpecRepo("jaxb-api")
                .assertScopeStatement("Jakarta XML Binding defines an API and tools that " +
                        "automate the mapping between XML documents and Java objects.");
    }

    @Test
    public void testJaxrApi() throws Exception {
        Results.of("jaxr-api")
                .assertProjectId("ee4j.jakartaee-stable")
                .assertSpecName("Jakarta XML Registries")
                .assertOldProjectName("Eclipse Project for Stable Jakarta EE APIs")
                .assertSpecVersion("1.0.9")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jakartaee-stable")
                .assertProjectName("Jakarta Stable APIs")
                .assertSpecCode("xml-registries")
                .assertSpecRepo("jaxr-api")
                .assertScopeStatement("Jakarta XML Registries describes Java API's designed specifically " +
                        "for an open and interoperable set of registry services that enable sharing of " +
                        "information between interested parties. The shared information is maintained as" +
                        " objects in a compliant registry. All access to registry content is exposed via the " +
                        "interfaces defined for the Registry Services.");
    }

    @Ignore
    @Test
    public void testJaxrsApi() throws Exception {
        Results.of("jaxrs-api")
                .generate();
    }

    @Test
    public void testJcaApi() throws Exception {
        Results.of("jca-api")
                .assertProjectId("ee4j.jca")
                .assertSpecName("Jakarta Connectors")
                .assertOldProjectName("Eclipse Project for JCA")
                .assertSpecVersion("1.7.2")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jca")
                .assertProjectName("Jakarta Connectors")
                .assertSpecCode("connectors")
                .assertSpecRepo("jca-api")
                .assertScopeStatement("Jakarta Connectors defines a standard architecture " +
                        "for Jakarta application components to connect to Enterprise Information Systems.");
    }

    @Test
    public void testJmsApi() throws Exception {
        Results.of("jms-api")
                .assertProjectId("ee4j.jms")
                .assertSpecName("Jakarta Messaging")
                .assertOldProjectName("Eclipse Project for JMS")
                .assertSpecVersion("2.0.2")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jms")
                .assertProjectName("Jakarta Messaging")
                .assertSpecCode("messaging")
                .assertSpecRepo("jms-api")
                .assertScopeStatement("Jakarta Messaging describes a means for Java" +
                        " applications to create, send, and receive messages via loosely " +
                        "coupled, reliable asynchronous communication services.");
    }

    @Test
    public void testJpaApi() throws Exception {
        Results.of("jpa-api")
                .assertProjectId("ee4j.jpa")
                .assertSpecName("Jakarta Persistence")
                .assertOldProjectName("Eclipse Project for JPA")
                .assertSpecVersion("2.2.2")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jpa")
                .assertProjectName("Jakarta Persistence")
                .assertSpecCode("persistence")
                .assertSpecRepo("jpa-api")
                .assertScopeStatement("Jakarta Persistence defines a standard for management " +
                        "of persistence and object/relational mapping in Java(R) environments.");
    }

    @Test
    public void testJsonbApi() throws Exception {
        Results.of("jsonb-api")
                .assertProjectId("ee4j.jsonb")
                .assertSpecName("Jakarta JSON Binding")
                .assertOldProjectName("Eclipse Project for JSONB")
                .assertSpecVersion("1.0.1")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jsonb")
                .assertProjectName("Jakarta JSON Binding")
                .assertSpecCode("jsonb")
                .assertSpecRepo("jsonb-api")
                .assertScopeStatement("Jakarta JSON Binding defines a binding framework for " +
                        "converting Java objects to and from JSON documents.");
    }

    @Test
    public void testJsonp() throws Exception {
        Results.of("jsonp")
                .assertProjectId("ee4j.jsonp")
                .assertSpecName("Jakarta JSON Processing")
                .assertOldProjectName("Eclipse Project for JSONP")
                .assertSpecVersion("1.1.5")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jsonp")
                .assertProjectName("Jakarta JSON Processing")
                .assertSpecCode("jsonp")
                .assertSpecRepo("jsonp")
                .assertScopeStatement("Jakarta JSON Processing defines a Java(R) based " +
                        "framework for parsing, generating, transforming, and querying JSON documents.");
    }

    @Test
    public void testJspApi() throws Exception {
        Results.of("jsp-api")
                .assertProjectId("ee4j.jsp")
                .assertSpecName("Jakarta Server Pages")
                .assertOldProjectName("Eclipse Project for JSP")
                .assertSpecVersion("2.3.4")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jsp")
                .assertProjectName("Jakarta Server Pages")
                .assertSpecCode("pages")
                .assertSpecRepo("jsp-api")
                .assertScopeStatement("Jakarta Server Pages defines a template engine for web " +
                        "applications that supports mixing of textual content (including HTML and " +
                        "XML) with custom tags, expression language, and embedded Java code, that " +
                        "gets compiled into a Jakarta Servlet.");
    }

    @Test
    public void testJstlApi() throws Exception {
        Results.of("jstl-api")
                .assertProjectId("ee4j.jstl")
                .assertSpecName("Jakarta Standard Tag Library")
                .assertOldProjectName("Eclipse Project for JSTL")
                .assertSpecVersion("1.2.3")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jstl")
                .assertProjectName("Jakarta Standard Tag Library")
                .assertSpecCode("tags")
                .assertSpecRepo("jstl-api")
                .assertScopeStatement("Jakarta Standard Tag Library defines a framework" +
                        " and simple tags for Jakarta Server Pages that provide support for" +
                        " common, structural tasks such as iteration and conditionals, tags for " +
                        "manipulating XML documents, internationalization tags, and SQL tags.");
    }

    @Test
    public void testJtaApi() throws Exception {
        Results.of("jta-api")
                .assertProjectId("ee4j.jta")
                .assertSpecName("Jakarta Transactions")
                .assertOldProjectName("Eclipse Project for JTA")
                .assertSpecVersion("1.3.2")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jta")
                .assertProjectName("Jakarta Transactions")
                .assertSpecCode("transactions")
                .assertSpecRepo("jta-api")
                .assertScopeStatement("Jakarta Transactions defines a standard that allows " +
                        "the demarcation of transactions and the transactional coordination of " +
                        "XA-aware resource managers as described in the X/Open XA-specification " +
                        "and mapped to the Java SE XAResource interface within Java applications.");
    }

    @Test
    public void testJwsApi() throws Exception {
        Results.of("jws-api")
                .assertProjectId("ee4j.jakartaee-stable")
                .assertSpecName("Jakarta Enterprise Web Services")
                .assertOldProjectName("Eclipse Project for Stable Jakarta EE APIs")
                .assertSpecVersion("1.1.1")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jakartaee-stable")
                .assertProjectName("Jakarta Enterprise Web Services")
                .assertSpecCode("enterprise-ws")
                .assertSpecRepo("jws-api")
                .assertScopeStatement("Jakarta XML Web Services defines a means for implementing " +
                        "XML-Based Web Services, Web Services Metadata, and SOAP with Attachments.");

    }

    @Test
    public void testManagementApi() throws Exception {
        Results.of("management-api")
                .assertProjectId("ee4j.jakartaee-stable")
                .assertSpecName("Jakarta Management")
                .assertOldProjectName("Eclipse Project for Stable Jakarta EE APIs")
                .assertSpecVersion("1.1.3")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jakartaee-stable")
                .assertProjectName("Jakarta Management")
                .assertSpecCode("management")
                .assertSpecRepo("management-api")
                .assertScopeStatement("Jakarta Management defines a standard management model for " +
                        "exposing and accessing the management information, operations, and parameters " +
                        "of the Jakarta EE Platform components.");
    }

    @Test
    public void testSaajApi() throws Exception {
        Results.of("saaj-api")
                .assertProjectId("ee4j.jaxws")
                .assertSpecName("Jakarta SOAP Attachments")
                .assertOldProjectName("Eclipse Project for JAX-WS")
                .assertSpecVersion("1.4.1")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jaxws")
                .assertProjectName("Jakarta XML Web Services")
                .assertSpecCode("soap")
                .assertSpecRepo("saaj-api")
                .assertScopeStatement("Jakarta XML Web Services defines a means for implementing" +
                        " XML-Based Web Services, Web Services Metadata, and SOAP with Attachments.");
    }

    @Test
    public void testSecurityApi() throws Exception {
        Results.of("security-api")
                .assertProjectId("ee4j.es")
                .assertSpecName("Jakarta Security")
                .assertOldProjectName("Eclipse Project for Enterprise Security")
                .assertSpecVersion("1.0.1")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.es")
                .assertProjectName("Jakarta Security")
                .assertSpecCode("security")
                .assertSpecRepo("security-api")
                .assertScopeStatement("Jakarta Security defines a standard for creating " +
                        "secure Jakarta EE applications in modern application paradigms.");
    }

    @Test
    public void testServletApi() throws Exception {
        Results.of("servlet-api")
                .assertProjectId("ee4j.servlet")
                .assertSpecName("Jakarta Servlet")
                .assertOldProjectName("Eclipse Project for Servlet")
                .assertSpecVersion("4.0.2")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.servlet")
                .assertProjectName("Jakarta Servlet")
                .assertSpecCode("servlet")
                .assertSpecRepo("servlet-api")
                .assertScopeStatement("Jakarta Servlet defines server-side handling for HTTP requests and responses.");
    }

    @Test
    public void testWebsocketApi() throws Exception {
        Results.of("websocket-api")
                .assertProjectId("ee4j.websocket")
                .assertSpecName("Jakarta WebSocket")
                .assertOldProjectName("Eclipse Project for WebSocket")
                .assertSpecVersion("1.1.1")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.websocket")
                .assertProjectName("Jakarta WebSocket")
                .assertSpecCode("websocket")
                .assertSpecRepo("websocket-api")
                .assertScopeStatement("Jakarta WebSocket defines how Jakarta based applications" +
                        " create and manage WebSocket Clients and Servers.");
    }

    @Test
    public void testJaf() throws Exception {
        Results.of("jaf")
                .assertProjectId("ee4j.jaf")
                .assertSpecName("Jakarta Activation")
                .assertOldProjectName("Eclipse Project for JAF")
                .assertSpecVersion("1.2.1")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.jaf")
                .assertProjectName("Jakarta Activation")
                .assertSpecCode("activation")
                .assertSpecRepo("jaf")
                .assertScopeStatement("Jakarta Activation defines a set of standard " +
                        "services to: determine the MIME type of an arbitrary piece of " +
                        "data; encapsulate access to it; discover the operations available " +
                        "on it; and instantiate the appropriate bean to perform the operation(s).");
    }

    @Test
    public void testJavamail() throws Exception {
        Results.of("javamail")
                .assertProjectId("ee4j.javamail")
                .assertSpecName("Jakarta Mail")
                .assertOldProjectName("Eclipse Project for JavaMail")
                .assertSpecVersion("1.6.3")
                .assertProjectUrl("https://projects.eclipse.org/projects/ee4j.javamail")
                .assertProjectName("Jakarta Mail")
                .assertSpecCode("mail")
                .assertSpecRepo("javamail")
                .assertScopeStatement("Jakarta Mail defines a platform-independent and " +
                        "protocol-independent framework to build mail and messaging applications.");
    }

    public static class Results {
        private final Boilerplate boilerplate;

        public Results(final Boilerplate boilerplate) {
            this.boilerplate = boilerplate;
        }

        public Results assertProjectId(final String string) {
            assertEquals(string, boilerplate.getSpec().getProjectId());
            return this;
        }

        public Results assertOldProjectName(final String oldProjectName) {
            assertEquals(oldProjectName, boilerplate.getSpec().getOldProjectName());
            return this;
        }

        public Results assertSpecCode(final String specCode) {
            assertEquals(specCode, boilerplate.getSpec().getSpecCode());
            return this;
        }

        public Results assertProjectUrl(final String projectUrl) {
            assertEquals(projectUrl, boilerplate.getSpec().getProjectUrl());
            return this;
        }

        public Results assertSpecVersion(final String specVersion) {
            assertEquals(specVersion, boilerplate.getSpec().getSpecVersion());
            return this;
        }

        public Results assertTckRepo(final String tckRepo) {
            assertEquals(tckRepo, boilerplate.getSpec().getTckRepo());
            return this;
        }

        public Results assertProjectName(final String projectName) {
            assertEquals(projectName, boilerplate.getSpec().getProjectName());
            return this;
        }

        public Results assertApiRepo(final String apiRepo) {
            assertEquals(apiRepo, boilerplate.getSpec().getApiRepo());
            return this;
        }

        public Results assertSpecRepo(final String specRepo) {
            assertEquals(specRepo, boilerplate.getSpec().getSpecRepo());
            return this;
        }

        public Results assertSpecName(final String specName) {
            assertEquals(specName, boilerplate.getSpec().getSpecName());
            return this;
        }

        public Results assertScopeAdoc(final String scopeAdoc) {
            assertEquals(scopeAdoc, boilerplate.getScopeAdoc());
            return this;
        }

        public Results assertReadmeMd(final String readmeMd) {
            assertEquals(readmeMd, boilerplate.getReadmeMd());
            return this;
        }

        public Results assertScopeStatement(final String scopeStatement) {
            assertEquals(scopeStatement, boilerplate.getScopeStatement());
            return this;
        }

        public Results assertPomXml(final String pomXml) {
            assertEquals(pomXml, boilerplate.getPomXml());
            return this;
        }

        public Results assertSpecAdoc(final String specAdoc) {
            assertEquals(specAdoc, boilerplate.getSpecAdoc());
            return this;
        }

        public Results assertAssemblyXml(final String assemblyXml) {
            assertEquals(assemblyXml, boilerplate.getAssemblyXml());
            return this;
        }

        public Results assertThemeYaml(final String themeYaml) {
            assertEquals(themeYaml, boilerplate.getThemeYaml());
            return this;
        }

        public Results assertLicenseEfslAdoc(final String licenseEfslAdoc) {
            assertEquals(licenseEfslAdoc, boilerplate.getLicenseEfslAdoc());
            return this;
        }

        public Results generate() {
//            final ObjectMap data = new ObjectMap(boilerplate.getSpec());
//            for (final Map.Entry<String, Object> entry : data.entrySet()) {
//                final String key = entry.getKey();
//
//                final String ucKey = Strings.ucfirst(key);
////                System.out.printf("        public Results assert%s(final String %s) {\n" +
////                        "            assertEquals(%s, boilerplate.get%s());\n" +
////                        "            return this;\n" +
////                        "        }\n" +
////                        "\n", ucKey, key, key, ucKey);
////                System.out.printf("System.out.printf(\"    .assert%s(%%s)%%n\", boilerplate.getSpec().get%s());%n", ucKey, ucKey);
//            }

            System.out.printf("    .assertProjectId(\"%s\")%n", boilerplate.getSpec().getProjectId());
            System.out.printf("    .assertSpecName(\"%s\")%n", boilerplate.getSpec().getSpecName());
            System.out.printf("    .assertOldProjectName(\"%s\")%n", boilerplate.getSpec().getOldProjectName());
            System.out.printf("    .assertSpecVersion(\"%s\")%n", boilerplate.getSpec().getSpecVersion());
            System.out.printf("    .assertProjectUrl(\"%s\")%n", boilerplate.getSpec().getProjectUrl());
            System.out.printf("    .assertProjectName(\"%s\")%n", boilerplate.getSpec().getProjectName());
            System.out.printf("    .assertSpecCode(\"%s\")%n", boilerplate.getSpec().getSpecCode());
            System.out.printf("    .assertSpecRepo(\"%s\")%n", boilerplate.getSpec().getSpecRepo());
//            System.out.printf("    .assertScopeAdoc(%s)%n", boilerplate.getScopeAdoc());
//            System.out.printf("    .assertReadmeMd(%s)%n", boilerplate.getReadmeMd());
            System.out.printf("    .assertScopeStatement(\"%s\")%n", boilerplate.getScopeStatement());
//            System.out.printf("    .assertPomXml(%s)%n", boilerplate.getPomXml());
//            System.out.printf("    .assertSpecAdoc(%s)%n", boilerplate.getSpecAdoc());
//            System.out.printf("    .assertAssemblyXml(%s)%n", boilerplate.getAssemblyXml());
//            System.out.printf("    .assertThemeYaml(%s)%n", boilerplate.getThemeYaml());
//            System.out.printf("    .assertLicenseEfslAdoc(%s)%n", boilerplate.getLicenseEfslAdoc());
//            System.out.printf("    .assertSpec(%s)%n", boilerplate.getSpec());
            System.out.printf(";%n");
            return this;
        }

        public static Results of(final String specName) throws IOException {
            final File file = getFile("extractpom/" + specName + "/pom.xml");
            return new Results(Boilerplate.loadFor(file));
        }
    }
}