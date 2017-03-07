package com.github.generator.xml;

import com.github.javaparser.ast.expr.SimpleName;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class SimpleNameConverter implements SingleValueConverter {

    @Override
    public String toString(Object obj) {
        return ((SimpleName)obj).getIdentifier();
    }

    @Override
    public Object fromString(String str) {
        return new SimpleName(str);
    }

    @Override
    public boolean canConvert(Class type) {
        return SimpleName.class.equals(type);
    }
}