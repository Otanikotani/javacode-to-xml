package com.github.generator.xml;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AnnotationExprConverter implements Converter {
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        AnnotationExpr expr = (AnnotationExpr)source;
        writer.addAttribute("name", expr.getNameAsString());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return null;
    }
    @Override
    public boolean canConvert(Class type) {
        return AnnotationExpr.class.equals(type);
    }
}
