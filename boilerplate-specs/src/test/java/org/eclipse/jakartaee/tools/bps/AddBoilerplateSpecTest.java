package org.eclipse.jakartaee.tools.bps;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
        final File resources = new File(Dirs.work().parent(), "boilerplate-specs/src/test/resources/");
        final File specDir = new File(resources, spec);
        AddBoilerplateSpec.createSpecFiles(specDir.toPath());
    }

}