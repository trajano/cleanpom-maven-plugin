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

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.Scanner;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * Cleans XML sources in general. Tied to the {@link LifecyclePhase#INITIALIZE}
 * so sources are cleaned up before doing anything else.
 */
@Mojo(name = "clean-xml", defaultPhase = LifecyclePhase.INITIALIZE, threadSafe = true)
public class CleanXmlMojo extends AbstractMojo {
    /**
     * Resource bundle.
     */
    private static final ResourceBundle R = ResourceBundle.getBundle("META-INF/Messages");

    /**
     * Build context.
     */
    @Component
    private BuildContext buildContext;

    /**
     * List of XML filesets to process. This defaults to
     * <code>src/main/*.xml</code> and <code>src/site/*.xml</code> .
     */
    @Parameter(required = false)
    private FileSet[] xmlFileSets;

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
    private TransformerHandler buildHandlerChain(final SAXTransformerFactory tf, final OutputStream outputStream)
            throws IOException, TransformerException {
        TransformerHandler handler = null;
        TransformerHandler lastHandler = null;

        final String[] xsltFiles = new String[] { "/META-INF/clean.xslt" };
        for (final String xsltFile : xsltFiles) {
            final InputStream xsltStream = getClass()
                    .getResourceAsStream(xsltFile.charAt(0) == '/' ? xsltFile : "/META-INF/" + xsltFile);
            // The stream source needs to be defined here.
            final TransformerHandler currentHandler = tf.newTransformerHandler(new StreamSource(xsltStream)); // NOPMD
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
     * Performs the cleanup.
     *
     * @throws MojoExecutionException
     *             problem with the execution.
     */
    @Override
    public void execute() throws MojoExecutionException {
        if (xmlFileSets == null) {
            xmlFileSets = new FileSet[2];
            xmlFileSets[0] = new FileSet();
            xmlFileSets[0].setDirectory("src/main");
            xmlFileSets[0].addInclude("**/*.xml");
            xmlFileSets[1] = new FileSet();
            xmlFileSets[1].setDirectory("src/site");
            xmlFileSets[1].addInclude("**/*.xml");
        }

        final File tempFile;
        try {
            tempFile = File.createTempFile("pom", "xml");
        } catch (final IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        for (final FileSet xmlFiles : xmlFileSets) {
            final File dir = new File(xmlFiles.getDirectory());
            if (!dir.exists()) {
                continue;
            }
            final Scanner scanner = buildContext.newScanner(dir, false);
            scanner.setIncludes(xmlFiles.getIncludes().toArray(new String[0]));
            scanner.scan();
            for (final String includedFile : scanner.getIncludedFiles()) {
                final File file = new File(scanner.getBasedir(), includedFile);

                try {
                    FileUtils.copyFile(file, tempFile);
                    transform(tempFile, file);
                } catch (final IOException e) {
                    throw new MojoExecutionException(format(R.getString("copyfail"), file, tempFile), e);
                }

            }
        }
        tempFile.delete();
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
    private void transform(final File sourceFile, final File targetFile) throws MojoExecutionException {
        try {
            final SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();

            final OutputStream outputStream = buildContext.newFileOutputStream(targetFile);
            final TransformerHandler handler = buildHandlerChain(tf, outputStream);
            final Transformer transformer = tf.newTransformer();
            transformer.transform(new StreamSource(sourceFile), new SAXResult(handler));
            getLog().debug(format(R.getString("donecleaning"), targetFile));
            sourceFile.delete();
            outputStream.close();
        } catch (final TransformerException e) {
            throw new MojoExecutionException(format(R.getString("transformfail"), sourceFile), e);
        } catch (final IOException e) {
            throw new MojoExecutionException(format(R.getString("transformfailio"), sourceFile), e);
        }
    }
}
