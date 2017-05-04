package cop5556sp17;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.Program;

public class CodeGenVisitorTest {

	static final boolean doPrint = true;

	static void show(Object s) {
		if (doPrint) {
			System.out.println(s);
		}
	}

	boolean devel = false;
	boolean grade = true;
	@Rule

	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void emptyProg() throws Exception {
		// scan, parse, and type check the program
		String progname = "emptyProg";
		String input = progname + "  {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);

		// generate code
		CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
		byte[] bytecode = (byte[]) program.visit(cv, null);

		// output the generated bytecode
		CodeGenUtils.dumpBytecode(bytecode);

		// write byte code to file
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("wrote classfile to " + classFileName);

		// directly execute bytecode
		String[] args = new String[0]; // create command line argument array to
										// initialize params, none in this case
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();
	}

	@Test
	public void myTestCase1() throws Exception {
		// scan, parse, and type check the program
		String progname = "myProg";
		String input = progname + "test10 url x, integer i {}";

		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);
		// generate code
		CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
		byte[] bytecode = (byte[]) program.visit(cv, null);
		// output the generated bytecode
		CodeGenUtils.dumpBytecode(bytecode);
		// write byte code to file
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("write to classfile" + classFileName);
		// directly execute bytecode
		String[] args = new String[] { "http://www.google.com", "1" }; // create
																		// command
																		// line
																		// argument
		// array to initialize
		// params, none in this case
		// args[0] = "5";
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();

	}

	@Test
	public void myTestCase4() throws Exception {
		// scan, parse, and type check the program
		String progname = "myProg";
		String input = progname + "assignParamNLocal integer i { i<-1;}";

		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);
		// generate code
		CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
		byte[] bytecode = (byte[]) program.visit(cv, null);
		// output the generated bytecode
		CodeGenUtils.dumpBytecode(bytecode);
		// write byte code to file
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("write to classfile" + classFileName);
		// directly execute bytecode
		String[] args = new String[] { "1" }; // create command line argument
												// array to initialize
												// params, none in this case
		// args[0] = "5";
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();

	}

	@Test
	public void myTestCase2() throws Exception {
		// scan, parse, and type check the program
		String progname = "myProgram2";
		String input = progname + "integer {boolean f " + " integer some integer something "
				+ " some <- (((4-2) * (4/2))+ 9); " + " }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);
		// generate code
		CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
		byte[] bytecode = (byte[]) program.visit(cv, null);
		// output the generated bytecode
		CodeGenUtils.dumpBytecode(bytecode);
		// write byte code to file
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("write to classfile" + classFileName);
		// directly execute bytecode
		String[] args = new String[] { "1" }; // create command line argument
												// array to initialize
												// params, none in this case
		// args[0] = "5";
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();

	}

	@Test
	public void myTestCase3() throws Exception {
		// scan, parse, and type check the program
		String progname = "myProgram2";
		String input = progname + "{integer u boolean f f<-true; " + "if(f) {u <- 3;}" + " integer x "
				+ "if(f) {x <- 5;}" + "}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);
		// generate code
		CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
		byte[] bytecode = (byte[]) program.visit(cv, null);
		// output the generated bytecode
		CodeGenUtils.dumpBytecode(bytecode);
		// write byte code to file
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("write to classfile" + classFileName);
		// directly execute bytecode
		String[] args = new String[] { "1", "true" }; // create command line
														// argument
		// array to initialize
		// params, none in this case
		// args[0] = "5";
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();

	}

	@Test
	public void testIfTrueBinaryDiv() throws Exception {
		// scan, parse, and type check the program
		String progname = "testSimple6";
		String input = progname + " {integer a integer c c<-2; boolean b if(10>2) {a<-11;}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);
		// generate code
		CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
		byte[] bytecode = (byte[]) program.visit(cv, null);
		// output the generated bytecode
		CodeGenUtils.dumpBytecode(bytecode);
		// write byte code to file
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("wrote classfile to " + classFileName);
		// directly execute bytecode
		String[] args = new String[0]; // create command line argument array to
										// initialize params, none in this case
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();
	}

	@Test
	public void compProg0() throws Exception {
		// scan, parse, and type check the program
		String progname = "compProg0";
		String input = progname
				+ " { integer a0 a0<-0;if(a0 == 0){integer a00 integer b00 integer c00 integer d00 integer e00 e00 <- 5; d00 <- 4; c00 <- 3; b00 <- 2; a00 <- 1; if(a00 == 1){integer a01 integer b01 integer c01 integer d01 integer e01 e01 <- 55; d01 <- 44; c01 <- 33; b01 <- 22; a01 <- 11; }}} ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);
		// generate code
		CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
		byte[] bytecode = (byte[]) program.visit(cv, null);
		// output the generated bytecode
		CodeGenUtils.dumpBytecode(bytecode);
		// write byte code to file
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("wrote classfile to " + classFileName);
		// directly execute bytecode
		String[] args = new String[0]; // create command line argument array to
										// initialize params, none in this case
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();
	}

	@Test
	public void compProg1() throws Exception {
		// scan, parse, and type check the program
		String progname = "compProg1";
		String input = progname
				+ "{integer a, integer b, integer c, boolean bool0 { a <- 4;  b <- 5; boolean boolA  boolean boolB  boolA <- true;  boolB <- false;  if(boolA == true)  {boolean a a <- boolA; bool0 <- false;while(a != boolB){integer d  integer e c <- 3 + 5; d <- 10 - 1; c <- c * d; e <- d / 3; a <- boolB;if(c > d) {     c <- d;     if(c <= d)     {        boolA <- false;    }    if(boolA < boolB)     {        c <- 0;    }}} } if(c >= 1) {     /*boolB <- bool0 | true;*/} a <- 7;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);
		// generate code
		CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
		byte[] bytecode = (byte[]) program.visit(cv, null);
		// output the generated bytecode
		CodeGenUtils.dumpBytecode(bytecode);
		// write byte code to file
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("wrote classfile to " + classFileName);
		// directly execute bytecode
		String[] args = new String[0]; // create command line argument array to
										// initialize params, none in this case
		args[0]="1";
		args[1]="2";
		args[3] ="3";
		args[4] ="false";
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();
	}
	
	@Test
	public void compProg2() throws Exception {
		// scan, parse, and type check the program
		String progname = "compProg2";
		String input = progname
				+ "integer x, integer y, integer z, boolean bool_1, boolean bool_2 { }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);
		// generate code
		CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
		byte[] bytecode = (byte[]) program.visit(cv, null);
		// output the generated bytecode
		CodeGenUtils.dumpBytecode(bytecode);
		// write byte code to file
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("wrote classfile to " + classFileName);
		// directly execute bytecode
		String[] args = new String[0]; // create command line argument array to
										// initialize params, none in this case
		args[0]="1";
		args[1]="2";
		args[3] ="3";
		args[4] ="false";
		args[5]="true";
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();
	}

	@Before
	public void initLog() {
		if (devel || grade)
			PLPRuntimeLog.initLog();
	}

	@After
	public void printLog() {
		System.out.println(PLPRuntimeLog.getString());
	}

}
