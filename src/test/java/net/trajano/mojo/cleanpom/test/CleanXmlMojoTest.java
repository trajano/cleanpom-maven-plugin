package net.trajano.mojo.cleanpom.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.MojoRule;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Rule;
import org.junit.Test;

import net.trajano.mojo.cleanpom.CleanXmlMojo;

/**
 * Tests the {@link CleanXmlMojo}.
 */
public class CleanXmlMojoTest {

    @Rule
    public MojoRule rule = new MojoRule();

    @Test(expected = MojoExecutionException.class)
    public void testFailWithTwoDTD() throws Exception {

        final File testPom = new File("src/test/resources/net/trajano/mojo/cleanpom/cleaner-pom.xml");
        final File xml = new File("src/test/resources/net/trajano/mojo/cleanpom/dual-doctype.xml");
        final File temp = File.createTempFile("dirty", "");
        temp.delete();
        temp.mkdirs();
        FileUtils.copyFile(xml, new File(temp, "dirty1.xml"));

        final CleanXmlMojo mojo = (CleanXmlMojo) rule.lookupMojo("clean-xml", testPom);
        final FileSet xmlFiles = new FileSet();
        xmlFiles.setDirectory(temp.getAbsolutePath());
        xmlFiles.addInclude("**/*.xml");
        rule.setVariableValueToObject(mojo, "xmlFileSets", new FileSet[] {
            xmlFiles
        });
        assertNotNull(mojo);
        try {
            mojo.execute();
        } finally {
            FileUtils.deleteDirectory(temp);
        }
    }

    /**
     * Tests general cleaning.
     *
     * @throws Exception
     */
    @Test
    public void testGeneralCleaning() throws Exception {

        final File testPom = new File("src/test/resources/net/trajano/mojo/cleanpom/cleaner-pom.xml");
        final File dirtyXml = new File("src/test/resources/net/trajano/mojo/cleanpom/dirty.xml");
        final File cleanedXml = new File("src/test/resources/net/trajano/mojo/cleanpom/cleaned.xml");
        final File temp = File.createTempFile("dirty", "");
        temp.delete();
        temp.mkdirs();
        FileUtils.copyFile(dirtyXml, new File(temp, "dirty1.xml"));
        FileUtils.copyFile(dirtyXml, new File(temp, "dirty2.xml"));
        FileUtils.copyFile(cleanedXml, new File(temp, "dirty3.xml"));
        assertTrue(temp.exists());

        final CleanXmlMojo mojo = (CleanXmlMojo) rule.lookupMojo("clean-xml", testPom);
        final FileSet xmlFiles = new FileSet();
        xmlFiles.setDirectory(temp.getAbsolutePath());
        xmlFiles.addInclude("**/*.xml");
        rule.setVariableValueToObject(mojo, "xmlFileSets", new FileSet[] {
            xmlFiles
        });
        assertNotNull(mojo);
        mojo.execute();
        final String cleanData;
        {
            final FileInputStream fileInputStream = new FileInputStream(cleanedXml);
            cleanData = IOUtils.toString(fileInputStream);
            fileInputStream.close();
        }
        {
            final FileInputStream fileInputStream = new FileInputStream(new File(temp, "dirty1.xml"));
            final String data = IOUtils.toString(fileInputStream);
            fileInputStream.close();
            assertEquals(cleanData, data);
        }
        {
            final FileInputStream fileInputStream = new FileInputStream(new File(temp, "dirty2.xml"));
            final String data = IOUtils.toString(fileInputStream);
            fileInputStream.close();
            assertEquals(cleanData, data);
        }
        {
            final FileInputStream fileInputStream = new FileInputStream(new File(temp, "dirty3.xml"));
            final String data = IOUtils.toString(fileInputStream);
            fileInputStream.close();
            assertEquals(cleanData, data);
        }
        FileUtils.deleteDirectory(temp);
    }

    @Test
    public void testNoProlog() throws Exception {

        final File testPom = new File("src/test/resources/net/trajano/mojo/cleanpom/cleaner-pom.xml");
        final File dirtyXml = new File("src/test/resources/net/trajano/mojo/cleanpom/cleaner-pom.xml");
        final File temp = File.createTempFile("dirty", "");
        temp.delete();
        temp.mkdirs();
        FileUtils.copyFile(dirtyXml, new File(temp, "dirty1.xml"));

        final CleanXmlMojo mojo = (CleanXmlMojo) rule.lookupMojo("clean-xml", testPom);
        final FileSet xmlFiles = new FileSet();
        xmlFiles.setDirectory(temp.getAbsolutePath());
        xmlFiles.addInclude("**/*.xml");
        rule.setVariableValueToObject(mojo, "xmlFileSets", new FileSet[] {
            xmlFiles
        });
        assertNotNull(mojo);
        mojo.execute();
        final String cleanData;
        {
            final FileInputStream fileInputStream = new FileInputStream(testPom);
            cleanData = IOUtils.toString(fileInputStream);
            fileInputStream.close();
        }
        {
            final FileInputStream fileInputStream = new FileInputStream(new File(temp, "dirty1.xml"));
            final String data = IOUtils.toString(fileInputStream);
            fileInputStream.close();
            assertEquals(cleanData, data);
        }
        FileUtils.deleteDirectory(temp);
    }

    @Test
    public void testWithDTD() throws Exception {

        final File testPom = new File("src/test/resources/net/trajano/mojo/cleanpom/cleaner-pom.xml");
        final File xml = new File("src/test/resources/net/trajano/mojo/cleanpom/checkstyle-configuration.xml");
        final File temp = File.createTempFile("dirty", "");
        temp.delete();
        temp.mkdirs();
        FileUtils.copyFile(xml, new File(temp, "dirty1.xml"));

        final CleanXmlMojo mojo = (CleanXmlMojo) rule.lookupMojo("clean-xml", testPom);
        final FileSet xmlFiles = new FileSet();
        xmlFiles.setDirectory(temp.getAbsolutePath());
        xmlFiles.addInclude("**/*.xml");
        rule.setVariableValueToObject(mojo, "xmlFileSets", new FileSet[] {
            xmlFiles
        });
        assertNotNull(mojo);
        mojo.execute();
        final String cleanData;
        {
            final FileInputStream fileInputStream = new FileInputStream(xml);
            cleanData = IOUtils.toString(fileInputStream);
            fileInputStream.close();
        }
        {
            final FileInputStream fileInputStream = new FileInputStream(new File(temp, "dirty1.xml"));
            final String data = IOUtils.toString(fileInputStream);
            fileInputStream.close();
            assertTrue(data.contains(
                "<!DOCTYPE module PUBLIC \"-//Puppy Crawl//DTD Check Configuration 1.3//EN\" \"http://www.puppycrawl.com/dtds/configuration_1_3.dtd\">"));
            assertEquals(cleanData, data);
        }
        FileUtils.deleteDirectory(temp);
    }
}
