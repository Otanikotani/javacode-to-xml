package com.github.generator.xml;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class XPathFinderTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    private InputStream simpleJavaClass = this.getClass().getResourceAsStream("Foo.java");

    @Test
    public void toDocumentReturnsDocumentWithExpectedClassName() throws Exception {
        CompilationUnit cu = JavaParser.parse(simpleJavaClass);

        Document document = Converters.newConverter().toDocument(cu);

        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression classNameExpression = xPath.compile("/CompilationUnit/ClassOrInterfaceDeclaration/@name");
        String name = (String) classNameExpression.evaluate(document, XPathConstants.STRING);
        assertThat(name).isEqualTo("SomeClass");
    }

    @Test
    public void toStringReturnsExpectedXml() throws Exception {
        File expectedXml = Paths.get(this.getClass().getResource("Foo.xml").toURI()).toFile();
        CompilationUnit cu = JavaParser.parse(simpleJavaClass);

        String string = Converters.newConverter().toXmlString(cu);

        assertThat(string).isXmlEqualToContentOf(expectedXml);
    }

    @Test
    public void toFileCorrectlyWriteXml() throws Exception {
        File expectedXml = Paths.get(this.getClass().getResource("Foo.xml").toURI()).toFile();
        CompilationUnit cu = JavaParser.parse(simpleJavaClass);
        File file = folder.newFile("Foo.xml");

        Converters.newConverter().toFile(cu, file.toPath());

        String result = new String(Files.readAllBytes(file.toPath()));
        assertThat(result).isXmlEqualToContentOf(expectedXml);
    }
}
