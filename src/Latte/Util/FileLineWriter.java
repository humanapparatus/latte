package Latte.Util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class FileLineWriter {
	private Writer				writer;
	private ArrayList<String>	program;

	public FileLineWriter(String path) throws FileNotFoundException {
		program = new ArrayList<String>();
		writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path)));
	}

	public void appendLine(String line) {
		program.add(line + "\n");
	}

	public void append(String s) {
		program.add(s);
	}

	public void close() throws IOException {
		writer.close();
	}

	public void writeToFile() throws IOException {
		writer.append(".EMPTYSTRING:\n");
		writer.append(".string \"\"\n");
		writer.append(".ERROR:\n");
		writer.append("call error\n");

		for (String line : program) {
			writer.append(line);
		}
	}

	public void sysout() {
		for (String line : program) {
			System.out.print(line);
		}
	}

}
