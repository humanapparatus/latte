package Latte.Environment;

import java.util.HashMap;
import java.util.Map;

import Latte.Absyn.Type;
import Latte.Typecheck.ClassDefinition;

public class CompileEnvironment extends AbstractEnvironment<Integer> {
	private static final Integer	DEFAULT_TYPE_SIZE	= 4;

	private Integer					varNumber;
	private Integer					retLabelIndex;
	private Integer					currentLabelIndex;
	private Integer					currentStringLabelIndex;
	private String					currentOnTrueLabel;
	private String					currentOnFalseLabel;
	private Type					currentType;
	private Type					currentReturnType;
	private boolean					insideComparison;
	private TypeEnvironment			typeEnvironment;
	private HashMap<String, String>	stringLabels;

	public CompileEnvironment(Integer labelIndex, Integer stringLabelIndex,
			TypeEnvironment previousTypeEnvironment) {

		this.varNumber = 1;
		this.retLabelIndex = labelIndex;
		this.currentLabelIndex = labelIndex + 1;
		this.currentStringLabelIndex = stringLabelIndex + 1;
		this.typeEnvironment = new TypeEnvironment();
		this.typeEnvironment.setFunctions(previousTypeEnvironment
				.getFunctions());
		this.typeEnvironment.setClasses(previousTypeEnvironment.getClasses());
		this.insideComparison = false;
		this.stringLabels = new HashMap<String, String>();
		openBlock();
	}

	public Integer addVariable(String ident, Type t) {
		Integer ret = -DEFAULT_TYPE_SIZE * varNumber++;
		super.addVariable(ident, ret);
		typeEnvironment.addVariable(ident, t);

		return ret;
	}

	public void addVariable(String ident, Type t, int i) {
		super.addVariable(ident, i);
		typeEnvironment.addVariable(ident, t);
	}

	public Integer getVariable(String ident) {
		return retrieveValue(ident);
	}

	public String getRetLabel() {
		return ".L" + retLabelIndex;
	}

	public void clear() {
		incRetLabel();
		currentOnFalseLabel = null;
		currentOnTrueLabel = null;
		insideComparison = false;
		varNumber = 1;
	}

	public void incRetLabel() {
		retLabelIndex = getCurrentLabelIndex();
	}

	public String getNewLabel() {
		return ".L" + getCurrentLabelIndex();
	}

	public Integer getCurrentLabelIndex() {
		return currentLabelIndex++;
	}

	public String getCurrentOnTrueLabel() {
		return currentOnTrueLabel;
	}

	public void setCurrentOnTrueLabel(String currentOnTrueLabel) {
		this.currentOnTrueLabel = currentOnTrueLabel;
	}

	public String getCurrentOnFalseLabel() {
		return currentOnFalseLabel;
	}

	public void setCurrentOnFalseLabel(String currentOnFalseLabel) {
		this.currentOnFalseLabel = currentOnFalseLabel;
	}

	public void setCurrentType(Type type) {
		this.currentType = type;
	}

	public Type getCurrentType() {
		return currentType;
	}

	public void setInsideComparison(boolean b) {
		this.insideComparison = b;
	}

	public boolean isInsideComparison() {
		return this.insideComparison;
	}

	public Type getVariableType(String ident) {
		return typeEnvironment.retrieveValue(ident);
	}

	@Override
	public void openBlock() {
		super.openBlock();
		typeEnvironment.openBlock();
	}

	@Override
	public void closeBlock() {
		super.closeBlock();
		typeEnvironment.closeBlock();
	}

	public String getLabelForString(String str) {
		if (!stringLabels.containsKey(str))
			stringLabels.put(str, ".S" + currentStringLabelIndex++);

		return stringLabels.get(str);
	}

	public Map<String, String> getStringLabels() {
		return this.stringLabels;
	}

	public Type getCurrentReturnType() {
		return currentReturnType;
	}

	public void setCurrentReturnType(Type currentReturnType) {
		this.currentReturnType = currentReturnType;
	}

	public void swapBoolLabels() {
		String tmp = this.currentOnFalseLabel;
		currentOnFalseLabel = currentOnTrueLabel;
		currentOnTrueLabel = tmp;
	}

	public TypeEnvironment getTypeEnvironment() {
		return typeEnvironment;
	}

	public Integer getCurrentStringLabelIndex() {
		return currentStringLabelIndex;
	}

	public static Integer getDefaultTypeSize() {
		return DEFAULT_TYPE_SIZE;
	}

	public boolean isVariableClassAttribute(String ident) {
		for (int i = env.size() - 1; i > 0; i--) {
			Map<String, Integer> currentEnv = env.get(i);
			if (currentEnv.containsKey(ident))
				return false;
		}

		return true;
	}

	public void setCurrentClass(ClassDefinition clazz) {
		currentClass = clazz;
		typeEnvironment.currentClass = clazz;
		lastBlockEnv().putAll(currentClass.getAttrOrder());
		typeEnvironment.lastBlockEnv().putAll(currentClass.getAttrs());

	}

	public void unsetCurrentClass() {
		currentClass = null;
		typeEnvironment.currentClass = null;
		closeBlock();
	}

	public String getAttribute(String ident) {
		// + 1 because at 0 index there is pointer to method ptr array
		return new Integer((firstBlockEnv().get(ident) + 1) * DEFAULT_TYPE_SIZE)
				.toString();
	}
}
