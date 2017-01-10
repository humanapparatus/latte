package Latte.Typecheck.Exception;

import java.util.ArrayList;

public class ClassDefinitionException extends Exception {
	private ArrayList<String> errors;
	private String classname;

	public ClassDefinitionException(String classname, ArrayList<String> errors) {
		this.classname = classname;
		this.errors = errors;
	}

	public String getClassname() {
		return classname;
	}

	public ArrayList<String> getErrors() {
		return errors;
	}
}
