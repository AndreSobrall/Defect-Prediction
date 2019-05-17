package pt.ist.bugPredictor.parser;

import java.util.List;
import java.util.ArrayList;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
// No Geral -> Statement
// BlockStmt, Expression, ExpressionStmt
// Controlo:        IfStatement, SwitchStatement
// Ciclos:          WhileStatement, DoStatement, ForStatement, ForEachStmt 
// Gestao Ciclos:   BreakStatement, ContinueStatement, ReturnStatement 
// Outras:          AssertStatement, TryStatement, ThrowStatement, SynchronizedStatement 
import com.github.javaparser.ast.stmt.*; 

public class NodeIterator {

    public final String ANSI_RESET = "\u001B[0m";
    public final String ANSI_RED   = "\u001B[31m";
    public final String ANSI_GREEN = "\u001B[32m";

    /* ------------------------ */
    /*      Data Structures     */
    /* ------------------------ */
    
    private List<String> tokens = new ArrayList();
 
    public void explore(Node node) {
        if (handle(node)) {
            // if handle returns true, we explore child nodes
            for (Node child : node.getChildNodes()) {
                explore(child);
            }
        }
    }

    private boolean handle(Node node) {

        String aux, token;

        // These are the tokens that are regarded as less frequent.
        // These are the variables that vary between classes.
        // Less Frequent:
        //      FormalParameter
        //      BasicType 
        //      PackageDeclaration 
        //      InterfaceDeclaration 
        //      CatchClauseParameter 
        //      ClassDeclaration 
        //      MethodInvocation 
        //      SuperMethodInvocation 
        //      MemberReference 
        //      SuperMemberReference 
        //      ConstructorDeclaration 
        //      ReferenceType 
        //      MethodDeclaration 
        //      VariableDeclarator 
        //      StatementExpression 
        //      TryResource CatchClause 
        //      CatchClauseParameter 
        //      SwitchStatementCase 
        //      ForControl 
        //      EnhancedForControl

        if(node instanceof CallableDeclaration) // method declaration, add to token
        { 
            CallableDeclaration cd = (CallableDeclaration) node;
            addToTokens(cd.getNameAsString());
            return true; 
        }
        
        if(node instanceof Statement) // parse statements of a method.
        {
            Statement stmt = (Statement) node;

            if(stmt.isBlockStmt())
                return true;

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

            
            // Apenas keywords, nao tem corpo para explorar.
            // assert, throw, break, continue, return
            if(stmt.isAssertStmt() || stmt.isThrowStmt() ||
               stmt.isBreakStmt()  || stmt.isContinueStmt() ||
               stmt.isReturnStmt()) 
            {   
                aux = stmt.toString();                
                token = parseKeyword(aux);
                // UNCOMMENT TO PRINT
                // debugPrint(aux, token);
                
                addToTokens(token);
                return false;
            } 
            // if, while, do, for, synchronized, try, switch
            else if(stmt.isIfStmt()     || stmt.isWhileStmt() || stmt.isDoStmt()     || 
                    stmt.isForStmt()    || stmt.isTryStmt()   || stmt.isSwitchStmt() || 
                    stmt.isSynchronizedStmt()) 
            {
                aux = stmt.toString();
                token = parseKeyword(aux);
                // UNCOMMENT TO PRINT
                debugPrint(aux, token);
                addToTokens(token);
                return true;
            }
            // foreach
            // Tem token custom para destinguir do "for" normal
            // nao da para utilizar funcao parseKeyword()
            else if(stmt.isForEachStmt()) {
                aux   = stmt.toString();
                token = "foreach";
                // UNCOMMENT TO PRINT
                debugPrint(aux, token);
                addToTokens(token);
                return true;
            }   
            
        } //end stament exploration

        // TODO: Change
        return true; // by default, explores children
    }

    private void addToTokens(String token) 
    {
        // System.out.print(ANSI_GREEN + token +  ANSI_RESET + "\n");
        this.tokens.add(token);
    }

    private String parseKeyword(String original) {
        String token;
        
        if(original.contains(" "))
            // gets the only the keyword
            token = original.split(" ")[0];
        else
            token = original;

        if(token.contains(";")) // Ex: return; -> return
            token = token.substring(0, token.length()-1);

        return token;
    }

    private void debugPrint(String original, String token) {
        System.out.print(ANSI_RED + original +  ANSI_RESET + "\n");
        System.out.print(ANSI_GREEN + token +  ANSI_RESET + "\n");
    }

    public void printTokens() {
        System.out.println("[");
        for(String token : tokens)
            System.out.println(token+",");
        System.out.println("]");
        System.out.println(); // empty line
    }

    public List<String> getFileTokens() {
        return tokens;
    }

}