package net.trajano.mojo.cleanpom;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * Cleans the POM.
 */
@Mojo(name = "clean",
    defaultPhase = LifecyclePhase.PREPARE_PACKAGE,
    threadSafe = true)
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
    @Parameter(defaultValue = "${basedir}/pom.xml",
        property = "cleanpom.pomFile",
        required = false)
    private File pomFile;

    /**
     * List of XSLT files as a comma separated value. Only one of xsltFiles or
     * xsltFileList is allowed.
     */
    @Parameter(defaultValue = "/META-INF/pom-clean.xslt",
        required = false,
        property = "cleanpom.xsltFileList")
    private String xsltFileList;

    /**
     * List of XSLT files.
     */
    @Parameter(required = false)
    private String[] xsltFiles;

    /**
     * Builds the handler chain.
     *
     * @param tf
     *            SAX transformer factory
     * @param outputStream
     *            output stream
     * @return handler chain.
     * @throws IOException
     *             I/O error when reading the XSLT file.
     * @throws TransformerException
     *             problem building the templates.
     */
    private TransformerHandler buildHandlerChain(
        final SAXTransformerFactory tf,
        final OutputStream outputStream)
            throws IOException, TransformerException {

        TransformerHandler handler = null;
        TransformerHandler lastHandler = null;

        for (final String xsltFile : xsltFiles) {
            final InputStream xsltStream = getClass().getResourceAsStream(
                xsltFile.charAt(0) == '/' ? xsltFile : "/META-INF/"
                        + xsltFile);
            // The stream source needs to be defined here.
            final TransformerHandler currentHandler = tf
                .newTransformerHandler(new StreamSource(xsltStream)); // NOPMD
            xsltStream.close();
            if (lastHandler != null) {
                // The result object needs to be created here.
                lastHandler.setResult(new SAXResult(currentHandler)); // NOPMD
                lastHandler = currentHandler;
            } else {
                lastHandler = currentHandler;
                handler = currentHandler;
            }
        }
        lastHandler.setResult(new StreamResult(outputStream));
        return handler;
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
    private void transform(final File sourceFile,
        final File targetFile)
            throws MojoExecutionException {

        try {
            final SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory
                .newInstance();

            if (xsltFiles == null) {
                if (xsltFileList != null) {
                    xsltFiles = StringUtils.split(xsltFileList, ",");
                } else {
                    xsltFiles = new String[] { "/META-INF/pom-clean.xslt" };
                }
            }
            final OutputStream outputStream = buildContext
                .newFileOutputStream(targetFile);
            final TransformerHandler handler = buildHandlerChain(tf,
                new EolNormalizingStream(outputStream));
            final Transformer transformer = tf.newTransformer();
            transformer.transform(new StreamSource(sourceFile), new SAXResult(
                handler));
            getLog().debug(format(R.getString("donecleaning"), targetFile));
            sourceFile.delete();
            outputStream.close();
        } catch (final TransformerException e) {
            throw new MojoExecutionException(format(
                R.getString("transformfail"), sourceFile), e);
        } catch (final IOException e) {
            throw new MojoExecutionException(format(
                R.getString("transformfailio"), sourceFile), e);
        }
    }
}
