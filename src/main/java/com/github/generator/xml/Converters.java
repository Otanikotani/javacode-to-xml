package com.github.generator.xml;

public final class Converters {
    private Converters() {
    }

    public static NodeToXmlConverter newConverter() {
        return new NodeToXmlConverterImpl();
    }
}
