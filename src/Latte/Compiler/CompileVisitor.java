package Latte.Compiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.StringEscapeUtils;

import Latte.Absyn.Ar;
import Latte.Absyn.Arg;
import Latte.Absyn.Arr;
import Latte.Absyn.ArrAcc;
import Latte.Absyn.Ass;
import Latte.Absyn.AttrAcc;
import Latte.Absyn.BStmt;
import Latte.Absyn.Blk;
import Latte.Absyn.Bool;
import Latte.Absyn.ClassDef;
import Latte.Absyn.ClassDefE;
import Latte.Absyn.Cond;
import Latte.Absyn.CondElse;
import Latte.Absyn.Decl;
import Latte.Absyn.Decr;
import Latte.Absyn.Div;
import Latte.Absyn.EAdd;
import Latte.Absyn.EAnd;
import Latte.Absyn.EApp;
import Latte.Absyn.EArrAccess;
import Latte.Absyn.EAttrAccess;
import Latte.Absyn.ECastNull;
import Latte.Absyn.ELitFalse;
import Latte.Absyn.ELitInt;
import Latte.Absyn.ELitTrue;
import Latte.Absyn.EMthCall;
import Latte.Absyn.EMul;
import Latte.Absyn.ENew;
import Latte.Absyn.ENewArr;
import Latte.Absyn.EOr;
import Latte.Absyn.EQU;
import Latte.Absyn.ERel;
import Latte.Absyn.EString;
import Latte.Absyn.EVar;
import Latte.Absyn.Empty;
import Latte.Absyn.Expr;
import Latte.Absyn.FCall;
import Latte.Absyn.FnDef;
import Latte.Absyn.For;
import Latte.Absyn.FunDef;
import Latte.Absyn.GE;
import Latte.Absyn.GTH;
import Latte.Absyn.Incr;
import Latte.Absyn.Init;
import Latte.Absyn.Int;
import Latte.Absyn.Item;
import Latte.Absyn.LE;
import Latte.Absyn.LTH;
import Latte.Absyn.LVAttrAcc;
import Latte.Absyn.LVEArrAccess;
import Latte.Absyn.LVFCall;
import Latte.Absyn.LVMethodCall;
import Latte.Absyn.LVSimpleIdent;
import Latte.Absyn.LValue;
import Latte.Absyn.ListItem;
import Latte.Absyn.MCall;
import Latte.Absyn.Minus;
import Latte.Absyn.Mod;
import Latte.Absyn.NE;
import Latte.Absyn.Neg;
import Latte.Absyn.NoInit;
import Latte.Absyn.Not;
import Latte.Absyn.Obj;
import Latte.Absyn.Plus;
import Latte.Absyn.Prog;
import Latte.Absyn.Ret;
import Latte.Absyn.SExp;
import Latte.Absyn.Stmt;
import Latte.Absyn.Str;
import Latte.Absyn.Times;
import Latte.Absyn.TopDef;
import Latte.Absyn.Type;
import Latte.Absyn.VRet;
import Latte.Absyn.Void;
import Latte.Absyn.While;
import Latte.Environment.CompileEnvironment;
import Latte.Environment.TypeEnvironment;
import Latte.Typecheck.ClassDefinition;
import Latte.Typecheck.TypecheckVisitor;
import Latte.Util.WriterVisitor;

public class CompileVisitor extends WriterVisitor implements
		Latte.Absyn.Program.Visitor<Object, CompileEnvironment>,
		Latte.Absyn.TopDef.Visitor<Object, CompileEnvironment>,
		Latte.Absyn.Block.Visitor<Object, CompileEnvironment>,
		Latte.Absyn.Stmt.Visitor<Object, CompileEnvironment>,
		Latte.Absyn.Item.Visitor<Object, CompileEnvironment>,
		Latte.Absyn.Expr.Visitor<Object, CompileEnvironment>,
		Latte.Absyn.AddOp.Visitor<Object, CompileEnvironment>,
		Latte.Absyn.MulOp.Visitor<Object, CompileEnvironment>,
		Latte.Absyn.RelOp.Visitor<Object, CompileEnvironment>,
		Latte.Absyn.LValue.Visitor<Boolean, CompileEnvironment>,
		Latte.Absyn.ArrAccess.Visitor<Type, CompileEnvironment>,
		Latte.Absyn.AttrAccess.Visitor<Boolean, CompileEnvironment>,
		Latte.Absyn.FunctionCall.Visitor<Object, CompileEnvironment>,
		Latte.Absyn.MethodCall.Visitor<Type, CompileEnvironment> {

	private CountVarVisitor	countVarVisitor;
	private TypeEnvironment	typeEnvironment;

	public CompileVisitor(String path, TypeEnvironment typeEnvironment)
			throws FileNotFoundException {

		super(path);
		this.typeEnvironment = typeEnvironment;
		countVarVisitor = new CountVarVisitor();
	}

	// value should be in %eax already
	private void compileBoolVal(CompileEnvironment env) {
		if (env.isInsideComparison()) {
			writer.appendLine("movl (%eax), %eax");
			writer.appendLine("testl %eax, %eax");
			writer.appendLine("je " + env.getCurrentOnFalseLabel());
			writer.appendLine("jne " + env.getCurrentOnTrueLabel());
		} else {
			writer.appendLine("pushl (%eax)");
		}
	}

	@Override
	public Object visit(EVar p, CompileEnvironment env) {
		new LVSimpleIdent(p.ident_).accept(this, env); // puts address of var on
														// stack
		writer.appendLine("popl %eax");
		Type t = p.ident_.equals("self") ? new Obj(env.getCurrentClass()
				.getClassname()) : env.getVariableType(p.ident_);

		if (t.equals(new Bool())) {
			compileBoolVal(env);
		} else if (p.ident_.equals("self")) {
			writer.appendLine("pushl %eax");
		} else {
			writer.appendLine("pushl (%eax)");
		}

		return null;
	}

	@Override
	public Object visit(ELitInt p, CompileEnvironment env) {
		writer.appendLine("pushl $" + p.integer_);

		return null;
	}

	@Override
	public Object visit(ELitTrue p, CompileEnvironment env) {
		writer.appendLine("jmp " + env.getCurrentOnTrueLabel());

		return null;
	}

	@Override
	public Object visit(ELitFalse p, CompileEnvironment env) {
		writer.appendLine("jmp " + env.getCurrentOnFalseLabel());

		return null;
	}

	@Override
	public Object visit(EApp p, CompileEnvironment env) {
		p.functioncall_.accept(this, env);

		return null;
	}

	@Override
	public Object visit(EString p, CompileEnvironment env) {
		String strLabel = env.getLabelForString(p.string_);
		writer.appendLine("pushl $" + strLabel);

		return null;
	}

	@Override
	public Object visit(Neg p, CompileEnvironment env) { // negation - multiply
															// by -1
		p.expr_.accept(this, env);
		writer.appendLine("popl %eax");
		writer.appendLine("movl $-1, %edi");

		new Times().accept(this, env);

		writer.appendLine("pushl %eax");

		return null;
	}

	@Override
	public Object visit(Not p, CompileEnvironment env) {
		env.swapBoolLabels();
		p.expr_.accept(this, env);
		env.swapBoolLabels();

		return null;
	}

	@Override
	public Object visit(EMul p, CompileEnvironment env) {
		p.expr_1.accept(this, env);
		p.expr_2.accept(this, env);

		writer.appendLine("popl %edi");
		writer.appendLine("popl %eax");

		p.mulop_.accept(this, env);

		writer.appendLine("pushl %eax");

		return null;
	}

	@Override
	public Object visit(EAdd p, CompileEnvironment env) {
		p.expr_1.accept(this, env);
		p.expr_2.accept(this, env);

		Type exprType = p.expr_1.accept(new TypecheckVisitor(),
				env.getTypeEnvironment());
		if (exprType.equals(new Int())) {
			writer.appendLine("popl %edx");
			writer.appendLine("popl %eax");

			p.addop_.accept(this, env);
		} else {
			// exprType == Str
			concatStrings();
		}

		writer.appendLine("pushl %eax");

		return null;
	}

	private void concatStrings() {
		writer.appendLine("call concat");
		writer.appendLine("addl $8, %esp");
		writer.appendLine("pushl %eax");
	}

	private void compareStrings() {
		writer.appendLine("call strcmp");
		writer.appendLine("addl $8, %esp");
		writer.appendLine("testl %eax, %eax");
	}

	@Override
	public Object visit(ERel p, CompileEnvironment env) {
		Boolean isInsideComparison = env.isInsideComparison();
		env.setInsideComparison(false);
		p.expr_1.accept(this, env);
		p.expr_2.accept(this, env);
		Type t1 = p.expr_1.accept(new TypecheckVisitor(),
				env.getTypeEnvironment());
		env.setInsideComparison(isInsideComparison);
		if (t1 instanceof Str) { // from typecheck we know that in this case t2
									// as well
			compareStrings();
		} else {
			writer.appendLine("popl %edx");
			writer.appendLine("popl %eax");
			writer.appendLine("cmpl %edx, %eax");
		}
		p.relop_.accept(this, env);

		return null;
	}

	@Override
	public Object visit(EAnd p, CompileEnvironment env) {
		String onTrue = env.getCurrentOnTrueLabel();
		String secondPart = env.getNewLabel();
		env.setCurrentOnTrueLabel(secondPart);
		p.expr_1.accept(this, env);
		writer.appendLine(secondPart + ":");
		env.setCurrentOnTrueLabel(onTrue);
		p.expr_2.accept(this, env);

		return null;
	}

	private String prepareBooleanAlternatives(CompileEnvironment env) {
		String onFalse = env.getNewLabel();
		String onTrue = env.getNewLabel();
		String expr = env.getNewLabel();
		String afterAll = env.getNewLabel();

		env.setCurrentOnFalseLabel(onFalse);
		env.setCurrentOnTrueLabel(onTrue);

		writer.appendLine("jmp " + expr);
		writer.appendLine(onFalse + ":");
		writer.appendLine("pushl $0");
		writer.appendLine("jmp " + afterAll);
		writer.appendLine(onTrue + ":");
		writer.appendLine("pushl $1");
		writer.appendLine("jmp " + afterAll);
		writer.appendLine(expr + ":");

		return afterAll;
	}

	@Override
	public Object visit(EOr p, CompileEnvironment env) {
		String onFalse = env.getCurrentOnFalseLabel();
		String secondPart = env.getNewLabel();
		env.setCurrentOnFalseLabel(secondPart);
		p.expr_1.accept(this, env);
		writer.appendLine(secondPart + ":");
		env.setCurrentOnFalseLabel(onFalse);
		p.expr_2.accept(this, env);

		return null;
	}

	@Override
	public Object visit(NoInit p, CompileEnvironment env) {
		Type currentType = env.getCurrentType();
		if (currentType.equals(new Str())) {
			writer.appendLine("movl $.EMPTYSTRING, "
					+ env.addVariable(p.ident_, currentType) + "(%ebp)");
		} else {
			writer.appendLine("movl $0, "
					+ env.addVariable(p.ident_, currentType) + "(%ebp)");
		}

		return null;
	}

	@Override
	public Object visit(Init p, CompileEnvironment env) {
		if (env.getCurrentType().equals(new Bool())) {
			String afterAlllabel = prepareBooleanAlternatives(env);
			p.expr_.accept(this, env);
			writer.appendLine(afterAlllabel + ":");
		} else {
			p.expr_.accept(this, env);
		}

		writer.appendLine("popl "
				+ env.addVariable(p.ident_, env.getCurrentType()) + "(%ebp)");

		return null;
	}

	@Override
	public Object visit(Decl p, CompileEnvironment env) {
		Type prevType = env.getCurrentType();
		env.setCurrentType(p.type_);

		for (Item item : p.listitem_) {
			item.accept(this, env);
		}

		env.setCurrentType(prevType);

		return null;
	}

	@Override
	public Object visit(Empty p, CompileEnvironment env) {
		return null;
	}

	@Override
	public Object visit(BStmt p, CompileEnvironment env) {
		p.block_.accept(this, env);

		return null;
	}

	@Override
	public Object visit(Ass p, CompileEnvironment env) {
		Type prevType = env.getCurrentType();

		TypeEnvironment te = env.getTypeEnvironment();
		Boolean insideClass = null != env.getCurrentClass();
		if (insideClass)
			te.prependClassEnv(env.getCurrentClass());
		Type lvaltype = p.lvalue_.accept(new TypecheckVisitor(), te);
		if (insideClass)
			te.cancelPrependingClassEnv(env.getCurrentClass());

		env.setCurrentType(lvaltype);

		if (lvaltype.equals(new Bool())) {
			String afterAlllabel = prepareBooleanAlternatives(env);
			p.expr_.accept(this, env);
			writer.appendLine(afterAlllabel + ":");
		} else {
			p.expr_.accept(this, env);
		}

		p.lvalue_.accept(this, env);

		env.setCurrentType(prevType);

		writer.appendLine("popl %eax");
		writer.appendLine("popl (%eax)");

		return null;
	}

	@Override
	public Object visit(Incr p, CompileEnvironment env) {
		p.lvalue_.accept(this, env);
		writer.appendLine("popl %eax");
		writer.appendLine("addl $1, (%eax)");

		return null;
	}

	@Override
	public Object visit(Decr p, CompileEnvironment env) {
		p.lvalue_.accept(this, env);
		writer.appendLine("popl %eax");
		writer.appendLine("subl $1, (%eax)");

		return null;
	}

	@Override
	public Object visit(Ret p, CompileEnvironment env) {
		if (env.getCurrentReturnType().equals(new Bool())) {
			String afterAlllabel = prepareBooleanAlternatives(env);
			env.setInsideComparison(true);
			p.expr_.accept(this, env);
			writer.appendLine(afterAlllabel + ":");
		} else {
			env.setCurrentType(env.getCurrentReturnType());
			p.expr_.accept(this, env);
		}

		writer.appendLine("popl %eax");
		writer.appendLine("jmp " + env.getRetLabel());

		return null;
	}

	@Override
	public Object visit(VRet p, CompileEnvironment env) {
		writer.appendLine("jmp " + env.getRetLabel());

		return null;
	}

	@Override
	public Object visit(Cond p, CompileEnvironment env) {
		String labelIf = env.getNewLabel();
		String labelAfter = env.getNewLabel();
		env.setCurrentOnTrueLabel(labelIf);
		env.setCurrentOnFalseLabel(labelAfter);
		env.setInsideComparison(true);

		p.expr_.accept(this, env);
		env.setInsideComparison(false);
		writer.appendLine(labelIf + ":");
		p.stmt_.accept(this, env);
		writer.appendLine(labelAfter + ":");

		return null;
	}

	@Override
	public Object visit(CondElse p, CompileEnvironment env) {
		String labelIf = env.getNewLabel();
		String labelElse = env.getNewLabel();
		String labelAfter = env.getNewLabel();
		env.setCurrentOnTrueLabel(labelIf);
		env.setCurrentOnFalseLabel(labelElse);
		env.setInsideComparison(true);

		p.expr_.accept(this, env);
		env.setInsideComparison(false);
		writer.appendLine(labelIf + ":");
		p.stmt_1.accept(this, env);
		writer.appendLine("jmp " + labelAfter);
		writer.appendLine(labelElse + ":");
		p.stmt_2.accept(this, env);
		writer.appendLine(labelAfter + ":");

		return null;
	}

	@Override
	public Object visit(While p, CompileEnvironment env) {
		String labelBefore = env.getNewLabel();
		String labelAfter = env.getNewLabel();
		String labelIn = env.getNewLabel();
		env.setCurrentOnFalseLabel(labelAfter);
		env.setCurrentOnTrueLabel(labelIn);
		env.setInsideComparison(true);

		writer.appendLine(labelBefore + ":");
		p.expr_.accept(this, env);
		env.setInsideComparison(false);
		writer.appendLine(labelIn + ":");
		p.stmt_.accept(this, env);
		writer.appendLine("jmp " + labelBefore);
		writer.appendLine(labelAfter + ":");

		return null;
	}

	@Override
	public Object visit(SExp p, CompileEnvironment env) {
		String labelAfter = env.getNewLabel();
		env.setCurrentOnFalseLabel(labelAfter);
		env.setCurrentOnTrueLabel(labelAfter);
		p.expr_.accept(this, env);
		writer.appendLine(labelAfter + ":");

		return null;
	}

	@Override
	public Object visit(Blk p, CompileEnvironment env) {
		env.openBlock();
		for (Stmt stmt : p.liststmt_) {
			stmt.accept(this, env);
		}
		env.closeBlock();

		return null;
	}

	private void emplaceArguments(FunDef p, CompileEnvironment env) {
		int i = 2 * CompileEnvironment.getDefaultTypeSize();
		for (Arg arg : p.listarg_) {
			Ar ar = (Ar) arg;
			env.addVariable(ar.ident_, ar.type_, i);
			i += CompileEnvironment.getDefaultTypeSize();
		}
	}

	@Override
	public Object visit(FnDef f, CompileEnvironment env) {
		compileFunction((FunDef) f.funcdef_, env);

		return null;
	}

	private void compileFunction(FunDef p, CompileEnvironment env) {
		env.setCurrentReturnType(p.type_);

		// header
		writer.appendLine(".globl " + p.ident_);
		writer.appendLine(p.ident_ + ":");

		// prolog
		writer.appendLine("pushl %ebp");
		writer.appendLine("movl %esp, %ebp");
		writer.appendLine("subl $" + CompileEnvironment.getDefaultTypeSize()
				* p.block_.accept(countVarVisitor, null) + ", %esp");

		env.openBlock();
		emplaceArguments(p, env);
		p.block_.accept(this, env);
		env.closeBlock();

		// epilog
		writer.appendLine(env.getRetLabel() + ":");
		writer.appendLine("leave");
		writer.appendLine("ret");
	}

	private String generateMethodName(String method, String clazz) {
		return "__C__" + clazz + "__" + method;
	}

	private void compileMethod(String method, ClassDefinition clazz,
			CompileEnvironment env) {

		FunDef fn = clazz.getMethod(method);
		FunDef m = new FunDef(fn.type_, generateMethodName(method,
				clazz.getClassname()), fn.listarg_, fn.block_);

		compileFunction(m, env);
	}

	private void compileClass(String classname, CompileEnvironment env) {
		ClassDefinition clazz = env.getTypeEnvironment().getClassDefinition(
				classname);

		env.setCurrentClass(clazz);

		for (String method : clazz.getMethodNames()) {
			compileMethod(method, clazz, env);
			env.clear();
		}

		env.unsetCurrentClass();
	}

	@Override
	public Object visit(ClassDef p, CompileEnvironment env) {
		compileClass(p.ident_, env);

		return null;
	}

	@Override
	public Object visit(ClassDefE p, CompileEnvironment env) {
		compileClass(p.ident_1, env);

		return null;
	}

	@Override
	public Object visit(Prog p, CompileEnvironment env) {
		Integer currentLabelIndex = 0;
		Integer currentStringLabelIndex = 0;

		for (TopDef fnDef : p.listtopdef_) {
			CompileEnvironment ce = new CompileEnvironment(currentLabelIndex,
					currentStringLabelIndex, typeEnvironment);

			fnDef.accept(this, ce);

			currentLabelIndex = ce.getCurrentLabelIndex();
			currentStringLabelIndex = ce.getCurrentStringLabelIndex();
			Map<String, String> strLabels = ce.getStringLabels();
			for (String strVal : strLabels.keySet()) {
				writer.appendLine(strLabels.get(strVal) + ":");
				writer.appendLine(".string \""
						+ StringEscapeUtils.escapeJava(strVal) + "\"");
			}
			writer.appendLine("");
		}

		try {
			writer.writeToFile();
			writer.close();
		} catch (IOException e) {
			System.err
					.println("An error occured while performing I/O operations. Reason "
							+ e.getMessage());
		}

		return null;
	}

	@Override
	public Object visit(Plus p, CompileEnvironment env) {
		writer.appendLine("addl %edx, %eax");

		return null;
	}

	@Override
	public Object visit(Minus p, CompileEnvironment env) {
		writer.appendLine("subl %edx, %eax");

		return null;
	}

	@Override
	public Object visit(Times p, CompileEnvironment env) {
		writer.appendLine("imull %edi, %eax");

		return null;
	}

	private void divide() {
		writer.appendLine("movl %eax, %edx");
		writer.appendLine("sar $31, %edx");

		// check if %edi != 0
		writer.appendLine("testl %edi, %edi");
		writer.appendLine("je .ERROR");

		writer.appendLine("idiv %edi");
	}

	@Override
	public Object visit(Div p, CompileEnvironment env) {
		divide();

		return null;
	}

	@Override
	public Object visit(Mod p, CompileEnvironment env) {
		divide();
		writer.appendLine("movl %edx, %eax");

		return null;
	}

	@Override
	public Object visit(LTH p, CompileEnvironment env) {
		writer.appendLine("jnl " + env.getCurrentOnFalseLabel());
		writer.appendLine("jl " + env.getCurrentOnTrueLabel());

		return null;
	}

	@Override
	public Object visit(LE p, CompileEnvironment env) {
		writer.appendLine("jnle " + env.getCurrentOnFalseLabel());
		writer.appendLine("jle " + env.getCurrentOnTrueLabel());

		return null;
	}

	@Override
	public Object visit(GTH p, CompileEnvironment env) {
		writer.appendLine("jng " + env.getCurrentOnFalseLabel());
		writer.appendLine("jg " + env.getCurrentOnTrueLabel());

		return null;
	}

	@Override
	public Object visit(GE p, CompileEnvironment env) {
		writer.appendLine("jnge " + env.getCurrentOnFalseLabel());
		writer.appendLine("jge " + env.getCurrentOnTrueLabel());

		return null;
	}

	@Override
	public Object visit(EQU p, CompileEnvironment env) {
		writer.appendLine("jne " + env.getCurrentOnFalseLabel());
		writer.appendLine("je " + env.getCurrentOnTrueLabel());

		return null;
	}

	@Override
	public Object visit(NE p, CompileEnvironment env) {
		writer.appendLine("je " + env.getCurrentOnFalseLabel());
		writer.appendLine("jne " + env.getCurrentOnTrueLabel());

		return null;
	}

	@Override
	public Object visit(ENewArr p, CompileEnvironment env) {
		Integer typeSize = typeEnvironment.getTypeSize(p.type_);

		p.expr_.accept(this, env);
		writer.appendLine("popl %eax");

		writer.appendLine("pushl %eax");
		writer.appendLine("pushl $" + typeSize);
		writer.appendLine("addl $1, %eax");
		writer.appendLine("pushl %eax");

		// check if size of array > 0
		writer.appendLine("movl $0, %esi");
		writer.appendLine("cmpl %esi, %eax");
		writer.appendLine("jng .ERROR");

		writer.appendLine("call calloc"); // sets all the memory on 0
		writer.appendLine("addl $8, %esp");
		writer.appendLine("popl (%eax)");
		writer.appendLine("pushl %eax");

		// strings are by default not 0, but .EMPTYSTRING
		if (((Arr) env.getCurrentType()).type_.equals(new Str())) {
			initStringArray(typeSize, env);
		}

		return null;
	}

	// array pointer must be in %eax
	private void initStringArray(Integer typeSize, CompileEnvironment env) {
		String label = env.getNewLabel();
		writer.appendLine("movl (%eax), %edi"); // length
		writer.appendLine("movl $1, %edx"); // currentIndex

		writer.appendLine(label + ":");
		writer.appendLine("addl $" + typeSize + ", %eax");
		writer.appendLine("movl $.EMPTYSTRING, (%eax)");
		writer.appendLine("addl $1, %edx"); // inc currentIndex
		writer.appendLine("cmpl %edi, %edx");
		writer.appendLine("jle " + label);
	}

	@Override
	public Object visit(EAttrAccess p, CompileEnvironment env) {
		if (!p.attraccess_.accept(this, env)) { // the method returns true if
												// the ptr is already on stack
			writer.appendLine("popl %eax");
			writer.appendLine("pushl (%eax)");
		}

		return null;
	}

	@Override
	public Object visit(EMthCall p, CompileEnvironment env) {
		p.methodcall_.accept(this, env);

		return null;
	}

	@Override
	public Object visit(EArrAccess p, CompileEnvironment env) {
		Arr t = (Arr) p.arraccess_.accept(this, env);

		writer.appendLine("popl %eax");
		if (t.type_.equals(new Bool())) {
			compileBoolVal(env);
		} else {
			writer.appendLine("pushl (%eax)");
		}

		return null;
	}

	@Override
	public Type visit(MCall p, CompileEnvironment env) {
		Obj type = (Obj) p.lvalue_.accept(new TypecheckVisitor(),
				env.getTypeEnvironment());

		FCall f = (FCall) p.functioncall_;
		ClassDefinition clazz = env.getTypeEnvironment().getClassDefinition(
				type.ident_);
		FunDef fn = clazz.getMethod(f.ident_);
		fn = new FunDef(fn.type_, generateMethodName(f.ident_,
				clazz.getClassname()), fn.listarg_, fn.block_);
		callFunction(f, fn, env,
				new MethodCaller(clazz.getMethodIndex(f.ident_),
						CompileEnvironment.getDefaultTypeSize()), p.lvalue_);

		if (fn.type_.equals(new Void())) {
			// writer.appendLine("popl %ebx");
		} else {
			// method result is on stack
			writer.appendLine("popl %eax");
			// writer.appendLine("popl %ebx");
			writer.appendLine("pushl %eax");
		}

		return fn.type_;
	}

	@Override
	public Boolean visit(LVFCall p, CompileEnvironment env) {
		p.functioncall_.accept(this, env);

		return true;
	}

	@Override
	public Boolean visit(LVMethodCall p, CompileEnvironment env) {
		p.methodcall_.accept(this, env);

		return true;
	}

	@Override
	public Boolean visit(LVSimpleIdent p, CompileEnvironment env) {
		if (p.ident_.equals("self")) {
			writer.appendLine("movl %ebx, %eax");
			writer.appendLine("pushl %eax");
			return true;
		} else if (env.isVariableClassAttribute(p.ident_)) {
			writer.appendLine("movl %ebx, %eax");
			writer.appendLine("addl $" + env.getAttribute(p.ident_) + ", %eax");
		} else {
			writer.appendLine("movl %ebp, %eax");
			writer.appendLine("addl $" + env.getVariable(p.ident_) + ", %eax");
		}

		writer.appendLine("pushl %eax");

		return false;
	}

	@Override
	public Boolean visit(LVEArrAccess p, CompileEnvironment env) {
		p.arraccess_.accept(this, env);

		return false;
	}

	@Override
	public Boolean visit(LVAttrAcc p, CompileEnvironment env) {
		return p.attraccess_.accept(this, env);
	}

	@Override
	public Type visit(ArrAcc p, CompileEnvironment env) {
		if (p.expr_ != null)
			p.expr_.accept(this, env);

		if (p.lvalue_.accept(this, env)) {
			writer.appendLine("popl %edx"); // array
		} else {
			// replace address of pointer with the memory it points to
			writer.appendLine("popl %eax");
			writer.appendLine("movl (%eax), %edx"); // array
		}

		writer.appendLine("popl %eax"); // index
		writer.appendLine("addl $1, %eax"); // arrays are indexed in Latte from
											// 0, in compiled x86 from 1,
											// because at index 0 there is
											// length

		checkIfArrayAccessCorrect();

		// continue calculation
		writer.appendLine("movl $" + CompileEnvironment.getDefaultTypeSize()
				+ ", %edi"); // size of pointer
		writer.appendLine("imull %edi, %eax");

		writer.appendLine("addl %eax, %edx");
		writer.appendLine("pushl %edx");

		return p.lvalue_.accept(new TypecheckVisitor(),
				env.getTypeEnvironment());
	}

	// array pointer must be in %edx
	private void checkIfArrayAccessCorrect() {
		// check if array was initialized (if length != 0)
		writer.appendLine("movl $0, %esi");
		writer.appendLine("cmpl %edx, %esi");
		writer.appendLine("je .ERROR");

		// check if the index in inside array
		writer.appendLine("cmpl %esi, %eax"); // check if > 0
		writer.appendLine("jng .ERROR");

		writer.appendLine("movl (%edx), %edi");
		writer.appendLine("cmpl %eax, %edi"); // check if <= length
		writer.appendLine("jl .ERROR");
	}

	@Override
	public Object visit(For p, CompileEnvironment env) {
		new EAttrAccess(new AttrAcc(new LVSimpleIdent(p.ident_2), "length"))
				.accept(this, env);
		String forLabel = env.getNewLabel();
		writer.appendLine("pushl $0");
		writer.appendLine("pushl $0");
		writer.appendLine(forLabel + ":");

		Decl decl = new Decl(p.type_, new ListItem());
		decl.listitem_.add(new Init(p.ident_1, new EArrAccess(new ArrAcc(
				new LVSimpleIdent(p.ident_2), null))));
		decl.accept(this, env);

		p.stmt_.accept(this, env);
		writer.appendLine("popl %eax"); // current index
		writer.appendLine("addl $1, %eax"); // inc

		writer.appendLine("popl %edx"); // array length
		writer.appendLine("pushl %edx"); // array length
		writer.appendLine("pushl %eax"); // current index
		writer.appendLine("pushl %eax"); // current index

		writer.appendLine("cmpl %edx, %eax");
		writer.appendLine("jl " + forLabel);

		writer.appendLine("popl %eax"); // remove current index
		writer.appendLine("popl %eax"); // remove current index
		writer.appendLine("popl %eax"); // remove array length

		return null;
	}

	@Override
	public Boolean visit(AttrAcc p, CompileEnvironment env) {
		Type lvaltype = p.lvalue_.accept(new TypecheckVisitor(),
				env.getTypeEnvironment());

		Boolean isNotPtr = p.lvalue_.accept(this, env);

		writer.appendLine("popl %eax");
		if (!isNotPtr) {
			writer.appendLine("movl (%eax), %eax");
		}

		if (p.ident_.equals("length") && lvaltype instanceof Arr) {
			// length is stored at the beginning of the array
		} else {
			Integer offset = env.getTypeEnvironment()
					.getClassDefinition(((Obj) lvaltype).ident_)
					.getAttrIndex(p.ident_) + 1;
			// + 1 because at index 0 is method array

			writer.appendLine("addl $"
					+ CompileEnvironment.getDefaultTypeSize() * offset
					+ ", %eax");
		}
		writer.appendLine("pushl %eax");

		return p.lvalue_ instanceof LVFCall;
	}

	private void callFunction(FCall f, FunDef fn, CompileEnvironment env,
			FunctionCaller fc, LValue lvalue) {

		writer.appendLine("pushl %ebx");

		// pushing arguments in reverse order
		Stack<Expr> args = new Stack<Expr>();
		for (Expr arg : f.listexpr_) {
			args.push(arg);
		}
		Stack<Type> argTypes = new Stack<Type>();
		for (Arg arg : fn.listarg_) {
			argTypes.push(((Ar) arg).type_);
		}
		Expr arg;
		while (args.size() > 0) {
			arg = args.pop();
			if (argTypes.pop().equals(new Bool())) {
				String onTrueLabel = env.getCurrentOnTrueLabel();
				String onFalseLabel = env.getCurrentOnFalseLabel();
				String afterAlllabel = prepareBooleanAlternatives(env);
				arg.accept(this, env);
				writer.appendLine(afterAlllabel + ":");
				env.setCurrentOnFalseLabel(onFalseLabel);
				env.setCurrentOnTrueLabel(onTrueLabel);
			} else {
				arg.accept(this, env);
			}
		}

		if (lvalue != null) {
			Boolean isNotPtr = lvalue.accept(this, env);
			writer.appendLine("popl %eax");
			if (!isNotPtr) {
				writer.appendLine("movl (%eax), %ebx");
			} else {
				writer.appendLine("movl %eax, %ebx");
			}
		}

		fc.call(writer);

		// cleaning arguments
		if (fn.listarg_.size() > 0)
			writer.appendLine("addl $"
					+ CompileEnvironment.getDefaultTypeSize()
					* fn.listarg_.size() + ", %esp");

		writer.appendLine("popl %ebx");

		// using the result
		if (fn.type_.equals(new Bool())) {
			writer.appendLine("testl %eax, %eax");
			writer.appendLine("jne " + env.getCurrentOnTrueLabel());
			writer.appendLine("je " + env.getCurrentOnFalseLabel());
		} else if (!fn.type_.equals(new Void())) {
			writer.appendLine("pushl %eax");
		}
	}

	@Override
	public Object visit(FCall f, CompileEnvironment env) {
		callFunction(f, typeEnvironment.retrieveFunction(f.ident_), env,
				new RealFunctionCaller(f.ident_), null);

		return null;
	}

	private void initializeAttributes(ClassDefinition clazz) {
		Map<String, Integer> attrOrder = clazz.getAttrOrder();
		for (String attr : attrOrder.keySet()) {
			Integer offset = CompileEnvironment.getDefaultTypeSize()
					* (attrOrder.get(attr) + 1);
			// + 1 because at 0 index there is method array ptr

			Type type = clazz.getAttr(attr);
			if (type.equals(new Str())) {
				writer.appendLine("movl $.EMPTYSTRING, " + offset + "(%eax)");
			} else {
				writer.appendLine("movl $0, " + offset + "(%eax)");
			}
		}
	}

	private void initializeMethods(ClassDefinition clazz) {
		Set<String> methodOrder = clazz.getMethodNames();

		writer.appendLine("pushl %eax");

		writer.appendLine("pushl $" + CompileEnvironment.getDefaultTypeSize());
		writer.appendLine("pushl $" + methodOrder.size());
		writer.appendLine("call calloc");
		writer.appendLine("addl $8, %esp");
		writer.appendLine("popl %edx");
		writer.appendLine("movl %eax, (%edx)"); // save ptr to method array in
												// object

		for (String method : methodOrder) {
			Integer index = clazz.getMethodIndex(method);
			String methodName = generateMethodName(method, clazz.getClassname());
			writer.appendLine("movl $" + methodName + ", " + index
					* CompileEnvironment.getDefaultTypeSize() + "(%eax)");
		}
	}

	@Override
	public Object visit(ENew p, CompileEnvironment env) {
		ClassDefinition clazz = env.getTypeEnvironment().getClassDefinition(
				p.ident_);

		writer.appendLine("pushl $" + CompileEnvironment.getDefaultTypeSize());
		writer.appendLine("pushl $" + (clazz.getAttrs().size() + 1));
		// + 1 because one additional ptr for method array

		writer.appendLine("call calloc");
		writer.appendLine("addl $8, %esp");
		writer.appendLine("pushl %eax");

		initializeAttributes(clazz);
		initializeMethods(clazz);

		return null;
	}

	@Override
	public Object visit(ECastNull p, CompileEnvironment arg) {
		writer.appendLine("pushl $0");

		return null;
	}

}
