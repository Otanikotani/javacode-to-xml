package com.aurea.generator.xml;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class BinaryExprConverter implements Converter {
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        BinaryExpr binaryExpr = (BinaryExpr)source;
        writer.addAttribute("op", binaryExpr.getOperator().name());
        if (binaryExpr.getLeft() != null) {
            writer.startNode(binaryExpr.getLeft().getClass().getSimpleName());
            writer.addAttribute("position", "left");
            context.convertAnother(binaryExpr.getLeft());
            writer.endNode();
        }
        if (binaryExpr.getRight() != null) {
            writer.startNode(binaryExpr.getRight().getClass().getSimpleName());
            writer.addAttribute("position", "right");
            context.convertAnother(binaryExpr.getRight());
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return null;
    }

    @Override
    public boolean canConvert(Class type) {
        return BinaryExpr.class.equals(type);
    }
}
