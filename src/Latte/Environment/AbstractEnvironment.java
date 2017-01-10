package Latte.Environment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import Latte.Typecheck.ClassDefinition;

public abstract class AbstractEnvironment<T> {
	protected LinkedList<Map<String, T>>	env;

	public AbstractEnvironment() {
		this.env = new LinkedList<Map<String, T>>();
	}

	public void openBlock() {
		env.add(new HashMap<String, T>());
	}

	public void closeBlock() {
		env.remove(env.size() - 1);
	}

	public void addVariable(String ident, T val) {
		lastBlockEnv().put(ident, val);
	}

	protected Map<String, T> lastBlockEnv() {
		return env.get(env.size() - 1);
	}

	protected Map<String, T> firstBlockEnv() {
		return env.get(0);
	}

	protected T retrieveValue(String ident) {
		for (int i = env.size() - 1; i >= 0; i--) {
			Map<String, T> currentEnv = env.get(i);
			if (currentEnv.containsKey(ident))
				return currentEnv.get(ident);
		}

		return null;
	}

	protected ClassDefinition	currentClass;

	public ClassDefinition getCurrentClass() {
		return currentClass;
	}
}
