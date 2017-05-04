package cop5556sp17;

import static cop5556sp17.AST.Type.TypeName.FILE;
import static cop5556sp17.AST.Type.TypeName.IMAGE;
import static cop5556sp17.AST.Type.TypeName.URL;
import static cop5556sp17.Scanner.Kind.KW_SCALE;
import static cop5556sp17.Scanner.Kind.KW_SCREENHEIGHT;
import static cop5556sp17.Scanner.Kind.KW_SCREENWIDTH;
import static cop5556sp17.Scanner.Kind.OP_BLUR;
import static cop5556sp17.Scanner.Kind.OP_CONVOLVE;
import static cop5556sp17.Scanner.Kind.OP_GRAY;
import static cop5556sp17.Scanner.Kind.OP_HEIGHT;
import static cop5556sp17.Scanner.Kind.OP_WIDTH;

import java.util.ArrayList;
import java.util.Iterator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cop5556sp17.Scanner.Kind;
import cop5556sp17.AST.ASTVisitor;
import cop5556sp17.AST.AssignmentStatement;
import cop5556sp17.AST.BinaryChain;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.Block;
import cop5556sp17.AST.BooleanLitExpression;
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
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.AST.WhileStatement;

public class CodeGenVisitor implements ASTVisitor, Opcodes {

	/**
	 * @param DEVEL
	 *            used as parameter to genPrint and genPrintTOS
	 * @param GRADE
	 *            used as parameter to genPrint and genPrintTOS
	 * @param sourceFileName
	 *            name of source file, may be null.
	 */
	public CodeGenVisitor(boolean DEVEL, boolean GRADE, String sourceFileName) {
		super();
		this.DEVEL = DEVEL;
		this.GRADE = GRADE;
		this.sourceFileName = sourceFileName;
	}

	ClassWriter cw;
	String className;
	String classDesc;
	String sourceFileName;

	MethodVisitor mv; // visitor of method currently under construction

	/** Indicates whether genPrint and genPrintTOS should generate code. */
	final boolean DEVEL;
	final boolean GRADE;
	private int slotNumber = 0;
	private int noOfArgs = 0;

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		className = program.getName();
		classDesc = "L" + className + ";";
		String sourceFileName = (String) arg;
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object",
				new String[] { "java/lang/Runnable" });
		cw.visitSource(sourceFileName, null);

		// generate constructor code
		// get a MethodVisitor
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "([Ljava/lang/String;)V", null, null);
		mv.visitCode();
		// Create label at start of code
		Label constructorStart = new Label();
		mv.visitLabel(constructorStart);
		// this is for convenience during development--you can see that the code
		// is doing something.
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering <init>");
		// generate code to call superclass constructor
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		// visit parameter decs to add each as field to the class
		// pass in mv so decs can add their initialization code to the
		// constructor.
		ArrayList<ParamDec> params = program.getParams();
		for (ParamDec dec : params)
			dec.visit(this, mv);
		mv.visitInsn(RETURN);
		// create label at end of code
		Label constructorEnd = new Label();
		mv.visitLabel(constructorEnd);
		// finish up by visiting local vars of constructor
		// the fourth and fifth arguments are the region of code where the local
		// variable is defined as represented by the labels we inserted.
		mv.visitLocalVariable("this", classDesc, null, constructorStart, constructorEnd, 0);
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, constructorStart, constructorEnd, 1);
		// indicates the max stack size for the method.
		// because we used the COMPUTE_FRAMES parameter in the classwriter
		// constructor, asm
		// will do this for us. The parameters to visitMaxs don't matter, but
		// the method must
		// be called.
		mv.visitMaxs(1, 1);
		// finish up code generation for this method.
		mv.visitEnd();
		// end of constructor

		// create main method which does the following
		// 1. instantiate an instance of the class being generated, passing the
		// String[] with command line arguments
		// 2. invoke the run method.
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mv.visitCode();
		Label mainStart = new Label();
		mv.visitLabel(mainStart);
		// this is for convenience during development--you can see that the code
		// is doing something.
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering main");
		mv.visitTypeInsn(NEW, className);
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "([Ljava/lang/String;)V", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, className, "run", "()V", false);
		mv.visitInsn(RETURN);
		Label mainEnd = new Label();
		mv.visitLabel(mainEnd);
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);
		mv.visitLocalVariable("instance", classDesc, null, mainStart, mainEnd, 1);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		// create run method
		mv = cw.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
		mv.visitCode();
		Label startRun = new Label();
		mv.visitLabel(startRun);
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering run");
		program.getB().visit(this, null);
		mv.visitInsn(RETURN);
		Label endRun = new Label();
		mv.visitLabel(endRun);
		mv.visitLocalVariable("this", classDesc, null, startRun, endRun, 0);
		// TODO visit the local variables

		// mv.visitLocalVariable("args", "[Ljava/lang/String;", null, startRun,
		// endRun, 1);
		Iterator DecIt = program.getB().getDecs().iterator();
		while (DecIt.hasNext()) {
			Dec dec = (Dec) DecIt.next();
			mv.visitLocalVariable(dec.getIdent().getText(), dec.get_Type().getJVMTypeDesc(), null, startRun, endRun,
					dec.getSlot());
		}
		mv.visitMaxs(1, 1);
		mv.visitEnd(); // end of run method

		cw.visitEnd();// end of class

		// generate classfile and return it
		return cw.toByteArray();
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {
		assignStatement.getE().visit(this, arg);
		CodeGenUtils.genPrint(DEVEL, mv, "\nassignment: " + assignStatement.var.getText() + "=");
		CodeGenUtils.genPrintTOS(GRADE, mv, assignStatement.getE().getType());
		assignStatement.getVar().visit(this, arg);
		return null;
	}

	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {
		binaryChain.getE0().setLeft(true); // TODO check setLeft
		if (binaryChain.getE0() instanceof FilterOpChain)
			arg = binaryChain.getArrow().kind;
		binaryChain.getE0().visit(this, arg);
		// TypeName leftExpTypeName = binaryChain.getE0().getType();
		
		if (binaryChain.getE1() instanceof FilterOpChain)
			arg = binaryChain.getArrow().kind;
		binaryChain.getE1().visit(this, arg);
		//mv.visitInsn(DUP);

		return null;

	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		binaryExpression.getE0().visit(this, arg);
		binaryExpression.getE1().visit(this, arg);
		Kind opKind = binaryExpression.getOp().kind;
		Label BinaryStart = new Label();
		Label BinaryEnd = new Label();
		switch (opKind) {
		case PLUS:
			if (binaryExpression.getE0().getType().equals(IMAGE) && binaryExpression.getE1().getType().equals(IMAGE)) {
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "add", PLPRuntimeImageOps.addSig, false);
			} else {
				mv.visitInsn(IADD);
			}
			break;
		case MINUS:
			if (binaryExpression.getE0().getType().equals(IMAGE) && binaryExpression.getE1().getType().equals(IMAGE)) {
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "sub", PLPRuntimeImageOps.subSig, false);
				// mv.visitInsn(DUP);
			} else {
				mv.visitInsn(ISUB);
			}
			break;
		case TIMES:
			if (binaryExpression.getE0().getType().equals(IMAGE)
					&& binaryExpression.getE1().getType().equals(TypeName.INTEGER)) {
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "mul", PLPRuntimeImageOps.mulSig, false);
			} else if (binaryExpression.getE0().getType().equals(TypeName.INTEGER)
					&& binaryExpression.getE1().getType().equals(IMAGE)) {
				mv.visitInsn(SWAP);
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "mul", PLPRuntimeImageOps.divSig, false);
			} else {
				mv.visitInsn(IMUL);
			}
			break;
		case DIV:
			if (binaryExpression.getE0().getType().equals(IMAGE)
					&& binaryExpression.getE1().getType().equals(TypeName.INTEGER)) {
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "div", PLPRuntimeImageOps.divSig, false);
			} else if (binaryExpression.getE0().getType().equals(TypeName.INTEGER)
					&& binaryExpression.getE1().getType().equals(IMAGE)) {
				mv.visitInsn(SWAP);
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "div", PLPRuntimeImageOps.divSig, false);
			} else {
				mv.visitInsn(IDIV);
			}
			break;
		case MOD:
			if (binaryExpression.getE0().getType().equals(IMAGE)
					&& binaryExpression.getE1().getType().equals(TypeName.INTEGER)) {
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "mod", PLPRuntimeImageOps.modSig, false);
			} else if (binaryExpression.getE0().getType().equals(TypeName.INTEGER)
					&& binaryExpression.getE1().getType().equals(TypeName.INTEGER)) {
				mv.visitInsn(IREM);
			} else {
				mv.visitInsn(IREM);
			}
			break;
		case AND:
			mv.visitInsn(IAND);
			break;
		case OR:
			mv.visitInsn(IOR);
			break;
		case LT:
			mv.visitJumpInsn(IF_ICMPLT, BinaryStart);
			mv.visitLdcInsn(false);
			mv.visitJumpInsn(GOTO, BinaryEnd);
			mv.visitLabel(BinaryStart);
			mv.visitLdcInsn(true);
			mv.visitLabel(BinaryEnd);
			break;
		case GT:
			mv.visitJumpInsn(IF_ICMPGT, BinaryStart);
			mv.visitLdcInsn(false);
			mv.visitJumpInsn(GOTO, BinaryEnd);
			mv.visitLabel(BinaryStart);
			mv.visitLdcInsn(true);
			mv.visitLabel(BinaryEnd);
			break;
		case LE:
			mv.visitJumpInsn(IF_ICMPLE, BinaryStart);
			mv.visitLdcInsn(false);
			mv.visitJumpInsn(GOTO, BinaryEnd);
			mv.visitLabel(BinaryStart);
			mv.visitLdcInsn(true);
			mv.visitLabel(BinaryEnd);
			break;
		case GE:
			mv.visitJumpInsn(IF_ICMPGE, BinaryStart);
			mv.visitLdcInsn(false);
			mv.visitJumpInsn(GOTO, BinaryEnd);
			mv.visitLabel(BinaryStart);
			mv.visitLdcInsn(true);
			mv.visitLabel(BinaryEnd);
			break;
		case EQUAL:
			if ((binaryExpression.getE0().getType().isType(TypeName.INTEGER)
					&& binaryExpression.getE1().getType().isType(TypeName.INTEGER))
					|| (binaryExpression.getE0().getType().isType(TypeName.BOOLEAN)
							&& binaryExpression.getE1().getType().isType(TypeName.BOOLEAN))) {
				mv.visitJumpInsn(IF_ICMPEQ, BinaryStart);
			} else {
				mv.visitJumpInsn(IF_ACMPEQ, BinaryStart);
			}
			mv.visitLdcInsn(false);
			mv.visitJumpInsn(GOTO, BinaryEnd);
			mv.visitLabel(BinaryStart);
			mv.visitLdcInsn(true);
			mv.visitLabel(BinaryEnd);
			break;
		case NOTEQUAL:
			if ((binaryExpression.getE0().getType().isType(TypeName.INTEGER)
					&& binaryExpression.getE1().getType().isType(TypeName.INTEGER))
					|| (binaryExpression.getE0().getType().isType(TypeName.BOOLEAN)
							&& binaryExpression.getE1().getType().isType(TypeName.BOOLEAN))) {
				mv.visitJumpInsn(IF_ICMPNE, BinaryStart);
			} else {
				mv.visitJumpInsn(IF_ACMPNE, BinaryStart);
			}
			mv.visitLdcInsn(false);
			mv.visitJumpInsn(GOTO, BinaryEnd);
			mv.visitLabel(BinaryStart);
			mv.visitLdcInsn(true);
			mv.visitLabel(BinaryEnd);
			break;
		default:
			break;
		}

		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		Label beginScope = new Label();
		mv.visitLabel(beginScope);
		Label endScope = new Label();
		Iterator decListIt = block.getDecs().iterator();
		Iterator statementListIt = block.getStatements().iterator();
		while (decListIt.hasNext()) {
			Dec dec = (Dec) decListIt.next();
			// mv.visitInsn(ACONST_NULL);
			// mv.visitLocalVariable(dec.getIdent().getText(),
			// dec.get_Type().getJVMTypeDesc(), null, beginScope, endScope,
			// dec.getSlot());
			dec.visit(this, arg);

		}
		while (statementListIt.hasNext()) {
			Statement statement = (Statement) statementListIt.next();
			if (statement instanceof AssignmentStatement) {
				if (((AssignmentStatement) statement).getVar().getDec() instanceof ParamDec) {
					mv.visitVarInsn(ALOAD, 0);
					// Dec dec = ((AssignmentStatement)
					// statement).getVar().getDec();
					// mv.visitLocalVariable(dec.getIdent().getText(),
					// dec.get_Type().getJVMTypeDesc(), null, beginScope,
					// endScope,
					// dec.getSlot());
				}
			}

			statement.visit(this, arg);
			if (statement instanceof BinaryChain) {
				mv.visitInsn(POP);
			}

		}
		mv.visitLabel(endScope);
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		mv.visitLdcInsn(booleanLitExpression.getValue());
		return null;
	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		// PLPRuntimeFrame.getScreenWidth or PLPRuntimeFrame.getScreenHeight.
		if (constantExpression.firstToken.kind.equals(KW_SCREENHEIGHT)) {
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "getScreenHeight",
					PLPRuntimeFrame.getScreenHeightSig, false);
		} else if (constantExpression.firstToken.kind.equals(KW_SCREENWIDTH)) {
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "getScreenWidth",
					PLPRuntimeFrame.getScreenWidthSig, false);
		}
		return null;
	}

	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		declaration.setSlot(slotNumber++);
		TypeName typeName = declaration.get_Type();
		switch (typeName) {
		case IMAGE:
			// mv.visitTypeInsn(NEW, "java/awt/image/BufferedImage");
			mv.visitInsn(ACONST_NULL);
			mv.visitVarInsn(ASTORE, slotNumber - 1);
			break;
		case FRAME:
			mv.visitInsn(ACONST_NULL);
			mv.visitVarInsn(ASTORE, slotNumber - 1);
			break;
		default:
			// mv.visitInsn(ACONST_NULL);
			break;
		}
		return null;
	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		filterOpChain.getArg().visit(this, arg);
		if (filterOpChain.getFirstToken().kind.equals(OP_BLUR)) {
			mv.visitInsn(ACONST_NULL);
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFilterOps.JVMName, "blurOp", PLPRuntimeFilterOps.opSig, false);
		} else if (filterOpChain.getFirstToken().kind.equals(OP_CONVOLVE)) {
			mv.visitInsn(ACONST_NULL);
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFilterOps.JVMName, "convolveOp", PLPRuntimeFilterOps.opSig,
					false);
		} else if (filterOpChain.getFirstToken().kind.equals(OP_GRAY)) {
			if (arg.equals(Kind.BARARROW)) {
				mv.visitInsn(DUP);
				// mv.visitMethodInsn(INVOKESTATIC,
				// "cop5556sp17/PLPRuntimeImageOps", "copyImage",
				// "(Ljava/awt/image/BufferedImage;)Ljava/awt/image/BufferedImage;",
				// false);
				// mv.visitInsn(SWAP);
			} else {
				mv.visitInsn(ACONST_NULL);
			}
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFilterOps.JVMName, "grayOp", PLPRuntimeFilterOps.opSig, false);
		}
		// mv.visitInsn(SWAP);
		// mv.visitInsn(POP);
		return null;
	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		frameOpChain.getArg().visit(this, arg);
		Kind frameOpKind = frameOpChain.firstToken.kind;
		
		switch (frameOpKind) {
		case KW_SHOW:
			mv.visitMethodInsn(INVOKEVIRTUAL, "cop5556sp17/PLPRuntimeFrame", "showImage",
					"()Lcop5556sp17/PLPRuntimeFrame;", false);
			mv.visitInsn(DUP);
			mv.visitInsn(POP);
			break;
		case KW_HIDE:
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "hideImage", PLPRuntimeFrame.hideImageDesc,
					false);
			mv.visitInsn(DUP);
			break;
		case KW_XLOC:
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "getXVal", PLPRuntimeFrame.getXValDesc,
					false);
			mv.visitInsn(DUP);
			break;
		case KW_YLOC:
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "getYVal", PLPRuntimeFrame.getYValDesc,
					false);
			mv.visitInsn(DUP);
			break;
		case KW_MOVE:
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "moveFrame", PLPRuntimeFrame.moveFrameDesc,
					false);
			mv.visitInsn(DUP);
			break;
		default:
			break;
		}

		return null;
	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {
		TypeName type = identChain.getType();
		Dec identDec = identChain.getDec();

		if (identChain.getDec() instanceof ParamDec) {
			//mv.visitVarInsn(ALOAD, 0);
			if (identChain.isLeft()) {
				if (type.equals(FILE)) {
					 mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETFIELD, className, identChain.getDec().getIdent().getText(),
							FILE.getJVMTypeDesc());
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "readFromFile",
							PLPRuntimeImageIO.readFromFileDesc, false);
					// mv.visitInsn(DUP);
				} else if (type.equals(URL)) {
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETFIELD, className, identChain.getDec().getIdent().getText(),
							URL.getJVMTypeDesc());
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "readFromURL",
							PLPRuntimeImageIO.readFromURLSig, false);
					// mv.visitVarInsn(ALOAD, identDec.getSlot());
					// mv.visitInsn(DUP);
				} else {
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETFIELD, className, identChain.getDec().getIdent().getText(),
							identChain.getDec().get_Type().getJVMTypeDesc());
					//mv.visitInsn(DUP);
				}
			} else {
				if (type.equals(FILE)) {
					mv.visitVarInsn(ALOAD, 0);
					//mv.visitInsn(DUP);
					mv.visitFieldInsn(GETFIELD, className, identChain.getDec().getIdent().getText(),
							FILE.getJVMTypeDesc());
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "write",
							PLPRuntimeImageIO.writeImageDesc, false);
					mv.visitInsn(DUP);
					mv.visitVarInsn(ASTORE, identDec.getSlot());
				} else {
					//mv.visitFieldInsn(PUTFIELD, className, identChain.getDec().getIdent().getText(),
							//identChain.getDec().get_Type().getJVMTypeDesc());
					mv.visitInsn(DUP); // TEST
					mv.visitVarInsn(ALOAD, 0);
					mv.visitInsn(SWAP);
					mv.visitFieldInsn(PUTFIELD, className, identChain.getDec()
							.getIdent().getText(), identChain.getDec()
							.get_Type().getJVMTypeDesc());

				}
			}
		} else {
			switch (type) {
			case INTEGER:
			case BOOLEAN:
				if (identChain.isLeft()) {
					mv.visitVarInsn(ILOAD, identDec.getSlot());
				} else {
					mv.visitInsn(DUP);
					mv.visitVarInsn(ISTORE, identDec.getSlot());
					//mv.visitVarInsn(ILOAD, identDec.getSlot());
				}
				break;
			case IMAGE:
				if (identChain.isLeft()) {
					mv.visitVarInsn(ALOAD, identDec.getSlot());
				} else {
					// mv.visitInsn(DUP);
					 mv.visitInsn(DUP);
					mv.visitVarInsn(ASTORE, identDec.getSlot());
					//mv.visitVarInsn(ALOAD, identDec.getSlot());
				}
				break;
			case FILE:
				if (identChain.isLeft()) {
					mv.visitVarInsn(ALOAD, identDec.getSlot());
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "readFromFile",
							PLPRuntimeImageIO.readFromFileDesc, false);
				} else {
					
					mv.visitVarInsn(ALOAD, identDec.getSlot());
					
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "write",
							PLPRuntimeImageIO.writeImageDesc, false);
					mv.visitInsn(DUP);
					///mv.visitVarInsn(ALOAD, identDec.getSlot());
				}
				break;
			case URL:
				if (identChain.isLeft()) {
					mv.visitVarInsn(ALOAD, identDec.getSlot());
				} else {
					mv.visitVarInsn(ALOAD, identDec.getSlot());
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "readFromURL",
							PLPRuntimeImageIO.readFromURLSig, false);
					mv.visitVarInsn(ALOAD, identDec.getSlot());
				}
				break;
			case FRAME:
				if (identChain.isLeft()) {
					mv.visitVarInsn(ALOAD, identDec.getSlot());
				} else {
					
					mv.visitVarInsn(ALOAD, identDec.getSlot());
					mv.visitMethodInsn(INVOKESTATIC, "cop5556sp17/PLPRuntimeFrame", "createOrSetFrame",
							"(Ljava/awt/image/BufferedImage;Lcop5556sp17/PLPRuntimeFrame;)Lcop5556sp17/PLPRuntimeFrame;",
							false);
					mv.visitInsn(DUP);
					mv.visitVarInsn(ASTORE, identDec.getSlot());
					//mv.visitVarInsn(ALOAD, identDec.getSlot());
				}
				break;
			default:
				break;

			}

		}

		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		Dec identExpressionDec = identExpression.getDec();
		if (identExpressionDec instanceof ParamDec) {
			mv.visitVarInsn(ALOAD, 0);
			if (identExpressionDec.get_Type() == TypeName.INTEGER) {
				mv.visitFieldInsn(GETFIELD, className, identExpression.getFirstToken().getText(),
						TypeName.INTEGER.getJVMTypeDesc());
			} else if (identExpressionDec.get_Type() == TypeName.BOOLEAN) {
				mv.visitFieldInsn(GETFIELD, className, identExpression.getFirstToken().getText(),
						TypeName.BOOLEAN.getJVMTypeDesc());
			} else if (identExpressionDec.get_Type() == TypeName.FILE) {
				mv.visitFieldInsn(GETFIELD, className, identExpression.getFirstToken().getText(),
						TypeName.FILE.getJVMTypeDesc());
			} else if (identExpressionDec.get_Type() == TypeName.URL) {
				mv.visitFieldInsn(GETFIELD, className, identExpression.getFirstToken().getText(),
						TypeName.URL.getJVMTypeDesc());
			}
		} else {

			if (identExpressionDec.get_Type() == TypeName.INTEGER
					|| identExpressionDec.get_Type() == TypeName.BOOLEAN) {
				mv.visitVarInsn(ILOAD, identExpressionDec.getSlot());
			} else {
				mv.visitVarInsn(ALOAD, identExpressionDec.getSlot());
			}
		}
		return null;
	}

	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		Dec identDec = identX.getDec();
		if (identDec instanceof ParamDec) {
			// mv.visitVarInsn(ALOAD, 0);
			if (identDec.get_Type() == TypeName.INTEGER) {
				mv.visitFieldInsn(PUTFIELD, className, identX.getText(), TypeName.INTEGER.getJVMTypeDesc());
			} else if (identDec.get_Type() == TypeName.BOOLEAN) {
				mv.visitFieldInsn(PUTFIELD, className, identX.getText(), TypeName.BOOLEAN.getJVMTypeDesc());
			} else if (identDec.get_Type() == TypeName.FILE) {
				mv.visitFieldInsn(PUTFIELD, className, identX.getText(), TypeName.FILE.getJVMTypeDesc());
			} else if (identDec.get_Type() == TypeName.FRAME) {
				mv.visitFieldInsn(PUTFIELD, className, identX.getText(), TypeName.FRAME.getJVMTypeDesc());
			} else if (identDec.get_Type() == TypeName.URL) {
				mv.visitFieldInsn(PUTFIELD, className, identX.getText(), TypeName.URL.getJVMTypeDesc());
			} else if (identDec.get_Type() == TypeName.IMAGE) {
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "copyImage",
						PLPRuntimeImageOps.copyImageSig, false);
				mv.visitFieldInsn(PUTFIELD, className, identX.getText(), TypeName.IMAGE.getJVMTypeDesc());
			}
		} else {
			if (identDec.get_Type() == TypeName.INTEGER || identDec.get_Type() == TypeName.BOOLEAN) {
				mv.visitVarInsn(ISTORE, identDec.getSlot());
			} else if (identDec.get_Type() == TypeName.IMAGE) {
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "copyImage",
						PLPRuntimeImageOps.copyImageSig, false);
				mv.visitVarInsn(ASTORE, identDec.getSlot());
			} else if (identDec.get_Type() == TypeName.FRAME || identDec.get_Type() == TypeName.URL
					|| identDec.get_Type() == TypeName.FILE) {
				mv.visitVarInsn(ASTORE, identDec.getSlot());
			}
		}
		return null;

	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		Label after = new Label();
		ifStatement.getE().visit(this, arg);
		mv.visitJumpInsn(IFEQ, after);
		ifStatement.getB().visit(this, arg);
		mv.visitLabel(after);
		return null;
	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {
		imageOpChain.getArg().visit(this, arg);
		
		if (imageOpChain.getFirstToken().kind.equals(KW_SCALE)) {
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "scale", PLPRuntimeImageOps.scaleSig, false);
			
		} else if (imageOpChain.getFirstToken().kind.equals(OP_HEIGHT)) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/awt/image/BufferedImage", "getHeight",
					PLPRuntimeImageOps.getHeightSig, false);
		} else if (imageOpChain.getFirstToken().kind.equals(OP_WIDTH)) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/awt/image/BufferedImage", "getWidth",
					PLPRuntimeImageOps.getWidthSig, false);
		}
		// todo for runtimeImageOps
		//mv.visitInsn(DUP);
		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		mv.visitLdcInsn(intLitExpression.value);
		return null;
	}

	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
		TypeName paramDecType = paramDec.get_Type();

		FieldVisitor fv = cw.visitField(ACC_PUBLIC, paramDec.getIdent().getText(), paramDecType.getJVMTypeDesc(), null,
				null);
		fv.visitEnd();

		paramDec.setSlot(slotNumber++);

		if (paramDec.get_Type() == TypeName.INTEGER || paramDec.get_Type() == TypeName.BOOLEAN) {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(noOfArgs++);
			mv.visitInsn(AALOAD);

			if (paramDec.get_Type().equals(TypeName.INTEGER)) {
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);

			} else {
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);

			}
		} else if (paramDec.get_Type() == TypeName.FILE) {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "java/io/File");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(noOfArgs);
			noOfArgs++;
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
		} else if (paramDec.get_Type() == TypeName.URL) {
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(noOfArgs++);
			// mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "getURL", PLPRuntimeImageIO.getURLSig, false);
		}
		mv.visitFieldInsn(PUTFIELD, className, paramDec.getIdent().getText(), paramDec.get_Type().getJVMTypeDesc());
		return null;
	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		sleepStatement.getE().visit(this, arg);
		mv.visitInsn(I2L);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "sleep", "(J)V", false);
		return null;
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		Iterator<Expression> tupleListIt = tuple.getExprList().iterator();
		while (tupleListIt.hasNext()) {
			Expression myExp = (Expression) tupleListIt.next();
			myExp.visit(this, arg);
		}

		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		Label guard = new Label();
		Label body = new Label();
		mv.visitJumpInsn(GOTO, guard);
		mv.visitLabel(body);
		whileStatement.getB().visit(this, arg);
		mv.visitLabel(guard);
		whileStatement.getE().visit(this, arg);
		mv.visitJumpInsn(IFNE, body);
		return null;
	}

}
