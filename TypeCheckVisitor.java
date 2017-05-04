package cop5556sp17;

import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.ASTVisitor;
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.AssignmentStatement;
import cop5556sp17.AST.BinaryChain;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.Block;
import cop5556sp17.AST.BooleanLitExpression;
import cop5556sp17.AST.Chain;
import cop5556sp17.AST.ChainElem;
import cop5556sp17.AST.ConstantExpression;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.Expression;
import cop5556sp17.AST.FilterOpChain;
import cop5556sp17.AST.FrameOpChain;
import cop5556sp17.AST.IdentChain;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.IdentLValue;
import cop5556sp17.AST.IfStatement;
import cop5556sp17.AST.ImageOpChain;
import cop5556sp17.AST.IntLitExpression;
import cop5556sp17.AST.ParamDec;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.SleepStatement;
import cop5556sp17.AST.Statement;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.AST.WhileStatement;

import java.util.ArrayList;
import java.util.Iterator;

import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.LinePos;
import cop5556sp17.Scanner.Token;
import static cop5556sp17.AST.Type.TypeName.*;
import static cop5556sp17.Scanner.Kind.ARROW;
import static cop5556sp17.Scanner.Kind.KW_HIDE;
import static cop5556sp17.Scanner.Kind.KW_MOVE;
import static cop5556sp17.Scanner.Kind.KW_SHOW;
import static cop5556sp17.Scanner.Kind.KW_XLOC;
import static cop5556sp17.Scanner.Kind.KW_YLOC;
import static cop5556sp17.Scanner.Kind.OP_BLUR;
import static cop5556sp17.Scanner.Kind.OP_CONVOLVE;
import static cop5556sp17.Scanner.Kind.OP_GRAY;
import static cop5556sp17.Scanner.Kind.OP_HEIGHT;
import static cop5556sp17.Scanner.Kind.OP_WIDTH;
import static cop5556sp17.Scanner.Kind.*;

public class TypeCheckVisitor implements ASTVisitor {

	@SuppressWarnings("serial")
	public static class TypeCheckException extends Exception {
		TypeCheckException(String message) {
			super(message);
		}
	}

	SymbolTable symtab = new SymbolTable();

	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {
		binaryChain.getE0().visit(this, arg);
		binaryChain.getE1().visit(this, arg);
		if (binaryChain.getE0().getType().equals(URL) && binaryChain.getArrow().kind.equals(ARROW)
				&& binaryChain.getE1().getType().equals(IMAGE)) {
			binaryChain.setType(IMAGE);
		} else if (binaryChain.getE0().getType().equals(FILE) && binaryChain.getArrow().kind.equals(ARROW)
				&& binaryChain.getE1().getType().equals(IMAGE)) {
			binaryChain.setType(IMAGE);
		} else if (binaryChain.getE0().getType().equals(FRAME) && binaryChain.getArrow().kind.equals(ARROW)
				&& (binaryChain.getE1().getClass().equals(FrameOpChain.class)
						&& (binaryChain.getE1().firstToken.kind.equals(KW_XLOC)
								|| binaryChain.getE1().firstToken.kind.equals(KW_YLOC)))) {
			binaryChain.setType(INTEGER);
		} else if (binaryChain.getE0().getType().equals(FRAME) && binaryChain.getArrow().kind.equals(ARROW)
				&& (binaryChain.getE1().getClass().equals(FrameOpChain.class)
						&& (binaryChain.getE1().firstToken.kind.equals(KW_SHOW)
								|| binaryChain.getE1().firstToken.kind.equals(KW_HIDE)
								|| binaryChain.getE1().firstToken.kind.equals(KW_MOVE)))) {
			binaryChain.setType(FRAME);
		} else if (binaryChain.getE0().getType().equals(IMAGE) && binaryChain.getArrow().kind.equals(ARROW)
				&& (binaryChain.getE1().getClass().equals(ImageOpChain.class)
						&& (binaryChain.getE1().firstToken.kind.equals(OP_WIDTH)
								|| binaryChain.getE1().firstToken.kind.equals(OP_HEIGHT)))) {
			binaryChain.setType(IMAGE);
		} else if (binaryChain.getE0().getType().equals(IMAGE) && binaryChain.getArrow().kind.equals(ARROW)
				&& (binaryChain.getE1().getType().equals(FRAME))) {
			binaryChain.setType(FRAME);
		} else if (binaryChain.getE0().getType().equals(IMAGE) && binaryChain.getArrow().kind.equals(ARROW)
				&& (binaryChain.getE1().getType().equals(FILE))) {
			binaryChain.setType(NONE);
		} else if (binaryChain.getE0().getType().equals(IMAGE)
				&& (binaryChain.getArrow().kind.equals(ARROW) || binaryChain.getArrow().kind.equals(BARARROW))
				&& (binaryChain.getE1().getClass().equals(FilterOpChain.class)
						&& (binaryChain.getE1().firstToken.kind.equals(OP_GRAY)
								|| binaryChain.getE1().firstToken.kind.equals(OP_BLUR)
								|| binaryChain.getE1().firstToken.kind.equals(OP_CONVOLVE)))) {
			binaryChain.setType(IMAGE);
		} else if (binaryChain.getE0().getType().equals(IMAGE) && binaryChain.getArrow().kind.equals(ARROW)
				&& (binaryChain.getE1().getClass().equals(ImageOpChain.class)
						&& (binaryChain.getE1().firstToken.kind.equals(KW_SCALE)))) {
			binaryChain.setType(IMAGE);
		} else if (binaryChain.getE0().getType().equals(IMAGE) && binaryChain.getArrow().kind.equals(ARROW)
				&& (binaryChain.getE1().getClass().equals(IdentChain.class))
				&& binaryChain.getE1().getType().equals(IMAGE)) {
			binaryChain.setType(IMAGE);
		} else if (binaryChain.getE0().getType().equals(INTEGER) && binaryChain.getArrow().kind.equals(ARROW)
				&& (binaryChain.getE1().getClass().equals(IdentChain.class))
				&& binaryChain.getE1().getType().equals(INTEGER)) {
			binaryChain.setType(INTEGER);
		} else {
			throw new TypeCheckException(
					"Illegal Chain construct starting with token " + binaryChain.getFirstToken().getText());
		}

		return binaryChain.getType();
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		binaryExpression.getE0().visit(this, arg);
		binaryExpression.getE1().visit(this, arg);
		if (binaryExpression.getE0().getType().equals(INTEGER) && binaryExpression.getOp().kind.equals(PLUS)
				&& binaryExpression.getE1().getType().equals(INTEGER)) {
			binaryExpression.setType(INTEGER);
		} else if (binaryExpression.getE0().getType().equals(INTEGER) && binaryExpression.getOp().kind.equals(MINUS)
				&& binaryExpression.getE1().getType().equals(INTEGER)) {
			binaryExpression.setType(INTEGER);
		} else if (binaryExpression.getE0().getType().equals(INTEGER) && binaryExpression.getOp().kind.equals(MOD)
				&& binaryExpression.getE1().getType().equals(INTEGER)) {
			binaryExpression.setType(INTEGER);
		} else if (binaryExpression.getE0().getType().equals(IMAGE) && binaryExpression.getOp().kind.equals(MOD)
				&& binaryExpression.getE1().getType().equals(INTEGER)) {
			binaryExpression.setType(IMAGE);
		} else if (binaryExpression.getE0().getType().equals(INTEGER)
				&& (binaryExpression.getOp().kind.equals(AND) || binaryExpression.getOp().kind.equals(OR))
				&& binaryExpression.getE1().getType().equals(INTEGER)) {
			binaryExpression.setType(BOOLEAN);
		} else if (binaryExpression.getE0().getType().equals(BOOLEAN)
				&& (binaryExpression.getOp().kind.equals(AND) || binaryExpression.getOp().kind.equals(OR))
				&& binaryExpression.getE1().getType().equals(BOOLEAN)) {
			binaryExpression.setType(BOOLEAN);
		} else if (binaryExpression.getE0().getType().equals(IMAGE)
				&& (binaryExpression.getOp().kind.equals(PLUS) || binaryExpression.getOp().kind.equals(MINUS))
				&& binaryExpression.getE1().getType().equals(IMAGE)) {
			binaryExpression.setType(IMAGE);
		} else if (binaryExpression.getE0().getType().equals(INTEGER)
				&& (binaryExpression.getOp().kind.equals(TIMES) || binaryExpression.getOp().kind.equals(DIV))
				&& binaryExpression.getE1().getType().equals(INTEGER)) {
			binaryExpression.setType(INTEGER);
		} else if (binaryExpression.getE0().getType().equals(IMAGE)
				&& (binaryExpression.getOp().kind.equals(TIMES) || binaryExpression.getOp().kind.equals(DIV))
				&& binaryExpression.getE1().getType().equals(INTEGER)) {
			binaryExpression.setType(IMAGE);
		} else if (binaryExpression.getE0().getType().equals(INTEGER) && (binaryExpression.getOp().kind.equals(TIMES))
				&& binaryExpression.getE1().getType().equals(IMAGE)) {
			binaryExpression.setType(IMAGE);
		} else if (binaryExpression.getE0().getType().equals(IMAGE) && (binaryExpression.getOp().kind.equals(TIMES))
				&& binaryExpression.getE1().getType().equals(INTEGER)) {
			binaryExpression.setType(IMAGE);
		} else if (binaryExpression.getE0().getType().equals(INTEGER)
				&& (binaryExpression.getOp().kind.equals(LT) || binaryExpression.getOp().kind.equals(GT)
						|| binaryExpression.getOp().kind.equals(LE) || binaryExpression.getOp().kind.equals(GE))
				&& binaryExpression.getE1().getType().equals(INTEGER)) {
			binaryExpression.setType(BOOLEAN);
		} else if (binaryExpression.getE0().getType().equals(BOOLEAN)
				&& (binaryExpression.getOp().kind.equals(LT) || binaryExpression.getOp().kind.equals(GT)
						|| binaryExpression.getOp().kind.equals(LE) || binaryExpression.getOp().kind.equals(GE))
				&& binaryExpression.getE1().getType().equals(BOOLEAN)) {
			binaryExpression.setType(BOOLEAN);
		} else if (binaryExpression.getE0().getType().equals(binaryExpression.getE1().getType())
				&& (binaryExpression.getOp().kind.equals(EQUAL) || binaryExpression.getOp().kind.equals(NOTEQUAL))) {
			binaryExpression.setType(BOOLEAN);
		} else {
			throw new TypeCheckException(
					"Illegal Expression construct starting with token" + binaryExpression.getFirstToken().getText());
		}
		return binaryExpression.getType();
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		symtab.enterScope();
		Iterator decListit = block.getDecs().iterator();
		while (decListit.hasNext()) {
			Dec blockDec = (Dec) decListit.next();
			blockDec.visit(this, arg);
		}
		Iterator stmtListit = block.getStatements().iterator();
		while (stmtListit.hasNext()) {
			Statement blockStmt = (Statement) stmtListit.next();
			blockStmt.visit(this, arg);
		}
		symtab.leaveScope();
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		booleanLitExpression.setType(BOOLEAN);
		return booleanLitExpression.getType();
	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		filterOpChain.getArg().visit(this, arg);
		if (filterOpChain.getArg().getExprList().size() == 0) {
			filterOpChain.setType(IMAGE);
		} else {
			throw new TypeCheckException("Illegal token" + filterOpChain.firstToken.getText());
		}

		return filterOpChain.getType();
	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		frameOpChain.getArg().visit(this, arg);
		Kind frameOpKind = frameOpChain.firstToken.kind;
		if (frameOpKind.equals(KW_SHOW) || frameOpKind.equals(KW_HIDE)) {
			if (frameOpChain.getArg().getExprList().size() == 0) {
				frameOpChain.setType(NONE);
			} else {
				throw new TypeCheckException("Illegal token" + frameOpChain.firstToken);
			}
		} else if (frameOpKind.equals(KW_XLOC) || frameOpKind.equals(KW_YLOC)) {
			if (frameOpChain.getArg().getExprList().size() == 0) {
				frameOpChain.setType(INTEGER);
			} else {
				throw new TypeCheckException("Illegal token" + frameOpChain.firstToken);
			}
		} else if (frameOpKind.equals(KW_MOVE)) {
			if (frameOpChain.getArg().getExprList().size() == 2) {
				frameOpChain.setType(NONE);
			} else {
				throw new TypeCheckException("Illegal token" + frameOpChain.firstToken.getText());
			}
		} else {
			throw new TypeCheckException("Illegal token" + frameOpChain.firstToken.getText());
		}

		return frameOpChain.getType();

	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {
		Dec SymDec = symtab.lookup(identChain.getFirstToken().getText());
		if (SymDec != null) {
			identChain.setType(SymDec.get_Type());
			identChain.setDec(SymDec);
		} else {
			throw new TypeCheckException("Illegal token" + identChain.firstToken.getText());
		}
		return identChain.getType();
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		Dec SymDec = symtab.lookup(identExpression.firstToken.getText());
		if (SymDec != null) {
			identExpression.setType(SymDec.get_Type());
			identExpression.setDec(SymDec);
		} else {
			throw new TypeCheckException("Declaration is null");
		}
		return identExpression.getType();
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		ifStatement.getE().visit(this, arg);
		ifStatement.getB().visit(this, arg);
		if (!ifStatement.getE().getType().equals(BOOLEAN)) {
			throw new TypeCheckException("expected boolean but was of type" + ifStatement.getE().getType());
		}
		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		intLitExpression.setType(INTEGER);
		return intLitExpression.getType();
	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		sleepStatement.getE().visit(this, arg);
		if (!sleepStatement.getE().getType().equals(INTEGER)) {
			throw new TypeCheckException("expected integer but was of type " + sleepStatement.getE().getType());
		}
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		whileStatement.getE().visit(this, arg);
		whileStatement.getB().visit(this, arg);
		if (!whileStatement.getE().getType().equals(BOOLEAN)) {
			throw new TypeCheckException("expected boolean but was of type " + whileStatement.getE().getType());
		}
		return null;
	}

	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		if (symtab.insert(declaration.getIdent().getText(), declaration)) {
			declaration.setType(declaration.type);
		} else
			throw new TypeCheckException("Cannot declare variables twice in same scope");
		return declaration.get_Type();
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		// program.visit(this, arg);
		symtab.enterScope();
		Iterator paramDecIt = program.getParams().iterator();
		while (paramDecIt.hasNext()) {
			ParamDec progParaDec = (ParamDec) paramDecIt.next();
			progParaDec.visit(this, arg);
		}
		program.getB().visit(this, arg);
		symtab.leaveScope();
		// System.out.println(symtab.toString());
		return null;
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {
		assignStatement.getE().visit(this, arg);
		assignStatement.getVar().visit(this, arg);
		if (!assignStatement.getE().getType().equals(assignStatement.getVar().getType())) {
			throw new TypeCheckException(
					"expected both expression and identLval to be of same type but expression is of type "
							+ assignStatement.getE().getType() + "and identLVal is of type "
							+ assignStatement.var.getType());
		}
		return null;
	}

	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		Dec SymDec = symtab.lookup(identX.firstToken.getText());
		if (SymDec != null) {
			identX.setDec(SymDec);
			// identX.getDec().visit(this, arg);
			identX.setType(SymDec.get_Type());
		} else {
			throw new TypeCheckException("Declaration is null");
		}
		return identX.getType();
	}

	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
		if (symtab.insert(paramDec.getIdent().getText(), paramDec)) {
			paramDec.setType(paramDec.type);
		} else
			throw new TypeCheckException("Cannot declare variables twice in same scope");
		return paramDec.get_Type();
	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		constantExpression.setType(INTEGER);
		return constantExpression.getType();
	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {
		Kind imageOpKind = imageOpChain.firstToken.kind;
		imageOpChain.getArg().visit(this, arg);
		if (imageOpKind.equals(OP_WIDTH) || imageOpKind.equals(OP_HEIGHT)) {
			if (imageOpChain.getArg().getExprList().size() == 0) {
				imageOpChain.setType(INTEGER);
			} else {
				throw new TypeCheckException("Illegal token" + imageOpChain.firstToken.getText());
			}
		} else if (imageOpKind.equals(KW_SCALE)) {
			if (imageOpChain.getArg().getExprList().size() == 1) {
				imageOpChain.setType(IMAGE);
			} else {
				throw new TypeCheckException("Illegal token" + imageOpChain.firstToken.getText());
			}
		} else {
			throw new TypeCheckException("Illegal token" + imageOpChain.firstToken.getText());
		}

		return imageOpChain.getType();
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		Iterator tupleIt = tuple.getExprList().iterator();

		while (tupleIt.hasNext()) {
			Expression tupleExpr = (Expression) tupleIt.next();
			tupleExpr.visit(this, arg);
			if (!tupleExpr.getType().equals(INTEGER)) {
				throw new TypeCheckException(
						"Expected expression to be of the type INTEGER but is" + tupleExpr.getType());
			}
		}
		return null;
	}

}
