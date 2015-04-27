package net.trajano.mojo.cleanpom;

import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class DtdResolver implements EntityResolver, ContentHandler {

    private String name;
    private String publicId;

    private String systemId;

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
        // TODO Auto-generated method stub

    }

    public String getName() {
        return name;
    }

    public String getPublicId() {
        return publicId;
    }

    public String getSystemId() {
        return systemId;
    }

    @Override
    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        // TODO Auto-generated method stub

    }

    public boolean isDtdPresent() {
        return name != null && publicId != null && systemId != null;
    }

    @Override
    public void processingInstruction(final String target, final String data) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
        this.publicId = publicId;
        this.systemId = systemId;
        return null;
    }

    @Override
    public void setDocumentLocator(final Locator locator) {
        // TODO Auto-generated method stub

    }

    @Override
    public void skippedEntity(final String name) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {

        if (name == null) {
            name = localName;
        }

    }

    @Override
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        // TODO Auto-generated method stub

    }

}
