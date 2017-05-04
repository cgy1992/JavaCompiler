package cop5556sp17;

import static cop5556sp17.Scanner.Kind.SEMI;
import static cop5556sp17.Scanner.Kind.DIV;
import static cop5556sp17.Scanner.Kind.TIMES;
import static cop5556sp17.Scanner.Kind.ASSIGN;
import static cop5556sp17.Scanner.Kind.EQUAL;
import static cop5556sp17.Scanner.Kind.IDENT;
import static cop5556sp17.Scanner.Kind.LE;
import static cop5556sp17.Scanner.Kind.NOTEQUAL;
import static cop5556sp17.Scanner.Kind.OR;
import static cop5556sp17.Scanner.Kind.KW_TRUE;
import static cop5556sp17.Scanner.Kind.KW_XLOC;
import static cop5556sp17.Scanner.Kind.KW_YLOC;
import static cop5556sp17.Scanner.Kind.OP_GRAY;
import static cop5556sp17.Scanner.Kind.GE;
import static cop5556sp17.Scanner.Kind.INT_LIT;
import static cop5556sp17.Scanner.Kind.KW_INTEGER;
import static cop5556sp17.Scanner.Kind.KW_SCREENHEIGHT;
import static cop5556sp17.Scanner.Kind.KW_SCREENWIDTH;
import static cop5556sp17.Scanner.Kind.KW_SCALE;
import static cop5556sp17.Scanner.Kind.KW_URL;
import static cop5556sp17.Scanner.Kind.KW_IMAGE;
import static cop5556sp17.Scanner.Kind.KW_FILE;
import static cop5556sp17.Scanner.Kind.KW_FRAME;
import static cop5556sp17.Scanner.Kind.KW_WHILE;
import static cop5556sp17.Scanner.Kind.KW_IF;
import static cop5556sp17.Scanner.Kind.KW_BOOLEAN;
import static cop5556sp17.Scanner.Kind.OP_SLEEP;
import static cop5556sp17.Scanner.Kind.OP_CONVOLVE;
import static cop5556sp17.Scanner.Kind.OP_BLUR;
import static cop5556sp17.Scanner.Kind.OP_WIDTH;
import static cop5556sp17.Scanner.Kind.OP_HEIGHT;
import static cop5556sp17.Scanner.Kind.KW_HIDE;
import static cop5556sp17.Scanner.Kind.KW_SHOW;
import static cop5556sp17.Scanner.Kind.KW_MOVE;
import static cop5556sp17.Scanner.Kind.KW_FALSE;
import static cop5556sp17.Scanner.Kind.LPAREN;
import static cop5556sp17.Scanner.Kind.RBRACE;
import static cop5556sp17.Scanner.Kind.RPAREN;
import static cop5556sp17.Scanner.Kind.LBRACE;

//import static cop5556sp17.Scanner.Kind.OP_BLUR;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.Scanner.LinePos;

public class ScannerTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();


	
	@Test
	public void testEmpty() throws IllegalCharException, IllegalNumberException {
		String input = "";
		Scanner scanner = new Scanner(input);
		scanner.scan();
	}

	@Test
	public void testSemiConcat() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = ";;;";
		//create and initialize the scanner
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(SEMI, token.kind);
		assertEquals(0, token.pos);
		String text = SEMI.getText();
		assertEquals(text.length(), token.length);
		assertEquals(text, token.getText());
		//get the next token and check its kind, position, and contents
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(SEMI, token1.kind);
		assertEquals(1, token1.pos);
		assertEquals(text.length(), token1.length);
		assertEquals(text, token1.getText());
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(SEMI, token2.kind);
		String text1 = SEMI.getText();
		assertEquals(2, token2.pos);
		assertEquals(text1.length(), token2.length);
		assertEquals(text1, token2.getText());
		//check that the scanner has inserted an EOF token at the end
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF,token3.kind);
	}
	

	
	
	/**
	 * This test illustrates how to check that the Scanner detects errors properly. 
	 * In this test, the input contains an int literal with a value that exceeds the range of an int.
	 * The scanner should detect this and throw and IllegalNumberException.
	 * 
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	@Test
	public void testIntOverflowError() throws IllegalCharException, IllegalNumberException{
		String input = "99999999999999999";
		Scanner scanner = new Scanner(input);
		thrown.expect(IllegalNumberException.class);
		scanner.scan();		
	}

	@Test
	public void testComments() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = "/*this is\n a comment*/ terribly hungry|<=bored>=== !=";
		//create and initialize the scanner
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(IDENT, token.kind);
		assertEquals(23, token.pos);
		String text = token.getText();
		assertEquals(text.length(), token.length);
		assertEquals(text, token.getText());
		//get the next token and check its kind, position, and contents
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(IDENT, token1.kind);
		assertEquals(32, token1.pos);
		String text1 = token1.getText();
		assertEquals(text1.length(), token1.length);
		assertEquals(text1, token1.getText());
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(OR, token2.kind);
		assertEquals(38, token2.pos);
		String text2 = token2.getText();
		assertEquals(text2.length(), token2.length);
		assertEquals(text2, token2.getText());
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(LE, token3.kind);
		assertEquals(39, token3.pos);
		String text3 = token3.getText();
		assertEquals(text3.length(), token3.length);
		assertEquals(text3, token3.getText());
		Scanner.Token token4 = scanner.nextToken();
		assertEquals(IDENT, token4.kind);
		assertEquals(41, token4.pos);
		String text4 = token4.getText();
		assertEquals(text4.length(), token4.length);
		assertEquals(text4, token4.getText());
		Scanner.Token token5 = scanner.nextToken();
		assertEquals(GE, token5.kind);
		assertEquals(46, token5.pos);
		String text5 = token5.getText();
		assertEquals(text5.length(), token5.length);
		assertEquals(text5, token5.getText());
		Scanner.Token token6 = scanner.nextToken();
		assertEquals(EQUAL, token6.kind);
		assertEquals(48, token6.pos);
		String text6 = token6.getText();
		assertEquals(text6.length(), token6.length);
		assertEquals(text6, token6.getText());
		Scanner.Token token7 = scanner.nextToken();
		assertEquals(NOTEQUAL, token7.kind);
		assertEquals(51, token7.pos);
		String text7 = token7.getText();
		assertEquals(text7.length(), token7.length);
		assertEquals(text7, token7.getText());
		//check that the scanner has inserted an EOF token at the end
		Scanner.Token token8 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF,token8.kind);
	}
	
	
	@Test
	public void testClosedComments() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = "/**/";
		//create and initialize the scanner
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//get the first token and check its kind, position, and contents	
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF,token1.kind);
		String text = token1.getText();
		assertEquals(text, Scanner.Kind.EOF.getText());
	}
	
	@Test
	public void testUnclosedComments() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = "/*this is\n a comment*/ terribly /*hun\ngry";
		//create and initialize the scanner
		Scanner scanner = new Scanner(input);	
		thrown.expect(IllegalCharException.class);
		scanner.scan();
	}
	
	@Test
	public void testIdent() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = "_Shining00$908 like the sun";
		// create and initialize the scanner
		Scanner scanner = new Scanner(input);
		scanner.scan();
		// get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(IDENT, token.kind);
		assertEquals(0, token.pos);
		String text = token.getText();
		assertEquals(text.length(), token.length);
		assertEquals(text, token.getText());
		// get the next token and check its kind, position, and contents
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(IDENT, token1.kind);
		assertEquals(15, token1.pos);
		String text1 = token1.getText();
		assertEquals(text1.length(), token1.length);
		assertEquals(text1, token1.getText());
		// get the next token and check its kind, position, and contents
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(IDENT, token2.kind);
		assertEquals(20, token2.pos);
		String text2 = token2.getText();
		assertEquals(text2.length(), token2.length);
		assertEquals(text2, token2.getText());
		// get the next token and check its kind, position, and contents
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(IDENT, token3.kind);
		assertEquals(24, token3.pos);
		String text3 = token3.getText();
		assertEquals(text3.length(), token3.length);
		assertEquals(text3, token3.getText());

		// check that the scanner has inserted an EOF token at the end
		Scanner.Token token4 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token4.kind);
	}
	
	@Test
	public void testIllegalIdent() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = "_Shining00$908 li=ke the sun";
		// create and initialize the scanner
		Scanner scanner = new Scanner(input);	
		thrown.expect(IllegalCharException.class);
		scanner.scan();
		// get the first token and check its kind, position, and contents
		
	}
	
	@Test
	public void testIntLitSep() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = "123400 00(345}<-{)";
		// create and initialize the scanner
		Scanner scanner = new Scanner(input);
		scanner.scan();
		// get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(INT_LIT, token.kind);
		assertEquals(0, token.pos);
		String text = token.getText();
		assertEquals(text.length(), token.length);
		assertEquals(text, token.getText());
		// get the next token and check its kind, position, and contents
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(INT_LIT, token1.kind);
		assertEquals(7, token1.pos);
		String text1 = token1.getText();
		assertEquals(text1.length(), token1.length);
		assertEquals(text1, token1.getText());
		// get the next token and check its kind, position, and contents
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(INT_LIT, token2.kind);
		assertEquals(8, token2.pos);
		String text2 = token2.getText();
		assertEquals(text2.length(), token2.length);
		assertEquals(text2, token2.getText());
		// get the next token and check its kind, position, and contents
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(LPAREN, token3.kind);
		assertEquals(9, token3.pos);
		String text3 = token3.getText();
		assertEquals(text3.length(), token3.length);
		assertEquals(text3, token3.getText());
		// get the next token and check its kind, position, and contents
		Scanner.Token token4 = scanner.nextToken();
		assertEquals(INT_LIT, token4.kind);
		assertEquals(10, token4.pos);
		String text4 = token4.getText();
		assertEquals(text4.length(), token4.length);
		assertEquals(text4, token4.getText());
		Scanner.Token token5 = scanner.nextToken();
		assertEquals(RBRACE, token5.kind);
		assertEquals(13, token5.pos);
		String text5 = token5.getText();
		assertEquals(text5, token5.getText());
		Scanner.Token token6 = scanner.nextToken();
		assertEquals(ASSIGN, token6.kind);
		assertEquals(14, token6.pos);
		String text6 = token6.getText();
		assertEquals(text6, token6.getText());
		Scanner.Token token7 = scanner.nextToken();
		assertEquals(LBRACE, token7.kind);
		assertEquals(16, token7.pos);
		String text7 = token7.getText();
		assertEquals(text7, token7.getText());
		Scanner.Token token8 = scanner.nextToken();
		assertEquals(RPAREN, token8.kind);
		assertEquals(17, token8.pos);
		String text8 = token8.getText();
		assertEquals(text8, token8.getText());
		// check that the scanner has inserted an EOF token at the end
		Scanner.Token token9 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token9.kind);
	}
	
	@Test
	public void testLinePos() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = "ive got\r\n lov*/ely\n bunch\n";
		// create and initialize the scanner
		Scanner scanner = new Scanner(input);
		scanner.scan();
		// get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(IDENT, token.kind);
		assertEquals(0, token.pos);
		String text = token.getText();
		LinePos linepos =token.getLinePos();
		assertEquals(text.length(), token.length);
		assertEquals(text, token.getText());
		assertEquals(0, linepos.line);
		assertEquals(0,linepos.posInLine);
		// get the next token and check its kind, position, and contents
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(IDENT, token1.kind);
		assertEquals(4, token1.pos);
		String text1 = token1.getText();
		assertEquals(text1.length(), token1.length);
		assertEquals(text1, token1.getText());
		LinePos linepos1 =token1.getLinePos();
		assertEquals(0, linepos1.line);
		assertEquals(4,linepos1.posInLine);
		// get the next token and check its kind, position, and contents
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(IDENT, token2.kind);
		assertEquals(10, token2.pos);
		String text2 = token2.getText();
		assertEquals(text2.length(), token2.length);
		assertEquals(text2, token2.getText());
		LinePos linepos2 =token2.getLinePos();
		assertEquals(1, linepos2.line);
		assertEquals(1,linepos2.posInLine);
		// get the next token and check its kind, position, and contents
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(TIMES, token3.kind);
		assertEquals(13, token3.pos);
		String text3 = token3.getText();
		assertEquals(text3.length(), token3.length);
		assertEquals(text3, token3.getText());
		LinePos linepos3 =token3.getLinePos();
		assertEquals(1, linepos3.line);
		assertEquals(4,linepos3.posInLine);
		Scanner.Token token4 = scanner.nextToken();
		assertEquals(DIV, token4.kind);
		assertEquals(14, token4.pos);
		String text4 = token4.getText();
		assertEquals(text4.length(), token4.length);
		assertEquals(text4, token4.getText());
		LinePos linepos4 = token4.getLinePos();
		assertEquals(1, linepos4.line);
		assertEquals(5, linepos4.posInLine);
		Scanner.Token token5 = scanner.nextToken();
		assertEquals(IDENT, token5.kind);
		assertEquals(15, token5.pos);
		String text5 = token5.getText();
		assertEquals(text5.length(), token5.length);
		assertEquals(text5, token5.getText());
		LinePos linepos5 = token5.getLinePos();
		assertEquals(1, linepos5.line);
		assertEquals(6, linepos5.posInLine);
		Scanner.Token token6 = scanner.nextToken();
		assertEquals(IDENT, token6.kind);
		assertEquals(20, token6.pos);
		String text6 = token6.getText();
		assertEquals(text6.length(), token6.length);
		assertEquals(text6, token6.getText());
		LinePos linepos6 = token6.getLinePos();
		assertEquals(2, linepos6.line);
		assertEquals(1, linepos6.posInLine);
		// check that the scanner has inserted an EOF token at the end
		Scanner.Token token7 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token7.kind);
	}
	
	@Test
	public void testReserved() throws IllegalCharException, IllegalNumberException {
		// input string
		String input = "integer boolean image url file frame while if sleep screenheight screenwidth gray convolve blur scale width height xloc yloc hide show move true false";
		// create and initialize the scanner
		Scanner scanner = new Scanner(input);
		scanner.scan();
		// get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(KW_INTEGER, token.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(KW_BOOLEAN, token1.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(KW_IMAGE, token2.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(KW_URL, token3.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token4 = scanner.nextToken();
		assertEquals(KW_FILE, token4.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token5 = scanner.nextToken();
		assertEquals(KW_FRAME, token5.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token6 = scanner.nextToken();
		assertEquals(KW_WHILE, token6.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token7 = scanner.nextToken();
		assertEquals(KW_IF, token7.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token8 = scanner.nextToken();
		assertEquals(OP_SLEEP, token8.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token9 = scanner.nextToken();
		assertEquals(KW_SCREENHEIGHT, token9.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token10 = scanner.nextToken();
		assertEquals(KW_SCREENWIDTH, token10.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token11 = scanner.nextToken();
		assertEquals(OP_GRAY, token11.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token12 = scanner.nextToken();
		assertEquals(OP_CONVOLVE, token12.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token13 = scanner.nextToken();
		assertEquals(OP_BLUR, token13.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token14 = scanner.nextToken();
		assertEquals(KW_SCALE, token14.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token15 = scanner.nextToken();
		assertEquals(OP_WIDTH, token15.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token16 = scanner.nextToken();
		assertEquals(OP_HEIGHT, token16.kind);
		// get the next token and check its kind, position, and contents
		Scanner.Token token17 = scanner.nextToken();
		assertEquals(KW_XLOC, token17.kind);
		Scanner.Token token18 = scanner.nextToken();
		assertEquals(KW_YLOC, token18.kind);
		Scanner.Token token19 = scanner.nextToken();
		assertEquals(KW_HIDE, token19.kind);
		Scanner.Token token20 = scanner.nextToken();
		assertEquals(KW_SHOW, token20.kind);
		Scanner.Token token21 = scanner.nextToken();
		assertEquals(KW_MOVE, token21.kind);
		Scanner.Token token22 = scanner.nextToken();
		assertEquals(KW_TRUE, token22.kind);
		Scanner.Token token23 = scanner.nextToken();
		assertEquals(KW_FALSE, token23.kind);		
		// check that the scanner has inserted an EOF token at the end
		Scanner.Token token24 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token24.kind);
	}
}
