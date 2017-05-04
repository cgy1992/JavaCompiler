package cop5556sp17;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;

public class ParserTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}

	@Test
	public void strongOp() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "%";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.strongOp();
	}

	@Test
	public void testIllegalElem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc*teddy%";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.elem();
	}

	@Test
	public void testElem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc* teddy\r\n&bloody%turds/owl";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.elem();
	}

	@Test
	public void testIllegalTerm() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc* teddy\r\n&bloody%turds/owl+screenheight-screenwidth|toodles|";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.term();
	}

	@Test
	public void testTerm() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc* teddy\r\n&bloody%turds/owl-screenheight";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.term();
	}
	

	@Test
	public void testIllegalExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "screenheight-screenwidth!=";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.expression();
	}

	@Test
	public void testExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "screenheight-screenwidth!=plato";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void testArg() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.arg();
	}

	@Test
	public void testArgerror() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}

	@Test
	public void testChainElem() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "blur  (3,5) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.chainElem();
	}

	@Test
	public void testChainElemErr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "blur  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.chainElem();
	}

	@Test
	public void testChain() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "blur  (3,5)->blur  (3,5)->blur  (3,5)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.chain();
	}

	@Test
	public void testChainErr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "blur  (3,5)->blur  (3,5)->->->blur  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.chain();
	}

	@Test
	public void testAssign() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "blurcrazy<-screenheight-screenwidth!=plato";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.assign();
	}

	@Test
	public void testAssignErr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "blurcrazy<-screenheight-screenwidth!=";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.assign();
	}

	@Test
	public void testDec2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "integer blue";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.dec();
	}

	@Test
	public void testDecErr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "integer blur";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.dec();
	}

	@Test
	public void testParamDec2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "url blue";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.paramDec();
	}

	@Test
	public void testParamDecErr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "url blur";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.paramDec();
	}

	@Test
	public void testBlock() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{integer blue sleep screenheight-screenwidth!=plato;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void testBlockErr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{integer blue sleep\n screenheight-screenwidth!=plato;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void testWhileStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while(true){integer blue sleep\n screenheight-screenwidth!=plato;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.statement();
	}

	@Test
	public void testWhileStatementErr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while(true)\r\n integer blue sleep\n screenheight-screenwidth!=plato;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
	}

	@Test
	public void testIfStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(screenheight-screenwidth!=plato){integer blue sleep\n screenheight-screenwidth!=plato;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.statement();
	}

	@Test
	public void testIfStatementErr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(screenheight-screenwidth!=plato)\r\n {integer blue sleep\n -screenwidth!=plato;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
	}

	@Test
	public void testStatement3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(screenheight-screenwidth!=plato)\r\n {integer blue sleep\n screenheight-screenwidth!=plato;blurcrazy<-screenheight-screenwidth!=plato;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.statement();
	}

	@Test
	public void testStatementErr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if(screenheight-screenwidth!=plato)\r\n {integer blue sleep\n screenheight-screenwidth!=;blurcrazy<-screenheight-screenwidth!=plato;} ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
	}

	@Test
	public void testProgram() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "toodles {if(screenheight-screenwidth!=plato)\r\n {integer blue sleep\n screenheight-screenwidth!=test;blurcrazy<-screenheight-screenwidth!=plato;}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.program();
	}

	@Test
	public void testProgramErr() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "terrible{if(screenheight-screenwidth!=plato)\r\n {integer blue sleep\n screenheight-screenwidth!=;blurcrazy<-screenheight-screenwidth!=plato;}} ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}

	@Test
	public void testProgram2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "terrible{if(screenheight-screenwidth!=(plato*kudrow))\r\n {integer blue sleep\n screenheight-screenwidth!=blurcrazy;}} ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		// thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}

	@Test
	public void testProgram0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog0 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}

	@Test
	public void testIf() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.ifStatement();
		String input1 = "if(){";
		Parser parser1 = new Parser(new Scanner(input1).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser1.ifStatement();
		String input2 = "if(){}";
		Parser parser2 = new Parser(new Scanner(input2).scan());
		parser2.ifStatement();
		String input3 = "if(abc>=123){integer def while(1){def -> blur}}";
		Parser parser3 = new Parser(new Scanner(input3).scan());
		parser3.ifStatement();
		String input4 = "if(abc>=123){integer def while(1){def -> blur somethingToCauseError}}";
		Parser parser4 = new Parser(new Scanner(input4).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser4.ifStatement();
	}

	@Test
	public void testProgram1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "value file value1 integer value2 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
		String input1 = "value file value1, integer value2 {}";
		Parser parser1 = new Parser(new Scanner(input1).scan());
		parser1.parse();
		String input2 = "value file value1, image value2 {}";
		Parser parser2 = new Parser(new Scanner(input2).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser2.parse();
	}

	@Test
	public void testProgram39() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "prog{i <- 0;\n while(i < 10){\n if( i % 2 == 0){\n j <- i / 2; \n show(j);\n };\n i <- i + 1;\n}}";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void testNested() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "value file value1, integer value2 {if((something)>=(SomethingElse)){sleep sleepExp;\r\n gray(somethings, again, and, again1)->screenwidth345;}}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}
	
	 @Test
	public void test5() throws IllegalCharException,IllegalNumberException, SyntaxException{
	String input = "Hello integer a,file _syllabus{image pooh sleep night>=day;} ";
	Parser parser = new Parser(new Scanner(input).scan());
	parser.parse();
	}                        
   @Test
	public void test8() throws IllegalCharException,IllegalNumberException, SyntaxException{
	String input = "{height(a,10)->b|->blur;k<-(i!=1)+1;}";
	Parser parser = new Parser(new Scanner(input).scan());
	parser.block();
	}
	
	@Test
	public void test9() throws IllegalCharException,IllegalNumberException, SyntaxException{
	String input = "(i>5,t+0,d|u)";
	Parser parser = new Parser(new Scanner(input).scan());
	parser.arg();
	}
	
	@Test
	public void testblock() throws IllegalCharException,IllegalNumberException, SyntaxException
	{
		String input = "{boolean b if(a==b){x<-a!=b;}}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.block();
	}
	@Test
	public void testStatement() throws IllegalCharException,IllegalNumberException, SyntaxException
	{
		String input = "show(screenheight/screenwidth | false <=10) -> hide |-> xloc(a<b);";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.statement();
	}
	@Test
	public void testArgerror1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "123(";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.chainElem();
	}
	
	@Test
	public void test4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "a->gray(true,b)width(6)|->frame";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chain();
	}
	
	@Test
	public void testProgram3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "terrible{if(screenheight-screenwidth!=(plato*kudrow))\r\n {integer blue sleep\n screenheight-screenwidth!=blurcrazy;}} ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		// thrown.expect(Parser.SyntaxException.class);
		parser.program();
	}

	@Test
	public void testProgram4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "urltrudy file amazing,integer {if(screenheight-screenwidth!=(plato*kudrow))\r\n {integer blue sleep\n screenheight-screenwidth!=blurcrazy;}} ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void testProgram5() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "urltrudy file amazing,integer food {if(screenheight-screenwidth!=(plato*kudrow))\r\n {integer blue sleep\n screenheight-screenwidth!=blurcrazy;}} ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		// thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void testProgram6() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "urltrudy {if(screenheight-screenwidth!=(plato*kudrow))\r\n {integer blue sleep\n screenheight-screenwidth!=blurcrazy;}{integer blue sleep\n screenheight-screenwidth!=blurcrazy;}{integer blue sleep\n screenheight-screenwidth!=blurcrazy;}} ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void testProgram7() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "urltrudy {if(screenheight-screenwidth!=(plato*kudrow))\r\n {integer blue sleep\n screenheight-screenwidth!=blurcrazy;}while(screenheight+screenwidth!=(plato*kudrow)){integer blue sleep\n screenheight-screenwidth!=blurcrazy;}if(trump==Hillary){integer blue sleep\n screenheight-screenwidth!=blurcrazy;}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		// thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}
	
	@Test
	public void testProgram8() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "urltrudy {if(screenheight-screenwidth!=(plato*kudrow))\r\n {integer blue sleep\n screenheight-screenwidth!=blurcrazy;}while(screenheight+screenwidth!=(plato*kudrow)){integer blue sleep\n screenheight-screenwidth!=blurcrazy;}if(trump==Hillary){integer blue sleep\n screenheight-screenwidth!=blurcrazy;}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		// thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

}