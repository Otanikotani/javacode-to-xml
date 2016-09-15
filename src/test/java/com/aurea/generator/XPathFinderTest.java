package com.aurea.generator;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class XPathFinderTest {

    @Test
    public void testSimpleXPath() throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(this.getClass().getResourceAsStream("JP_AbnViewRowImpl.xml"));

        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression businessValidation = xPath.compile(
                "/MethodDeclaration[@name='businessValidation']");
        XPathExpression count = xPath.compile(
                "count(/MethodDeclaration/BlockStmt/*)");
        XPathExpression firstIsIfUnmodified = xPath.compile(
                "/MethodDeclaration/BlockStmt/IfStmt[position() = 1]" +
                "/BinaryExpr[@op='equals']/NameExpr[@name='UNMODIFIED' and @position='right']");
        XPathExpression lastIsIfSuperBusinessValidation = xPath.compile(
                "/MethodDeclaration/BlockStmt/ExpressionStmt[last()]" +
                "/MethodCallExpr[@name='businessValidation']/scope[@class='SuperExpr']");
        XPathExpression secondStatementIsCheckForNew = xPath.compile(
                "/MethodDeclaration/BlockStmt/IfStmt[2]" +
                "/BinaryExpr[@op='equals']/NameExpr[@name='NEW' and @position='right']");
        XPathExpression newBodyHasTwoNodes = xPath.compile(
                "count(/MethodDeclaration/BlockStmt/IfStmt[2]/*)");
        XPathExpression newBodyIfHasTwoNodes = xPath.compile(
                "count(/MethodDeclaration/BlockStmt/IfStmt[2]/BlockStmt/IfStmt/*)");
        XPathExpression getCodsocPhyStmt = xPath.compile(
                "//MethodCallExpr[@name='getCodsocPhy']/StringLiteralExpr/@value");

        Boolean evaluate = (Boolean) businessValidation.evaluate(document, XPathConstants.BOOLEAN);
        int countEv = ((Double) count.evaluate(document, XPathConstants.NUMBER)).intValue();
        int nodesInNewBody = ((Double) newBodyHasTwoNodes.evaluate(document, XPathConstants.NUMBER)).intValue();
        int newBodyIfNodes = ((Double) newBodyIfHasTwoNodes.evaluate(document, XPathConstants.NUMBER)).intValue();
        Boolean isUnmodified = (Boolean) firstIsIfUnmodified.evaluate(document, XPathConstants.BOOLEAN);
        Boolean isLastSuper = (Boolean) lastIsIfSuperBusinessValidation.evaluate(document, XPathConstants.BOOLEAN);
        Boolean isSecondCheckStatementForNew = (Boolean) secondStatementIsCheckForNew.evaluate(document, XPathConstants.BOOLEAN);
        Node result = (Node) getCodsocPhyStmt.evaluate(document, XPathConstants.NODE);
        String code = result.getNodeValue();

        assertThat(evaluate).isTrue();
//        assertThat(countEv).isEqualTo(3);
        assertThat(nodesInNewBody).isEqualTo(2);
        assertThat(newBodyIfNodes).isEqualTo(2);
        assertThat(isUnmodified).isTrue();
        assertThat(isLastSuper).isTrue();
        assertThat(isSecondCheckStatementForNew).isTrue();
        assertThat(code).isEqualTo("P_ABN");

    }
}
