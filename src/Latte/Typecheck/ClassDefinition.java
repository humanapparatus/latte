package Latte.Typecheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Latte.Absyn.ClassAtr;
import Latte.Absyn.ClassBlock;
import Latte.Absyn.ClassDef;
import Latte.Absyn.ClassDefE;
import Latte.Absyn.ClassElem;
import Latte.Absyn.ClassMeth;
import Latte.Absyn.FunDef;
import Latte.Absyn.Type;
import Latte.Typecheck.Exception.ClassDefinitionException;

public class ClassDefinition {
	private Map<String, Type>		attrs;
	private Map<String, FunDef>		methods;
	private String					classname;
	private String					parent;
	private Map<String, Integer>	attrOrder;
	private Map<String, Integer>	methodOrder;
	private Set<String>				definedMethods;

	public ClassDefinition() {
		attrs = new HashMap<String, Type>();
		methods = new HashMap<String, FunDef>();
		attrOrder = new HashMap<String, Integer>();
		methodOrder = new HashMap<String, Integer>();
		definedMethods = new HashSet<String>();
	}

	public ClassDefinition(ClassDef clazz) throws ClassDefinitionException {
		this();
		initialize(clazz.ident_, (ClassBlock) clazz.classbody_);
	}

	public ClassDefinition(ClassDefE clazz, ClassDefinition parent)
			throws ClassDefinitionException {

		this();
		attrs.putAll(parent.attrs);
		methods.putAll(parent.methods);
		attrOrder.putAll(parent.attrOrder);
		methodOrder.putAll(parent.methodOrder);
		this.parent = parent.classname;
		initialize(clazz.ident_1, (ClassBlock) clazz.classbody_);
	}

	private void initialize(String classname, ClassBlock cb)
			throws ClassDefinitionException {

		this.classname = classname;
		ArrayList<String> errors = new ArrayList<String>();
		for (ClassElem elem : cb.listclasselem_) {
			String possibleError = elem.accept(new ClassElemVisitor(), this);
			if (possibleError != null)
				errors.add(possibleError);
		}
		if (errors.size() > 0)
			throw new ClassDefinitionException(classname, errors);
	}

	public String getClassname() {
		return classname;
	}

	public Map<String, Type> getAttrs() {
		return attrs;
	}

	public Map<String, Integer> getAttrOrder() {
		return attrOrder;
	}

	public Set<String> getMethodNames() {
		return methods.keySet();
	}

	public FunDef getMethod(String ident) {
		return methods.get(ident);
	}

	public Type getAttr(String attribute) {
		return attrs.get(attribute);
	}

	public String getParentClass() {
		return parent;
	}

	public Integer getAttrIndex(String ident) {
		return attrOrder.get(ident);
	}

	public Integer getMethodIndex(String ident) {
		return methodOrder.get(ident);
	}

	public Integer getSize() {
		return attrOrder.size() + methodOrder.size();
	}

	private class ClassElemVisitor implements
			Latte.Absyn.ClassElem.Visitor<String, ClassDefinition> {

		@Override
		public String visit(ClassMeth p, ClassDefinition clazz) {
			FunDef fn = (FunDef) p.funcdef_;

			if (fn.ident_.equals("self") || fn.ident_.equals("null"))
				return "Declaration of method with illegal identifier";

			if (clazz.definedMethods.contains(fn.ident_))
				return "Method " + fn.ident_ + " already declared";

			if (clazz.methodOrder.containsKey(fn.ident_)) {
				FunDef parentFun = clazz.methods.get(fn.ident_);
				if (!parentFun.type_.equals(fn.type_)) {
					return "Method " + fn.ident_ + " overriden with wrong type";
				}
				if (parentFun.listarg_.size() != fn.listarg_.size()) {
					return "Method " + fn.ident_
							+ " overriden with different number of arguments";
				}
				for (int i = 0; i < fn.listarg_.size(); i++) {
					if (!fn.listarg_.get(i).equals(parentFun.listarg_.get(i))) {
						return "Method " + fn.ident_
								+ " overriden with different arguments (arg " + i
								+ ")";
					}
				}
			}
			else {
				clazz.methodOrder.put(fn.ident_, methodOrder.size());
			}

			clazz.methods.put(fn.ident_, fn);
			clazz.definedMethods.add(fn.ident_);

			return null;
		}

		@Override
		public String visit(ClassAtr p, ClassDefinition clazz) {
			if (p.ident_.equals("self") || p.ident_.equals("null"))
				return "Declaration of class attribute with illegal identifier";

			if (clazz.attrs.containsKey(p.ident_))
				return "Attribute " + p.ident_ + " already declared";

			clazz.attrs.put(p.ident_, p.type_);
			clazz.attrOrder.put(p.ident_, attrOrder.size());

			return null;
		}
	}

}
