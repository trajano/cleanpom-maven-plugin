package net.trajano.mojo.cleanpom.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.transform.TransformerException;

import net.trajano.mojo.cleanpom.CleanMojo;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.MojoRule;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests the {@link CleanMojo}.
 */
public class CleanMojoTest {
    @Rule
    public MojoRule rule = new MojoRule();

    /**
     * Tests with just the dest directory set.
     *
     * @throws Exception
     */
    @Test
    public void testDefault() throws Exception {
        final File testPom = new File(
                "src/test/resources/net/trajano/mojo/cleanpom/default-pom.xml");
        final File tempPom = new File("target/test-pom.xml");
        FileUtils.copyFile(testPom, tempPom);
        assertTrue(tempPom.exists());

        final CleanMojo mojo = (CleanMojo) rule.lookupMojo("clean", tempPom);
        assertNotNull(mojo);
        mojo.execute();
    }

    /**
     * Fail test because input was not XML.
     *
     * @throws Exception
     */
    @Test()
    public void testNonPom() throws Exception {
        final File defaultPom = new File(
                "src/test/resources/net/trajano/mojo/cleanpom/default-pom.xml");
        final File testPom = new File(
                "src/main/resources/META-INF/Messages.properties");
        final File tempPom = new File("target/test-pom.xml");
        FileUtils.copyFile(testPom, tempPom);
        assertTrue(tempPom.exists());

        final CleanMojo mojo = (CleanMojo) rule.lookupMojo("clean", defaultPom);
        try {
            mojo.execute();
            fail("Should not reach here.");
        } catch (final MojoExecutionException e) {
            assertEquals(TransformerException.class, e.getCause().getClass());
        }
    }

    /**
     * Makes sure license is preserved.
     *
     * @throws Exception
     */
    @Test
    public void testPomWithLicense() throws Exception {
        final File testPom = new File(
                "src/test/resources/net/trajano/mojo/cleanpom/default-pom-with-license.xml");
        final File tempPom = new File("target/test-pom.xml");
        FileUtils.copyFile(testPom, tempPom);
        assertTrue(tempPom.exists());

        final CleanMojo mojo = (CleanMojo) rule.lookupMojo("clean", tempPom);
        assertNotNull(mojo);
        mojo.execute();

        final FileInputStream fileInputStream = new FileInputStream(tempPom);
        assertTrue(IOUtils.toString(fileInputStream).contains(
                "DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER."));
        fileInputStream.close();
    }

}
