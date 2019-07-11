package org.eclipse.jakartaee.tools.bps;

import org.junit.Test;
import org.tomitribe.util.IO;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ExtractParentPomTest {

    @Test
    public void test() throws Exception {
        final URL url = Resources.find("jms-api/pom.xml");
        final File file = File.createTempFile("pom-before", ".xml");
        file.deleteOnExit();
        IO.copy(url, file);

        String s = ExtractParentPom.from(file);
        assertEquals(Resources.load("jms-api/pom.after.xml"), s);
    }
}