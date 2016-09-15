package com.github.generator.xml;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
