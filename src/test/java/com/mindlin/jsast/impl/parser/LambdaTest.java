package com.mindlin.jsast.impl.parser;

import static com.mindlin.jsast.impl.parser.JSParserTest.assertExceptionalExpression;
import static com.mindlin.jsast.impl.parser.JSParserTest.assertIdentifier;
import static com.mindlin.jsast.impl.parser.JSParserTest.assertKind;
import static com.mindlin.jsast.impl.parser.JSParserTest.assertLiteral;
import static com.mindlin.jsast.impl.parser.JSParserTest.assertModifiers;
import static com.mindlin.jsast.impl.parser.JSParserTest.assertSpecialType;
import static com.mindlin.jsast.impl.parser.JSParserTest.parseExpression;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.mindlin.nautilus.tree.ArrowFunctionTree;
import com.mindlin.nautilus.tree.Modifiers;
import com.mindlin.nautilus.tree.ParameterTree;
import com.mindlin.nautilus.tree.ReturnTree;
import com.mindlin.nautilus.tree.Tree.Kind;
import com.mindlin.nautilus.tree.type.SpecialTypeTree.SpecialType;

public class LambdaTest {
	
	@Test
	public void testInvalid() {
		assertExceptionalExpression("()=>()", "Empty parentheses aren't a valid lambda body");
		assertExceptionalExpression("x=>()", "Empty parentheses aren't a valid lambda body");
		assertExceptionalExpression("(x,y)=>()", "Empty parentheses aren't a valid lambda body");
		//assertExceptionalExpression("...x=>{}", "Rest parameters aren't allowed without parentheses");
		assertExceptionalExpression("x:void=>{}", "Types aren't allowed without parentheses");
		assertExceptionalExpression("x?=>{}", "Optional parameters not allowed without parentheses");
		assertExceptionalExpression("(x?=5)=>{}", "Initializers not allowed for optional parameters");
		//assertExceptionalExpression("(x?, y)=>{}", "Required parameters may not follow an optional parameter");
		assertExceptionalExpression("(...x, y)=>{}", "No parameter may follow a rest parameter");
	}
	
	@Test
	public void testSingleParamNoParen() {
		ArrowFunctionTree lambda = parseExpression("x=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(1,params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertNull(param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
		
		//TODO check body
	}
	
	@Test
	public void testSingleParanWithParen() {
		ArrowFunctionTree lambda = parseExpression("(x)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(1,params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertNull(param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
	}
	
	@Test
	public void testSingleRestParam() {
		ArrowFunctionTree lambda = parseExpression("(...x)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(1,params.size());
		
		ParameterTree param0 = params.get(0);
		assertTrue(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertNull(param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
	}
	
	@Test
	public void testMultipleParams() {
		ArrowFunctionTree lambda = parseExpression("(x,y)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(2, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertNull(param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
		
		ParameterTree param1 = params.get(1);
		assertFalse(param1.isRest());
		assertModifiers(param1.getModifiers(), Modifiers.NONE);
		assertNull(param1.getType());
		assertNull(param1.getInitializer());
		assertIdentifier("y", param1.getName());
	}
	
	@Test
	public void testBracketedBody() {
		ArrowFunctionTree lambda = parseExpression("()=>{}", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(0, params.size());
	}
	
	@Test
	public void testMultiParamsWithRest() {
		ArrowFunctionTree lambda = parseExpression("(x,...y)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(2, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertNull(param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
		
		ParameterTree param1 = params.get(1);
		assertTrue(param1.isRest());
		assertModifiers(param1.getModifiers(), Modifiers.NONE);
		assertNull(param1.getType());
		assertNull(param1.getInitializer());
		assertIdentifier("y", param1.getName());
	}
	
	@Test
	public void testSingleParamWithType() {
		ArrowFunctionTree lambda = parseExpression("(x:string)=>x", Kind.ARROW_FUNCTION);

		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(1, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertSpecialType(SpecialType.STRING, param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
	}
	
	@Test
	public void testMultipleParamsWithType() {
		ArrowFunctionTree lambda = parseExpression("(x:string, y:number)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(2, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertSpecialType(SpecialType.STRING, param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
		
		ParameterTree param1 = params.get(1);
		assertFalse(param1.isRest());
		assertModifiers(param1.getModifiers(), Modifiers.NONE);
		assertSpecialType(SpecialType.NUMBER, param1.getType());
		assertNull(param1.getInitializer());
		assertIdentifier("y", param1.getName());
	}
	
	@Test
	public void testSingleParamDefaultValue() {
		ArrowFunctionTree lambda = parseExpression("(x = 5)=>x", Kind.ARROW_FUNCTION);

		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(1, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertNull(param0.getType());
		assertLiteral(5, param0.getInitializer());
		assertIdentifier("x", param0.getName());
	}
	
	@Test
	public void testSingleParamOptionalTyped() {
		ArrowFunctionTree lambda = parseExpression("(x?:string)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(1, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.OPTIONAL);
		assertSpecialType(SpecialType.STRING, param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
	}
	
	@Test
	public void testSingleParamTypedDefault() {
		ArrowFunctionTree lambda = parseExpression("(x:number=5)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(1, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertSpecialType(SpecialType.NUMBER, param0.getType());
		assertLiteral(5, param0.getInitializer());
		assertIdentifier("x", param0.getName());
	}
	
	@Test
	public void testSingleParamOptional() {
		ArrowFunctionTree lambda = parseExpression("(x?)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(1, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.OPTIONAL);
		assertNull(param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
	}
	
	@Test
	public void testMultipleParamsOneOptional() {
		ArrowFunctionTree lambda = parseExpression("(x, y?)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(2, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertNull(param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
		
		ParameterTree param1 = params.get(1);
		assertFalse(param1.isRest());
		assertModifiers(param1.getModifiers(), Modifiers.OPTIONAL);
		assertNull(param1.getType());
		assertNull(param1.getInitializer());
		assertIdentifier("y", param1.getName());
	}
	
	@Test
	public void testMultipleParamsMultipleOptional() {
		ArrowFunctionTree lambda = parseExpression("(x?, y?)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(2, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.OPTIONAL);
		assertNull(param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
		
		ParameterTree param1 = params.get(1);
		assertFalse(param1.isRest());
		assertModifiers(param1.getModifiers(), Modifiers.OPTIONAL);
		assertNull(param1.getType());
		assertNull(param1.getInitializer());
		assertIdentifier("y", param1.getName());
	}
	
	@Test
	public void testMultiParamsDefaultValue() {
		ArrowFunctionTree lambda = parseExpression("(x = 5, y = 6)=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(2, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertNull(param0.getType());
		assertLiteral(5, param0.getInitializer());
		assertIdentifier("x", param0.getName());
		
		ParameterTree param1 = params.get(1);
		assertFalse(param1.isRest());
		assertModifiers(param1.getModifiers(), Modifiers.NONE);
		assertNull(param1.getType());
		assertLiteral(6, param1.getInitializer());
		assertIdentifier("y", param1.getName());
	}
	
	@Test
	public void testNested() {
		ArrowFunctionTree lambda = parseExpression("x=>y=>x", Kind.ARROW_FUNCTION);
		
		List<? extends ParameterTree> params = lambda.getParameters();
		assertEquals(1, params.size());
		
		ParameterTree param0 = params.get(0);
		assertFalse(param0.isRest());
		assertModifiers(param0.getModifiers(), Modifiers.NONE);
		assertNull(param0.getType());
		assertNull(param0.getInitializer());
		assertIdentifier("x", param0.getName());
		
		
		//Check returned function
		ReturnTree result = assertKind(Kind.RETURN, lambda.getBody());
		ArrowFunctionTree lambda1 = assertKind(Kind.ARROW_FUNCTION, result.getExpression());
		List<? extends ParameterTree> params1 = lambda1.getParameters();
		assertEquals(1, params1.size());
		
		ParameterTree param1_1 = params1.get(0);
		assertFalse(param1_1.isRest());
		assertModifiers(param1_1.getModifiers(), Modifiers.NONE);
		assertNull(param1_1.getType());
		assertNull(param1_1.getInitializer());
		assertIdentifier("y", param1_1.getName());
		
		ReturnTree result1 = assertKind(Kind.RETURN, lambda1.getBody());
		assertIdentifier("x", result1.getExpression());
	}
}
