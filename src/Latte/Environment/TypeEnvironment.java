package Latte.Environment;

import java.util.HashMap;
import java.util.Map;

import Latte.Absyn.Arr;
import Latte.Absyn.Bool;
import Latte.Absyn.ClassDef;
import Latte.Absyn.ClassDefE;
import Latte.Absyn.FunDef;
import Latte.Absyn.Int;
import Latte.Absyn.Obj;
import Latte.Absyn.Str;
import Latte.Absyn.Type;
import Latte.Typecheck.ClassDefinition;
import Latte.Typecheck.LibraryFunctions;
import Latte.Typecheck.Exception.ClassDefinitionException;

public class TypeEnvironment extends AbstractEnvironment<Type> {
	private Map<String, FunDef>				functions;
	private Map<String, ClassDefinition>	classes;

	private FunDef							currentFunction;

	public TypeEnvironment() {
		this.functions = new HashMap<String, FunDef>();
		this.classes = new HashMap<String, ClassDefinition>();
		LibraryFunctions.addLibraryFunctions(this);
		openBlock();
	}

	public Map<String, ClassDefinition> getClasses() {
		return classes;
	}

	public void setClasses(Map<String, ClassDefinition> classes) {
		this.classes = classes;
	}

	public void addFunction(FunDef funcdef_) {
		functions.put(funcdef_.ident_, funcdef_);
	}

	public boolean blockContainsVariable(String ident) {
		return lastBlockEnv().containsKey(ident);
	}

	public boolean functionAlreadyDeclared(FunDef fn) {
		return functions.containsKey(fn.ident_);
	}

	public void setCurrentFunction(FunDef fn) {
		currentFunction = fn;
	}

	public FunDef getCurrentFunction() {
		return currentFunction;
	}

	public Type retrieveVariableType(String ident) {
		return retrieveValue(ident);
	}

	public FunDef retrieveFunction(String ident) {
		return functions.get(ident);
	}

	public Boolean checkForMainFunction() {
		return LibraryFunctions.checkForMainFunction(functions);
	}

	public Integer getTypeSize(Type type_) {
		return CompileEnvironment.getDefaultTypeSize();
	}

	public Map<String, FunDef> getFunctions() {
		return functions;
	}

	public void setFunctions(Map<String, FunDef> functions) {
		this.functions = functions;
	}

	public boolean isTypeKnown(Type type) {
		if (type instanceof Obj)
			return classAlreadyDeclared(((Obj) type).ident_);

		if (type instanceof Arr) {
			Type arrType = ((Arr) type).type_;
			return isTypeKnown(arrType);
		}

		return true;
	}

	public boolean classAlreadyDeclared(String ident_) {
		return classes.containsKey(ident_);
	}

	public void addClass(ClassDefE c) throws ClassDefinitionException {
		classes.put(c.ident_1, new ClassDefinition(c, classes.get(c.ident_2)));
	}

	public void addClass(ClassDef c) throws ClassDefinitionException {
		classes.put(c.ident_, new ClassDefinition(c));
	}

	public void setCurrentClass(String classname) {
		if (classname != null) {
			this.currentClass = classes.get(classname);
			openBlock();
			lastBlockEnv().putAll(currentClass.getAttrs());
		} else {
			this.currentClass = null;
			closeBlock();
		}
	}

	public static boolean isBasicType(Type ret) {
		return ret instanceof Int || ret instanceof Str || ret instanceof Bool
				|| ret instanceof Arr;
	}

	public FunDef retrieveMethod(String clazz, String method) {
		ClassDefinition cd = classes.get(clazz);

		return cd.getMethod(method);
	}

	public boolean classContainsAttribute(String classname, String attribute) {
		return getClassAttribute(classname, attribute) != null;
	}

	public Type getClassAttribute(String classname, String attribute) {
		return classes.get(classname).getAttr(attribute);
	}

	public boolean isSubclass(String child, String parent) {
		while (child != null) {
			if (child.equals(parent))
				return true;

			child = classes.get(child).getParentClass();
		}

		return false;
	}

	public ClassDefinition getClassDefinition(String ident) {
		return classes.get(ident);
	}

	public boolean checkForIllegalIdentifiers(String ident, TypeEnvironment env) {
		return (ident.equals("self") || ident.equals("null"));
	}

	public void prependClassEnv(ClassDefinition currentClass) {
		env.addFirst(currentClass.getAttrs());
	}

	public void cancelPrependingClassEnv(ClassDefinition currentClass) {
		env.removeFirst();
	}

	public boolean doesExtend(Type exprType, Type lvaltype) {
		if (!(exprType instanceof Obj) || !(lvaltype instanceof Obj)) {
			return false;
		}

		String child = ((Obj) exprType).ident_;
		String parent = ((Obj) lvaltype).ident_;

		return isSubclass(child, parent);
	}

}
