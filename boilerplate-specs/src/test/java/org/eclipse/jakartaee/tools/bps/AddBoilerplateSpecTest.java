package org.eclipse.jakartaee.tools.bps;

import org.apache.openejb.loader.IO;
import org.junit.Assert;
import org.junit.Test;
import org.tomitribe.util.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AddBoilerplateSpecTest {

    @Test
    public void testCommonAnnotationsApi() throws Exception {
        assertSpecFiles("common-annotations-api");
    }

    @Test
    public void testConcurrencyApi() throws Exception {
        assertSpecFiles("concurrency-api");
    }

    @Test
    public void testEjbApi() throws Exception {
        assertSpecFiles("ejb-api");
    }

    @Test
    public void testElRi() throws Exception {
        assertSpecFiles("el-ri");
    }

    @Test
    public void testEnterpriseDeployment() throws Exception {
        assertSpecFiles("enterprise-deployment");
    }

    @Test
    public void testInterceptorApi() throws Exception {
        assertSpecFiles("interceptor-api");
    }

    @Test
    public void testJacc() throws Exception {
        assertSpecFiles("jacc");
    }

    @Test
    public void testJaspic() throws Exception {
        assertSpecFiles("jaspic");
    }

    @Test
    public void testJaxRpcApi() throws Exception {
        assertSpecFiles("jax-rpc-api");
    }

    @Test
    public void testJaxWsApi() throws Exception {
        assertSpecFiles("jax-ws-api");
    }

    @Test
    public void testJaxbApi() throws Exception {
        assertSpecFiles("jaxb-api");
    }

    @Test
    public void testJaxrApi() throws Exception {
        assertSpecFiles("jaxr-api");
    }

    @Test
    public void testJcaApi() throws Exception {
        assertSpecFiles("jca-api");
    }

    @Test
    public void testJmsApi() throws Exception {
        assertSpecFiles("jms-api");
    }

    @Test
    public void testJpaApi() throws Exception {
        assertSpecFiles("jpa-api");
    }

    @Test
    public void testJsonbApi() throws Exception {
        assertSpecFiles("jsonb-api");
    }

    @Test
    public void testJsonp() throws Exception {
        assertSpecFiles("jsonp");
    }

    @Test
    public void testJspApi() throws Exception {
        assertSpecFiles("jsp-api");
    }

    @Test
    public void testJstlApi() throws Exception {
        assertSpecFiles("jstl-api");
    }

    @Test
    public void testJtaApi() throws Exception {
        assertSpecFiles("jta-api");
    }

    @Test
    public void testJwsApi() throws Exception {
        assertSpecFiles("jws-api");
    }

    @Test
    public void testManagementApi() throws Exception {
        assertSpecFiles("management-api");
    }

    @Test
    public void testSaajApi() throws Exception {
        assertSpecFiles("saaj-api");
    }

    @Test
    public void testSecurityApi() throws Exception {
        assertSpecFiles("security-api");
    }

    @Test
    public void testServletApi() throws Exception {
        assertSpecFiles("servlet-api");
    }

    @Test
    public void testWebsocketApi() throws Exception {
        assertSpecFiles("websocket-api");
    }

    @Test
    public void testJaf() throws Exception {
        assertSpecFiles("jaf");
    }

    @Test
    public void testJavamail() throws Exception {
        assertSpecFiles("javamail");
    }

    private void assertSpecFiles(final String spec) throws IOException {
        final File tmpdir = Files.tmpdir();

//        { // uncomment to save the new state of what is generated
//            final File resources = new File(Dirs.work().parent(), "boilerplate-specs/src/test/resources/genspec-after");
//            final File specDir = new File(resources, spec);
//            AddBoilerplateSpec.createSpecFiles(specDir.toPath());
//        }

        // Create a clean copy that can be modified
        final Path specDir = new File(new File(Dirs.work().parent(), "boilerplate-specs/src/test/resources/genspec-before"), spec).toPath();
        final Path expected = new File(new File(Dirs.work().parent(), "boilerplate-specs/src/test/resources/genspec-after"), spec).toPath();

        final Path actual = tmpdir.toPath();

        copyFolder(specDir, actual);

        // Do the modifications
        AddBoilerplateSpec.createSpecFiles(actual);

        // Compare with the expected
        compareFolder(expected, actual);
    }


    public void copyFolder(Path src, Path dest) throws IOException {
        java.nio.file.Files.walk(src)
                .forEach(source -> copy(source, dest.resolve(src.relativize(source))));
    }

    private void copy(Path source, Path dest) {
        try {
            java.nio.file.Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void compareFolder(Path src, Path dest) throws IOException {
        java.nio.file.Files.walk(src)
                .forEach(source -> compare(source, dest.resolve(src.relativize(source))));
    }

    private void compare(Path expectedPath, Path actualPath) {
        if (expectedPath.toFile().isDirectory()) return;
        try {
            final String expected = IO.slurp(expectedPath.toFile());
            final String actual = IO.slurp(actualPath.toFile());
            Assert.assertEquals(expected, actual);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}