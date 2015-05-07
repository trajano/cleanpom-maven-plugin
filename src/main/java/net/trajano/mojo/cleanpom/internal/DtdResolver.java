package net.trajano.mojo.cleanpom.internal;

import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Processes the DOCTYPE tag if present so it can be recreated.
 */
public class DtdResolver implements
    EntityResolver,
    ContentHandler {

    /**
     * Document locator.
     */
    @SuppressWarnings("unused")
    private Locator documentLocator;

    /**
     * Name of the document element.
     */
    private String name;

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
    public void endDocument()
        throws SAXException {

        // does nothing
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
    public void endPrefixMapping(final String prefix)
        throws SAXException {

        // does nothing
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
     * Checks if the DTD information is present.
     *
     * @return <code>true</code> if the DTD information is present.
     */
    public boolean isDtdPresent() {

        return name != null && publicId != null && systemId != null;
    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void processingInstruction(final String target,
        final String data)
        throws SAXException {

        // does nothing
    }

    /**
     * Processes a DOCTYPE and extracts the data from it.
     *
     * @param doctypePublicId
     *            public ID
     * @param doctypeSystemId
     *            system ID
     * @return <code>null</code>
     */
    @Override
    public InputSource resolveEntity(final String doctypePublicId,
        final String doctypeSystemId)
        throws SAXException,
        IOException {

        publicId = doctypePublicId;
        systemId = doctypeSystemId;

        return null;
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
    public void startDocument()
        throws SAXException {

        //does nothing
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

        if (name == null) {
            name = localName;
        }

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
