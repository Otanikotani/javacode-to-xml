package com.github.generator.xml;

import com.github.javaparser.ast.Node;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;

public interface NodeToXmlConverter {

    String toXmlString(Node node);

    void toFile(Node node, Path path) throws IOException;

    Document toDocument(Node node) throws ParserConfigurationException;
}
