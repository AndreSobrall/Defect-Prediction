package pt.ist.bugPredictor;

import pt.ist.bugPredictor.parser.TokenVisitor;
import pt.ist.bugPredictor.parser.OutputMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // System.out.println( "Hello World!" );
        // TokenVisitor parser = new TokenVisitor();
        // parser.exec();
        OutputMap mapping = new OutputMap();
        mapping.addToken("ola");
        mapping.addToken("andre");
        mapping.addToken("luis");
        mapping.addToken("carolina");
        mapping.addToken("fender");
        mapping.addToken("defectPrediction");
        mapping.addToken("exec");
        mapping.addToken("leonidas");
        mapping.addToken("allrighty");
        mapping.addToken("theyams");
        mapping.addToken("indierocku");
        mapping.addToken("theyams");
        mapping.addToken("ola");
        mapping.addToken("andre");
        mapping.addToken("ola");
        mapping.addToken("andre");
        mapping.addToken("luis");
        mapping.addToken("carolina");
        mapping.addToken("fender");
        mapping.addToken("defectPrediction");
        mapping.addToken("exec");
        mapping.addToken("leonidas");
        mapping.addToken("allrighty");
        mapping.addToken("theyams");
        mapping.addToken("indierocku");
        mapping.addToken("theyams");
        mapping.addToken("ola");
        mapping.addToken("andre");
        // mapping.printMapping();
        mapping.printIntArray();
        try {
        	mapping.writeToFile("HelloWorld.txt"); 
    	} catch(Exception e) {
    		System.out.println(e.getMessage());
    	}

    }
}
