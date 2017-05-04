package cop5556sp17;

import static cop5556sp17.Scanner.Kind.*;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.BooleanLitExpression;
import cop5556sp17.AST.ConstantExpression;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.IntLitExpression;

public class ASTTest {

    static final boolean doPrint = true;

    static void show(Object s) {
        if (doPrint) {
            System.out.println(s);
        }
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "abc";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode ast = parser.expression();
        assertEquals(IdentExpression.class, ast.getClass());
    }

    @Test
    public void testFactor1() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "123";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode ast = parser.expression();
        assertEquals(IntLitExpression.class, ast.getClass());
    }

    @Test
    public void testProgram13() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "abc";
        Parser parser = new Parser(new Scanner(input).scan());
        ASTNode ast = parser.expression();
        assertEquals(IdentExpression.class, ast.getClass());
    }

    @Test
    public void testProgram14() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "123";
        Parser parser = new Parser(new Scanner(input).scan());
        ASTNode ast = parser.expression();
        assertEquals(IntLitExpression.class, ast.getClass());
    }

    @Test
    public void testProgram15() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "true";
        Parser parser = new Parser(new Scanner(input).scan());
        ASTNode ast = parser.expression();
        assertEquals(BooleanLitExpression.class, ast.getClass());
    }

    @Test

    public void testFactor3() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "false";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BooleanLitExpression.class, ast.getClass());

    }

    @Test

    public void testFactor4() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "screenwidth";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(ConstantExpression.class, ast.getClass());

    }

    @Test

    public void testBinaryExpr0() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1+abc";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(IntLitExpression.class, be.getE0().getClass());

        assertEquals(IdentExpression.class, be.getE1().getClass());

        assertEquals(PLUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr1() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1+2";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(IntLitExpression.class, be.getE0().getClass());

        assertEquals(IntLitExpression.class, be.getE1().getClass());

        assertEquals(PLUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr2() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1+2+3";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(BinaryExpression.class, be.getE0().getClass());

        assertEquals(IntLitExpression.class, be.getE1().getClass());

        assertEquals(PLUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr3() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1+2+3+4";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(BinaryExpression.class, be.getE0().getClass());

        assertEquals(IntLitExpression.class, be.getE1().getClass());

        assertEquals(PLUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr4() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1+2+3+4-5";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(BinaryExpression.class, be.getE0().getClass());

        assertEquals(IntLitExpression.class, be.getE1().getClass());

        assertEquals(MINUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr5() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1-2+3-4-5";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(BinaryExpression.class, be.getE0().getClass());

        assertEquals(IntLitExpression.class, be.getE1().getClass());

        assertEquals(MINUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr6() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1-2+3-4+5";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(BinaryExpression.class, be.getE0().getClass());

        assertEquals(IntLitExpression.class, be.getE1().getClass());

        assertEquals(PLUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr7() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1%2";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(IntLitExpression.class, be.getE0().getClass());

        assertEquals(IntLitExpression.class, be.getE1().getClass());

        assertEquals(MOD, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr9() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1-2+3-4%5";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(BinaryExpression.class, be.getE0().getClass());

        assertEquals(BinaryExpression.class, be.getE1().getClass());

        assertEquals(MINUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr10() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1+2%3-4-5";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(BinaryExpression.class, be.getE0().getClass());

        assertEquals(IntLitExpression.class, be.getE1().getClass());

        assertEquals(MINUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr11() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "1+2%3-4%5";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(BinaryExpression.class, be.getE0().getClass());

        assertEquals(BinaryExpression.class, be.getE1().getClass());

        assertEquals(MINUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr12() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "shesh+mishra";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(IdentExpression.class, be.getE0().getClass());

        assertEquals(IdentExpression.class, be.getE1().getClass());

        assertEquals(PLUS, be.getOp().kind);

    }

    @Test

    public void testBinaryExpr13() throws IllegalCharException, IllegalNumberException, SyntaxException {

        String input = "shesh+nath+123+hello-shesh";

        Scanner scanner = new Scanner(input);

        scanner.scan();

        Parser parser = new Parser(scanner);

        ASTNode ast = parser.expression();

        assertEquals(BinaryExpression.class, ast.getClass());

        BinaryExpression be = (BinaryExpression) ast;

        assertEquals(BinaryExpression.class, be.getE0().getClass());

        assertEquals(IdentExpression.class, be.getE1().getClass());

        assertEquals(MINUS, be.getOp().kind);

    }

}