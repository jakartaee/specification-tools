package org.eclipse.jakartaee.tools.bps;

import org.junit.Test;
import org.tomitribe.util.IO;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ExtractParentPomTest {

    @Test
    public void testJms() throws Exception {
        assertExtraction("jms-api");
    }

    @Test
    public void testJsonp() throws Exception {
        assertExtraction("jsonp");
    }

    @Test
    public void testJaxb() throws Exception {
        assertExtraction("jaxb-api");
    }

    public static void assertExtraction(final String spec) throws IOException {
        final URL url = Resources.find(spec + "/pom.xml");
        final File file = File.createTempFile("pom-before", ".xml");
        file.deleteOnExit();
        IO.copy(url, file);

        String s = ExtractParentPom.from(file);
        assertEquals(Resources.load(spec + "/pom.after.xml"), s);
    }
}