package net.trajano.mojo.cleanpom;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

/**
 * Cleans the POM.
 */
@Mojo(name = "clean", defaultPhase = LifecyclePhase.VERIFY, threadSafe = true)
public class CleanMojo extends AbstractMojo {
    /**
     * Resource bundle.
     */
    private static final ResourceBundle R = ResourceBundle
            .getBundle("META-INF/Messages");
    /**
     * The POM file to update.
     */
    @Parameter(defaultValue = "${basedir}/pom.xml", required = false)
    private File pomFile;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException {
        final File tempFile;
        try {
            tempFile = File.createTempFile("pom", "xml");
            tempFile.delete();
        } catch (final IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        try {
            if (pomFile == null) {
                pomFile = new File("pom.xml");
            }
            FileUtils.copyFile(pomFile, tempFile);
        } catch (final IOException e) {
            throw new MojoExecutionException(format(R.getString("copyfail"),
                    pomFile, tempFile), e);
        }
        try {
            final Transformer t = TransformerFactory.newInstance()
                    .newTransformer(
                            new StreamSource(getClass().getResourceAsStream(
                                    "/META-INF/pom-clean.xslt")));
            t.transform(new StreamSource(tempFile), new StreamResult(pomFile));
            getLog().debug(format(R.getString("donecleaning"), pomFile));
            tempFile.delete();
        } catch (final TransformerException e) {
            throw new MojoExecutionException(format(
                    R.getString("transformfail"), tempFile), e);
        }
    }
}
