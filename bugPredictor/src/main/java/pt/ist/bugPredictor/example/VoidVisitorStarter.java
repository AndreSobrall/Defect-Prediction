package pt.ist.bugPredictor.example;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.*;

public class VoidVisitorStarter {
	private final String FILE_PATH = "src/main/java/pt/ist/bugPredictor/samples/ReversePolishNotation.java";

	public void exec() {
		try {
			CompilationUnit cu = JavaParser.parse(new File(FILE_PATH));

			// Void Visitor of Methods
			VoidVisitor<?> methodNameVisitor = new MethodNamePrinter();
			methodNameVisitor.visit(cu, null);

			// List<String> Visitor of Methods
			List<String> methodNames = new ArrayList();
			VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();
			methodNameCollector.visit(cu, methodNames);
			methodNames.forEach(n -> System.out.println("Method Name Collected: " + n));

		} catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	// Simple visitor with no state, arg is not utilized
	private class MethodNamePrinter extends VoidVisitorAdapter<Void> {

		@Override
		public void visit(MethodDeclaration md, Void arg) {
			// We make a call to super to ensure child nodes of the current node are also visited. 
			// This is typically recommended as additional visits accrue little overhead, 
			// however not visiting the whole tree is more likely inclined to result in unwanted behaviour.
			super.visit(md,arg);
			System.out.println("Method Name Printed: " + md.getName());
		}
	}

	// It is often useful during traversal to be able to maintain a record of what we have seen so far. 
	// This may be helpful for our current decision-making process, 
	// or we may want to collect all occurrences of something deemed interesting.
	private class MethodNameCollector extends VoidVisitorAdapter<List<String>> {

		@Override
		public void visit(MethodDeclaration md, List<String> collector) {
			super.visit(md,collector);
			collector.add(md.getNameAsString());
		}

	}
}
