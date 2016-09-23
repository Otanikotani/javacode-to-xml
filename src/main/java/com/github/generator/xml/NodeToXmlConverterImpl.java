package com.github.generator.xml;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.expr.*;
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

        nameAsAttributeFor(AnnotationExpr.class);

        xstream.useAttributeFor(MethodDeclaration.class, "modifiers");
        xstream.useAttributeFor(MethodDeclaration.class, "arrayCount");
        xstream.useAttributeFor(MethodDeclaration.class, "isDefault");
        nameAsAttributeFor(MethodDeclaration.class);

        nameAsAttributeFor(MethodCallExpr.class);
        nameAsAttributeFor(TypeDeclaration.class);

        xstream.useAttributeFor(ClassOrInterfaceType.class, "name");
        xstream.useAttributeFor(VariableDeclaratorId.class, "name");

        xstream.useAttributeFor(FieldAccessExpr.class, "field");
        xstream.registerLocalConverter(FieldAccessExpr.class, "field", NAME_EXPR_CONVERTER);

        xstream.omitField(Node.class, "range");
        xstream.omitField(Node.class, "orphanComments");
        xstream.omitField(Node.class, "parentNode");
        xstream.omitField(BlockStmt.class, "stmts");

        xstream.useAttributeFor(StringLiteralExpr.class, "value");
        xstream.useAttributeFor(NameExpr.class, "name");

        xstream.addImplicitCollection(Node.class, "childrenNodes");

        xstream.registerConverter(new BinaryExprConverter());

        xstream.omitField(IfStmt.class, "condition");
        xstream.omitField(IfStmt.class, "thenStmt");

        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
    }

    public String toXmlString(Node node) {
        return xstream.toXML(node);
    }

    public void toFile(Node node, Path path) throws IOException {
        xstream.toXML(node, new FileWriter(path.toFile()));
    }

    public Document toDocument(Node node) throws ParserConfigurationException {
        String xml = xstream.toXML(node);
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        try {
            return builder.parse(new InputSource(new StringReader(xml)));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            return builder.newDocument();
        }
    }

    private void nameAsAttributeFor(Class<? extends Node> clazz) {
        xstream.useAttributeFor(clazz, "name");
        xstream.registerLocalConverter(clazz, "name", NAME_EXPR_CONVERTER);
    }
}
