package com.github.generator.xml;

import com.github.javaparser.ast.expr.NameExpr;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class NameExprConverter implements SingleValueConverter {
    @Override
    public String toString(Object obj) {
        return ((NameExpr)obj).getName();
    }

    @Override
    public Object fromString(String str) {
        return new NameExpr(str);
    }

    @Override
    public boolean canConvert(Class type) {
        return NameExpr.class.equals(type);
    }
}
