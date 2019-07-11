package org.eclipse.jakartaee.tools.bps;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SpecTest {

    @Test
    public void testFromTsv() throws Exception {
        final String row = "ee4j.jaf\tJakarta Activation\tEclipse Project for JAF\thttps://projects.eclipse.org/projects/ee4j.jaf\tJakarta Activation\tactivation\t1.2.1\tYes\t\t\tactivation\tActivation.adoc\n";

        final Spec spec = Spec.fromTsv(row);

        // Ugly way to assert, but quick and better than nothing
        assertEquals("Spec(projectId=ee4j.jaf, projectName=Jakarta Activation, " +
                "oldProjectName=Eclipse Project for JAF, " +
                "projectUrl=https://projects.eclipse.org/projects/ee4j.jaf, " +
                "specName=Jakarta Activation, specCode=activation, specVersion=1.2.1, " +
                "specRepo=null, apiRepo=null, tckRepo=null)", spec.toString());
    }

    @Test
    public void testLoadTsv() throws Exception {
        final List<Spec> specs = Spec.loadTsv();

        final Spec spec = specs.get(0);

        // Ugly way to assert, but quick and better than nothing
        assertEquals("Spec(projectId=ee4j.jaf, projectName=Jakarta Activation, " +
                "oldProjectName=Eclipse Project for JAF, " +
                "projectUrl=https://projects.eclipse.org/projects/ee4j.jaf, " +
                "specName=Jakarta Activation, specCode=activation, specVersion=1.2.1, " +
                "specRepo=null, apiRepo=null, tckRepo=null)", spec.toString());
    }

    @Test
    public void createScopes() throws Exception {
        final List<Spec> specs = Spec.loadTsv();

        final File file = new File("/Users/dblevins/work/jakartaee/specification-tools/boilerplate-specs/src/main/resources/scopes");
        for (final Spec spec : specs) {
            final String specCode = spec.getSpecCode();
            if (specCode == null) continue;
            final File scope = new File(file, specCode + ".txt");
            scope.createNewFile();
        }
    }

}