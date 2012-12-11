package net.sf.jstring.io.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import net.sf.jstring.builder.BundleBuilder;
import net.sf.jstring.builder.BundleKeyBuilder;
import net.sf.jstring.builder.BundleSectionBuilder;
import net.sf.jstring.builder.BundleValueBuilder;
import net.sf.jstring.io.AbstractParser;
import net.sf.jstring.io.CannotOpenException;
import net.sf.jstring.io.CannotParseException;
import net.sf.jstring.model.Bundle;
import net.sf.jstring.model.BundleKey;
import net.sf.jstring.model.BundleSection;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser extends AbstractParser<XMLParser> {

    public interface ElementIterator {

        void onElement (int index, Element node);

    }

    public XMLParser() {
        super();
    }

    protected XMLParser(boolean trace) {
        super(trace);
    }
    
    @Override
    public XMLParser withTraces() {
    	return new XMLParser(true);
    }

    @Override
    public Bundle parse(URL url) {
        Document dom = null;
        try {
            dom = parseDOM(url);
        } catch (IOException e) {
            throw new CannotParseException(url, e);
        } catch (ParserConfigurationException e) {
            throw new CannotParseException(url, e);
        } catch (SAXException e) {
            throw new CannotParseException(url, e);
        }
        return parse(dom, getBundleName(url));
    }

    private Document parseDOM(URL url) throws IOException, ParserConfigurationException, SAXException {
        InputStream in = url.openStream();
        if (in == null) {
            throw new CannotOpenException(url);
        } else {
            Document dom;
            try {
                // Parsing
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilderFactory.setNamespaceAware(true);
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                dom = documentBuilder.parse(in);
                // Validation
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = schemaFactory.newSchema(getClass().getResource("/schema/strings.xsd"));
                Validator validator = schema.newValidator();
                validator.validate(new DOMSource(dom));
            } finally {
                in.close();
            }
            return dom;
        }
    }

    private Bundle parse(Document dom, String name) {
        final BundleBuilder builder = BundleBuilder.create(name);
        // Top comments
        each(
                dom.getDocumentElement(), "comment",
                new ElementIterator() {
                    @Override
                    public void onElement(int index, Element node) {
                        builder.comment(getTextForComment(node));
                    }
                }
            );
        // Top keys
        final BundleSectionBuilder defaultSection = BundleSectionBuilder.create("default");
        each(
                dom.getDocumentElement(), "key",
                new ElementIterator() {
                    @Override
                    public void onElement(int index, Element keyNode) {
                        defaultSection.key(parseKey((Element) keyNode));
                    }
                }
        );
        BundleSection section = defaultSection.build();
        if (!section.getKeys().isEmpty()) {
            builder.section(section);
        }
        // Sections
        each(
                dom.getDocumentElement(), "section",
                new ElementIterator() {
                    @Override
                    public void onElement(int index, Element node) {
                        parseSection(builder,  node);
                    }
                }
        );
        // OK
        return builder.build();
    }

    private void parseSection(BundleBuilder builder, Element sectionNode) {
        final BundleSectionBuilder section = BundleSectionBuilder.create(sectionNode.getAttribute("name"));
        // Comments
        each(
                sectionNode, "comment",
                new ElementIterator() {
                    @Override
                    public void onElement(int index, Element node) {
                        section.comment(getTextForComment(node));
                    }
                }
        );
        // Keys
        each(
                sectionNode, "key",
                new ElementIterator() {
                    @Override
                    public void onElement(int index, Element node) {
                        section.key(parseKey(node));
                    }
                }
        );
        // OK
        builder.section(section.build());
    }

    private BundleKey parseKey(Element keyNode) {
        String name = keyNode.getAttribute("name");
        final BundleKeyBuilder key = BundleKeyBuilder.create(name);
        // Comments
        each(
                keyNode, "comment",
                new ElementIterator() {
                    @Override
                    public void onElement(int index, Element node) {
                        key.comment(getTextForComment(node));
                    }
                }
        );
        // Values
        each(
                keyNode, "value",
                new ElementIterator() {
                    @Override
                    public void onElement (int index, Element node) {
                        parseValue (key, node);
                    }
                }
        );
        // OK
        return key.build();
    }

    private void parseValue(BundleKeyBuilder key, Element valueNode) {
        String language = valueNode.getAttribute("lang");
        final BundleValueBuilder valueBuilder = BundleValueBuilder.create();
        // Gets all comments
        each (
            valueNode, "comment",
            new ElementIterator() {
                @Override
                public void onElement(int index, Element node) {
                    valueBuilder.comment(getTextForComment(node));
                }
            }
        );
        // Gets the text nodes
        NodeList nodes = valueNode.getChildNodes();
        int count = nodes.getLength();
        for (int i = 0 ; i < count ; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                String text = getText(node);
                valueBuilder.value(text);
            }
        }
        // OK
        key.value(language, valueBuilder.build());
    }

    private void each (Element parent, String tagName, ElementIterator iterator) {
        NodeList nodes = parent.getChildNodes();
        int count = nodes.getLength();
        for (int i = 0 ; i < count ; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                if (StringUtils.equals(tagName, e.getTagName())) {
                    iterator.onElement(i, e);
                }
            }
        }
    }

    private String getTextForComment(Node node) {
        return StringUtils.trim(node.getTextContent());
    }

    private String getText(Node node) {
        String raw = StringUtils.trim(node.getTextContent());
        // Gets all lines
        String[] lines = StringUtils.split(raw, "\n\r");
        List<String> texts = new ArrayList<String>();
        for (String line: lines) {
            line = StringUtils.trim(line);
            texts.add(line);
        }
        // Join
        String text = StringUtils.join(texts, " ");
        // Escaping
        text = StringEscapeUtils.unescapeJava(text);
        return text;
    }
}
