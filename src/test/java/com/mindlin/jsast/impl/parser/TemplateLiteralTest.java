package com.mindlin.jsast.impl.parser;

import static com.mindlin.jsast.impl.parser.JSParserTest.assertIdentifier;
import static com.mindlin.jsast.impl.parser.JSParserTest.assertKind;
import static com.mindlin.jsast.impl.parser.JSParserTest.assertLiteral;
import static com.mindlin.jsast.impl.parser.JSParserTest.parseExpression;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.mindlin.nautilus.tree.BinaryExpressionTree;
import com.mindlin.nautilus.tree.ExpressionTree;
import com.mindlin.nautilus.tree.TaggedTemplateLiteralTree;
import com.mindlin.nautilus.tree.TemplateElementTree;
import com.mindlin.nautilus.tree.TemplateLiteralTree;
import com.mindlin.nautilus.tree.Tree.Kind;

public class TemplateLiteralTest {
	
	@Test
	public void testTemplateString() {
		TemplateLiteralTree template = parseExpression("`Hello`", Kind.TEMPLATE_LITERAL);
		
		List<TemplateElementTree> quasis = template.getQuasis();
		assertEquals(1, quasis.size());
		
		TemplateElementTree t0 = quasis.get(0);
		assertEquals("Hello", t0.getCooked());
	}
	
	@Test
	public void testTemplateOnlyExpr() {
		TemplateLiteralTree template = parseExpression("`${1+2}`", Kind.TEMPLATE_LITERAL);

		List<TemplateElementTree> quasis = template.getQuasis();
		assertEquals(2, quasis.size());
		assertEquals("", quasis.get(0).getCooked());
		assertEquals("", quasis.get(1).getCooked());
		
		List<ExpressionTree> exprs = template.getExpressions();
		assertEquals(1, exprs.size());
		BinaryExpressionTree expr = assertKind(Kind.ADDITION, exprs.get(0));
		assertLiteral(1, expr.getLeftOperand());
		assertLiteral(2, expr.getRightOperand());
	}
	
	@Test
	public void testTemplateComplex() {
		TemplateLiteralTree template = parseExpression("`Hello, ${'world!'}`", Kind.TEMPLATE_LITERAL);
		List<TemplateElementTree> quasis = template.getQuasis();
		assertEquals(2, quasis.size());
		assertEquals("Hello, ", quasis.get(0).getCooked());
		assertEquals("", quasis.get(1).getCooked());
		
		List<ExpressionTree> exprs = template.getExpressions();
		assertEquals(1, exprs.size());
		assertLiteral("world!", exprs.get(0));
	}
	
	@Test
	public void testTagged() {
		TaggedTemplateLiteralTree expr = parseExpression("tag `string text ${expression} string text`", Kind.TAGGED_TEMPLATE);
		assertIdentifier("tag", expr.getTag());
		
		
		TemplateLiteralTree template = expr.getQuasi();
		
		List<TemplateElementTree> quasis = template.getQuasis();
		assertEquals(2, quasis.size());
		
		assertEquals("string text ", quasis.get(0).getCooked());
		assertEquals(" string text", quasis.get(1).getCooked());
		
		List<? extends ExpressionTree> exprs = template.getExpressions();
		assertEquals(1, exprs.size());
		assertLiteral("expression", exprs.get(0));
	}
}
