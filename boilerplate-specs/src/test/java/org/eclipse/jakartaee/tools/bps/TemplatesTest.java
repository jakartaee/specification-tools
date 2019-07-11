package org.eclipse.jakartaee.tools.bps;

import org.apache.openejb.util.Join;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TemplatesTest {

    @Test
    public void simple() throws Exception {
        final HashMap<String, Object> map = new HashMap<>();

        map.put("year", "2019");
        map.put("header", "Copyright (c) {year}");


        final String expected = "header: Copyright (c) 2019\n" +
                "-------\n" +
                "year: 2019";

        assertInterpolation(map, expected);
    }

    @Test
    public void ordered() throws Exception {
        final HashMap<String, Object> map = new HashMap<>();

        map.put("year", "2019");
        map.put("shortName", "jms");
        map.put("repoName", "{shortName}-api");
        map.put("scmUrl", "git://github.com/eclipse-ee4j/{repoName}.git");


        final String expected = "repoName: jms-api\n" +
                "-------\n" +
                "scmUrl: git://github.com/eclipse-ee4j/jms-api.git\n" +
                "-------\n" +
                "shortName: jms\n" +
                "-------\n" +
                "year: 2019";

        assertInterpolation(map, expected);
    }


    @Test
    public void ignoresDollarBrace() throws Exception {
        final HashMap<String, Object> map = new HashMap<>();

        map.put("year", "2019");
        map.put("shortName", "jms");
        map.put("repoName", "{shortName}-api");
        map.put("scmUrl", "git://${github}.com/eclipse-ee4j/{repoName}.git");


        final String expected = "repoName: jms-api\n" +
                "-------\n" +
                "scmUrl: git://${github}.com/eclipse-ee4j/jms-api.git\n" +
                "-------\n" +
                "shortName: jms\n" +
                "-------\n" +
                "year: 2019";

        assertInterpolation(map, expected);
    }

    public static void assertInterpolation(final HashMap<String, Object> map, final String expected) {
        Templates.interpolate(map);

        final List<String> list = map.entrySet().stream()
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        assertEquals(expected, Join.join("\n-------\n", list));
    }
}