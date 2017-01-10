package Latte.Typecheck;

import java.util.ArrayList;
import java.util.List;

import Latte.Absyn.Ar;
import Latte.Absyn.Arg;
import Latte.Absyn.Arr;
import Latte.Absyn.ArrAcc;
import Latte.Absyn.Ass;
import Latte.Absyn.AttrAcc;
import Latte.Absyn.BStmt;
import Latte.Absyn.Blk;
import Latte.Absyn.Bool;
import Latte.Absyn.ClassAtr;
import Latte.Absyn.ClassBlock;
import Latte.Absyn.ClassDef;
import Latte.Absyn.ClassDefE;
import Latte.Absyn.ClassElem;
import Latte.Absyn.ClassMeth;
import Latte.Absyn.Cond;
import Latte.Absyn.CondElse;
import Latte.Absyn.Decl;
import Latte.Absyn.Decr;
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
import Latte.Absyn.Incr;
import Latte.Absyn.Init;
import Latte.Absyn.Int;
import Latte.Absyn.Item;
import Latte.Absyn.LVAttrAcc;
import Latte.Absyn.LVEArrAccess;
import Latte.Absyn.LVFCall;
import Latte.Absyn.LVMethodCall;
import Latte.Absyn.LVSimpleIdent;
import Latte.Absyn.LValue;
import Latte.Absyn.MCall;
import Latte.Absyn.NE;
import Latte.Absyn.Neg;
import Latte.Absyn.NoInit;
import Latte.Absyn.Not;
import Latte.Absyn.Obj;
import Latte.Absyn.Plus;
import Latte.Absyn.Prog;
import Latte.Absyn.Program;
import Latte.Absyn.Ret;
import Latte.Absyn.SExp;
import Latte.Absyn.Stmt;
import Latte.Absyn.Str;
import Latte.Absyn.TopDef;
import Latte.Absyn.Type;
import Latte.Absyn.VRet;
import Latte.Absyn.Void;
import Latte.Absyn.While;
import Latte.Environment.TypeEnvironment;

public class TypecheckVisitor implements
		Latte.Absyn.Program.Visitor<Latte.Absyn.Program, TypeEnvironment>,
		Latte.Absyn.TopDef.Visitor<Object, TypeEnvironment>,
		Latte.Absyn.Block.Visitor<Boolean, TypeEnvironment>,
		Latte.Absyn.Stmt.Visitor<Boolean, TypeEnvironment>,
		Latte.Absyn.Item.Visitor<String, TypeEnvironment>,
		Latte.Absyn.Expr.Visitor<Type, TypeEnvironment>,
		Latte.Absyn.ArrAccess.Visitor<Type, TypeEnvironment>,
		Latte.Absyn.LValue.Visitor<Type, TypeEnvironment>,
		Latte.Absyn.AttrAccess.Visitor<Type, TypeEnvironment>,
		Latte.Absyn.ClassElem.Visitor<Object, TypeEnvironment>,
		Latte.Absyn.MethodCall.Visitor<Type, TypeEnvironment> {

	private List<String>		errors				= new ArrayList<String>();
	private Type				currentType;
	private DefinitionVisitor	definitionVisitor	= new DefinitionVisitor(
															errors);

	public List<String> getErrors() {
		return errors;
	}

	private void addError(String error, TypeEnvironment env) {
		FunDef curFn = env.getCurrentFunction();
		ClassDefinition curClass = env.getCurrentClass();

		StringBuilder sb = new StringBuilder(error);
		if (curFn != null) {
			if (curClass != null) {
				sb.append(" in method " + curFn.ident_);
			} else {
				sb.append(" in function " + curFn.ident_);
			}
		}
		if (curClass != null) {
			sb.append(" in class " + curClass.getClassname());
		}

		errors.add(sb.toString());
	}

	private Type retrieveVarType(TypeEnvironment env, String ident) {
		if (ident.equals("null")) {
			addError("Use of illegal identifier null", env);
			return null;
		}

		if (ident.equals("self"))
			return new Obj(env.getCurrentClass().getClassname());

		Type t = env.retrieveVariableType(ident);
		if (t == null)
			addError("Use of undeclared variable " + ident, env);

		return t;
	}

	private Type typeCheckBinaryOp(Expr expr1, Expr expr2, TypeEnvironment env,
			List<Type> types) {

		Type t1 = expr1.accept(this, env);
		Type t2 = expr2.accept(this, env);

		for (Type t : types) {
			if (t1 != null && t2 != null && t1.equals(t2) && t1.equals(t)) {
				return t;
			}
		}

		return null;
	}

	private Type typecheckBinaryOp(String id, Expr expr1, Expr expr2,
			TypeEnvironment env, Type t) {

		if (expr1 instanceof ECastNull || expr2 instanceof ECastNull) {
			addError("Use of null cast in illegal place", env);
			return null;
		}

		Type t1 = expr1.accept(this, env);
		Type t2 = expr2.accept(this, env);
		if (t1 == null || t2 == null || !t1.equals(t) || !t2.equals(t)) {
			addError("Not " + t.getClass().getSimpleName() + " types inside "
					+ id + " operator", env);
		}

		return t;
	}

	private Boolean typecheckCond(String id, Expr boolExpr, Stmt stmt,
			TypeEnvironment env) {

		Type exprType = boolExpr.accept(this, env);
		if (!new Bool().equals(exprType))
			addError("Expression in " + id + " statement has not boolean type",
					env);

		return stmt.accept(this, env);
	}

	private void typecheckFunction(FunDef fn, TypeEnvironment env) {
		env.openBlock();
		env.setCurrentFunction(fn);
		for (Arg a : fn.listarg_) {
			Ar ar = (Ar) a; // it's safe cast - arg is abstract and only ar
							// inherits from arg
			if (env.blockContainsVariable(ar.ident_)) {
				addError("Duplicate argument " + ar.ident_, env);
			}
			if (new Void().equals(ar.type_)) {
				addError("Void argument " + ar.ident_, env);
			}
			env.addVariable(ar.ident_, ar.type_);
		}
		if (fn.type_ instanceof Arr
				&& ((Arr) fn.type_).type_.equals(new Void())) {
			addError("Type of function can't be array of void", env);
		}
		if (!env.isTypeKnown(fn.type_)) {
			addError("Unknown return type ", env);
		}
		Boolean hasReturn = fn.block_.accept(this, env);
		if (!fn.type_.equals(new Void()) && !hasReturn) {
			addError("Lack of return statement", env);
		}
		env.setCurrentFunction(null);
		env.closeBlock();
	}

	@Override
	public Object visit(FnDef p, TypeEnvironment env) {
		FunDef fn = (FunDef) p.funcdef_;
		typecheckFunction(fn, env);

		return null;
	}

	@Override
	public Program visit(Prog p, TypeEnvironment env) {
		// first prepare declarations of all functions for recursive calls
		p.accept(definitionVisitor, env);

		// typecheck functions
		for (TopDef topdef : p.listtopdef_) {
			topdef.accept(this, env);
		}

		if (!env.checkForMainFunction()) {
			errors.add("Main function not defined");
		}

		return null;
	}

	@Override
	public Boolean visit(Blk blk, TypeEnvironment env) {
		env.openBlock();

		Boolean ret = false;
		for (Stmt stmt : blk.liststmt_) {
			ret = ret || stmt.accept(this, env);
		}

		env.closeBlock();

		return ret;
	}

	@Override
	public Boolean visit(Decl decl, TypeEnvironment env) {
		if (decl.type_.equals(new Void())) {
			addError("Variable declared void", env);
			return false;
		}

		if (!env.isTypeKnown(decl.type_)) {
			addError("Variable declared with undeclared type", env);
		}

		if (decl.type_ instanceof Arr) {
			Type arrType = ((Arr) decl.type_).type_;
			if (arrType.equals(new Void())) {
				addError("Array declared void", env);
				return false;
			}
			if (!env.isTypeKnown(arrType)) {
				addError("Array declared with undeclared type", env);
				return false;
			}
		}

		currentType = decl.type_;
		for (Item var : decl.listitem_) {
			String ident = var.accept(this, env);
			if (ident != null) {
				if (env.blockContainsVariable(ident))
					addError("Duplicate declaration of variable " + ident, env);

				env.addVariable(ident, decl.type_);
			}
		}

		return false;
	}

	@Override
	public String visit(NoInit p, TypeEnvironment env) {
		if (checkForIllegalIdentifiers(p.ident_, env))
			return null;

		return p.ident_;
	}

	private boolean checkInheritance(Type t1, Type t2, TypeEnvironment env) {
		if (t1 instanceof Obj && t2 instanceof Obj) {
			return env.isSubclass(((Obj) t1).ident_, ((Obj) t2).ident_);
		}

		return false;
	}

	@Override
	public String visit(Init p, TypeEnvironment env) {
		checkForIllegalIdentifiers(p.ident_, env);

		Type exprType = p.expr_.accept(this, env);

		if (!currentType.equals(exprType)
				&& !checkInheritance(exprType, currentType, env)) {

			addError("Non matching types in declaration of variable "
					+ p.ident_, env);
		}

		return p.ident_;
	}

	@Override
	public Boolean visit(BStmt p, TypeEnvironment env) {
		return p.block_.accept(this, env);
	}

	private boolean checkIfCorrectLValue(String operation, LValue lvalue,
			TypeEnvironment env) {

		if (lvalue instanceof LVFCall || lvalue instanceof LVMethodCall) {
			addError("Use of incorrect expression as lvalue in " + operation,
					env);
			return false;
		}

		if (lvalue instanceof LVSimpleIdent) {
			LVSimpleIdent lv = (LVSimpleIdent) lvalue;
			checkForIllegalIdentifiers(lv.ident_, env);
		}

		return true;
	}

	@Override
	public Boolean visit(Ass p, TypeEnvironment env) {
		Type exprType = p.expr_.accept(this, env);
		if (p.lvalue_ instanceof LVAttrAcc) {
			LVAttrAcc lv = (LVAttrAcc) p.lvalue_;
			AttrAcc ac = (AttrAcc) lv.attraccess_;
			Type clazz = ac.lvalue_.accept(this, env);
			String attribute = ac.ident_;
			if (clazz instanceof Arr) {
				if (attribute.equals("length"))
					addError("Assignment to array length", env);
				else
					addError("Assignment to nonexisting array attribute "
							+ attribute, env);

				return false;
			}
		}

		Type lvaltype = p.lvalue_.accept(this, env);
		if (checkIfCorrectLValue("assignment", p.lvalue_, env)
				&& exprType != null && !exprType.equals(lvaltype)
				&& !env.doesExtend(exprType, lvaltype)) {

			if (lvaltype != null && exprType != null) {
				addError("Assignment to lvalue of type "
						+ lvaltype.getClass().getSimpleName()
						+ " expression with type "
						+ exprType.getClass().getSimpleName(), env);
			}
		}

		return false;
	}

	@Override
	public Boolean visit(Incr p, TypeEnvironment env) {
		Type lvaltype = p.lvalue_.accept(this, env);

		if (checkIfCorrectLValue("incrementation", p.lvalue_, env)
				&& lvaltype == null || !new Int().equals(lvaltype)) {

			addError("Incrementation of non-int lvalue", env);
		}

		return false;
	}

	@Override
	public Boolean visit(Decr p, TypeEnvironment env) {
		Type lvaltype = p.lvalue_.accept(this, env);

		if (checkIfCorrectLValue("decrementation", p.lvalue_, env)
				&& lvaltype == null || !new Int().equals(lvaltype)) {

			addError("Decrementation of non-int lvalue", env);
		}

		return false;
	}

	private void typecheckReturn(Type retType, TypeEnvironment env) {
		FunDef currentFunction = env.getCurrentFunction();
		if (!currentFunction.type_.equals(retType)
				&& !checkInheritance(retType, currentFunction.type_, env)) {

			addError("Incorrect return type", env);
		}
	}

	@Override
	public Boolean visit(Ret p, TypeEnvironment env) {
		Type retType = p.expr_.accept(this, env);
		if (env.getCurrentFunction().type_.equals(new Void()))
			addError("Return with value in void function", env);
		else
			typecheckReturn(retType, env);

		return true;
	}

	@Override
	public Boolean visit(VRet p, TypeEnvironment env) {
		typecheckReturn(new Void(), env);

		return true;
	}

	@Override
	public Boolean visit(Cond p, TypeEnvironment env) {
		Boolean ret = typecheckCond("if", p.expr_, p.stmt_, env);

		if (p.expr_ instanceof ELitTrue)
			return ret;

		return false;
	}

	@Override
	public Boolean visit(CondElse p, TypeEnvironment env) {
		Boolean block1Ret = typecheckCond("ifelse", p.expr_, p.stmt_1, env);
		Boolean block2Ret = p.stmt_2.accept(this, env);

		if (p.expr_ instanceof ELitTrue)
			return block1Ret;

		if (p.expr_ instanceof ELitFalse)
			return block2Ret;

		return block1Ret && block2Ret;
	}

	@Override
	public Boolean visit(While p, TypeEnvironment env) {
		Boolean ret = typecheckCond("while", p.expr_, p.stmt_, env);

		if (p.expr_ instanceof ELitTrue)
			return ret;

		return false;
	}

	@Override
	public Boolean visit(SExp p, TypeEnvironment env) {
		p.expr_.accept(this, env);

		return false;
	}

	@Override
	public Type visit(EVar p, TypeEnvironment env) {
		return retrieveVarType(env, p.ident_);
	}

	@Override
	public Type visit(ELitInt p, TypeEnvironment env) {
		return new Int();
	}

	@Override
	public Type visit(ELitTrue p, TypeEnvironment env) {
		return new Bool();
	}

	@Override
	public Type visit(ELitFalse p, TypeEnvironment env) {
		return new Bool();
	}

	@Override
	public Type visit(EApp p, TypeEnvironment env) {
		FCall f = (FCall) p.functioncall_;

		return typecheckFCall(f, env.retrieveFunction(f.ident_), env);

	}

	@Override
	public Type visit(EString p, TypeEnvironment env) {
		return new Str();
	}

	@Override
	public Type visit(Neg p, TypeEnvironment env) {
		Type exprType = p.expr_.accept(this, env);
		if (exprType == null || !exprType.equals(new Int())) {
			addError("Non-int type of expression in operator neg", env);
		}

		return new Int();
	}

	@Override
	public Type visit(Not p, TypeEnvironment env) {
		Type exprType = p.expr_.accept(this, env);
		if (exprType == null || !exprType.equals(new Bool())) {
			addError("Non-boolean type of expression in operator not", env);
		}

		return new Bool();
	}

	@Override
	public Type visit(EMul p, TypeEnvironment env) {
		return typecheckBinaryOp("mul", p.expr_1, p.expr_2, env, new Int());
	}

	@Override
	public Type visit(EAdd p, TypeEnvironment env) {
		ArrayList<Type> types = new ArrayList<Type>();
		if (p.addop_ instanceof Plus)
			types.add(new Str());
		types.add(new Int());
		Type ret = typeCheckBinaryOp(p.expr_1, p.expr_2, env, types);

		if (ret == null)
			addError("Invalid types in add operator", env);

		return ret;
	}

	@Override
	public Type visit(ERel p, TypeEnvironment env) {
		if (p.relop_ instanceof EQU || p.relop_ instanceof NE) {
			Type t1 = p.expr_1.accept(this, env);
			Type t2 = p.expr_2.accept(this, env);
			if (t1 == null
					|| t2 == null
					|| (!t1.equals(t2) && !checkInheritance(t1, t2, env) && !checkInheritance(
							t2, t1, env)) || t1.equals(new Void())
					|| t2.equals(new Void())) {

				if (t1 != null && t2 != null) {
					addError("Incorrect types in "
							+ p.relop_.getClass().getSimpleName()
							+ " comparison operator: "
							+ t1.getClass().getSimpleName() + ", "
							+ t2.getClass().getSimpleName(), env);
				} else {
					addError("Incorrect types in "
							+ p.relop_.getClass().getSimpleName()
							+ " comparison operator", env);
				}
			}
		} else {
			typecheckBinaryOp("comparison", p.expr_1, p.expr_2, env, new Int());
		}

		return new Bool();
	}

	@Override
	public Type visit(EAnd p, TypeEnvironment env) {
		typecheckBinaryOp("and", p.expr_1, p.expr_2, env, new Bool());

		return new Bool();
	}

	@Override
	public Type visit(EOr p, TypeEnvironment env) {
		typecheckBinaryOp("or", p.expr_1, p.expr_2, env, new Bool());

		return new Bool();
	}

	@Override
	public Boolean visit(Empty p, TypeEnvironment env) {
		return false;
	}

	@Override
	public Type visit(ENewArr p, TypeEnvironment env) {
		if (!(new Int().equals(p.expr_.accept(this, env)))) {
			addError("Non int array size in declaration of "
					+ p.type_.getClass().getSimpleName() + " array", env);
		}
		if (p.type_.equals(new Void())) {
			addError("Array instantiated void", env);
		}

		return new Arr(p.type_);
	}

	@Override
	public Type visit(EAttrAccess p, TypeEnvironment env) {
		return p.attraccess_.accept(this, env);
	}

	@Override
	public Type visit(EMthCall p, TypeEnvironment env) {
		return p.methodcall_.accept(this, env);
	}

	@Override
	public Type visit(EArrAccess p, TypeEnvironment env) {
		return p.arraccess_.accept(this, env);
	}

	@Override
	public Type visit(LVFCall p, TypeEnvironment env) {
		FCall f = (FCall) p.functioncall_;

		return typecheckFCall(f, env.retrieveFunction(f.ident_), env);
	}

	@Override
	public Type visit(LVMethodCall p, TypeEnvironment env) {
		Type ret = p.methodcall_.accept(this, env);

		if (TypeEnvironment.isBasicType(ret)) {
			addError("Basic type can't be left value", env);
			return null;
		}

		return ret;
	}

	@Override
	public Type visit(LVSimpleIdent p, TypeEnvironment env) {
		return retrieveVarType(env, p.ident_);
	}

	@Override
	public Type visit(LVEArrAccess p, TypeEnvironment env) {
		return p.arraccess_.accept(this, env);
	}

	@Override
	public Type visit(LVAttrAcc p, TypeEnvironment env) {
		return p.attraccess_.accept(this, env);
	}

	private Type typecheckFCall(FCall f, FunDef fn, TypeEnvironment env) {
		if (fn == null) {
			addError("Call of undeclared function " + f.ident_, env);
			return null;
		}

		if (f.listexpr_.size() != fn.listarg_.size()) {
			addError("Wrong number of arguments (needed: " + fn.listarg_.size()
					+ ", provided: " + f.listexpr_.size()
					+ ") in call of function " + f.ident_, env);

			return fn.type_;
		}

		for (int i = 0; i < fn.listarg_.size(); i++) {
			Type argType = ((Ar) fn.listarg_.get(i)).type_;
			Type callType = f.listexpr_.get(i).accept(this, env);
			if (callType == null
					|| (!argType.equals(callType) && !checkInheritance(argType,
							callType, env))) {

				addError("Wrong type of argument number " + i
						+ " in call of function " + fn.ident_, env);
			}
		}

		return fn.type_;
	}

	@Override
	public Type visit(ArrAcc p, TypeEnvironment env) {
		if (!p.expr_.accept(this, env).equals(new Int())) {
			addError("Non int parameter of array access operation", env);
		}
		Type arrType = p.lvalue_.accept(this, env);
		if (!(arrType instanceof Arr)) {
			addError("Non array in array access operation", env);
			return null;
		}

		return ((Arr) arrType).type_;
	}

	@Override
	public Boolean visit(For p, TypeEnvironment env) {
		env.openBlock();
		env.addVariable(p.ident_1, p.type_);

		if (checkForIllegalIdentifiers(p.ident_1, env)
				|| checkForIllegalIdentifiers(p.ident_2, env)) {

			return false;
		}

		Type arr = env.retrieveVariableType(p.ident_2);
		if (arr instanceof Arr) {
			Arr a = (Arr) arr;
			if (!a.type_.equals(p.type_)
					&& !checkInheritance(a.type_, p.type_, env)) {

				addError("Non matching types in for loop; element type: "
						+ p.type_.getClass().getSimpleName()
						+ ", arr type: array of "
						+ a.type_.getClass().getSimpleName(), env);
			}
		} else {
			addError("Non matching types in for loop; element type: "
					+ p.type_.getClass().getSimpleName() + ", arr type: "
					+ arr.getClass().getSimpleName(), env);
		}

		Boolean ret = p.stmt_.accept(this, env);
		env.closeBlock();

		return ret;
	}

	@Override
	public Type visit(AttrAcc p, TypeEnvironment env) {
		Type lvaltype = p.lvalue_.accept(this, env);

		if (lvaltype == null)
			return null;

		if (p.ident_.equals("length") && lvaltype instanceof Arr) {
			return new Int();
		}

		if (checkForIllegalIdentifiers(p.ident_, env))
			return null;

		if (lvaltype instanceof Obj) {
			String attribute = p.ident_;
			String classname = ((Obj) lvaltype).ident_;
			if (!env.classAlreadyDeclared(classname)) {
				addError("Use of object of undefined class " + classname, env);
				return null;
			}
			if (!env.classContainsAttribute(classname, attribute)) {
				addError("Class " + classname + " does not contain attribute "
						+ attribute, env);
			}

			return env.getClassAttribute(classname, attribute);
		}

		return null;
	}

	@Override
	public Type visit(ENew p, TypeEnvironment env) {
		if (env.classAlreadyDeclared(p.ident_))
			return new Obj(p.ident_);

		addError("Use of undefined class " + p.ident_, env);

		return null;
	}

	private void typecheckClassBlock(String classname, ClassBlock cb,
			TypeEnvironment env) {

		if (env.classAlreadyDeclared(classname)) {
			env.setCurrentClass(classname);
			for (ClassElem ce : cb.listclasselem_) {
				ce.accept(this, env);
			}
			env.setCurrentClass(null);
		}
	}

	@Override
	public Object visit(ClassDef p, TypeEnvironment env) {
		typecheckClassBlock(p.ident_, (ClassBlock) p.classbody_, env);

		return null;
	}

	@Override
	public Object visit(ClassDefE p, TypeEnvironment env) {
		typecheckClassBlock(p.ident_1, (ClassBlock) p.classbody_, env);

		return null;
	}

	@Override
	public Object visit(ClassMeth p, TypeEnvironment env) {
		FunDef fn = (FunDef) p.funcdef_;
		typecheckFunction(fn, env);

		return null;
	}

	@Override
	public Object visit(ClassAtr p, TypeEnvironment env) {
		if (!env.isTypeKnown(p.type_))
			addError("Class attribute " + p.ident_
					+ " declared with an unknown type", env);

		if (p.type_ instanceof Void)
			addError("Class attribute " + p.ident_ + " declared void", env);

		return null;
	}

	@Override
	public Type visit(MCall p, TypeEnvironment env) {
		Type lvaltype = p.lvalue_.accept(this, env);

		if (TypeEnvironment.isBasicType(lvaltype)) {
			addError("Basic type can't be left value", env);
			return null;
		}

		if (new Void().equals(lvaltype)) {
			addError("Void type can't be left value", env);
			return null;
		}

		if (lvaltype == null)
			return null;

		String lvalClassname = ((Obj) lvaltype).ident_;
		FCall f = (FCall) p.functioncall_;
		if (!env.classAlreadyDeclared(lvalClassname)) {
			addError("Use of object of undefined class " + lvalClassname, env);
			return null;
		}
		FunDef method = env.retrieveMethod(lvalClassname, f.ident_);

		if (method == null) {
			addError("Use of undefined in type " + lvalClassname + " method "
					+ f.ident_, env);
			return null;
		}

		Type ret = typecheckFCall(f, method, env);

		return ret;
	}

	private boolean checkForIllegalIdentifiers(String ident, TypeEnvironment env) {
		if (env.checkForIllegalIdentifiers(ident, env)) {
			addError("Declaration of a variable with illegal identifier "
					+ ident, env);

			return true;
		}

		return false;
	}

	@Override
	public Type visit(ECastNull p, TypeEnvironment env) {
		if (TypeEnvironment.isBasicType(p.type_) || p.type_.equals(new Void())) {
			addError("Cast on basic type", env);
		}

		return p.type_;
	}

}
