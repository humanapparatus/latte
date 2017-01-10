package Latte.Compiler;

import Latte.Absyn.Ass;
import Latte.Absyn.BStmt;
import Latte.Absyn.Blk;
import Latte.Absyn.Cond;
import Latte.Absyn.CondElse;
import Latte.Absyn.Decl;
import Latte.Absyn.Decr;
import Latte.Absyn.Empty;
import Latte.Absyn.For;
import Latte.Absyn.Incr;
import Latte.Absyn.Ret;
import Latte.Absyn.SExp;
import Latte.Absyn.Stmt;
import Latte.Absyn.VRet;
import Latte.Absyn.While;


public class CountVarVisitor implements
		Latte.Absyn.Block.Visitor<Integer, Object>,
		Latte.Absyn.Stmt.Visitor<Integer, Object> {

	@Override
	public Integer visit(Empty p, Object arg) {
		return 0;
	}

	@Override
	public Integer visit(BStmt p, Object arg) {
		return p.block_.accept(this, arg);
	}

	@Override
	public Integer visit(Decl p, Object arg) {
		return p.listitem_.size();
	}

	@Override
	public Integer visit(Ass p, Object arg) {
		return 0;
	}

	@Override
	public Integer visit(Incr p, Object arg) {
		return 0;
	}

	@Override
	public Integer visit(Decr p, Object arg) {
		return 0;
	}

	@Override
	public Integer visit(Ret p, Object arg) {
		return 0;
	}

	@Override
	public Integer visit(VRet p, Object arg) {
		return 0;
	}

	@Override
	public Integer visit(Cond p, Object arg) {
		return p.stmt_.accept(this, arg);
	}

	@Override
	public Integer visit(CondElse p, Object arg) {
		return p.stmt_1.accept(this, arg) + p.stmt_2.accept(this, arg);
	}

	@Override
	public Integer visit(While p, Object arg) {
		return p.stmt_.accept(this, arg);
	}

	@Override
	public Integer visit(SExp p, Object arg) {
		return 0;
	}

	@Override
	public Integer visit(Blk p, Object arg) {
		Integer ret = 0;
		for (Stmt stmt : p.liststmt_) {
			ret += stmt.accept(this, arg);
		}

		return ret;
	}

	@Override
	public Integer visit(For p, Object arg) {
		return 1 + p.stmt_.accept(this, arg);
	}

}
