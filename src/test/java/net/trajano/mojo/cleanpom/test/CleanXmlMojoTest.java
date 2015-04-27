package net.trajano.mojo.cleanpom.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.testing.MojoRule;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Rule;
import org.junit.Test;

import net.trajano.mojo.cleanpom.CleanMojo;
import net.trajano.mojo.cleanpom.CleanXmlMojo;

/**
 * Tests the {@link CleanMojo}.
 */
public class CleanXmlMojoTest {
    @Rule
    public MojoRule rule = new MojoRule();

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
        rule.setVariableValueToObject(mojo, "xmlFileSets", new FileSet[] { xmlFiles });
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

}
