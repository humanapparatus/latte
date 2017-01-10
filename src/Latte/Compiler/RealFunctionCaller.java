package Latte.Compiler;

import Latte.Util.FileLineWriter;

public class RealFunctionCaller implements FunctionCaller {

	private String function;

	public RealFunctionCaller(String function) {
		this.function = function;
	}

	@Override
	public void call(FileLineWriter writer) {
		writer.appendLine("call " + function);
	}

}
