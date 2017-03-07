package com.github.generator.xml;

import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class NodeWithNameConverter implements Converter {
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        NodeWithName nodeWithName = (NodeWithName) source;
        writer.addAttribute("name", nodeWithName.getNameAsString());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return null;
    }

    @Override
    public boolean canConvert(Class type) {
        return NodeWithName.class.isAssignableFrom(type);
    }
}
