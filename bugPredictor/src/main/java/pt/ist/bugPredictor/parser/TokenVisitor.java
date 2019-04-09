package pt.ist.bugPredictor.parser;

import java.io.File;
import java.io.FileNotFoundException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;

import pt.ist.bugPredictor.parser.NodeIterator;

public class TokenVisitor {
	private final String FILE_PATH = "src/main/java/pt/ist/bugPredictor/samples/ReversePolishNotation.java";

	public void exec() {

		try {
		 	NodeIterator nodes = new NodeIterator();
		 	nodes.explore(JavaParser.parse(new File(FILE_PATH)));
		 	nodes.printTokens();
        } catch (FileNotFoundException e) {
            new RuntimeException(e);
            System.out.println(e.getMessage());
        }
	}


}
