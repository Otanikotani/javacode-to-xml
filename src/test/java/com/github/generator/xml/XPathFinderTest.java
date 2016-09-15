package com.github.generator.xml;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class XPathFinderTest {

    @Test
    public void testSimpleXPath() throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(this.getClass().getResourceAsStream("example.xml"));

        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression classNameExpression = xPath.compile("/CompilationUnit/ClassOrInterfaceDeclaration/@name");
        String name = (String) classNameExpression.evaluate(document, XPathConstants.STRING);

        assertThat(name).isEqualTo("NameExprConverter");
    }
}
