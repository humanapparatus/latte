package Latte.Compiler;

import Latte.Util.FileLineWriter;

public interface FunctionCaller {
	void call(FileLineWriter writer);
}
