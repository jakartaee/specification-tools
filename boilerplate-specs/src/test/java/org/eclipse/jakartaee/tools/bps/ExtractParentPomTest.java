package org.eclipse.jakartaee.tools.bps;

import org.junit.Test;
import org.tomitribe.util.IO;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ExtractParentPomTest {

    @Test
    public void testCommonAnnotationsApi() throws Exception {
        assertExtraction("common-annotations-api");
    }

    @Test
    public void testConcurrencyApi() throws Exception {
        assertExtraction("concurrency-api");
    }

    @Test
    public void testEjbApi() throws Exception {
        assertExtraction("ejb-api");
    }

    @Test
    public void testElRi() throws Exception {
        assertExtraction("el-ri");
    }

    @Test
    public void testEnterpriseDeployment() throws Exception {
        assertExtraction("enterprise-deployment");
    }

    @Test
    public void testInterceptorApi() throws Exception {
        assertExtraction("interceptor-api");
    }

    @Test
    public void testJacc() throws Exception {
        assertExtraction("jacc");
    }

    @Test
    public void testJaspic() throws Exception {
        assertExtraction("jaspic");
    }

    @Test
    public void testJaxRpcApi() throws Exception {
        assertExtraction("jax-rpc-api");
    }

    @Test
    public void testJaxWsApi() throws Exception {
        assertExtraction("jax-ws-api");
    }

    @Test
    public void testJaxbApi() throws Exception {
        assertExtraction("jaxb-api");
    }

    @Test
    public void testJaxrApi() throws Exception {
        assertExtraction("jaxr-api");
    }

    @Test
    public void testJcaApi() throws Exception {
        assertExtraction("jca-api");
    }

    @Test
    public void testJmsApi() throws Exception {
        assertExtraction("jms-api");
    }

    @Test
    public void testJpaApi() throws Exception {
        assertExtraction("jpa-api");
    }

    @Test
    public void testJsonbApi() throws Exception {
        assertExtraction("jsonb-api");
    }

    @Test
    public void testJsonp() throws Exception {
        assertExtraction("jsonp");
    }

    @Test
    public void testJspApi() throws Exception {
        assertExtraction("jsp-api");
    }

    @Test
    public void testJstlApi() throws Exception {
        assertExtraction("jstl-api");
    }

    @Test
    public void testJtaApi() throws Exception {
        assertExtraction("jta-api");
    }

    @Test
    public void testJwsApi() throws Exception {
        assertExtraction("jws-api");
    }

    @Test
    public void testManagementApi() throws Exception {
        assertExtraction("management-api");
    }

    @Test
    public void testSaajApi() throws Exception {
        assertExtraction("saaj-api");
    }

    @Test
    public void testSecurityApi() throws Exception {
        assertExtraction("security-api");
    }

    @Test
    public void testServletApi() throws Exception {
        assertExtraction("servlet-api");
    }

    @Test
    public void testWebsocketApi() throws Exception {
        assertExtraction("websocket-api");
    }

    @Test
    public void testJaf() throws Exception {
        assertExtraction("jaf");
    }

    @Test
    public void testJavamail() throws Exception {
        assertExtraction("javamail");
    }

    public static void assertExtraction(final String spec) throws IOException {
        final URL url = Resources.find("extractpom/" + spec + "/pom.xml");
        final File file = File.createTempFile("pom-before", ".xml");
        file.deleteOnExit();
        IO.copy(url, file);

        final String actual = ExtractParentPom.from(file);
        final String expected = Resources.load("extractpom/" + spec + "/pom.after.xml");

//        final File resources = new File(Dirs.work().parent(), "boilerplate-specs/src/test/resources/");
//        final File dir = new File(resources, spec);
//        final File pomAfterXml = new File(dir, "pom.after.xml");
//        IO.copy(IO.read(actual), pomAfterXml);

        assertEquals(expected, actual);
    }
}