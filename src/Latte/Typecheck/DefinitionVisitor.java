package Latte.Typecheck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Latte.Absyn.ClassDef;
import Latte.Absyn.ClassDefE;
import Latte.Absyn.FnDef;
import Latte.Absyn.FunDef;
import Latte.Absyn.Prog;
import Latte.Absyn.TopDef;
import Latte.Environment.TypeEnvironment;
import Latte.Typecheck.Exception.ClassDefinitionException;

public class DefinitionVisitor implements
		Latte.Absyn.Program.Visitor<Object, TypeEnvironment>,
		Latte.Absyn.TopDef.Visitor<Object, TypeEnvironment> {

	private List<String>			errors;
	private Map<String, ClassDefE>	classesToProcess;

	public DefinitionVisitor(List<String> errors) {
		this.errors = errors;
		this.classesToProcess = new HashMap<String, ClassDefE>();
	}

	@Override
	public Object visit(FnDef p, TypeEnvironment env) {
		FunDef fn = (FunDef) p.funcdef_; // safe cast

		if (env.checkForIllegalIdentifiers(fn.ident_, env)) {
			errors.add("Definiton of function with illegal identifier");
			return null;
		}

		if (env.functionAlreadyDeclared(fn)) {
			errors.add("Function " + fn.ident_ + " was already declared");
		} else {
			env.addFunction(fn);
		}

		return null;
	}

	@Override
	public Object visit(ClassDef p, TypeEnvironment env) {
		if (env.checkForIllegalIdentifiers(p.ident_, env)) {
			errors.add("Definiton of class with illegal identifier");
			return null;
		}

		if (env.classAlreadyDeclared(p.ident_)) {
			errors.add("Class " + p.ident_ + " was already defined");
		} else {
			addClass(p, env);
		}

		return null;
	}

	private void addClass(ClassDef p, TypeEnvironment env) {
		try {
			env.addClass(p);
		} catch (ClassDefinitionException e) {
			appendError(e);
		}
	}

	private void addClass(ClassDefE p, TypeEnvironment env) {
		try {
			env.addClass(p);
		} catch (ClassDefinitionException e) {
			appendError(e);
		}
	}

	private void appendError(ClassDefinitionException e) {
		errors.add("Failed to define class: " + e.getClassname());
		for (String err : e.getErrors()) {
			errors.add(err);
		}
	}

	@Override
	public Object visit(ClassDefE p, TypeEnvironment env) {
		if (env.checkForIllegalIdentifiers(p.ident_1, env)) {
			errors.add("Definiton of class with illegal identifier");
			return null;
		}
		if (env.checkForIllegalIdentifiers(p.ident_2, env)) {
			errors.add("Definiton of class with illegal parent identifier");
			return null;
		}

		if (env.classAlreadyDeclared(p.ident_1)) {
			errors.add("Class " + p.ident_1 + " was already declared");
		} else if (env.classAlreadyDeclared(p.ident_2)) {
			addClass(p, env);
		} else {
			if (classesToProcess.containsKey(p.ident_1)) {
				errors.add("Multiple definition of class " + p.ident_1);
			} else {
				classesToProcess.put(p.ident_1, p);
			}
		}

		return null;
	}

	@Override
	public Object visit(Prog p, TypeEnvironment env) {
		for (TopDef topDef : p.listtopdef_) {
			topDef.accept(this, env);
		}

		defineRemainingClasses(env);

		return null;
	}

	private void doDefineRemainingClasses(TypeEnvironment env) {
		for (String c : classesToProcess.keySet()) {
			ClassDefE clazz = classesToProcess.get(c);
			if (env.classAlreadyDeclared(clazz.ident_2)) {
				addClass(clazz, env);
				classesToProcess.remove(c);
			}
		}
	}

	private void defineRemainingClasses(TypeEnvironment env) {
		Integer currentSize = 0;

		while (currentSize != classesToProcess.size()) {
			currentSize = classesToProcess.size();
			doDefineRemainingClasses(env);
		}

		if (currentSize > 0) {
			for (String c : classesToProcess.keySet()) {
				errors.add("Class " + c
						+ " could not be defined (lack of parent class)");
			}
		}
	}

}
