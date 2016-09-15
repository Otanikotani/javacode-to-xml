package com.github.generator.xml;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.thoughtworks.xstream.XStream;

import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class XStreamSerializer {

    private final static NameExprConverter NAME_EXPR_CONVERTER = new NameExprConverter();

    private XStream xstream;

    public void serialize(Node node, String className, Writer out) {
        Path path = Paths.get(Constants.XML_OUT_DIR, className + ".xml");
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

        xstream.toXML(node, out);
    }

    private void nameAsAttributeFor(Class<? extends Node> clazz) {
        xstream.useAttributeFor(clazz, "name");
        xstream.registerLocalConverter(clazz, "name", NAME_EXPR_CONVERTER);
    }
}
