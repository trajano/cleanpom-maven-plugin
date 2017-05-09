package net.trajano.mojo.cleanpom.internal;

import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * Processes the DOCTYPE tag if present so it can be recreated.
 */
public class DtdResolver implements
    EntityResolver,
    LexicalHandler,
    ContentHandler {

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
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void characters(final char[] ch,
        final int start,
        final int length)
        throws SAXException {

        // does nothing
    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void comment(final char[] ch,
        final int start,
        final int length) throws SAXException {

        // Does nothing

    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void endCDATA() throws SAXException {

        // Does nothing

    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void endDocument()
        throws SAXException {

        // does nothing
    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void endDTD() throws SAXException {

        // TODO Auto-generated method stub

    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void endElement(final String uri,
        final String localName,
        final String qName)
        throws SAXException {

        // does nothing
    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void endEntity(final String name) throws SAXException {

        // Does nothing
    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void endPrefixMapping(final String prefix)
        throws SAXException {

        // does nothing
    }

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
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void ignorableWhitespace(final char[] ch,
        final int start,
        final int length)
        throws SAXException {

        // does nothing
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
     * Internet.
     *
     * @param doctypePublicId
     *            public ID
     * @param doctypeSystemId
     *            system ID
     * @return empty string reader.
     */
    @Override
    public InputSource resolveEntity(final String doctypePublicId,
        final String doctypeSystemId)
        throws SAXException,
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
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void skippedEntity(final String localName)
        throws SAXException {

        //does nothing
    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void startCDATA() throws SAXException {

        // does nothing
    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void startDocument()
        throws SAXException {

        //does nothing
    }

    @Override
    public void startDTD(final String doctypeName,
        final String doctypePublicId,
        final String doctypeSystemId) throws SAXException {

        name = doctypeName;
        publicId = doctypePublicId;
        systemId = doctypeSystemId;

    }

    /**
     * Extracts local name the root element. Once it is determined this method
     * does nothing.
     *
     * @param uri
     *            URI
     * @param localName
     *            local name
     * @param qName
     *            qualified name
     * @param atts
     *            attributes
     */
    @Override
    public void startElement(final String uri,
        final String localName,
        final String qName,
        final Attributes atts)
        throws SAXException {
        // does nothing

    }

    @Override
    public void startEntity(final String name) throws SAXException {

        // TODO Auto-generated method stub

    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void startPrefixMapping(final String prefix,
        final String uri)
        throws SAXException {

        //does nothing
    }

}
