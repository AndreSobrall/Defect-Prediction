package pt.ist.bugPredictor.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;

import pt.ist.bugPredictor.parser.NodeIterator;

public class Tokenizer {
	// private final String FILE_PATH = "src/main/java/pt/ist/bugPredictor/samples/ReversePolishNotation.java";

	public List<String> execute(String filePath) {
		try {
		 	NodeIterator nodes = new NodeIterator();
		 	nodes.explore(JavaParser.parse(new File(filePath)));
		 	// nodes.printTokens();
		 	return nodes.getFileTokens();
        } catch (FileNotFoundException e) {
            new RuntimeException(e);
            System.out.println(e.getMessage());
            return null;
        }
	}

}
