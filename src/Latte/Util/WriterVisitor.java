package Latte.Util;

import java.io.FileNotFoundException;

public abstract class WriterVisitor {

	protected FileLineWriter	writer;

	public WriterVisitor(String path) throws FileNotFoundException {
		this.writer = new FileLineWriter(path);
	}

	public FileLineWriter getWriter() {
		return writer;
	}
}
