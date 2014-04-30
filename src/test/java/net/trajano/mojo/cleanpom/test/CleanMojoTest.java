package net.trajano.mojo.cleanpom.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        final FileInputStream fileInputStream = new FileInputStream(tempPom);
        final String data = IOUtils.toString(fileInputStream);
        assertTrue(data.contains("<pomFile>target/test-pom.xml</pomFile>"));
        assertTrue(data.contains("<!-- Configuration comment -->"));
        fileInputStream.close();
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
     * Makes sure empty configuration blocks are removed.
     *
     * @throws Exception
     */
    @Test
    public void testPomWithEmptyConfiguration() throws Exception {
        final File defaultPom = new File(
                "src/test/resources/net/trajano/mojo/cleanpom/default-pom.xml");
        final File testPom = new File(
                "src/test/resources/net/trajano/mojo/cleanpom/no-configuration-pom.xml");
        final File tempPom = new File("target/test-pom.xml");
        FileUtils.copyFile(testPom, tempPom);
        assertTrue(tempPom.exists());

        final CleanMojo mojo = (CleanMojo) rule.lookupMojo("clean", defaultPom);
        assertNotNull(mojo);
        mojo.execute();

        final FileInputStream fileInputStream = new FileInputStream(tempPom);
        final String string = IOUtils.toString(fileInputStream);
        assertFalse(string.contains("<configuration"));
        fileInputStream.close();
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

    /**
     * Makes sure organization and developer details are is preserved.
     *
     * @throws Exception
     */
    @Test
    public void testPomWithOrganization() throws Exception {
        final File defaultPom = new File(
                "src/test/resources/net/trajano/mojo/cleanpom/default-pom.xml");
        final File testPom = new File(
                "src/test/resources/net/trajano/mojo/cleanpom/organization-pom.xml");
        final File tempPom = new File("target/test-pom.xml");
        FileUtils.copyFile(testPom, tempPom);
        assertTrue(tempPom.exists());

        final CleanMojo mojo = (CleanMojo) rule.lookupMojo("clean", defaultPom);
        assertNotNull(mojo);
        mojo.execute();

        final FileInputStream fileInputStream = new FileInputStream(tempPom);
        final String data = IOUtils.toString(fileInputStream);
        fileInputStream.close();
        assertTrue(data.contains("<organization>Trajano</organization>"));
        assertTrue(data.contains("<url>http://www.trajano.net/</url>"));
    }

    /**
     * Makes sure plugin order is preserved.
     *
     * @throws Exception
     */
    @Test
    public void testPomWithPlugins() throws Exception {
        final File defaultPom = new File(
                "src/test/resources/net/trajano/mojo/cleanpom/default-pom.xml");
        final File testPom = new File(
                "src/test/resources/net/trajano/mojo/cleanpom/plugins-pom.xml");
        final File tempPom = new File("target/test-pom.xml");
        FileUtils.copyFile(testPom, tempPom);
        assertTrue(tempPom.exists());

        final CleanMojo mojo = (CleanMojo) rule.lookupMojo("clean", defaultPom);
        assertNotNull(mojo);
        mojo.execute();

        final FileInputStream fileInputStream = new FileInputStream(tempPom);
        final String data = IOUtils.toString(fileInputStream);
        fileInputStream.close();
        assertTrue(data.indexOf("cleanpom-maven-plugin") < data
                .indexOf("jaxws-maven-plugin"));
        assertTrue(data.indexOf("cleanpom-maven-plugin") < data
                .indexOf("batik-maven-plugin"));
    }
}
