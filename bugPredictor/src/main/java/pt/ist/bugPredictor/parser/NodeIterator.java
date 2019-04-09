package pt.ist.bugPredictor.parser;

import java.util.List;
import java.util.ArrayList;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.expr.ObjectCreationExpr; 
import com.github.javaparser.ast.stmt.*; //Statement, BlockStmt, ExpressionStmt
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.BlockStmt;
 
public class NodeIterator {
    private List<String> tokens = new ArrayList();
 
    public void explore(Node node) {
        if (handle(node)) {
            for (Node child : node.getChildNodes()) {
                explore(child);
            }
        }
    }

    private boolean handle(Node node) {

        String aux = new String();

        if(node instanceof CallableDeclaration) // method declaration, add to token
        { 
            CallableDeclaration cd = (CallableDeclaration) node;
            addToTokens(cd.getNameAsString());
            return true; 
        }
        else if(node instanceof Statement) // parse statements of a method.
        {
            if(node instanceof ExpressionStmt) { 
                ExpressionStmt st = (ExpressionStmt) node;
                if (st.getExpression().isMethodCallExpr()) { // method calls
                    MethodCallExpr mc = st.getExpression().asMethodCallExpr();
                    aux = mc.getNameAsString();
                    System.out.println("Method Call: " + aux);
                    addToTokens(aux);
                }
                else {
                    if(st.getExpression().isVariableDeclarationExpr()) {
                        VariableDeclarationExpr vd = st.getExpression().asVariableDeclarationExpr();
                        for(VariableDeclarator v : vd.getVariables())
                            if(!v.getType().isPrimitiveType() && !v.getType().isArrayType()) {
                                System.out.println("Variable Declaration: " + st);
                                aux = v.getType().toString();
                                System.out.println("Type: " + aux);
                                addToTokens(aux);
                                if(v.getInitializer().isPresent() && !v.getInitializer().get().isObjectCreationExpr()) {
                                    aux = v.getInitializer().get().toString();
                                    System.out.println("MethodCall to init: " + aux);
                                    addToTokens(aux);
                                }
                            }

                    } // TODO: assignments
                }
                return false;
            }
        }
    
        return true;
    }

    private void addToTokens(String token) 
    {
        // System.out.println(token);
        this.tokens.add(token);
    }

    public void printTokens() {
        System.out.println("[");
        for(String token : tokens)
            System.out.println(token+",");
        System.out.println("]");
        System.out.println(); // empty line
    }


}