package com.github.generator.xml;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.thoughtworks.xstream.XStream;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

class NodeToXmlConverterImpl implements NodeToXmlConverter {

    private final static NameExprConverter NAME_EXPR_CONVERTER = new NameExprConverter();

    private XStream xstream;

    public NodeToXmlConverterImpl() {
        xstream = new XStream();

        xstream.aliasPackage("", "com.github.javaparser.ast.body");
        xstream.aliasPackage("", "com.github.javaparser.ast.type");
        xstream.aliasPackage("", "com.github.javaparser.ast.expr");
        xstream.aliasPackage("", "com.github.javaparser.ast.stmt");
        xstream.aliasPackage("", "com.github.javaparser.ast");
        xstream.addDefaultImplementation(LinkedList.class, List.class);

//        nameAsAttributeFor(AnnotationExpr.class);
        xstream.omitField(AnnotationExpr.class, "innerList");
        xstream.omitField(AnnotationExpr.class, "observers");

        xstream.useAttributeFor(MethodDeclaration.class, "modifiers");
        xstream.useAttributeFor(MethodDeclaration.class, "isDefault");
        nameAsAttributeFor(MethodDeclaration.class);

        nameAsAttributeFor(MethodCallExpr.class);
        nameAsAttributeFor(TypeDeclaration.class);

        xstream.useAttributeFor(ClassOrInterfaceType.class, "name");
        xstream.omitField(ClassOrInterfaceType.class, "innerList");
        xstream.omitField(ClassOrInterfaceType.class, "observers");

        xstream.registerLocalConverter(FieldAccessExpr.class, "name", NAME_EXPR_CONVERTER);

        xstream.omitField(Node.class, "range");
        xstream.omitField(Node.class, "orphanComments");
        xstream.omitField(Node.class, "parentNode");
        xstream.omitField(Node.class, "observers");
        xstream.omitField(BlockStmt.class, "stmts");

        xstream.useAttributeFor(StringLiteralExpr.class, "value");
        xstream.useAttributeFor(Name.class, "identifier");
        xstream.useAttributeFor(SimpleName.class, "identifier");

        xstream.addImplicitCollection(Node.class, "childNodes");

        xstream.registerConverter(new BinaryExprConverter());

        xstream.omitField(IfStmt.class, "condition");
        xstream.omitField(IfStmt.class, "thenStmt");

        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
    }

    public String toXmlString(Node node) {
        return xstream.toXML(node);
    }

    public void toFile(Node node, Path path) {
        try {
            xstream.toXML(node, new FileWriter(path.toFile()));
        } catch (IOException e) {
            throw new NodeToXmlConverterException("Failed to write xml to file", e);
        }
    }

    public Document toDocument(Node node) {
        String xml = xstream.toXML(node);
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException e) {
            throw new NodeToXmlConverterException("Failed to create new document builder!", e);
        } catch (IOException | SAXException e) {
            return builder.newDocument();
        }
    }

    private void nameAsAttributeFor(Class<? extends Node> clazz) {
        xstream.useAttributeFor(clazz, "name");
        xstream.registerLocalConverter(clazz, "name", NAME_EXPR_CONVERTER);
    }
}
