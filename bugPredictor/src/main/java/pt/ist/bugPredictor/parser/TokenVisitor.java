package pt.ist.bugPredictor.parser;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.*;
import com.github.javaparser.ast.Node;

public class TokenVisitor {
	private final String FILE_PATH = "src/main/java/pt/ist/bugPredictor/samples/ReversePolishNotation.java";
	private List<String> tokens = new ArrayList();
	private CompilationUnit cu;

	public TokenVisitor() {
		try {
			cu = JavaParser.parse(new File(FILE_PATH));

		} catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void exec() {
		VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();
		methodNameCollector.visit(cu, tokens);
		printTokens();
	}

	// It is often useful during traversal to be able to maintain a record of what we have seen so far. 
	// This may be helpful for our current decision-making process, 
	// or we may want to collect all occurrences of something deemed interesting.
	private class MethodNameCollector extends VoidVisitorAdapter<List<String>> {

		@Override
		public void visit(MethodDeclaration md, List<String> tokens) {
			super.visit(md,tokens);
			tokens.add(md.getNameAsString());
		}

	}

	// private class nodeCollector extends VoidVisitorAdapter<List<String>> {

	// 	@Override
	// 	public void visit(Node node, List<String> tokens) {
	// 		super.visit(node, tokens);
	// 		tokens.add(node.getNameAsString());
	// 	}

	// }

	private void printTokens() {
		System.out.println("[");
		for(String token : tokens)
			System.out.println(token+",");
		System.out.println("]");
	}

}
