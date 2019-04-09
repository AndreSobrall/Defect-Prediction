package pt.ist.bugPredictor;

import pt.ist.bugPredictor.parser.TokenVisitor;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        TokenVisitor parser = new TokenVisitor();
        parser.exec();
    }
}
