package com.mindlin.jsast.impl.parser;

import static com.mindlin.jsast.impl.parser.JSParserTest.assertExceptionalExpression;
import static com.mindlin.jsast.impl.parser.JSParserTest.assertIdentifier;
import static com.mindlin.jsast.impl.parser.JSParserTest.assertLiteral;
import static com.mindlin.jsast.impl.parser.JSParserTest.parseExpression;
import static com.mindlin.jsast.impl.parser.JSParserTest.parseExpressionWith;

import org.junit.Test;

import com.mindlin.nautilus.tree.DeleteExpression;
import com.mindlin.nautilus.tree.PostfixUnaryExpressionTree;
import com.mindlin.nautilus.tree.PrefixUnaryExpressionTree;
import com.mindlin.nautilus.tree.SpreadElementTree;
import com.mindlin.nautilus.tree.Tree.Kind;
import com.mindlin.nautilus.tree.TypeOfExpression;
import com.mindlin.nautilus.tree.VoidExpressionTree;
import com.mindlin.nautilus.tree.YieldTree;

public class UnaryOperatorTest {
	@Test
	public void testPrefixIncrement() {
		PrefixUnaryExpressionTree expr = parseExpression("++a", Kind.PREFIX_INCREMENT);
		assertIdentifier("a", expr.getOperand());
	}

	@Test
	public void testPrefixDecrement() {
		PrefixUnaryExpressionTree expr = parseExpression("--a", Kind.PREFIX_DECREMENT);
		assertIdentifier("a", expr.getOperand());
	}
	
	@Test
	public void testPostfixIncrement() {
		PostfixUnaryExpressionTree expr = parseExpression("a++", Kind.POSTFIX_INCREMENT);
		assertIdentifier("a", expr.getOperand());
	}
	
	@Test
	public void testPostfixDecrement() {
		PostfixUnaryExpressionTree expr = parseExpression("a--", Kind.POSTFIX_DECREMENT);
		assertIdentifier("a", expr.getOperand());
	}

	@Test
	public void testUnaryPlus() {
		PrefixUnaryExpressionTree expr = parseExpression("+a", Kind.UNARY_PLUS);
		assertIdentifier("a", expr.getOperand());
	}

	@Test
	public void testUnaryMinus() {
		PrefixUnaryExpressionTree expr = parseExpression("-a", Kind.UNARY_MINUS);
		assertIdentifier("a", expr.getOperand());
	}
	
	@Test
	public void testLogicalNot() {
		PrefixUnaryExpressionTree expr = parseExpression("!a", Kind.LOGICAL_NOT);
		assertIdentifier("a", expr.getOperand());
	}
	
	@Test
	public void testBitwiseNot() {
		PrefixUnaryExpressionTree expr = parseExpression("~a", Kind.BITWISE_NOT);
		assertIdentifier("a", expr.getOperand());
	}
	
	@Test
	public void testYield() {
		YieldTree expr = parseExpressionWith("yield a", true, true, false, Kind.YIELD);
		assertIdentifier("a", expr.getExpression());
	}
	
	@Test
	public void testYieldToGenerator() {
		YieldTree expr = parseExpressionWith("yield* a", true, true, false, Kind.YIELD_GENERATOR);
		assertIdentifier("a", expr.getExpression());
	}

	@Test
	public void testTypeof() {
		TypeOfExpression expr = parseExpression("typeof a", Kind.TYPEOF);
		assertIdentifier("a", expr.getExpression());
	}
	
	@Test
	public void testTypeofLiteral() {
		TypeOfExpression expr = parseExpression("typeof 'foo'", Kind.TYPEOF);
		assertLiteral("foo", expr.getExpression());
	}

	@Test
	public void testDelete() {
		DeleteExpression expr = parseExpression("delete a", Kind.DELETE);
		assertIdentifier("a", expr.getExpression());
	}
	
	@Test
	public void testVoid() {
		VoidExpressionTree expr = parseExpression("void a", Kind.VOID);
		assertIdentifier("a", expr.getExpression());
	}
	
	@Test
	public void testSpread() {
		SpreadElementTree expr = parseExpression("...a", Kind.SPREAD);
		assertIdentifier("a", expr.getExpression());
	}
	
	@Test
	public void testInvalidUpdateTargetStringLiteral() {
		final String message = "Cannot update string literal";
		assertExceptionalExpression("++'foo'", message);
		assertExceptionalExpression("'foo'++", message);
	}
	
	@Test
	public void testInvalidUpdateTargetNumericLiteral() {
		final String message = "Cannot update numeric literal";
		assertExceptionalExpression("++4", message);
		assertExceptionalExpression("4++", message);
	}
	
	@Test
	public void testInvalidUpdateTargetBooleanLiteral() {
		final String message = "Cannot update boolean literal";
		assertExceptionalExpression("++true", message);
		assertExceptionalExpression("true++", message);
	}
	
	@Test
	public void testInvalidUpdateTargetNullLiteral() {
		final String message = "Cannot update null literal";
		assertExceptionalExpression("++null", message);
		assertExceptionalExpression("null++", message);
	}
	
	@Test
	public void testInvalidStrictDeleteUnqualifiedIdentifier() {
		assertExceptionalExpression("delete foo", "Cannot delete unqualified identifier in strict mode", true);
	}
}
