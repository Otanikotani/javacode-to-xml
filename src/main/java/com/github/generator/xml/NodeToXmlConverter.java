package com.github.generator.xml;

import com.github.javaparser.ast.Node;
import org.w3c.dom.Document;

import java.io.IOException;
import java.nio.file.Path;

public interface NodeToXmlConverter {

    String toXmlString(Node node);

    void toFile(Node node, Path path);

    Document toDocument(Node node);
}
