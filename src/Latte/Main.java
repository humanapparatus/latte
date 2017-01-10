package Latte;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;

import Latte.Compiler.CompileVisitor;
import Latte.Environment.TypeEnvironment;
import Latte.Typecheck.TypecheckVisitor;

public class Main {
	public static void main(String args[]) throws Exception {
		Yylex l = null;
		parser p;
		try {
			if (args.length == 0)
				l = new Yylex(new InputStreamReader(System.in));
			else
				l = new Yylex(new FileReader(args[0]));
		} catch (FileNotFoundException e) {
			System.err.println("ERROR");
			System.err.println("Error: File not found: " + args[0]);
			System.exit(1);
		}
		p = new parser(l);
		try {
			Latte.Absyn.Program parse_tree = p.pProgram();
			TypeEnvironment te = new TypeEnvironment();
			TypecheckVisitor tv = new TypecheckVisitor();
			parse_tree.accept(tv, te);
			List<String> errors = tv.getErrors();
			if (errors.size() > 0) {
				System.err.println("ERROR");
				for (String err : errors) {
					System.err.println("[ERROR] " + err);
				}
				System.exit(1);
			}
			else {
				System.err.println("OK");
			}
			CompileVisitor cv = new CompileVisitor(extractFileName(args[0]), te);
			parse_tree.accept(cv, null);
		} catch (Throwable e) {
			System.err.println("ERROR");
			System.err.println("At line " + String.valueOf(l.line_num())
					+ ", near \"" + l.buff() + "\" :");
			System.err.println("     " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static String extractFileName(String filename) {
		String prefix = "";
		if (filename.contains("/")) {
			prefix = filename.substring(0, filename.lastIndexOf('/'));
			filename = filename.substring(filename.lastIndexOf('/'));
		}
		String prefixOfFilename = filename.substring(0, filename.lastIndexOf('.'));

		return prefix + prefixOfFilename + ".s";
	}
}