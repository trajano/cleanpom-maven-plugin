package net.trajano.mojo.cleanpom.internal;

import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * Processes the DOCTYPE tag if present so it can be recreated.
 */
public class DtdResolver extends DefaultHandler2 {

    /**
     * Document locator.
     */
    private Locator documentLocator;

    /**
     * Name of the document element.
     */
    private String name;

    /**
     * Flag to indicate that a processing instruction was found.
     */
    private boolean processingInstructionFound;

    /**
     * Public ID.
     */
    private String publicId;

    /**
     * System ID.
     */
    private String systemId;

    /**
     * Gets the document locator used to track the progress in the XML file.
     *
     * @return the document locator.
     */
    public Locator getDocumentLocator() {

        return documentLocator;
    }

    /**
     * Local name of the first element.
     *
     * @return local name of the first element
     */
    public String getName() {

        return name;
    }

    /**
     * Public ID.
     *
     * @return public ID.
     */
    public String getPublicId() {

        return publicId;
    }

    /**
     * System ID.
     *
     * @return system ID.
     */
    public String getSystemId() {

        return systemId;
    }

    /**
     * Checks if the DTD information is present. Only name is required
     * especially for HTML5 which defines itself as
     * <code>&lt;!DOCTYPE html&gt;</code>.
     *
     * @return <code>true</code> if the DTD information is present.
     */
    public boolean isDtdPresent() {

        return name != null;
    }

    /**
     * Checks if a processing instruction has been found so far.
     *
     * @return <code>true</code> if a processing instruction has been found so
     *         far.
     */
    public boolean isProcessingInstructionFound() {

        return processingInstructionFound;
    }

    /**
     * Tracks that a processing instruction is found. {@inheritDoc}
     */
    @Override
    public void processingInstruction(final String target,
        final String data)
        throws SAXException {

        processingInstructionFound = true;
    }

    /**
     * Processes a DOCTYPE and extracts the data from it. It returns a new
     * {@link InputSource} to prevent DTDs from being retrieved from the
     * Internet. {@inheritDoc}
     * 
     * @param doctypePublicId
     *            public ID
     * @param doctypeSystemId
     *            system ID
     * @return empty string reader.
     */
    @Override
    public InputSource resolveEntity(final String name,
        final String doctypePublicId,
        final String baseURI,
        final String doctypeSystemId) throws SAXException,
        IOException {

        final InputSource inputSource = new InputSource(new StringReader(""));

        inputSource.setPublicId(doctypePublicId);
        inputSource.setSystemId(doctypeSystemId);
        return inputSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDocumentLocator(final Locator locator) {

        documentLocator = locator;
    }

    /**
     * Read the DTD information and store it for later. {@inheritDoc}
     */
    @Override
    public void startDTD(final String doctypeName,
        final String doctypePublicId,
        final String doctypeSystemId) throws SAXException {

        name = doctypeName;
        publicId = doctypePublicId;
        systemId = doctypeSystemId;

    }

}
