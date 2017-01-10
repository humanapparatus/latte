package Latte.Compiler;

import Latte.Util.FileLineWriter;

public class MethodCaller implements FunctionCaller {

	private Integer methodOffset;
	private Integer typesize;

	public MethodCaller(Integer methodOffset, Integer typesize) {
		this.methodOffset = methodOffset;
		this.typesize = typesize;
	}

	@Override
	public void call(FileLineWriter writer) {
		writer.appendLine("movl (%ebx), %eax");
		writer.appendLine("movl " + methodOffset * typesize + "(%eax), %eax");
		writer.appendLine("call *%eax");
	}

}
