package cop5556sp17;
 
import static org.junit.Assert.*;
 
import java.util.ArrayList;
import java.util.Map.Entry;
 
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
 
import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.Statement;
import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.TypeCheckVisitor.TypeCheckException;
 
public class TypeCheckVisitorTest {
 
 
 @Rule
 public ExpectedException thrown = ExpectedException.none();
 
 @Test
 public void testAssignmentBoolLit0() throws Exception{
  String input = "p {\nboolean y \ny <- false;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
  ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testAssignmentBoolLitError0() throws Exception {
  String input = "p {\nboolean y \ny <- 3;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
  ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  thrown.expect(TypeCheckVisitor.TypeCheckException.class);
  program.visit(v, null);
 }
 
 @Test
 public void testVisit1() throws Exception {
  String input = "method file x { }";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  //System.out.println(scanner);
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
  
 @Test
 public void testVisit2() throws Exception {
  String input = "chelsea{if(a<b <= c){}}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  //a, b, and c not declared
  thrown.expect(TypeCheckVisitor.TypeCheckException.class);
  program.visit(v, null);
 }
 
 @Test
 public void testVisit3() throws Exception {
  String input = "chelsea{integer a\n a <- 5;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testVisit4() throws Exception {
  String input = "chelsea{integer a\n a <- false;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  thrown.expect(TypeCheckVisitor.TypeCheckException.class);
  program.visit(v, null);
 }
 
 @Test
 public void testVisit5() throws Exception {
  String input = "chelsea{if(true){integer a}\n a <- 5;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  //a <- 5 is out of scope
  thrown.expect(TypeCheckVisitor.TypeCheckException.class);
  program.visit(v, null);
 }
 
 @Test
 public void testExpTable1() throws Exception {
  String input = "chelsea{integer a\n integer b\n integer c\n c <- (a + b);}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testExpTable2() throws Exception {
  String input = "chelsea{integer a\n integer b\n integer c\n c <- (a - b);}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
  //System.out.println(v.symtab.toString());
 }
 
 @Test
 public void testExpTable3() throws Exception {
  String input = "chelsea{image a\n image b\n image c\n c <- (a + b);}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
  //System.out.println(v.symtab.toString());
 }
 
 @Test
 public void testExpTable5() throws Exception {
  String input = "chelsea{image a\n image b\n image c\n c <- (a - b);}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testExpTable6() throws Exception {
  String input = "chelsea{integer a\n integer b\n integer c\n c <- (a * b);}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testExpTable7() throws Exception {
  String input = "chelsea{integer a\n integer b\n integer c\n c <- (a / b);}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testExpTable8() throws Exception {
  String input = "chelsea{integer a\n image b\n image c\n c <- (a * b);}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 //Some random nested combinations in the binary expression table
 
 @Test
 public void testExpTableNest1() throws Exception {
  String input = "chelsea{integer a\n integer b\n integer c\n while(a != (b + c)){}}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 //Some random bad combinations in the binary expression table
 @Test
 public void testExpTableBad1() throws Exception {
  String input = "chelsea{boolean a\n integer b\n if(a != b){}}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  thrown.expect(TypeCheckVisitor.TypeCheckException.class);
  program.visit(v, null);
 }
 
 @Test
 public void testExpTableBad2() throws Exception {
  String input = "chelsea{boolean a\n integer b\n while(a > b){}}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  thrown.expect(TypeCheckVisitor.TypeCheckException.class);
  program.visit(v, null);
 }
 
 @Test
 public void testChainTable1() throws Exception {
  String input = "chelsea url tim{image i\n tim -> i;}";//type image
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testChainTable2() throws Exception {
  String input = "chelsea file metcalf{image i\n tim -> i;}";//type image
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testChainTable3() throws Exception {
  String input = "chelsea{frame f \n f -> xloc(5);}";//type integer
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testChainTable4() throws Exception {
  String input = "chelsea{frame f \n f -> yloc(3);}";//type integer
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testChainTable5() throws Exception {
  String input = "chelsea{frame f \n f -> move;}";//type frame
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testChainNested1() throws Exception {
  String input = "chelsea{image i\n i -> scale(5) -> blur;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
  //System.out.println(v.symtab.toString());
 }
 
 @Test
 public void testChainNested2() throws Exception {
  String input = "chelsea url u{image i\n image i2\n i -> u -> i2;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
  //System.out.println(v.symtab.toString());
 }
 
 @Test
 public void testChainNested3() throws Exception {
  String input = "chelsea{image i\n i -> blur -> scale(5);}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testScope1() throws Exception {
  String input = "chelsea url u{integer i\n if(true){integer i\n i <- 5;}\n i <- 2;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
  //System.out.println(v.symtab.toString());
 }
 
 @Test
 public void testScope2() throws Exception {
  String input = "chelsea{if(true){integer i}i <- 3;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  thrown.expect(TypeCheckVisitor.TypeCheckException.class);
  program.visit(v, null);
 }
 
 @Test
 public void testScope3() throws Exception {
  String input = "chelsea{integer i\n if(true){integer i2} i <- i2;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  thrown.expect(TypeCheckVisitor.TypeCheckException.class);
  program.visit(v, null);
 }
 
 @Test
 public void testConstantExpression1() throws Exception {
  String input = "chelsea{integer i \n i <- screenwidth;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testAssignment1() throws Exception {
  String input = "chelsea{integer i \n integer c \n i <- c;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }
 
 @Test
 public void testBadAssignment1() throws Exception {
  String input = "chelsea{integer i \n boolean c \n i <- c;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  thrown.expect(TypeCheckVisitor.TypeCheckException.class);
  program.visit(v, null);
 }
}