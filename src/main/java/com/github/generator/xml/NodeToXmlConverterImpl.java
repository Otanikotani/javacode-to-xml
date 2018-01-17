package com.github.generator.xml;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
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
import java.util.Arrays;
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

        xstream.useAttributeFor(MethodDeclaration.class, "modifiers");
        nameAsAttributeFor(MethodDeclaration.class);

        nameAsAttributeFor(MethodCallExpr.class);
        nameAsAttributeFor(TypeDeclaration.class);
        nameAsAttributeFor(ClassOrInterfaceType.class);
        nameAsAttributeFor(Parameter.class);

        xstream.registerLocalConverter(FieldAccessExpr.class, "name", NAME_EXPR_CONVERTER);

        xstream.omitField(Node.class, "range");
        xstream.omitField(Node.class, "tokenRange");
        xstream.omitField(Node.class, "parsed");
        xstream.omitField(Node.class, "orphanComments");
        xstream.omitField(Node.class, "parentNode");
        xstream.omitField(Node.class, "observers");
        xstream.omitField(NodeList.class, "innerList");
        xstream.omitField(NodeList.class, "observers");
        xstream.omitField(NodeList.class, "parentNode");
        xstream.omitField(BlockStmt.class, "stmts");

        xstream.useAttributeFor(StringLiteralExpr.class, "value");
        xstream.useAttributeFor(Name.class, "identifier");
        xstream.useAttributeFor(SimpleName.class, "identifier");
        xstream.useAttributeFor(ClassOrInterfaceDeclaration.class, "isInterface");

        xstream.addImplicitCollection(Node.class, "childNodes");

        ignoreAnnotations();
        ignoreMembers();

        xstream.registerConverter(new BinaryExprConverter());
        xstream.registerConverter(new NodeWithNameConverter());
        xstream.registerConverter(new AnnotationExprConverter());

        xstream.useAttributeFor(ClassOrInterfaceDeclaration.class, "name");
        xstream.useAttributeFor(VariableDeclarator.class, "name");
        xstream.registerConverter(new SimpleNameConverter());

        xstream.useAttributeFor(PrimitiveType.class, "type");

        xstream.omitField(IfStmt.class, "condition");
        xstream.omitField(IfStmt.class, "thenStmt");
        xstream.omitField(FieldDeclaration.class, "initializer");
        xstream.omitField(VariableDeclarator.class, "type");
        xstream.omitField(VariableDeclarator.class, "initializer");
        ignoreCollectionField(FieldDeclaration.class, "variables");

        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
    }

    private void ignoreAnnotations() {
        Arrays.asList(AnnotationDeclaration.class,
                AnnotationMemberDeclaration.class,
                ArrayCreationLevel.class,
                ArrayType.class,
                BodyDeclaration.class,
                CallableDeclaration.class,
                ClassOrInterfaceDeclaration.class,
                ClassOrInterfaceType.class,
                ConstructorDeclaration.class,
                EnumConstantDeclaration.class,
                EnumDeclaration.class,
                FieldDeclaration.class,
                InitializerDeclaration.class,
                IntersectionType.class,
                MethodDeclaration.class,
                ModuleDeclaration.class,
                Name.class,
                PackageDeclaration.class,
                Parameter.class,
                PrimitiveType.class,
                TypeDeclaration.class,
                TypeParameter.class,
                UnionType.class,
                VariableDeclarationExpr.class,
                VoidType.class,
                WildcardType.class).forEach(this::ignoreAnnotation);
    }

    private void ignoreAnnotation(Class<?> clazz) {
        ignoreCollectionField(clazz, "annotations");
    }

    private void ignoreMembers() {
        Arrays.asList(
                AnnotationDeclaration.class,
                ClassOrInterfaceDeclaration.class,
                EnumDeclaration.class,
                TypeDeclaration.class).forEach(this::ignoreMembers);
    }

    private void ignoreMembers(Class<?> clazz) {
        ignoreCollectionField(clazz, "members");
    }

    private void ignoreCollectionField(Class<?> clazz, String collectionFieldsName) {
        xstream.addImplicitCollection(clazz, collectionFieldsName);
        xstream.omitField(clazz, collectionFieldsName);
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
