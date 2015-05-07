package net.trajano.mojo.cleanpom;


import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import net.trajano.mojo.cleanpom.internal.DtdResolver;
import net.trajano.mojo.cleanpom.internal.EolNormalizingStream;
import net.trajano.mojo.cleanpom.internal.Messages;

/**
 * Cleans XML sources in general. Tied to the {@link LifecyclePhase#INITIALIZE}
 * so sources are cleaned up before doing anything else.
 */
@Mojo(name = "clean-xml",
    defaultPhase = LifecyclePhase.INITIALIZE,
    threadSafe = true)
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
     * <code>src/main/*.[xml|xsd|xslt]</code> and <code>src/site/*.xml</code> .
     */
    @Parameter(required = false)
    private FileSet[] xmlFileSets;

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
            xmlFileSets[0].addInclude("**/*.xsd");
            xmlFileSets[0].addInclude("**/*.xslt");
            xmlFileSets[1] = new FileSet();
            xmlFileSets[1].setDirectory("src/site");
            xmlFileSets[1].addInclude("**/*.xml");
        }

        final File tempFile;
        try {
            tempFile = File.createTempFile("temp", "xml");
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
    private void transform(final File sourceFile,
        final File targetFile) throws MojoExecutionException {

        try {
            final SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            final XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            final DtdResolver resolver = new DtdResolver();
            xmlReader.setEntityResolver(resolver);
            xmlReader.setContentHandler(resolver);

            final FileInputStream source = new FileInputStream(sourceFile);
            xmlReader.parse(new InputSource(source));
            source.close();

            final SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
            final OutputStream outputStream = buildContext.newFileOutputStream(targetFile);
            final Transformer transformer;
            if (resolver.isDtdPresent()) {
                transformer = tf.newTransformer(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/clean-with-dtd.xslt")));
                transformer.setOutputProperty("{http://xml.apache.org/xalan}line-separator", "\n");
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, resolver.getPublicId());
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, resolver.getSystemId());
            } else {
                transformer = tf.newTransformer(new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/clean.xslt")));
            }
            transformer.transform(new StreamSource(sourceFile), new StreamResult(new EolNormalizingStream(outputStream)));
            getLog().debug(format(R.getString("donecleaning"), targetFile));
            sourceFile.delete();
            outputStream.close();
        } catch (final SAXException e) {
            throw new MojoExecutionException(format(Messages.TRANSFORM_FAIL, targetFile), e);
        } catch (final TransformerException e) {
            throw new MojoExecutionException(format(Messages.TRANSFORM_FAIL, targetFile), e);
        } catch (final IOException e) {
            throw new MojoExecutionException(format(Messages.TRANSFORM_FAIL_IO, targetFile), e);
        } catch (final ParserConfigurationException e) {
            throw new MojoExecutionException(format(Messages.TRANSFORM_FAIL, targetFile), e);
        }
    }
}
