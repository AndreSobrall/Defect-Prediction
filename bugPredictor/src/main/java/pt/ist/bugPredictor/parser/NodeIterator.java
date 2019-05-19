package pt.ist.bugPredictor.parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;

import com.github.javaparser.ast.expr.*;
// No Geral -> Statement
// BlockStmt, Expression, ExpressionStmt
// Controlo:        IfStatement, SwitchStatement
// Ciclos:          WhileStatement, DoStatement, ForStatement, ForEachStmt 
// Gestao Ciclos:   BreakStatement, ContinueStatement, ReturnStatement 
// Outras:          AssertStatement, TryStatement, ThrowStatement, SynchronizedStatement 
import com.github.javaparser.ast.stmt.*; 

public class NodeIterator {

    public final String ANSI_RESET  = "\u001B[0m";
    public final String ANSI_RED    = "\u001B[31m";
    public final String ANSI_GREEN  = "\u001B[32m";
    public final String ANSI_YELLOW = "\u001B[33m";
    public final String ANSI_BLUE   = "\u001B[34m";

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

        String aux, token = new String();

        // Parse declarations 
        if(node instanceof BodyDeclaration) {

            BodyDeclaration bd = (BodyDeclaration) node;

            // Only interesting ones.
            if(bd.isClassOrInterfaceDeclaration() || bd.isMethodDeclaration()  ||
               bd.isConstructorDeclaration()      || bd.isEnumDeclaration()) 
            {    
                aux = bd.toString();

                // -> Em vez de utilizar o nome da funcao, que tem pouco ou nenhum significado entre projetos.
                // Utiliza-se o token "<method-declaration>" que identifica como elemento semantico comum

                // TODO: PackageDeclaration -> needed?
                // if(bd.isPackageDeclaration()) 
                // {
                //     PackageDeclaration pd = bd.asPackageDeclaration();
                //     token = pd.getName().asString();
                // }

                // ClassDeclaration or InterfaceDeclaration 
                if(bd.isClassOrInterfaceDeclaration()) 
                {
                    token = "<class-declaration>";
                    // UNCOMMENT TO PRINT
                    // debugPrint(aux, token);
                    // inspectChildren(bd, token);
                }
                // MethodDeclaration
                else if(bd.isMethodDeclaration()) 
                {
                    token = "<method-declaration>";
                    // UNCOMMENT TO PRINT
                    // debugPrint(aux, token);
                    // inspectChildren(bd, token);
                }
                // ConstructorDeclaration
                else if(bd.isConstructorDeclaration()) 
                {
                    token = "<constructor-declaration>";
                    // UNCOMMENT TO PRINT
                    // debugPrint(aux, token);
                    // inspectChildren(bd, token);
                }
                // EnumDeclaration
                else if(bd.isEnumDeclaration()) 
                {
                    token = "<enum-declaration>";
                    // UNCOMMENT TO PRINT
                    // debugPrint(aux, token);
                    // inspectChildren(bd, token);
                } 
                
                // UNCOMMENT TO PRINT ALL
                // debugPrint(aux, token);
                // inspectChildren(bd, token);
                // System.out.print(ANSI_GREEN + token +  ANSI_RESET + "\n");
                addToTokens(token);
                return true; 
            }
        }
        
        // Parse statements 
        if(node instanceof Statement) 
        {
            Statement stmt = (Statement) node;

            // Always explore block statements ({})
            if(stmt.isBlockStmt())
                return true;

            // Expressions
            if(stmt.isExpressionStmt()) 
            { 
                ExpressionStmt st = (ExpressionStmt) node;
                Expression expr   = st.getExpression();
                
                // method calls
                // MethodInvocation (already done?)
                if (expr.isMethodCallExpr()) 
                {   
                    aux = expr.toString();
                    token = "<method-invocation>";
                    
                    // UNCOMMENT TO PRINT ALL
                    // debugPrint(aux, token);
                    // inspectChildren(expr, token);                    
                    
                    addToTokens(token);
                    
                    return false;
                }
                // SuperMethodInvocation 
                else if(expr.isSuperExpr()) {
                    aux = expr.toString();
                    token = "<super>";
                    
                    // UNCOMMENT TO PRINT ALL
                    // debugPrint(aux, token);
                    // inspectChildren(expr, token);                    
                    
                    addToTokens(token);
                    
                    return false;
                }
                else if(expr.isMethodReferenceExpr()) {
                    aux = expr.toString();
                    token = expr.asMethodReferenceExpr().getIdentifier();

                    // UNCOMMENT TO PRINT ALL
                    // debugPrint(aux, token);

                    addToTokens(token);

                    return false;
                }

                else if(expr.isVariableDeclarationExpr()) 
                {
                    VariableDeclarationExpr vd = expr.asVariableDeclarationExpr();
                    
                    for(VariableDeclarator v : vd.getVariables())
                        
                        // Only stores non-primitive and non-array types
                        if(!v.getType().isPrimitiveType() && !v.getType().isArrayType())
                        {   
                            aux = v.toString();  
                            token = v.getType().toString();
                             
                            // UNCOMMENT TO PRINT ALL
                            // debugPrint(aux, token);
                            
                            addToTokens(token);

                            // TODO: toggle on/off, see results.
                            // Stores initializer 
                            // if(v.getInitializer().isPresent() && !v.getInitializer().get().isObjectCreationExpr())
                            // {
                            //     aux = v.getInitializer().get().toString();
                            //     // System.out.println("MethodCall to init: " + aux);
                            //     addToTokens(aux);
                            // }
                        }

                    return false;
                }

                return false;
            }
            
            
            // Apenas keywords. Nao tem corpo para explorar.
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

            // Queremos explorar o corpo ({})
            // if, while, do, for, synchronized, try, switch
            else if(stmt.isIfStmt()     || stmt.isWhileStmt()  || stmt.isDoStmt() ||
                    stmt.isSwitchStmt() || stmt.isSynchronizedStmt() || stmt.isTryStmt() ||
                    stmt.isForStmt()) 
            {
                aux = stmt.toString();
                token = parseKeyword(aux);

                // UNCOMMENT TO PRINT
                // debugPrint(aux, token);

                addToTokens(token);
                return true;
            }

            // foreach
            // Tem token custom para destinguir do "for" normal
            // Nao da para utilizar funcao parseKeyword()
            else if(stmt.isForEachStmt()) {
                aux   = stmt.toString();
                token = "<foreach>";
                
                // UNCOMMENT TO PRINT
                // debugPrint(aux, token);
                
                addToTokens(token);
                return true; 
            }

            // Ignore: nao e preciso explorar child nodes.
            else if(stmt.isEmptyStmt() || stmt.isUnparsableStmt() || stmt.isLabeledStmt())
                return false;
            
        } //end stament exploration

        // catch
        // tbm trata do catch parameter
        else if(node instanceof CatchClause) {
            aux   = node.toString();
            token = "<catch>";
            
            // UNCOMMENT TO PRINT
            // debugPrint(aux, token);
            // inspectChildren(node, token);

            // Adds catch to token list
            addToTokens(token);

            // Processes CatchClauseParameter in particular.
            // No other parameteres are parsed so this is done here directly
            for (Node child : node.getChildNodes()) {    
                if(child instanceof Parameter) {
                    Parameter p  = (Parameter) child;
                    aux   = child.toString();
                    token = parseCatchParameter(p.getType().toString());

                    // UNCOMMENT TO PRINT
                    // debugPrint(aux, token);

                    // Adds catch parameter to token list
                    addToTokens(token); 

                    break; // found Parameter node, time to leave.
                } 
            }

            return true;
        } // end of Catchlause

        // Switch cases:
        else if(node instanceof SwitchEntry) 
        { 
            aux   = node.toString();
            token = "<case>";
            
            // UNCOMMENT TO PRINT
            // debugPrint(aux, token);
            // inspectChildren(node, token);

            addToTokens(token);
            return true;
        }     

        return true; // by default, explores children
    }

    private void addToTokens(String token) 
    {
        // System.out.print(ANSI_GREEN + token +  ANSI_RESET + "\n");
        this.tokens.add(token);
    }

    // Parses the java keyword from the full statement
    private String parseKeyword(String original) {
        String token;
        
        if(original.contains(" "))
            // gets the only the keyword
            token = original.split(" ")[0];
        else
            token = original;

        if(token.contains(";")) // Ex: return; -> return
            token = token.substring(0, token.length()-1);

        return "<" + token + ">";
    }

    private void debugPrint(String original, String token) {
        System.out.print(ANSI_RED + original +  ANSI_RESET + "\n");
        System.out.print(ANSI_GREEN + token +  ANSI_RESET + "\n");
    }

    // Prints children nodes in 2 levels: root-children and BlockStmt-children
    private void inspectChildren(Node root, String nodeName) {
        // Root node children
        for (Node child : root.getChildNodes()) {
            System.out.print(ANSI_BLUE + nodeName + "-children:" + ANSI_RESET + "\n");
            System.out.print(ANSI_YELLOW + child.getClass().getName() + ANSI_RESET + "\n");

            // BlockStmt child nodes
            if(child.getClass().getName().equals("com.github.javaparser.ast.stmt.BlockStmt"))
                for (Node childOfChild : child.getChildNodes()) {
                    System.out.print(ANSI_BLUE + "BlockStmt-children:" + ANSI_RESET + "\n");
                    System.out.print(ANSI_YELLOW + childOfChild.getClass().getName() + ANSI_RESET + "\n");
                }
        }
    }

    // Ex: InstantiationException | IllegalAccessException
    // Solution: Always store them ordered by alphabetical order.
    // In that case: 
    // InstantiationException | IllegalAccessException or  IllegalAccessException | InstantiationException
    // are treated as the same token.
    private String parseCatchParameter(String token) {
        String finalToken = new String();

        if(finalToken.contains("\\|")) {
            List<String> l_tokens = new ArrayList<String>();

            // Get each individual exception        
            String tokens[] = token.split(" \\| ");
            
            // Translate to List<String>
            for(int i = 0; i < tokens.length; i++)
                l_tokens.add(tokens[i]);

            // Sort List alphabetically
            Collections.sort(l_tokens, String.CASE_INSENSITIVE_ORDER);
            
            // Combines tokens into a single one
            int i = 0;
            for(String ex : l_tokens) {
                if(i < l_tokens.size()-1)
                    finalToken += ex + ",";
                else
                    finalToken += ex;
                i++;
            }
        } 
        else
            finalToken = token;

        return finalToken;
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