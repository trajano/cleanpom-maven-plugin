package net.trajano.mojo.cleanpom;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.sonatype.plexus.build.incremental.BuildContext;

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
     * Build context.
     */
    @Component
    private BuildContext buildContext;

    /**
     * The POM file to update.
     */
    @Parameter(defaultValue = "${basedir}/pom.xml", required = false)
    private File pomFile;

    /**
     * Transformer.
     */
    private final Transformer transformer;

    /**
     * Initializes the transfomer.
     */
    public CleanMojo() {
        try {
            final InputStream xsltStream = getClass().getResourceAsStream(
                    "/META-INF/pom-clean.xslt");
            transformer = TransformerFactory.newInstance().newTransformer(
                    new StreamSource(xsltStream));
            xsltStream.close();
        } catch (final TransformerConfigurationException e) {
            throw new IllegalStateException(e);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Builds the temporary file based on the {@link #pomFile}.
     *
     * @return temporary file
     * @throws MojoExecutionException
     *             wraps {@link IOException}
     */
    private File buildTempFile() throws MojoExecutionException {
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
        return tempFile;
    }

    /**
     * Performs the cleanup.
     *
     * @throws MojoExecutionException
     *             problem with the execution.
     */
    @Override
    public void execute() throws MojoExecutionException {
        final File tempFile = buildTempFile();
        transform(tempFile, pomFile);
    }

    /**
     * Performs the transformation.
     *
     * @param sourceFile
     *            source file
     * @param targetFile
     *            target file
     * @throws MojoExecutionException
     *             problem with the execution.
     */
    private void transform(final File sourceFile, final File targetFile)
            throws MojoExecutionException {
        try {
            final OutputStream outputStream = buildContext
                    .newFileOutputStream(targetFile);
            transformer.transform(new StreamSource(sourceFile),
                    new StreamResult(outputStream));
            getLog().debug(format(R.getString("donecleaning"), targetFile));
            sourceFile.delete();
            outputStream.close();
        } catch (final TransformerException e) {
            throw new MojoExecutionException(format(
                    R.getString("transformfail"), sourceFile), e);
        } catch (final IOException e) {
            throw new MojoExecutionException(format(
                    R.getString("transformfail"), sourceFile), e);
        }
    }
}
