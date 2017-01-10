package Latte.Typecheck;

import java.util.Map;

import Latte.Absyn.Ar;
import Latte.Absyn.FunDef;
import Latte.Absyn.Int;
import Latte.Absyn.ListArg;
import Latte.Absyn.Str;
import Latte.Absyn.Void;
import Latte.Environment.TypeEnvironment;

public class LibraryFunctions {
	private static void printInt(TypeEnvironment env) {
		ListArg listArg = new ListArg();
		Ar ar = new Ar(new Int(), "x");
		listArg.add(ar);
		FunDef printInt = new FunDef(new Void(), "printInt", listArg, null);
		env.addFunction(printInt);
	}

	private static void printString(TypeEnvironment env) {
		ListArg listArg = new ListArg();
		Ar ar = new Ar(new Str(), "x");
		listArg.add(ar);
		FunDef printString = new FunDef(new Void(), "printString", listArg, null);
		env.addFunction(printString);
	}

	private static void error(TypeEnvironment env) {
		ListArg listArg = new ListArg();
		FunDef error = new FunDef(new Void(), "error", listArg, null);
		env.addFunction(error);
	}

	private static void readInt(TypeEnvironment env) {
		ListArg listArg = new ListArg();
		FunDef readInt = new FunDef(new Int(), "readInt", listArg, null);
		env.addFunction(readInt);
	}

	private static void readString(TypeEnvironment env) {
		ListArg listArg = new ListArg();
		FunDef readString = new FunDef(new Str(), "readString", listArg, null);
		env.addFunction(readString);
	}

	public static void addLibraryFunctions(TypeEnvironment env) {
		printInt(env);
		printString(env);
		error(env);
		readInt(env);
		readString(env);
	}

	public static Boolean checkForMainFunction(
			Map<String, FunDef> functions) {

		FunDef main = functions.get("main");

		return main != null && main.type_.equals(new Int()) && main.listarg_.size() == 0;
	}
}
