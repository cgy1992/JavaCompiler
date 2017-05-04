package cop5556sp17;

import cop5556sp17.Scanner.Kind;
import static cop5556sp17.Scanner.Kind.*;

import java.util.ArrayList;
import java.util.List;

import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.*;

public class Parser {

	/**
	 * Exception to be thrown if a syntax error is detected in the input. You
	 * will want to provide a useful error message.
	 *
	 */
	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		public SyntaxException(String message) {
			super(message);
		}
	}

	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * parse the input using tokens from the scanner. Check for EOF (i.e. no
	 * trailing junk) when finished
	 * 
	 * @throws SyntaxException
	 */
	Program parse() throws SyntaxException {
		// Token firstToken = t;
		Program myprogram = program();
		matchEOF();
		return myprogram;
	}

	// expression = term ( relOp term)*
	Expression expression() throws SyntaxException {
		Expression myExpression1 = null;
		Token firstToken = t;
		BinaryExpression myBinaryExpression = new BinaryExpression(null, null, null, null);
		if (t.kind.equals(IDENT) || t.kind.equals(INT_LIT) || t.kind.equals(KW_TRUE) || t.kind.equals(KW_FALSE)
				|| t.kind.equals(KW_SCREENHEIGHT) || t.kind.equals(KW_SCREENWIDTH) || t.kind.equals(LPAREN)) {
			Expression e0 = term();
			if (t.kind.equals(KW_TRUE) || t.kind.equals(KW_FALSE)) {
				myExpression1 = new BooleanLitExpression(firstToken);
			} else if (t.kind.equals(IDENT)) {
				myExpression1 = new IdentExpression(firstToken);
			} else if (t.kind.equals(INT_LIT)) {
				myExpression1 = new IntLitExpression(firstToken);
			} else if (t.kind.equals(KW_SCREENHEIGHT) || t.kind.equals(KW_SCREENWIDTH)) {
				myExpression1 = new ConstantExpression(firstToken);
			} else {
				myExpression1 = e0;
			}

			// use Weak Operators
			while (true) {
				if (t.kind.equals(LT) || t.kind.equals(LE) || t.kind.equals(GT) || t.kind.equals(GE)
						|| t.kind.equals(NOTEQUAL) || t.kind.equals(EQUAL)) {
					Token op = relOp();
					Expression e1 = term();
					myBinaryExpression = new BinaryExpression(firstToken, myExpression1, op, e1);
					myExpression1 = myBinaryExpression;
				} else {
					return myExpression1;
				}
			}
		} else {
			throw new SyntaxException("illegal Strong Operator---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}

	}

	// term = elem ( weakOp elem)*
	Expression term() throws SyntaxException {
		BinaryExpression myBinaryExpression = new BinaryExpression(null, null, null, null);
		Expression myExpression = null;
		Token firstToken = t;
		if (t.kind.equals(IDENT) || t.kind.equals(INT_LIT) || t.kind.equals(KW_TRUE) || t.kind.equals(KW_FALSE)
				|| t.kind.equals(KW_SCREENHEIGHT) || t.kind.equals(KW_SCREENWIDTH) || t.kind.equals(LPAREN)) {
			Expression e0 = elem();
			if (t.kind.equals(KW_TRUE) || t.kind.equals(KW_FALSE)) {
				myExpression = new BooleanLitExpression(firstToken);
			} else if (t.kind.equals(IDENT)) {
				myExpression = new IdentExpression(firstToken);
			} else if (t.kind.equals(INT_LIT)) {
				myExpression = new IntLitExpression(firstToken);
			} else if (t.kind.equals(KW_SCREENHEIGHT) || t.kind.equals(KW_SCREENWIDTH)) {
				myExpression = new ConstantExpression(firstToken);
			} else {
				myExpression = e0;
			}

			// use Weak Operators
			while (true) {
				if (t.kind.equals(PLUS) || t.kind.equals(MINUS) || t.kind.equals(OR)) {
					Token op = weakOp();
					Expression e1 = elem();
					myBinaryExpression = new BinaryExpression(firstToken, myExpression, op, e1);
					myExpression = myBinaryExpression;
				} else {
					return myExpression;
				}
			}
		} else {
			throw new SyntaxException("illegal Strong Operator---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
	}

	// elem = factor ( strongOp factor)*
	Expression elem() throws SyntaxException {
		Expression myExpression = null;
		BinaryExpression myBinaryExpression = new BinaryExpression(null, null, null, null);
		Token firstToken = t;
		if (t.kind.equals(IDENT) || t.kind.equals(INT_LIT) || t.kind.equals(KW_TRUE) || t.kind.equals(KW_FALSE)
				|| t.kind.equals(KW_SCREENHEIGHT) || t.kind.equals(KW_SCREENWIDTH) || t.kind.equals(LPAREN)) {
			Expression e0 = factor();
			if (t.kind.equals(KW_TRUE) || t.kind.equals(KW_FALSE)) {
				myExpression = new BooleanLitExpression(firstToken);
			} else if (t.kind.equals(IDENT)) {
				myExpression = new IdentExpression(firstToken);
			} else if (t.kind.equals(INT_LIT)) {
				myExpression = new IntLitExpression(firstToken);
			} else if (t.kind.equals(KW_SCREENHEIGHT) || t.kind.equals(KW_SCREENWIDTH)) {
				myExpression = new ConstantExpression(firstToken);
			} else {
				myExpression = e0;
			}
			// use Strong operators

			while (true) {
				if (t.kind.equals(TIMES) || t.kind.equals(DIV) || t.kind.equals(MOD) || t.kind.equals(AND)) {
					Token op = strongOp();
					Expression e1 = factor();
					myBinaryExpression = new BinaryExpression(firstToken, myExpression, op, e1);
					myExpression = myBinaryExpression;
				} else {
					return myExpression;
				}
			}

		} else {
			throw new SyntaxException("illegal Strong Operator---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}

	}

	// factor = IDENT | INT_LIT | KW_TRUE | KW_FALSE| KW_SCREENWIDTH |
	// KW_SCREENHEIGHT | ( expression )
	Expression factor() throws SyntaxException {
		Expression myExpression = null;
		Token firstToken = t;
		// Expression myExpression = new Binary(t);
		Kind kind = t.kind;
		switch (kind) {
		case IDENT: {
			myExpression = new IdentExpression(firstToken);
			consume();
		}
			break;
		case INT_LIT: {
			myExpression = new IntLitExpression(firstToken);
			consume();
		}
			break;
		case KW_TRUE:
		case KW_FALSE: {
			myExpression = new BooleanLitExpression(firstToken);
			consume();
		}
			break;
		case KW_SCREENWIDTH:
		case KW_SCREENHEIGHT: {
			myExpression = new ConstantExpression(firstToken);
			consume();
		}
			break;
		case LPAREN: {
			consume();
			myExpression = expression();
			match(RPAREN);
		}
			break;
		default:
			// you will want to provide a more useful error message
			throw new SyntaxException("illegal factor---> " + t.getText() + " at " + t.pos + " present in Line No: "
					+ t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return myExpression;
	}

	Token relOp() throws SyntaxException {
		Kind kind = t.kind;
		Token firstToken = t;
		switch (kind) {
		case LT:
		case LE:
		case GT:
		case GE:
		case EQUAL:
		case NOTEQUAL: {
			consume();
		}
			break;
		default:
			// you will want to provide a more useful error message
			throw new SyntaxException("illegal Relational Operator--> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return firstToken;
	}

	Token weakOp() throws SyntaxException {
		Token firstToken = t;
		Kind kind = t.kind;
		switch (kind) {
		case PLUS:
		case MINUS:
		case OR: {
			consume();
		}
			break;
		default:
			// you will want to provide a more useful error message
			throw new SyntaxException("illegal Weak Operator---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return firstToken;
	}

	Token strongOp() throws SyntaxException {
		Token firstToken = t;
		Kind kind = t.kind;
		switch (kind) {
		case TIMES:
		case DIV:
		case AND:
		case MOD: {
			consume();
		}
			break;
		default:
			// you will want to provide a more useful error message
			throw new SyntaxException("illegal Strong Operator---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return firstToken;
	}

	// frameOp = KW_SHOW | KW_HIDE | KW_MOVE | KW_XLOC | KW_YLOC
	Token frameOp() throws SyntaxException {
		Token firstToken = t;
		Kind kind = t.kind;
		switch (kind) {
		case KW_SHOW:
		case KW_HIDE:
		case KW_MOVE:
		case KW_XLOC:
		case KW_YLOC: {
			consume();
		}
			break;
		default:
			// you will want to provide a more useful error message
			throw new SyntaxException("illegal Frame Operator---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return firstToken;
	}

	// filterOp = OP_BLUR | OP_GRAY | OP_CONVOLVE
	Token filterOp() throws SyntaxException {
		Kind kind = t.kind;
		Token firstToken = t;
		switch (kind) {
		case OP_BLUR:
		case OP_GRAY:
		case OP_CONVOLVE: {
			consume();
		}
			break;
		default:
			// you will want to provide a more useful error message
			throw new SyntaxException("illegal Filter Operator---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return firstToken;
	}

	// imageOp = OP_WIDTH | OP_HEIGHT | KW_SCALE
	Token imageOp() throws SyntaxException {
		Kind kind = t.kind;
		Token firstToken = t;
		switch (kind) {
		case OP_WIDTH:
		case OP_HEIGHT:
		case KW_SCALE: {
			consume();
		}
			break;
		default:
			// you will want to provide a more useful error message
			throw new SyntaxException("illegal Frame Operator---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return firstToken;
	}

	Token arrowOp() throws SyntaxException {
		Kind kind = t.kind;
		Token firstToken = t;
		switch (kind) {
		case ARROW:
		case BARARROW: {
			consume();
		}
			break;
		default:
			// you will want to provide a more useful error message
			throw new SyntaxException("illegal Arrow Operator---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return firstToken;
	}

	Token keywordOp() throws SyntaxException {
		Kind kind = t.kind;
		Token firstToken = t;
		switch (kind) {
		case KW_INTEGER:
		case KW_IMAGE:
		case KW_BOOLEAN:
		case KW_FRAME: {
			consume();
		}
			break;
		default:
			// you will want to provide a more useful error message
			throw new SyntaxException("illegal keyword Operator---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return firstToken;
	}

	// assign = IDENT ASSIGN expression
	AssignmentStatement assign() throws SyntaxException {
		Token firstToken = t;
		AssignmentStatement myAssignmentStatement = new AssignmentStatement(null, null, null);
		if (t.kind.equals(IDENT)) {
			IdentLValue myIdentValue = new IdentLValue(t);
			match(IDENT);
			match(ASSIGN);
			Expression e = expression();
			myAssignmentStatement = new AssignmentStatement(firstToken, myIdentValue, e);
		} else {
			throw new SyntaxException("illegal factor---> " + t.getText() + " at " + t.pos + " present in Line No: "
					+ t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return myAssignmentStatement;

	}

	// block = { ( dec | statement) * }
	Block block() throws SyntaxException {
		// if (t.kind.equals(LBRACE)) {
		ArrayList<Dec> myDecList = new ArrayList<>();
		ArrayList<Statement> myStatementList = new ArrayList<>();
		Block myBlock = new Block(null, null, null);
		Token firstToken = t;
		match(LBRACE);
		while (true) {
			if (t.kind.equals(KW_INTEGER) || t.kind.equals(KW_IMAGE) || t.kind.equals(KW_FRAME)
					|| t.kind.equals(KW_BOOLEAN)) {
				Dec myDec = dec();
				myDecList.add(myDec);
				myBlock = new Block(firstToken, myDecList, myStatementList);
			} else if (t.kind.equals(OP_SLEEP) || t.kind.equals(KW_IF) || t.kind.equals(KW_WHILE)
					|| t.kind.equals(IDENT) || t.kind.equals(KW_SHOW) || t.kind.equals(KW_HIDE)
					|| t.kind.equals(KW_MOVE) || t.kind.equals(KW_XLOC) || t.kind.equals(KW_YLOC)
					|| t.kind.equals(OP_WIDTH) || t.kind.equals(OP_HEIGHT) || t.kind.equals(KW_SCALE)
					|| t.kind.equals(OP_BLUR) || t.kind.equals(OP_GRAY) || t.kind.equals(OP_CONVOLVE)) {
				Statement myStatement = statement();
				myStatementList.add(myStatement);
				myBlock = new Block(firstToken, myDecList, myStatementList);
			} else {
				myBlock = new Block(firstToken, myDecList, myStatementList);
				match(RBRACE);
				return myBlock;
			}
		}
		/*
		 * } else { return; }
		 */

	}

	Program program() throws SyntaxException {
		ArrayList<ParamDec> myParamDecList = new ArrayList<>();
		Program myProgram = new Program(null, null, null);
		Token firstToken = t;
		Block myBlock = null;
		match(IDENT);
		if (t.kind.equals(LBRACE)) {
			myBlock = block();
		} else if (t.kind.equals(KW_URL) || t.kind.equals(KW_FILE) || t.kind.equals(KW_INTEGER)
				|| t.kind.equals(KW_BOOLEAN)) {
			ParamDec myfirstParamDec = paramDec();
			myParamDecList.add(myfirstParamDec);
			while (true) {
				if (t.kind.equals(COMMA)) {
					match(COMMA);
					if (t.kind.equals(KW_URL) || t.kind.equals(KW_FILE) || t.kind.equals(KW_INTEGER)
							|| t.kind.equals(KW_BOOLEAN)) {
						ParamDec mySubParamDec = paramDec();
						myParamDecList.add(mySubParamDec);
						myProgram = new Program(firstToken, myParamDecList, myBlock);
					} else {
						throw new SyntaxException(
								"illegal factor---> " + t.getText() + " at " + t.pos + " present in Line No: "
										+ t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
					}
				} else if (t.kind.equals(LBRACE)) {
					myBlock = block();
					myProgram = new Program(firstToken, myParamDecList, myBlock);
					break;
				} else {
					throw new SyntaxException(
							"illegal factor---> " + t.getText() + " at " + t.pos + " present in Line No: "
									+ t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
				}
			}

		} else {
			throw new SyntaxException("illegal factor---> " + t.getText() + " at " + t.pos + " present in Line No: "
					+ t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		myProgram = new Program(firstToken, myParamDecList, myBlock);
		return myProgram;
	}

	// paramDec = ( KW_URL | KW_FILE | KW_INTEGER | KW_BOOLEAN ) IDENT
	ParamDec paramDec() throws SyntaxException {
		Token firstToken = t;
		ParamDec paramDec = new ParamDec(null, null);
		if (t.kind.equals(KW_URL) || t.kind.equals(KW_FILE) || t.kind.equals(KW_INTEGER) || t.kind.equals(KW_BOOLEAN)) {
			consume();
			if (t.kind.equals(IDENT)) {
				Token ident = t;
				paramDec = new ParamDec(firstToken, ident);
			}
			match(IDENT);
		} else {
			throw new SyntaxException("illegal  elem---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return paramDec;
	}

	// dec = ( KW_INTEGER | KW_BOOLEAN | KW_IMAGE | KW_FRAME ) IDENT
	Dec dec() throws SyntaxException {
		Dec myDec = new Dec(null, null);
		Token firstToken = t;
		if (t.kind.equals(KW_INTEGER) || t.kind.equals(KW_BOOLEAN) || t.kind.equals(KW_IMAGE)
				|| t.kind.equals(KW_FRAME)) {
			keywordOp();
			if (t.kind.equals(IDENT)) {
				Token ident = t;
				myDec = new Dec(firstToken, ident);
			}
			match(IDENT);

		} else {
			throw new SyntaxException("illegal  elem---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return myDec;

	}

	// whileStatement = KW_WHILE ( expression ) block
	WhileStatement whileStatement() throws SyntaxException {
		WhileStatement myWhileStatement = new WhileStatement(null, null, null);
		Token firstToken = t;
		if (t.kind.equals(KW_WHILE)) {
			match(KW_WHILE);
			match(LPAREN);
			Expression myExpression = expression();
			match(RPAREN);
			Block myBlock = block();
			myWhileStatement = new WhileStatement(firstToken, myExpression, myBlock);
		} else {
			throw new SyntaxException("illegal factor---> " + t.getText() + " at " + t.pos + " present in Line No: "
					+ t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return myWhileStatement;
	}

	// ifStatement = KW_IF ( expression ) block
	IfStatement ifStatement() throws SyntaxException {
		IfStatement myIfStatement = new IfStatement(null, null, null);
		Token firstToken = t;
		if (t.kind.equals(KW_IF)) {
			match(KW_IF);
			match(LPAREN);
			Expression myExpression = expression();
			match(RPAREN);
			Block myBlock = block();
			myIfStatement = new IfStatement(firstToken, myExpression, myBlock);
		} else {
			throw new SyntaxException("illegal factor---> " + t.getText() + " at " + t.pos + " present in Line No: "
					+ t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return myIfStatement;

	}

	Statement statement() throws SyntaxException {
		Statement myStatement = null;
		Token firstToken = t;
		if (t.kind.equals(OP_SLEEP)) {
			consume();
			Expression e = expression();
			myStatement = new SleepStatement(firstToken, e);
			if (t.kind.equals(SEMI)) {
				consume();
			} else {
				throw new SyntaxException("illegal  elem---> " + t.getText() + " at token position " + t.pos
						+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
			}
		} else if (t.kind.equals(KW_WHILE)) {
			WhileStatement myWhileStatement = whileStatement();
			myStatement = new WhileStatement(firstToken, myWhileStatement.getE(), myWhileStatement.getB());
		} else if (t.kind.equals(KW_IF)) {
			IfStatement ifStatement = ifStatement();
			myStatement = new IfStatement(firstToken, ifStatement.getE(), ifStatement.getB());
		} else if (t.kind.equals(IDENT) && scanner.peek().kind.equals(ASSIGN)) {
			AssignmentStatement myAssignStatement = assign();
			myStatement = new AssignmentStatement(firstToken, myAssignStatement.getVar(), myAssignStatement.getE());
			if (t.kind.equals(SEMI)) {
				consume();
			} else {
				throw new SyntaxException("illegal  elem---> " + t.getText() + " at token position " + t.pos
						+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
			}
		} else if (t.kind.equals(IDENT) || t.kind.equals(KW_SHOW) || t.kind.equals(KW_HIDE) || t.kind.equals(KW_MOVE)
				|| t.kind.equals(KW_XLOC) || t.kind.equals(KW_YLOC) || t.kind.equals(OP_WIDTH)
				|| t.kind.equals(OP_HEIGHT) || t.kind.equals(KW_SCALE) || t.kind.equals(OP_BLUR)
				|| t.kind.equals(OP_GRAY) || t.kind.equals(OP_CONVOLVE)) {
			BinaryChain myChain = chain();
			myStatement = new BinaryChain(firstToken, myChain.getE0(), myChain.getArrow(), myChain.getE1());
			if (t.kind.equals(SEMI)) {
				consume();
			} else {
				throw new SyntaxException("illegal  elem---> " + t.getText() + " at token position " + t.pos
						+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
			}

		} else {
			return myStatement;
		}
		return myStatement;
	}

	// chain = chainElem arrowOp chainElem ( arrowOp chainElem)*
	BinaryChain chain() throws SyntaxException {
		Token firstToken = t;
		BinaryChain myBinaryChain = new BinaryChain(null, null, null, null);
		if (t.kind.equals(IDENT) || t.kind.equals(KW_SHOW) || t.kind.equals(KW_HIDE) || t.kind.equals(KW_MOVE)
				|| t.kind.equals(KW_XLOC) || t.kind.equals(KW_YLOC) || t.kind.equals(OP_WIDTH)
				|| t.kind.equals(OP_HEIGHT) || t.kind.equals(KW_SCALE) || t.kind.equals(OP_BLUR)
				|| t.kind.equals(OP_GRAY) || t.kind.equals(OP_CONVOLVE)) {
			ChainElem myFirstElem = chainElem();
			Token chainArrow = arrowOp();
			ChainElem mySecondElem = chainElem();
			myBinaryChain = new BinaryChain(firstToken, myFirstElem, chainArrow, mySecondElem);
			while (true) {
				if (t.kind.equals(ARROW) || t.kind.equals(BARARROW)) {
					Token myarrow = arrowOp();
					ChainElem mychainElem = chainElem();
					myBinaryChain = new BinaryChain(firstToken, myBinaryChain, myarrow, mychainElem);
				} else {
					return myBinaryChain;
				}
			}
		} else {
			throw new SyntaxException("illegal  elem---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
	}

	// chainElem = IDENT | filterOp arg | frameOp arg | imageOp arg
	ChainElem chainElem() throws SyntaxException {
		ChainElem mychainElem = null;
		Token firstToken = t;
		if (t.kind.equals(IDENT)) {
			mychainElem = new IdentChain(firstToken);
			consume();
		} else if (t.kind.equals(KW_SHOW) || t.kind.equals(KW_HIDE) || t.kind.equals(KW_MOVE) || t.kind.equals(KW_XLOC)
				|| t.kind.equals(KW_YLOC)) {
			frameOp();
			Tuple myArgFrame = arg();
			mychainElem = new FrameOpChain(firstToken, myArgFrame);
		} else if (t.kind.equals(OP_WIDTH) || t.kind.equals(OP_HEIGHT) || t.kind.equals(KW_SCALE)) {
			imageOp();
			Tuple myArgImage = arg();
			mychainElem = new ImageOpChain(firstToken, myArgImage);
		} else if (t.kind.equals(OP_BLUR) || t.kind.equals(OP_GRAY) || t.kind.equals(OP_CONVOLVE)) {
			filterOp();
			Tuple myArgFilter = arg();
			mychainElem = new FilterOpChain(firstToken, myArgFilter);
		} else {
			throw new SyntaxException("illegal  elem---> " + t.getText() + " at token position " + t.pos
					+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);
		}
		return mychainElem;
	}

	// arg = empty | ( expression ( , expression)* )
	Tuple arg() throws SyntaxException {
		Token firstToken = t;
		List<Expression> myExpressionList = new ArrayList<>();
		Tuple myTuple = new Tuple(t, myExpressionList);
		if (t.kind.equals(null) || t.kind.equals("")) {
			consume();
		} else if (t.kind.equals(LPAREN)) {
			match(LPAREN);
			Expression e = expression();
			myExpressionList.add(e);
			myTuple = new Tuple(firstToken, myExpressionList);
			// use comma
			while (true) {
				if (t.kind.equals(COMMA)) {
					consume();
					Expression eNew = expression();
					myExpressionList.add(eNew);
					myTuple = new Tuple(firstToken, myExpressionList);
				} else {
					match(RPAREN);
					return myTuple;
				}
			}
		} else {
			return myTuple;
		}
		return myTuple;
	}

	/**
	 * Checks whether the current token is the EOF token. If not, a
	 * SyntaxException is thrown.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.kind.equals(EOF)) {
			return t;
		}
		throw new SyntaxException("illegal  elem---> " + t.getText() + " at token position " + t.pos
				+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);

	}

	/**
	 * Checks if the current token has the given kind. If so, the current token
	 * is consumed and returned. If not, a SyntaxException is thrown.
	 * 
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
		if (t.kind.equals(kind)) {
			return consume();
		}
		throw new SyntaxException("illegal  elem---> " + t.getText() + " at token position " + t.pos
				+ " present in Line No: " + t.getLinePos().line + " Column No: " + t.getLinePos().posInLine);

	}

	/**
	 * Checks if the current token has one of the given kinds. If so, the
	 * current token is consumed and returned. If not, a SyntaxException is
	 * thrown.
	 * 
	 * * Precondition: for all given kinds, kind != EOF
	 * 
	 * @param kinds
	 *            list of kinds, matches any one
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind... kinds) throws SyntaxException {
		// TODO. Optional but handy
		return null; // replace this statement
	}

	/**
	 * Gets the next token and returns the consumed token.
	 * 
	 * Precondition: t.kind != EOF
	 * 
	 * @return
	 * 
	 */
	private Token consume() throws SyntaxException {
		Token tmp = t;
		t = scanner.nextToken();
		return tmp;
	}

}
