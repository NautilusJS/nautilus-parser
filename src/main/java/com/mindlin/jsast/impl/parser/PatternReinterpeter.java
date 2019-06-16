package com.mindlin.jsast.impl.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mindlin.jsast.exception.JSSyntaxException;
import com.mindlin.jsast.exception.JSUnsupportedException;
import com.mindlin.jsast.impl.tree.ArrayPatternTreeImpl;
import com.mindlin.jsast.impl.tree.ObjectPatternTreeImpl;
import com.mindlin.nautilus.tree.ArrayLiteralTree;
import com.mindlin.nautilus.tree.AssignmentPropertyTree;
import com.mindlin.nautilus.tree.AssignmentTree;
import com.mindlin.nautilus.tree.BinaryExpressionTree;
import com.mindlin.nautilus.tree.BooleanLiteralTree;
import com.mindlin.nautilus.tree.CastExpressionTree;
import com.mindlin.nautilus.tree.ClassTreeBase.ClassExpressionTree;
import com.mindlin.nautilus.tree.ConditionalExpressionTree;
import com.mindlin.nautilus.tree.ExpressionTree;
import com.mindlin.nautilus.tree.ExpressionTree.MemberExpressionTree;
import com.mindlin.nautilus.tree.ExpressionTreeVisitor;
import com.mindlin.nautilus.tree.FunctionCallTree;
import com.mindlin.nautilus.tree.FunctionExpressionTree;
import com.mindlin.nautilus.tree.GetAccessorDeclarationTree;
import com.mindlin.nautilus.tree.IdentifierTree;
import com.mindlin.nautilus.tree.MethodDeclarationTree;
import com.mindlin.nautilus.tree.NewTree;
import com.mindlin.nautilus.tree.NullLiteralTree;
import com.mindlin.nautilus.tree.NumericLiteralTree;
import com.mindlin.nautilus.tree.ObjectLiteralElement;
import com.mindlin.nautilus.tree.ObjectLiteralElementVisitor;
import com.mindlin.nautilus.tree.ObjectLiteralTree;
import com.mindlin.nautilus.tree.ObjectPatternTree.ObjectPatternElement;
import com.mindlin.nautilus.tree.ParenthesizedTree;
import com.mindlin.nautilus.tree.PatternTree;
import com.mindlin.nautilus.tree.RegExpLiteralTree;
import com.mindlin.nautilus.tree.SequenceExpressionTree;
import com.mindlin.nautilus.tree.SetAccessorDeclarationTree;
import com.mindlin.nautilus.tree.ShorthandAssignmentPropertyTree;
import com.mindlin.nautilus.tree.SpreadElementTree;
import com.mindlin.nautilus.tree.StringLiteralTree;
import com.mindlin.nautilus.tree.SuperExpressionTree;
import com.mindlin.nautilus.tree.TaggedTemplateLiteralTree;
import com.mindlin.nautilus.tree.TemplateLiteralTree;
import com.mindlin.nautilus.tree.ThisExpressionTree;
import com.mindlin.nautilus.tree.Tree;
import com.mindlin.nautilus.tree.Tree.Kind;

public class PatternReinterpeter implements Function<ExpressionTree, PatternTree> {
	
	@Override
	public PatternTree apply(ExpressionTree expr) {
		return null;//TODO
	}
	
	protected PatternTree reinterpretAsPattern(ExpressionTree node) {
		return null;//TODO
	}
	
	protected PatternTree reinterpretAsPatternArrayElem(ExpressionTree node) {
		return null;//TODO
	}
	
	protected ObjectPatternElement reinterpretAsPatternObjectElem(ObjectLiteralElement node) {
		return null;//TODO
	}
	
	public class ObjectPropertyToPattern implements ObjectLiteralElementVisitor<ObjectPatternElement, Void> {
		protected ObjectPatternElement visitDefault(Tree node, Void inArray) {
			// TODO: I18N
			String message = String.format("Cannot reinterpret %s as pattern", node);
			throw new JSSyntaxException(message, node.getRange());
		}
		
		@Override
		public ObjectPatternElement visitGetAccessorDeclaration(GetAccessorDeclarationTree node, Void inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public ObjectPatternElement visitMethodDeclaration(MethodDeclarationTree node, Void inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public ObjectPatternElement visitSetAccessorDeclaration(SetAccessorDeclarationTree node, Void inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public ObjectPatternElement visitAssignmentProperty(AssignmentPropertyTree node, Void inArray) {
			// TODO: finish
			throw new JSUnsupportedException("spread -> rest", node.getRange());
		}
		
		@Override
		public ObjectPatternElement visitShorthandAssignmentProperty(ShorthandAssignmentPropertyTree node, Void inArray) {
			// TODO: finish
			throw new JSUnsupportedException("spread -> rest", node.getRange());
		}
		
		@Override
		public ObjectPatternElement visitSpread(SpreadElementTree node, Void inArray) {
			// Convert to rest
			PatternTree target = reinterpretAsPattern(node.getExpression());
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class ExpressionToPattern implements ExpressionTreeVisitor<PatternTree, Boolean> {

		protected PatternTree visitDefault(Tree node, Boolean inArray) {
			// TODO: I18N
			String message = String.format("Cannot reinterpret %s as pattern", node);
			throw new JSSyntaxException(message, node.getRange());
		}
		
		@Override
		public PatternTree visitArrayLiteral(ArrayLiteralTree expr, Boolean inArray) {
			ArrayList<PatternTree> elements = new ArrayList<>();
			for (ExpressionTree elem : expr.getElements())
				elements.add(elem == null ? null : reinterpretAsPatternArrayElem(elem));
			elements.trimToSize();
			return new ArrayPatternTreeImpl(expr.getStart(), expr.getEnd(), elements);
		}
		
		@Override
		public PatternTree visitObjectLiteral(ObjectLiteralTree node, Boolean inArray) {
			List<ObjectPatternElement> properties = node.getProperties()
					.stream()
					.map(PatternReinterpeter.this::reinterpretAsPatternObjectElem)
					.collect(Collectors.toList());
			return new ObjectPatternTreeImpl(node.getStart(), node.getEnd(), properties);
		}
		
		@Override
		public PatternTree visitIdentifier(IdentifierTree node, Boolean inArray) {
			return node;
		}
		
		public PatternTree visitMemberExpression(MemberExpressionTree node, Boolean inArray) {
			return node;
		}

		@Override
		public PatternTree visitSpread(SpreadElementTree node, Boolean inArray) {
			if (inArray) {
				// Convert to rest
				PatternTree target = reinterpretAsPattern(node.getExpression());
				//TODO
				return null;
			} else {
				return this.visitDefault(node, inArray);
			}
		}
		
		@Override
		public PatternTree visitAssignment(AssignmentTree node, Boolean inArray) {
			//TODO
			// return new AssignmentPatternTreeImpl(expr.getStart(), expr.getEnd(), ((AssignmentTree)expr).getVariable(), ((AssignmentTree)expr).getValue());
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitAwait(AwaitTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitBinary(BinaryExpressionTree node, Boolean inArray) {
			if (node.getKind() == Kind.MEMBER_SELECT)
				return this.visitMemberExpression((MemberExpressionTree) node, inArray);
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitBooleanLiteral(BooleanLiteralTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitCast(CastExpressionTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitClassExpression(ClassExpressionTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitConditionalExpression(ConditionalExpressionTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitFunctionCall(FunctionCallTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitFunctionExpression(FunctionExpressionTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitNew(NewTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitNullLiteral(NullLiteralTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitNumericLiteral(NumericLiteralTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitParentheses(ParenthesizedTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitRegExpLiteral(RegExpLiteralTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitSequence(SequenceExpressionTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitStringLiteral(StringLiteralTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitSuper(SuperExpressionTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitTemplateLiteral(TemplateLiteralTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitTaggedTemplate(TaggedTemplateLiteralTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitThis(ThisExpressionTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
		
		@Override
		public PatternTree visitUnary(UnaryTree node, Boolean inArray) {
			return this.visitDefault(node, inArray);
		}
	}
	
}
